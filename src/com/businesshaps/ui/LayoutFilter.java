package com.businesshaps.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.businesshaps.am.tools.Includer;
import com.businesshaps.am.tools.IncluderResponse;
import com.businesshaps.oi.DataObject;
import com.businesshaps.oi.JsonOutput;
import com.businesshaps.oi.ObInject;
import com.businesshaps.params.API;
import com.businesshaps.params.APIs;
import com.businesshaps.params.Email;
import com.businesshaps.params.Mapping;
import com.businesshaps.params.PropWatcher;
import com.businesshaps.params.Resource;
import com.businesshaps.params.Sync;
import com.businesshaps.params.SystemProperties;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
public class LayoutFilter implements Filter {
     
	private FilterConfig filterConfig;

	private ObInject obij = null;
    public void init(FilterConfig filterConfig) throws ServletException {
    	this.filterConfig = filterConfig;
    }

    public void log(final String type, final HttpServletRequest request, final String sp, final String filename) {
    	Thread thread = (new Thread() {
    		public void run() {
    			//System.out.println("Log " + type + ": " + (new java.util.Date()) + " - " + sp + " - " + filename + " - " + request.getRemoteAddr());
    		}
    	});
    	thread.start();
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
    	SystemProperties data = PropWatcher.getInstance().getData();

    	try {
			String psp = request.getParameter("sp");
			String rsp = request.getServletPath();

			String rfilename = rsp.split("/")[rsp.split("/").length - 1];
			String ext;
			if (psp!=null) {
				if (psp.endsWith("/")) {
					psp += rfilename;
				} else {
					psp += "/" + rfilename;
				} 
			} 
	        String sp = psp!=null?psp:request.getServletPath(); 
			sp = sp.replaceAll("//", "/");
			String[] sps = sp.split("\\.");
			if (sps.length>0) {
				ext = sps[sps.length-1];
			} else {
				ext = "jsp"; 
			}

			//this.loadConfig();

			
			String layout = getResource(data, sp, "layout.jsp", false, filterConfig.getServletContext().getRealPath(""));
			request.setAttribute("layout", layout);
	
			for (Resource resource : data.getResources()) {
				request.setAttribute(resource.getName(), getResource(data, sp, resource.getFilename(), resource.isInRoot(), filterConfig.getServletContext().getRealPath("")));
			}

			boolean nolayout = new Boolean(request.getParameter("nolayout")==null?"false":request.getParameter("nolayout"));
			boolean nomap = new Boolean(request.getParameter("nomap")==null?"false":request.getParameter("nomap"));
	
			com.businesshaps.params.Mapping mapped = null;
			String mappedFile = null;
			boolean doDefault = false;
			if (rsp.endsWith("/")||rfilename.indexOf("xx.")==-1) {
				doDefault = true;
			} else {
				mappedFile = rfilename;
			}
			boolean isIndex = false;
			boolean isIndexPage = false;
			boolean isCombine = false;
			//System.out.println("sp : "+  sp + " - " + Includer.WebFileExists(sp, request, true) + " _ " + doDefault + " - " + mappedFile);
	
			for (com.businesshaps.params.Mapping mapping : data.getMappings()) {
					
				mappedFile = doDefault?mapping.getDefaultFile():mappedFile;
				if (request.getServletPath().indexOf(mapping.getPath())>-1&&request.getServletPath().indexOf(mapping.getPath() + mappedFile)==-1) {
					if (mapping.getCombine()!=null&&mapping.getCombine().length>0) {
						isCombine = true;
					}
					mapped = mapping;
					break;
				}
			}
			if (mapped!=null&&Includer.WebFileExists(sp, request, true)&&sp.indexOf("index.")==-1) {
				mappedFile = null;
				doDefault = false;
				mapped = null;
			} else {
			}
			if (mapped==null) {

				if (data.getBypassExtensions()!=null) {
					if (("," + data.getBypassExtensions().replaceAll(" ", "") + ",").indexOf("," + ext.toLowerCase() +",")>-1) {
			            filterChain.doFilter(request, response);
			            return;
					}
				}

				if (data.getBypassDirectories()!=null) {
					String[] bdirs = data.getBypassDirectories().split(",");
					for (String bdir : bdirs) {
						if (request.getServletPath().indexOf(bdir.trim())>-1) {
				            filterChain.doFilter(request, response);
				            return;
						}
					}
				}  
			}

			if (mapped!=null&&!nomap) {
				this.log("Mapping", request, sp + (request.getQueryString()!=null?"?" + request.getQueryString():""), rfilename);
				if (mapped.isApi()) {
					if (!this.setApiAttributes(mapped, request, response)) {
						return;
					}
				}
				if (isCombine) {
					this.writeCombineTextFile(request, response, mapped);
					if (mapped.getContentType()!=null) {
						response.setContentType(mapped.getContentType());
					}

				} else {
	//				System.out.println("Log Mapping: " + sp + "?" + request.getQueryString() + " - " + rfilename + " - " + psp);
					RequestDispatcher rd = request.getRequestDispatcher(mapped.getPath() + mappedFile + "?sp=" + sp);
		        	rd.forward(request,response);
				}
			} else if (layout!=null&&!nolayout) {
				this.log("Layout", request, sp + (request.getQueryString()!=null?"?" + request.getQueryString():""), rfilename);
	//			System.out.println("Log Layout: " + sp + "?" + request.getQueryString() + " - " + rfilename + " - " + psp + " - " + data.getBypassDirectories());
	        	RequestDispatcher rd = request.getRequestDispatcher(layout + "?top=true");
	        	rd.include(request, response);
	            filterChain.doFilter(request, response);
	        	rd = request.getRequestDispatcher(layout + "?top=bottom");
	        	rd.include(request, response);
	        } else {
				this.log("Other", request, sp + (request.getQueryString()!=null?"?" + request.getQueryString():""), rfilename);
//				System.out.println("Log Other: " + sp + "?" + request.getQueryString() + " - " + rfilename + " - " + psp);
	            filterChain.doFilter(request, response);
	        }
    	} catch(Exception e) {
    		e.printStackTrace();
            filterChain.doFilter(request, response);
    	}
    }
	private Hashtable<String, Hashtable<String, Long>> mapped_file_last_mod = new Hashtable<String, Hashtable<String, Long>>();
    
    public void writeCombineTextFile(HttpServletRequest request, HttpServletResponse response, Mapping mapped) throws Exception {

		RequestDispatcher rd;;
		IncluderResponse resp;
		StringBuilder sb;
		String line;
		String fileText;
		BufferedReader br;
		Hashtable<String, Long> file_last_mod = null;
		boolean fileChanged = false;
		boolean fileFirstLoad = false;
		if (mapped.isCombineAsFile()) {

			if (!mapped_file_last_mod.containsKey(mapped.getPath())) {
				file_last_mod =  new Hashtable<String, Long>();
				fileFirstLoad = true;
			} else {
				file_last_mod = mapped_file_last_mod.get(mapped.getPath());
			}
			
			mapped_file_last_mod.put(mapped.getPath(), file_last_mod);
			for (String combineUri : mapped.getCombine()) {
				resp = Includer.handleInclude(request, response, combineUri);				
				if (file_last_mod.get(combineUri)!=null) {
					if (!file_last_mod.get(combineUri).equals(new File(resp.getFilePath()).lastModified())) {
						System.out.println(combineUri + " " + new File(resp.getFilePath()).lastModified());
						fileChanged = true;
					}
				} else {
					fileChanged = true;
				}
				if ( resp!=null&&new File(resp.getFilePath())!=null) {
					file_last_mod.put(combineUri, new File(resp.getFilePath()).lastModified());
				}
			}
		}

		if (mapped.isCombineAsFile()) {
			FileWriter fstream = null;
			BufferedWriter out = null;
			StringBuffer outputFile = new StringBuffer();
			
			String dir = filterConfig.getServletContext().getRealPath("");
			File dirCombine = new File(dir + fileSep + "WEB-INF" + fileSep + "gudeam" + fileSep + "combineFile");
			dirCombine.mkdirs();
			String mappedCombineFile = mapped.getPath().replaceAll("/", "_");
			File fileCombine = new File(dir + fileSep + "WEB-INF" + fileSep + "gudeam" + fileSep + "combineFile" + fileSep + mappedCombineFile);
			if (fileChanged&&fileCombine.exists()&&fileFirstLoad) {
				fileChanged = false;
			}
			if (fileChanged||!fileCombine.exists()) {
				System.out.println("File Changed: " + mappedCombineFile);
				fstream = new FileWriter(fileCombine);
				
				out = new BufferedWriter(fstream);
				for (String combineUri : mapped.getCombine()) {
					resp = Includer.handleInclude(request, response, combineUri);

					if (resp.exists()) {

						try {
						    br = new BufferedReader(new FileReader(resp.getFilePath())); 
						    try { 
						        sb = new StringBuilder(); 
						        line = br.readLine(); 
						 
						        while (line != null) { 
						            sb.append(line); 
						            sb.append("\n"); 
						            line = br.readLine(); 
						        } 
						         //fileText = line;
						        outputFile.append(sb);
						    } catch(Exception e) { 
						    	e.printStackTrace();
						    } finally { 
						        br.close(); 
						    } 
						} catch(Exception e) {
							e.printStackTrace();
						}								
					}

				}
				
				out.write(outputFile.toString());
				out.close();
				if (mapped.isJavascriptCompress()) {
					YuiCompressor.YuiCompressor(fileCombine.getAbsolutePath(), fileCombine.getAbsolutePath());
				}
				//response.getWriter().write(outputFile.toString());
				//outputFile = null;				
			} 
			outputFile = new StringBuffer();
			try {
			    br = new BufferedReader(new FileReader(fileCombine)); 
			    try { 
			        sb = new StringBuilder(); 
			        line = br.readLine(); 
			 
			        while (line != null) { 
			            sb.append(line); 
			            sb.append("\n"); 
			            line = br.readLine(); 
			        } 
			         //fileText = line;
			        outputFile.append(sb);
			    } catch(Exception e) { 
			    	e.printStackTrace();
			    } finally { 
			        br.close(); 
			    } 
				
				response.getWriter().write(outputFile.toString());
				outputFile = null;
			} catch(Exception e) {
				e.printStackTrace();
			}								

		} else {
			for (String combineUri : mapped.getCombine()) {
				rd = request.getRequestDispatcher(combineUri);
				rd.include(request, response);
			}
		}
	

    }
     
	private static String fileSep = System.getProperty("file.separator");
    public static String getResource(SystemProperties data, String servletPath, String fileName, boolean inRoot, String realPath) {

//		String reqUrl = request.getRequestURL().toString();
		//String domain = reqUrl.substring(0, reqUrl.indexOf("/", 8));
		String sp = servletPath;
		sp = sp.replaceAll("//", "/");
		String script = sp.substring(sp.lastIndexOf("/") + 1);
		String[] scriptSplit = script.split("\\.");
		String layoutType = "";
		String[] dir = null; 
		String path = null;
		if (fileSep.equals("\\\\")) {
			dir = sp.split("\\");
			path = sp;  
		} else {
			dir = sp.split("/");
			path = sp; 
		}
		
		String reg = null;
		String check = "";
		File file = null;
		String include = null;
		if (scriptSplit.length>1) {
			layoutType = scriptSplit[scriptSplit.length-2];
		}
		String ext = "";
		if (scriptSplit.length>0) {
			ext = scriptSplit[scriptSplit.length-1];
		} else {
			ext = "jsp";
		}
		String layoutable = data.getLayoutExtensions() + ",";
		boolean isLayoutable = layoutable.indexOf(ext + ",")>-1;
		if (layoutType!=null&&isLayoutable) {
			for (int i=0;i<dir.length - 1;i++) {
				reg = "/" + dir[(dir.length - i) - 1];
				path = path.replaceAll(reg, "");
				check = realPath + path;

				if (!fileSep.equals("/")) {
					check =  check.replaceAll("/", fileSep + fileSep);
					check = check.endsWith("\\")?check.substring(0, check.length()-1):check;
					if (inRoot) {
						check += "\\" + data.getResourceDirectory() + "\\" + fileName;
					} else {
						check += "\\" + data.getResourceDirectory() + "\\" +  layoutType + "\\" + fileName;
					}
				} else {
					if (inRoot) {
						check += fileSep + "" + data.getResourceDirectory() + "" + fileSep + fileName;  
					} else {
						check += fileSep + "" + data.getResourceDirectory() + "" + fileSep + layoutType + "/" + fileName;  
					}
				}
//				System.out.println(i + " - " + realPath + " - " + path + " - "+ check + " - " + reg + " - " + sp);

				file = new File(check);
				if (file.exists()) {
					if (inRoot) {
						include = path.replaceAll("\\\\", "/") + "/" + data.getResourceDirectory() + "/" + fileName;
					} else {
						include = path.replaceAll("\\\\", "/") + "/" + data.getResourceDirectory() + "/" +  layoutType + "/" + fileName;
					}
					break;
				}
			}
		}
		if (include!=null) {
			include = include.replaceAll("//", "/");
		}
		return include;
    }
    
    public boolean setApiAttributes(Mapping mapped, HttpServletRequest request, HttpServletResponse response) {
    	obij = ObInject.getInstance();
//    	this.loadApis();
		String sp = request.getServletPath();
		String[] dirs = sp.split("/where/")[0].split("/id/")[0].split("/sort/")[0].split("/");
		String[] ids = new String[0];
		if (sp.indexOf("/id/")>-1) {
			ids = sp.split("/id/")[1].split("/sort/")[0].split("/");
		}
		String[] wheres = new String[0];
		if (sp.indexOf("/where/")>-1) {
			wheres = sp.split("/where/")[1].split("/sort/")[0].split("/");
		}
		String className = "";
		Integer pageIndex = 0;
		Integer items = 0;
		int classIndex = dirs.length - 1;
		String sort = "";
		if (sp.indexOf("/sort/")>-1) {
			sort = sp.split("/sort/")[1].split("/")[0];
		}
		/*if (dirs[classIndex].equals("sort")) {
			sort = dirs[classIndex + 1];
			classIndex --;
		}*/
		
		try {
			pageIndex = new Integer(dirs[classIndex-1]);
			try {
				items = new Integer(dirs[classIndex]);
				classIndex --;
				classIndex --;
			} catch(Exception e2) {
			}
		} catch(Exception e) {
			try {
				pageIndex = new Integer(dirs[classIndex]);
				classIndex --;
			} catch(Exception e3) {
				pageIndex = 0;
			}
		}
		className = dirs[classIndex];
		DataObject[] dataObjects = (DataObject[])obij.get(DataObject.class, "where name='" +className+"'");
		DataObject[] siblings = new DataObject[0];
		DataObject[] parents;
		DataObject parent = null;
		
		DataObject[] parentSiblings = new DataObject[0];
		DataObject[] children = new DataObject[0];
		int parentId;
		String path = "/";
		String parentPath = "/";
		DataObject o = null;
		String parentClassName = "";
		if (dataObjects.length!=0) {
			for (DataObject obj : dataObjects) {
				//parentClassName = dirs[classIndex--];
				o = obj;
			}
		} else {
//			System.out.println(dataObjects.length + " is length");
			//o = dataObjects[0]; 
			o = new DataObject();
		}
		//todo: exclude objects where parent doesn't match path
		siblings = (DataObject[])obij.get(DataObject.class, "where parentId=" +o.getParentId()+" and id <> " + o.getId());
		parents = (DataObject[])obij.get(DataObject.class, "where id=" +o.getParentId()+"");
		children = (DataObject[])obij.get(DataObject.class, "where parentId=" +o.getId()+"");
		if (parents.length>0) {
			parent = parents[0];
			parentSiblings = (DataObject[])obij.get(DataObject.class, "where parentId=" +parent.getParentId()+"");
			parentId = o.getParentId();
			while (parentId!=0) {
				if (!path.equals("/")) {
					parentPath = "/" + parent.getName() + parentPath;
				}
				path = "/" + parent.getName() + path;
				
				parents = (DataObject[])obij.get(DataObject.class, "where id=" +parentId+"");
				if (parents.length>0) {
					parentId = parents[0].getParentId();
				} else {
					parentId = 0;
				}
			}
		}
		
		path = mapped.getPath().substring(0, mapped.getPath().length()-1) + path;
		parentPath = mapped.getPath().substring(0, mapped.getPath().length()-1) + parentPath;
		JsonOutput output = new JsonOutput();
		output.setDisplayName(o.getDisplayName());
		output.setId(o.getId());
		output.setName(className);
		output.setParentId(o.getParentId());
		output.setItemsPerPage(items!=0?items:o.getDefaultItemsPerPage());
		output.setIds(ids); 
		output.setPageIndex(pageIndex);
		output.setChildren(children);
		output.setSort(sort); 
		output.setUri(path + "" + output.getName() + "/");
		output.setDefaultItemsPerPage(o.getDefaultItemsPerPage());
		if (output.getItemsPerPage()==0) {
			output.setItemsPerPage(20);
		}
		
		for (DataObject sibling : siblings) {
			sibling.setUri(path + "" + sibling.getName() + "/");
		}

		for (DataObject child : children) {
			child.setUri(path + output.getName() + "/" + child.getName() + "/");
		}

		for (DataObject sibling : parentSiblings) {
			sibling.setUri(parentPath + "" + sibling.getName() + "/");
		}

		//output.setParentSiblings(parentSiblings);
//		output.setSiblings(siblings);
		Gson gson = new Gson();
		
		ObInject oi = ObInject.getInstance();
		Class cls = oi.getClassByName(className);
		boolean hasId = false;
		Object tid;
		//System.out.println("cls: " + cls + " - " + className);
		if (cls!=null) {
			String q = " where 1=1";
			if (ids.length>0) {
				q += " and id=" + ids[0];
			}
			if (wheres.length>0) {
				q += " and " + wheres[0];
			}
			if (sort!=null&&sort.length()>0) {
				q += " order by " + sort;
			} else {
				try {
					tid = (Object)cls.getMethod("getId");
					q += " order by id";
				} catch(Exception e2) {
					e2.printStackTrace();
				}
			}
			//System.out.println("q: " + q);
			Object[] objs = (Object[])oi.get(cls, q);
			int pageSizeOut = output.getItemsPerPage();
			if (objs.length>0) {
				pageSizeOut = objs.length>pageSizeOut?pageSizeOut:objs.length;
			}

			if (objs.length>0&&(output.getPageIndex() * output.getItemsPerPage())+pageSizeOut>objs.length) {
				pageSizeOut = objs.length % (output.getPageIndex() * output.getItemsPerPage()); 
			}
			if ((pageSizeOut * output.getPageIndex())>(objs.length)&&output.getPageIndex()==0) {
				pageSizeOut = objs.length;
			}
			if ((pageSizeOut * output.getPageIndex())>=objs.length) {
				pageSizeOut = 0;
			}
			Object[] objOut = new Object[pageSizeOut];
			if (objs.length>0){
				for (int i=0;i<pageSizeOut;i++) {
					objOut[i] = objs[i + ((output.getPageIndex()) * pageSizeOut)];
				}
			} 
			
			output.setTotalItems(objs.length);
			output.setItems(objOut);
		}
		//SystemProperties data = PropWatcher.getInstance().getData();
		try {
//			if (mapped.getContentType()!=null) {
				response.setContentType("text/json");
//			}
			
			response.getWriter().write(gson.toJson(output));
			return false;
		} catch(Exception e) {
			e.printStackTrace();
		}
 
		
		request.setAttribute("api.output", output);
		return true;
    }
    
    public void destroy() {
    }
}

