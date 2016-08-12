package com.businesshaps.params;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.oi.DataObject;
import com.businesshaps.oi.ObInject;
import com.google.gson.Gson;

public class PropWatcher {
	private SystemProperties data = null;
	private APIs apis = null;
	private ObInject obij = null;
	public static String homePath = null;
	
	public SystemProperties getData() {
		return data;
	}
	public APIs getApis() {
		return apis;
	}
	private static PropWatcher instance = null;
	public static PropWatcher getInstance() {
		if (instance==null) {
			instance = new PropWatcher();
		}
		
		return instance;
	}
	private PropWatcher() {
    	this.loadConfig(); 
    	final PropWatcher tthis = this;
    	Thread t = (new Thread() {
    		public void run() {
    			boolean runOnce = false;
    			while (true) {
    				try {
	    		    	tthis.loadConfig(); 
	    		    	if (!runOnce) {
	    		    	}
	    		    	runOnce = true;
    					Thread.sleep(1000);
    				} catch(Exception e) {
    					e.printStackTrace();
    				}
    			}
    			
    		}
    	});
    	t.start();

	}

    public void handleApis(API[] apis, DataObject parent) {
    	DataObject dob;
    	for (API api : apis) {
    		dob = new DataObject();
    		dob.setDisplayName(api.getDisplayName());
    		dob.setName(api.getName());
    		dob.setDefaultItemsPerPage(api.getDefaultItemsPerPage());
    		//dob.setParent(parent) 
    		if (parent!=null) {
    			dob.setParentId(parent.getId());
    		}
			dob = (DataObject)obij.set(dob, "where parentId=" + dob.getParentId() + " and name='" + api.getName() + "'");
    		//System.out.println("API: " + dob.getName() + " - " + dob.getParentId());
    		if (api.getChildren()!=null) {
    			this.handleApis(api.getChildren(), dob);
    		}
    	}
    } 
    private long configLastModified = 0;
    public void loadConfig() {
    	String fileSep = System.getProperty("file.separator");
    	//String homePath = filterConfig.getServletContext().getRealPath("") + fileSep + "WEB-INF";
    	//String dbHomePath = filterConfig.getServletContext().getRealPath("") + fileSep + ".." + fileSep + ".." + fileSep + ".." + fileSep + "data";
    	String dbPath = homePath + fileSep + "data";
    	dbPath = dbPath.replaceAll("\\\\", "\\\\\\\\");
    	String configFile = homePath + fileSep + "am.json";
		String json = null;
		File file = new File(configFile);
		long lastModified = file.lastModified();
		if (lastModified==configLastModified) {
			return;
		}
		configLastModified = lastModified;

		try {
		    BufferedReader br = new BufferedReader(new FileReader(configFile)); 
		    try { 
		        StringBuilder sb = new StringBuilder(); 
		        String line = br.readLine(); 
		 
		        while (line != null) { 
		            sb.append(line); 
		            sb.append("\n"); 
		            line = br.readLine(); 
		        } 
		        json = sb.toString(); 
		    } catch(Exception e) { 
		    	e.printStackTrace();
		    } finally { 
		        br.close(); 
		    } 
		} catch(Exception e) {
			e.printStackTrace();
		}
//		String json = sbconfig.toString();
		
		Gson gson = new Gson();
		data = gson.fromJson(json, SystemProperties.class); 
		if (data.getResourceDirectory()==null) {
			data.setResourceDirectory("_resource");
		}
		if (data.getBypassExtensions()==null) {
			data.setBypassExtensions("js, css, png, jpg, jpeg, gif");
		}
		if (data.getBypassDirectories()==null) {
			data.setBypassExtensions("/img/,/js/");
		}
		
		if (data.getLayoutExtensions()==null) {
			data.setLayoutExtensions("jsp, htm, html");
		}
		if (data.getJdbc()==null) {
			data.setJdbc("jdbc:derby:#local#;create=true");
		}
		
		data.setJdbc(data.getJdbc().replaceAll("#local#", dbPath));
		System.out.println("#### Loading Config " + homePath + " jdbc: " + data.getJdbc());

        ObInject.defaultJdbc = data.getJdbc();
		
    	obij = ObInject.getInstance();
    	if (initialized) {
    		this.loadAfter();
    	}

    }
    
    private void loadAfter() {
    	if (data.getGroups()!=null&&data.getGroups().length>0) {
    		AppGroup[] appGroups;
    		for (AppGroup grp : data.getGroups()) {
    			appGroups = (AppGroup[])obij.get(AppGroup.class, "where website = '"+grp.getWebsite()+"'");
    			
    			if (appGroups!=null&&appGroups.length>0) {
    				grp.setId(appGroups[0].getId());
    			}
    				grp.setDomain(data.getDomain());
    				grp.setGroupAlphaId("CMP_" + new Date().getTime());
    				System.out.println("Adding group: " + grp.getWebsite());
    				obij.set(grp);
    			//}
    		}
    	}
        Class cls;
        if (data.getSyncs()!=null) {
        	for (com.businesshaps.params.Sync sync : data.getSyncs()) { 
        		try {
	        		cls = Class.forName(sync.getPath());
	        		obij.syncTable(cls);
        		} catch(Exception e) {
        			e.printStackTrace();
        		}
        	}
        }

		try {
			obij.truncateTable(DataObject.class);
		} catch(Exception e) {
			
		}
		

		this.handleApis(data.getApis(), null);
    }
    private boolean initialized = false;
    public void initialized() {
		this.loadAfter();

    	initialized = true;
    }

}
