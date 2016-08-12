package com.businesshaps.oi;
import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReflectionDBObject;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class NoSQLAdapter {
	private static NoSQLAdapter instance = null;
	private static String server = "localhost";
	private static Integer port = 27017;
	private static String database = "test";
	public static NoSQLAdapter getInstance() {
		if (instance==null) {
			instance = new NoSQLAdapter();
		}
		return instance;
	}
	private static String pattern = "MM/dd/yyyy HH:mm:ss.S zzz";
    private static SimpleDateFormat format = new SimpleDateFormat(pattern);
    private static Gson gson = new GsonBuilder().setDateFormat(pattern).create();
    //private static DB db = null;
	private NoSQLAdapter() {
		//YuiCompressor("C:\\Java\\apache-tomcat-7.0.25\\am\\ROOT\\WEB-INF\\gudeam\\combineFile\\_js_combine_jsfile.js", "C:\\Java\\apache-tomcat-7.0.25\\am\\ROOT\\WEB-INF\\gudeam\\combineFile\\_js_combine_jsfile.out.js");
		AppUser o = new AppUser();
		try {
			//System.out.println("New Database Connection: " + server + " - " + port );
			//db = new MongoClient( server , port ).getDB(database);
		} catch(Exception e) {
			e.printStackTrace();
		}
/*
		o.setUsername("testa");
		o.setId(1);
		//this.set(o);
		AppGroup[] objs = (AppGroup[])this.get(AppGroup.class, "");
		for (AppGroup j : objs) {
			System.out.println(j.getWebsite() + " - " + j.getGroupAlphaId());
		}*/
	/*	
			for (String s : colls) {
			    System.out.println(s);
			}
*/
	}
	
	public Object set(Object o) {
		try {
			MongoClient client = new MongoClient( server , port );
			DB db = client.getDB(database);
			Class type = o.getClass();
			DBCollection coll = db.getCollection(type.getSimpleName());

			
			String fldName; String fldNameForGet;
			BasicDBObject dob = new BasicDBObject();
			String id = "";
			Object val;
			Date d;
	        for (Field fld : type.getDeclaredFields()) {
	            fldName = fld.getName();
	            fldNameForGet = Character.toUpperCase(fldName.charAt(0)) + fldName.substring(1);
	            //System.out.println(fldName);
	            if (fldName.toLowerCase().equals("id")) {
		            if (fld.getType().getName().equals("int")||fld.getType().getName().equals("java.lang.Integer")) {
		            	id = new Integer((Integer)type.getMethod("get" + fldNameForGet).invoke(o)).toString();
		            } else {
		            	id = (String)type.getMethod("get" + fldNameForGet).invoke(o);
		            }	

	            }
	            if (fld.getType().getName().equals("boolean")||fld.getType().getName().equals("java.lang.Boolean")) {
	            	dob.append(fldName, type.getMethod("is" + fldNameForGet).invoke(o));
	            } else if (fld.getType().getName().equals("java.util.Date")) {
	            	val = type.getMethod("get" + fldNameForGet).invoke(o);
	            	if (val!=null) {
	            		d = (Date)val;;
	            		val = format.format(d);
	            	}
	            	dob.append(fldName, val);
	            } else {
	            	val = type.getMethod("get" + fldNameForGet).invoke(o);
	            	if (val!=null) {
	            		//val = val.toString();
	            	}
	            	dob.append(fldName, val);
	            }	
	        }
	
			Set<String> colls = db.getCollectionNames();
			
			BasicDBObject obj = new BasicDBObject();
			
	        if (id.equals("")||id.equals("0")) {
	        	id = new Long(coll.getCount() + 1).toString();
	        	dob.append("id", id);
				coll.insert(dob);
			   dob.removeField("_id");
		       o = gson.fromJson(dob.toString(), type);

	        } else {
	        	dob.append("id", id);
				coll.update(new BasicDBObject().append("id", id), dob);
	        }
	        
		   db.cleanCursors(true);
		   client.close();
	        //System.out.println(dob.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return o;
	}
	public Object[] get(Class type, BasicDBObject query) {
		if (query==null) {
			query = new BasicDBObject();
		}
	    
		ArrayList objs = new ArrayList();
		try {
			MongoClient client = new MongoClient( server , port );
			DB db = client.getDB(database);
			DBCollection coll = db.getCollection(type.getSimpleName());


			//System.out.println(coll.getCount() + " - " + type.getSimpleName() + " - " + query.toString());
			
			DBCursor cursor = coll.find(query);
			String json;
			//Gson gson = new Gson();
			
			DBObject dbob;
			try {
			   while(cursor.hasNext()) {
			       dbob = cursor.next();
			       dbob.removeField("_id");
			       //System.out.println(dbob.toString());
			       objs.add(gson.fromJson(dbob.toString(), type));
			       
			   }
			} finally {
			   cursor.close();
			   db.cleanCursors(true);
			   client.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Object oo = null;
		Object[] o;
        if (objs.size()>0) {
            oo = java.lang.reflect.Array.newInstance(type, objs.size());
            int i = 0;
            for (Object item: objs) {
                java.lang.reflect.Array.set(oo,  i, item);
                i++;
            }
            o = (Object[])oo;
        } else {
        	o = (Object[])java.lang.reflect.Array.newInstance(type, 0);
        }
		return o;
	}
	public Object[] get(Class type, String where) {
		BasicDBObject query = new BasicDBObject();
		if (where!=null&&where.indexOf("where")>-1) {
			String[] wheres = where.split("where")[1].split("order ")[0].split(" and | or ");
			String[] crit;
			String t = ""; String v = "";
			for (String c : wheres) {
				crit = c.split("=|<>");
				try {
					t = crit[0].replaceAll(" ", "");
					v = crit[1].trim();
				} catch(Exception e) {
					v = "";
					//System.out.println(crit);
				}

				
				//System.out.println("c: " + c + " - title:(" + t + ") - val:(" + v + ")");
				try {
					query.append(t, new Integer(v));
				} catch(Exception e) {
					v = v.replaceAll("'", "").trim();
					query.append(t, v);
				} finally {
					
				}

				
			}
			//System.out.println(query.toString());
		}
		return this.get(type, query);
	}
	public static void main(String[] args){
		NoSQLAdapter.getInstance();
	}
}
