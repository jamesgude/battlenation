/**
 * <p>Title: ObInject</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.oi;


import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import com.businesshaps.oi.DataSync;

public class ObInject {
    private String jdbc = null;
    private String driver = null;
    private static boolean nosql = false; 
    private static Hashtable<String, ObInject> instances = new Hashtable<String, ObInject>();

    public static String defaultJdbc = null;
    
    public String getJdbc() {
        return jdbc;
    }

    public static ObInject getInstance() {
    	return getInstance(defaultJdbc);
    }
    public static ObInject getInstance(String jdbc) {
    	ObInject ins;
        if (instances.containsKey(jdbc)) {
            ins = instances.get(jdbc);
        } else {
            ins = new ObInject();
            ins.setJDBC(jdbc);
            ins.syncTable(DataSync.class); 
            instances.put(jdbc, ins);
        }
        return ins;
    }

    private ObInject() {
    	
    }

    public Object set(Object o) {
    	if (nosql) {
    		return NoSQLAdapter.getInstance().set(o);
    	} else {
    		return set(o.getClass(), o, false, false);
    		
    	}
    }
    private String getFormattedValue(Class type, String field_name, String value) {
        SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aaa");

        Field fld = null;
        try {
            fld = type.getDeclaredField(field_name);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (value!=null) {
            value = value.substring(0, value.length()>7990?7990:value.length());
        }
        if (value==null||value.toLowerCase().equals("null")) {
            value = "NULL";
        } else if (fld == null) {
            if (value != null && !value.equals("null")) {
                value = "'" + value.replaceAll("'", "''") + "'";
            }
        } else if (fld.getType().getName().equals("java.lang.String")) {
            if (value != null && !value.equals("null")) {
                value = "'" + value.replaceAll("'", "''") + "'";
            }
        } else if (fld.getType().getName().equals("boolean") || fld.getType().getName().equals("java.lang.Boolean")) {
            if (value != null && value.equals("true") || value.equals("1")) {
                value = "1";
            } else {
                value = "0";
            }
        } else if (fld.getType().getName().equals("java.util.Date")) {
            if (value != null && !value.equals("null")) {
                value = "'" + formatTime.format(new java.util.Date(new Long(value.replaceAll("'", "''")))) + "'";
            }
        } else if (fld.getType().getName().equals("int") || fld.getType().getName().equals("java.lang.Integer")) {
            if (value == null || value.equals("") || value.equals("null")) {
                value = "0";
            }
        } else if (fld.getType().getName().equals("long") || fld.getType().getName().equals("java.lang.Long")) {
            if (value == null || value.equals("") || value.equals("null")) {
                value = "0";
            }
        }

        return value;
    }

/*
    public Object set(Object o) {
        return set(o.getClass(), o, false, false);
    }
*/
    public Object set(Object o, String match) {
    	if (nosql) return NoSQLAdapter.getInstance().set(o);
    	Object[] objs = (Object[])this.get(o.getClass(), match);
    	if (objs.length==0) {
    		return set(o.getClass(), o, false, false);
    	} else {
    		return objs[0];
    	}
    }

    public Object set(Object o, boolean quickInsert, boolean batch) {
    	if (nosql) {
    		return NoSQLAdapter.getInstance().set(o);
    	}
        return set(o.getClass(), o, false, batch);
    }
    private static Hashtable<String, Connection> connections = new Hashtable<String, Connection>();

    public static Connection handleConnection(String jdbc) throws SQLException {
    	if (nosql) {
    		return null;
    	}
        Connection conn = null;
        if (connections.contains(jdbc)) {
        	conn = connections.get(jdbc);
        } else {
	        if (jdbc.indexOf("?") > -1) {
	            String user = jdbc.split("username=")[1].split("\\|")[0];
	            String pass = jdbc.split("password=")[1].split("\\|")[0];
	            conn = DriverManager.getConnection(jdbc.split("\\?")[0], user, pass);
	        } else {
	            conn = DriverManager.getConnection(jdbc);
	        }
        }
        if (!keepAliveRunning) {
        	keepAliveRunning = true;
        	Thread thread = (new Thread() {
        		public void run() {
        			try {
        				keepAlive();
        				Thread.sleep(5000);
        			} catch(Exception e) {
        				
        			}
        		}
        	});
        	thread.start();
        }
        return conn;
    }
    private static boolean keepAliveRunning = false;
    
    public static void keepAlive() {
        Statement stmt;
        ResultSet rs = null;

    	for (String key : connections.keySet()) {
            try {
                stmt = connections.get(key).createStatement();
                rs = stmt.executeQuery("select 1=1");
                rs.close();
            } catch (Exception error) {
            }
    		
    	}
    }
    public Object set(Class type, Object o, boolean quickInsert, boolean batch) {
    	if (nosql) return NoSQLAdapter.getInstance().set(o);
    		
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = ObInject.handleConnection(jdbc);
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }

        String str = "";
        String fldName;
        String id;
        String sret;
        Integer iret;
        Long lret;
        Float fret;
        java.util.Date dret;
        Boolean bret;
        Hashtable<String, String> record_fields = new Hashtable<String, String>();
        str = "";
        for (Field fld : type.getDeclaredFields()) {
            fldName = fld.getName();
            fldName = Character.toUpperCase(fldName.charAt(0)) + fldName.substring(1);
            if (!fldName.toLowerCase().equals("children")) {
            	
	
	            try {
		            if (fldName.toLowerCase().equals("xdatelastmodified")) {
		                //record_fields.put("dateLastModified", new Long(new java.util.Date().getTime()).toString());
		            } else if (fld.getType().getName().equals("java.lang.String")) {
		            	sret = (String)type.getMethod("get" + fldName).invoke(o);
		            	if (sret!=null) record_fields.put(fld.getName(), sret); 
		            } else if (fld.getType().getName().equals("int") ) {
		            	iret = (Integer)type.getMethod("get" + fldName).invoke(o);
		            	if (iret!=null) record_fields.put(fld.getName(), iret.toString()); 
		            } else if (fld.getType().getName().equals("java.lang.Integer")) {
		            	iret = (Integer)type.getMethod("get" + fldName).invoke(o);
		            	if (iret!=null) record_fields.put(fld.getName(), iret.toString());
		            } else if (fld.getType().getName().equals("long")) {
		            	lret = (Long)type.getMethod("get" + fldName).invoke(o);
		            	if (lret!=null) record_fields.put(fld.getName(), lret.toString());
		            } else if (fld.getType().getName().equals("java.lang.Long")) {
		            	lret = (Long)type.getMethod("get" + fldName).invoke(o);
		            	if (lret!=null) record_fields.put(fld.getName(), lret.toString());
		            } else if (fld.getType().getName().equals("float")) {
		            	fret = (Float)type.getMethod("get" + fldName).invoke(o);
		            	if (fret!=null) record_fields.put(fld.getName(), fret.toString());
		            } else if (fld.getType().getName().equals("java.lang.Float")) {
		            	fret = (Float)type.getMethod("get" + fldName).invoke(o);
		            	if (fret!=null) record_fields.put(fld.getName(), fret.toString());
		            } else if (fld.getType().getName().equals("java.util.Date")) { 
		            	dret = (java.util.Date)type.getMethod("get" + fldName).invoke(o);
		            	if (dret!=null) record_fields.put(fld.getName(), new Long(dret.getTime()).toString());
		            } else if (fld.getType().getName().equals("boolean")) {
		            	bret = (Boolean)type.getMethod("is" + fldName).invoke(o);
		            	if (bret!=null) record_fields.put(fld.getName(), new Boolean(bret).toString());
		            } else if (fld.getType().getName().equals("java.lang.Boolean")) {
		            	bret = (Boolean)type.getMethod("is" + fldName).invoke(o);
		            	if (bret!=null) record_fields.put(fld.getName(), new Boolean(bret).toString());
		            } else {
		            	sret = (String)type.getMethod("get" + fldName).invoke(o);
		            	if (sret!=null) record_fields.put(fld.getName(), sret);
		            }
	            } catch(Exception e) {
	            	System.out.println("1: getting: " + fldName);
	            	e.printStackTrace();
	            }
            }
        }
        
        id = record_fields.get("id");

        StringBuffer query = new StringBuffer();
        String user_owner;
        String value;
        if (id != null && !id.equals("null") && !id.equals("") && !id.equals("0")) {
            query.append("UPDATE " + type.getSimpleName() + " SET");
            for (String record_field : record_fields.keySet()) {
                if (!record_field.toLowerCase().equals("id") && !record_field.toLowerCase().equals("file_upload")) {
                    value = this.getFormattedValue(type, record_field, record_fields.get(record_field));
                    query.append(", ").append(record_field).append(" = ").append(value.toString().replaceAll("\\\\", "\\\\\\\\")).append("");
                }
            }
            query.append(" WHERE ID = ").append(id).append("");
            query = new StringBuffer(query.toString().replaceAll("SET,", "SET"));
        } else {
            query.append("INSERT INTO " + type.getSimpleName() + " (");
            for (String record_field : record_fields.keySet()) {
                if (!record_field.toLowerCase().equals("id") && !record_field.toLowerCase().equals("file_upload")) {
                    query.append(", ").append(record_field).append("");
                }
            }
            query.append(") values (");
            for (String record_field : record_fields.keySet()) {
                if (!record_field.toLowerCase().equals("id") && !record_field.toLowerCase().equals("file_upload")) {
                    value = this.getFormattedValue(type, record_field, record_fields.get(record_field));
                    query.append(", ").append(value.toString().replaceAll("\\\\", "\\\\\\\\")).append("");
                }
            }
            query.append(")");
            query = new StringBuffer(query.toString().replaceAll("\\(,", "\\("));
        }
        try {
        	if (batch){
        		this.addBatch(query.toString());
        	} else {
	            stmt = conn.createStatement();
	            stmt.executeUpdate(query.toString());
	            if (id != null && !id.equals("0")||quickInsert) {
	            } else {
	                stmt = conn.createStatement();
	                rs = stmt.executeQuery(" SELECT MAX(ID) as ID FROM " + type.getSimpleName() + "");
	                rs.next();
	                id = rs.getString("ID");
	                o = this.get(type, new Integer(id));
	            }
        	}
        } catch (Exception e) {
            System.out.println(query);
            e.printStackTrace();
        }

        try {
//            conn.close();
        } catch (Exception error) {
        }

        return o;
    }
    
    public Object get(Class type, int id) {
        Object rtn = null;
        Object[] rtns = get(type, "where id = " + id + "");
        if (rtns.length > 0) {
            rtn = rtns[0]; 
        } else {
            rtn = null;
        } 
        return rtn;
    }

    public ResultSet execute(String query) {
        Connection conn;
        Statement stmt;
        ResultSet rs = null;
        try {
            conn = ObInject.handleConnection(jdbc);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query.toString());
//            conn.close();
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }

        return rs;
    }

    public void executeUpdate(String query) {
    	if (nosql) return;
        Connection conn;
        Statement stmt;
        ResultSet rs = null;
        try {
            conn = ObInject.handleConnection(jdbc);
            stmt = conn.createStatement();
            stmt.executeUpdate(query.toString());
//            conn.close();
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }
    }
	private boolean didBatch = false;
    private Statement batchStmt = null;
    public void addBatch(String query) {
        Connection conn;
        
        try {
        	if (batchStmt==null) {
	            conn = ObInject.handleConnection(defaultJdbc);
	            batchStmt = conn.createStatement();
        	}
        	batchStmt.addBatch(query.toString());
//            conn.close();
        } catch (Exception error) {
            error.printStackTrace();

        }
    }
    public void clearBatch() {
    	try {
    		batchStmt.clearBatch();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	didBatch = false;
    	batchStmt = null;
    }
    
    public void executeBatch() {
    	try {
    		batchStmt.executeBatch();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	didBatch = false;
    	batchStmt = null;
    }

    public Object[] get(Class type, String where) {
    	if (nosql) {
    		return NoSQLAdapter.getInstance().get(type, where);	
    	} else {
    		return this.get(type, where, 0, 0);
    	}
    }

    public Object[] get(Class<? extends Object> type, String where, int start, int length) {
    	this.syncTable(type, true);
        SimpleDateFormat formatsql = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aaa");
        SimpleDateFormat formatsqlDash = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Hashtable<Integer, Hashtable<String, String>> recordset = getObjectData(type.getSimpleName(), where, start, length);
        Hashtable<String, String> record;
        String value;
        Object[] o = new Object[0];
        ArrayList<Object> objects = new ArrayList<Object>();
        String str = "";
        String fldName;
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");
        java.util.Date date;
        Object obj;

    	//System.out.println(recordset.size() + " is whatevr");
            for (int j = 0; j < recordset.size(); j++) {
                record = recordset.get(j);
                for (String field : record.keySet()) {
                    value = record.get(field);
                }

                str = "";
                str += type.getName() + " o = new " + type.getName() + "();\n";
                try {
                	obj = type.newInstance();
                } catch(Exception e) {
                	obj = null;
                	e.printStackTrace();
                }
                for (Field fld : type.getDeclaredFields()) {
                	
                    fldName = fld.getName();
                    fldName = Character.toUpperCase(fldName.charAt(0)) + fldName.substring(1);
                    if (!fldName.toLowerCase().equals("children")) {
                    	
	
	                    value = record.get(fld.getName().toUpperCase());
	                    //value = value==null?"":value;
	                    if (!fldName.equals("html")) {
	                        value = value.replaceAll("\n", "");
	                    } else {
	                        value = value.replaceAll("\n", "");
	                    }
	                    value = value.replaceAll("\\\\+", "/");
	                    value = value.replaceAll("\"", "'");
	                    try {
		                    if (fld.getType().getName().equals("java.lang.String")) {
		                        type.getMethod("set" + fldName, String.class).invoke(obj, value);
		                    } else if (fld.getType().getName().equals("boolean")) {
		                        if (value.equals("1") || value.equals("true")) {
		                            value = "true";
		                        } else {
		                            value = "false";
		                        }
		                        type.getMethod("set" + fldName, boolean.class).invoke(obj, value.equals("true"));
		                    } else if (fld.getType().getName().equals("java.lang.Boolean")) {
		                        if (value.equals("1") || value.equals("true")) {
		                            value = "true";
		                        } else {
		                            value = "false";
		                        }
		                        type.getMethod("set" + fldName, Boolean.class).invoke(obj, value);
		                    } else if (fld.getType().getName().equals("java.util.Date")) {
		                        date = null;
		                        try {
		                            if (value.indexOf("-") > -1) {
		                                date = formatsqlDash.parse(value);
		                            } else {
		                                date = formatsql.parse(value);
		                            }
		                            //System.out.println("Good Date: " + date);
		                        } catch (Exception e) {
		                            //System.out.println("Bad Date: " + value);
		                            try {
		                                date = format.parse(value);
		                            } catch (Exception e2) {
		                                try {
		                                    date = formatTime.parse(value);
		                                } catch (Exception e3) {
		                                    date = null;
		                                }
		                            }
		                        }
		                        if (date != null) {
		                            type.getMethod("set" + fldName, java.util.Date.class).invoke(obj, date);
		                        }
		                    } else if (fld.getType().getName().equals("int")) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        }

		                        //test
		                        type.getMethod("set" + fldName, int.class).invoke(obj, new Integer(value).intValue());
		                    } else if (fld.getType().getName().equals("java.lang.Integer")) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        }
		                        type.getMethod("set" + fldName, Integer.class).invoke(obj, new Integer(value));
		                    } else if (fld.getType().getName().equals("long") ) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        } 
		                        type.getMethod("set" + fldName, long.class).invoke(obj, new Long(value).longValue());
		                    } else if ( fld.getType().getName().equals("java.lang.Long")) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        }
		                        type.getMethod("set" + fldName, Long.class).invoke(obj, new Long(value));
		                    } else if (fld.getType().getName().equals("float") ) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        }
		                        type.getMethod("set" + fldName, float.class).invoke(obj, new Float(value));
		                    } else if (fld.getType().getName().equals("java.lang.Float")) {
		                        if (value == null || value.equals("")) {
		                            value = "0";
		                        }
		                        type.getMethod("set" + fldName, Float.class).invoke(obj, new Float(value).floatValue());
		                    }
	                    } catch (Exception e) {
	                    	System.out.println("2: setting: " + fldName);
	                        e.printStackTrace();
	                    }
	                }
                    
                }
                objects.add(obj);

            }
            Object oo = null;
            if (objects.size()>0) {
	            oo = java.lang.reflect.Array.newInstance(type, objects.size());
	            int i = 0;
	            for (Object item: objects) {
	                java.lang.reflect.Array.set(oo,  i, item);
	                i++;
	            }
	            o = (Object[])oo;
            } else {
            	o = (Object[])java.lang.reflect.Array.newInstance(type, 0);
            }
            
//o = Arrays.copyOf(objects, objects.size(), type);
            //o = objects.toArray(new Object[objects.size()]);
            
        return o;
    }

    public void truncateTable(Class type) {
        String table_name = type.getSimpleName().toUpperCase();
        this.executeUpdate("drop table " + table_name);
        this.syncTable(type);
    }

    private Hashtable<String, Class> classes = new Hashtable<String, Class>();

    public void truncateTable(String table_name) {
        this.executeUpdate("drop table " + table_name);
        if (classes.get(table_name.toUpperCase()) != null) {
            this.syncTable(classes.get(table_name.toUpperCase()));
        }
    }
    public Class getClassByName(String name) {
    	return classes.get(name.toUpperCase());
    }
    public void syncTable(Class type, boolean initial) {
    	if (classes.containsKey(type.getSimpleName().toUpperCase())) {
    		return;
    	} else {
    		this.syncTable(type);
    	}
    }
    public void syncTable(Class type) {
        classes.put(type.getSimpleName().toUpperCase(), type);

   
        String table_name = type.getSimpleName().toUpperCase();
        if (nosql) {
        	
        	return;
        }
        
        Statement stmt;
        Connection conn;
        ResultSet rs;
        //System.out.println("3");
        try {
            conn = ObInject.handleConnection(jdbc);
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }
        boolean database_exists = false;
        boolean table_exists = false;
        StringBuffer table_fields;
        String varchar = "varchar(8000)";
        if (jdbc.split(":")[1].toLowerCase().equals("sqlserver")) {
        } else if (jdbc.split(":")[1].toLowerCase().equals("derby")) {
        } else {
            varchar = "text";
        }
        if (conn != null) {
            //check if database exists
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM " + table_name + " WHERE 1 = 2");
                database_exists = true;
                table_exists = true;
                rs.close();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (!table_exists) {
                if (!type.getSimpleName().equals("DataSync")) {
                    DataSync dataSync = new DataSync();
                    dataSync.setLongName(type.getName());
                    dataSync.setShortName(type.getSimpleName());
                    dataSync.setSelector(type.getSimpleName().toLowerCase());
                    ObInject obij = ObInject.getInstance(jdbc);
                    obij.set(dataSync);

                }

                table_fields = new StringBuffer();
                table_fields.append("CREATE TABLE " + table_name + " (");
                if (jdbc.split(":")[1].toLowerCase().equals("sqlserver")) {
                    table_fields.append(" ID int IDENTITY(1,1) NOT NULL ");
                } else if (jdbc.split(":")[1].toLowerCase().equals("derby")) {
                    table_fields.append(" ID int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ");
                } else {
                    table_fields.append(" ID int NOT NULL AUTO_INCREMENT, PRIMARY KEY (ID) ");
                }
                table_fields.append(")");
                try {
                    System.out.println("Query Create: " + table_fields);
                    stmt = conn.createStatement();
                    stmt.executeUpdate(table_fields.toString());
//                    table_exists = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 

            //2 check if fields exist


            String name;
            String[] values;
            Hashtable<String, String> record_fields = new Hashtable<String, String>();
            String field_name;
            StringBuffer add_fields = new StringBuffer();
            ArrayList<String> current_fields = new ArrayList<String>();
            String record_field;
            try {
                stmt = conn.createStatement();
                table_fields = new StringBuffer();

                rs = stmt.executeQuery("SELECT * FROM " + table_name + " WHERE 1 = 2");

                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    field_name = rs.getMetaData().getColumnName(i + 1).toUpperCase();
                    current_fields.add(field_name);
                }

                rs.close();
                boolean do_addfields = false;
                for (Field fld : type.getDeclaredFields()) {
                    record_field = fld.getName();
                    if (!record_field.toLowerCase().equals("children")) {
	                    	
	                    if (!current_fields.contains(record_field.toUpperCase())) {
	                        System.out.println("add_fields: " + record_field + " - " + table_name + " - " + fld.getType().toString());
	                        if (fld.getType().toString().equals("boolean")) {
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " smallint");
	                        } else if (fld.getType().toString().equals("java.util.Date")) {
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " datetime");
	                        } else if (fld.getType().toString().equals("int")) {
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " int");
	                        } else if (fld.getType().toString().equals("long")) {
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " bigint");
	                        } else if (fld.getType().toString().equals("float")) {
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " float");
	                        } else {
	                            System.out.println("ALTER TABLE " + table_name + " add " + record_field + " " + varchar);
	                            stmt.executeUpdate("ALTER TABLE " + table_name + " add " + record_field + " " + varchar);
	                        }
	
	                        do_addfields = true;
	                    }
                    }
                }


                if (!table_exists) {
                    table_fields = new StringBuffer();
                    table_fields.append("");
                    stmt = conn.createStatement();
                    try {
                        stmt.executeUpdate("ALTER TABLE " + table_name + " add PARENTID int ");
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    try {
                        stmt.executeUpdate("ALTER TABLE " + table_name + " add  TITLE " + varchar);
                    } catch (Exception e) {
                        //     e.printStackTrace();
                    }

                    try {
                        stmt.executeUpdate("ALTER TABLE " + table_name + " add  DESCRIPTION " + varchar);
                    } catch (Exception e) {
                        //   e.printStackTrace();
                    }

                    try {
                        stmt.executeUpdate("ALTER TABLE " + table_name + " add  DELETED  " + varchar);
                    } catch (Exception e) {
                        //     e.printStackTrace();
                    }

                    table_exists = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
//                conn.close();
            } catch (Exception error) {
            }
        }
    }

    public void setJDBC(String jdbc) {
        this.jdbc = jdbc;
        String driver = "";
        if (jdbc.split(":")[1].toLowerCase().equals("sqlserver")) {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (jdbc.split(":")[1].toLowerCase().equals("derby")) {
            driver = "org.apache.derby.jdbc.EmbeddedDriver";
        } else {
            driver = "com.mysql.jdbc.Driver";
        }
        try {
            Driver d = (Driver) Class.forName(driver).newInstance();
        } catch (Exception error) {
            error.printStackTrace();
        }
        this.driver = driver;
    }

    public void setJDBC(String jdbc, String driver) {
        this.jdbc = jdbc;

        try {
            Driver d = (Driver) Class.forName(driver).newInstance();
        } catch (Exception error) {
            error.printStackTrace();
        }
        this.driver = driver;
    }

    public Hashtable<Integer, Hashtable<String, String>> getObjectData(String tableName, String where) {
        return this.getObjectData(tableName, where, 0, 0);
    }
    public Hashtable<Integer, Hashtable<String, String>> getObjectData(String tableName, String where, int start, int length) {
        Hashtable<Integer, Hashtable<String, String>> rtn = new Hashtable<Integer, Hashtable<String, String>>();
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = ObInject.handleConnection(jdbc);
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }
        Hashtable rec;
        String qry = "";
        int itemIndex = 0;
        int itemPrint = 0;
        try {
            stmt = conn.createStatement();
            qry = "SELECT * FROM " + tableName + " " + where;
            //System.out.println(qry);
            rs = stmt.executeQuery(qry);
            String fid;
            String ftitle;
            boolean breakWhile = false;
            String field;
            String value;
            if (rs != null) {
                while (rs.next() && !breakWhile) {
                    if (itemIndex >= start) {
                        itemPrint++;
//                    fid = rs.getString("ID");
                        //ftitle = rs.getString("TITLE");
                        rec = new Hashtable<String, String>();
                        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                            field = rs.getMetaData().getColumnName(i + 1).toLowerCase();
                            value = rs.getString(field);
                            if (value == null) {
                                value = "";
                            }
                            rec.put(field.toUpperCase(), value);
                        }
                        
                        if ((rec.get("DELETED") != null && rec.get("DELETED").equals("1"))) {

                        } else {
                            rtn.put(rtn.size(), rec);
                        }
                    }
                    itemIndex++;
                    if (itemPrint >= length && length != 0) {
                       // breakWhile = true;
//                        break;

                    }
                    //                System.out.println(title);
                }
                rs.next();
                rs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QRY: " + qry);
        }
        try {
//            conn.close();
        } catch (Exception error) {
        }
        return rtn;
    }

    public boolean dbObjectExists(String dbobject) {
        boolean exists = false;
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = ObInject.handleConnection(jdbc);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + dbobject + " WHERE 1 = 2");
            exists = true;
            rs.close();
            stmt.close();
//            conn.close();
        } catch (Exception e) {
//                e.printStackTrace();
        }
        return exists;
    }

    public Hashtable<String, Hashtable<String, String>> getObjectData(String query) {
        Hashtable<String, Hashtable<String, String>> rtn = new Hashtable<String, Hashtable<String, String>>();
        Connection conn;
        Statement stmt;
        ResultSet rs;
        try {
            conn = ObInject.handleConnection(jdbc);
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }
        Hashtable rec;
        int idind = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            String fid;
            String ftitle;
            String field;
            String value;
            while (rs.next()) {
                rec = new Hashtable<String, String>();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    field = rs.getMetaData().getColumnName(i + 1).toLowerCase();
                    value = rs.getString(field);
                    if (value == null) {
                        value = "";
                    }
                    rec.put(field.toUpperCase(), value);
                }
                idind++;
                rtn.put(Integer.toString(idind), rec);
//                System.out.println(title);
            }
            rs.next();
            rs.close();
//            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public String[] getTableHeaderFieldNames(String table_name) {
        String[] rtn = new String[0];

        Statement stmt;
        Connection conn;
        ResultSet rs;
        //System.out.println("3");
        try {
            conn = ObInject.handleConnection(jdbc);
        } catch (Exception error) {
            error.printStackTrace();
            conn = null;
        }
        boolean database_exists = false;
        boolean table_exists = false;
        StringBuffer table_fields;
        String field_name;

        ArrayList<String> current_fields = new ArrayList<String>();

        try {
            stmt = conn.createStatement();
            table_fields = new StringBuffer();

            rs = stmt.executeQuery("SELECT * FROM " + table_name + " WHERE 1 = 2");

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                field_name = rs.getMetaData().getColumnName(i + 1).toUpperCase();
                current_fields.add(field_name);
//                    System.out.println("Current Field: " + field_name);
            }
            rs.close();
            stmt.close();
//            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return current_fields.toArray(new String[current_fields.size()]);
    }

 
}