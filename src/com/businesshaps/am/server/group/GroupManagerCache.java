/**
 * <p>Title: GroupManagerCache</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.group;


import com.businesshaps.am.businessobjects.AppGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

public class GroupManagerCache implements GroupManager {
    private Hashtable<Integer, AppGroup> groups;
    private Hashtable<String, AppGroup> groupByGroupAlphaId;
    private Hashtable<String, Hashtable<String, AppGroup>> groupsByGroupTypeAlphaId;
    private Hashtable<String, Hashtable<String, AppGroup>> groupsByParentGroupAlphaId;
    private Hashtable<String, Hashtable<String, AppGroup>> groupsByOwner;

    public GroupManagerCache() {
        this.reload();
    }

    public AppGroup[] getGroup() {
        return groups.values().toArray(new AppGroup[groups.values().size()]);
    }


    public AppGroup getGroupByAlphaId(String groupAlphaId) {
        return groupByGroupAlphaId.get(groupAlphaId.toLowerCase());
    }

    public AppGroup getGroupByGroupId(Integer groupId) {
        return groups.get(groupId);
    }

    public AppGroup setGroup(AppGroup group) {
        this.groups.put(group.getId(), group);
        //System.out.println(group.getGroupAlphaId() + " group id");
        if (group.getGroupAlphaId()!=null) {
        		this.groupByGroupAlphaId.put(group.getGroupAlphaId().toLowerCase(), group);
        }

        Hashtable<String, AppGroup> superlist;
        if (group.getGroupTypeAlphaId()!=null) {
	        if (groupsByGroupTypeAlphaId.containsKey(group.getGroupTypeAlphaId().toLowerCase())) {
	            superlist = groupsByGroupTypeAlphaId.get(group.getGroupTypeAlphaId().toLowerCase());
	        } else {
	            superlist = new Hashtable<String, AppGroup>();
	        }
	        superlist.put(group.getGroupAlphaId(), group);
	        groupsByGroupTypeAlphaId.put(group.getGroupTypeAlphaId().toLowerCase(), superlist);
        }

        return group;
    }

    public void reload() {
        this.groups = new Hashtable<Integer, AppGroup>();
        this.groupByGroupAlphaId = new Hashtable<String, AppGroup>();
        this.groupsByGroupTypeAlphaId = new Hashtable<String, Hashtable<String, AppGroup>>();
        this.groupsByParentGroupAlphaId = new Hashtable<String, Hashtable<String, AppGroup>>();
        this.groupsByOwner = new Hashtable<String, Hashtable<String, AppGroup>>();
    }

    public AppGroup[] getGroupByGroupTypeAlphaId(String groupTypeAlphaId) {
        Hashtable<String, AppGroup> superlist;
        if (groupsByGroupTypeAlphaId.containsKey(groupTypeAlphaId.toLowerCase())) {
            superlist = groupsByGroupTypeAlphaId.get(groupTypeAlphaId.toLowerCase());
        } else {
            superlist = new Hashtable<String, AppGroup>();
        }
        AppGroup[] rtn = superlist.values().toArray(new AppGroup[superlist.size()]);
        Arrays.sort(rtn, new Comparator<AppGroup>() {
            public int compare(AppGroup o1, AppGroup o2) {
                return o2.getGroupName().compareTo(o1.getGroupName());
            }
        });
        return rtn;
    }

    public AppGroup[] getGroupByFilter(String parentGroupAlphaIds, String groupTypeAlphaIds, String textSearch) {
        ArrayList<AppGroup> search = new ArrayList<AppGroup>();
        boolean exclude;
        boolean textInclude;

        for (AppGroup group : groups.values()) {
            exclude = false;

            if (!exclude && textSearch != null && !textSearch.equals("")) {
                textInclude = false;
                if (!textInclude && new String(group.getGroupName() != null ? group.getGroupName().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }

                if (!textInclude && new String(group.getGroupAlphaId() != null ? group.getGroupAlphaId().toLowerCase() : "").indexOf(textSearch.toLowerCase()) != -1) {
                    textInclude = true;
                }

                exclude = (!textInclude);

            }

            if (!exclude && groupTypeAlphaIds != null && !groupTypeAlphaIds.equals("")) {
                if (groupTypeAlphaIds.indexOf(group.getGroupTypeAlphaId() != null ? group.getGroupTypeAlphaId() : "null") == -1) {
                    exclude = true;
                }
            }

            if (!exclude && parentGroupAlphaIds != null && !parentGroupAlphaIds.equals("")) {
                if (parentGroupAlphaIds.indexOf(group.getParentGroupAlphaId() != null ? group.getParentGroupAlphaId() : "null") == -1) {
                    exclude = true;
                }
            }

            if (!exclude) {
                search.add(group);
            }
        }
        return search.toArray(new AppGroup[search.size()]);
    }

    private String stripDomain(String url) {
//        System.out.println("url: " + url);
        if (url.indexOf("://")>-1) {
            url = url.substring(url.indexOf("/", 8), url.length());
        }
        return url;
    }

    private String stripPort(String url) {
        int portPos = url.indexOf(":", 8);
        if (portPos > -1) {
            String nurl = url.substring(0, portPos);
            nurl += url.substring(url.indexOf("/", portPos), url.length());
            url = nurl;
        }
        return url;
    }

    public AppGroup getGroupByURL(String url) {
        AppGroup rtn = this.getGroupByAlphaId("CMP_ROOT");
        url = stripPort(url);
        String website;
        url = url.endsWith("/")?url:url + "/";
        //url = stripDomain(url);
        String furl;
        ArrayList<String> awebsites = new ArrayList<String>();
        String[] websites = new String[0];
        String[] domains;
        for (AppGroup group : groups.values()) {
            awebsites = new ArrayList<String>();
            website = group.getWebsite()!=null?group.getWebsite().toLowerCase():"";
            website = website.endsWith("/") ? website : website + "/";
            website = stripDomain(website);
            website = website.replaceAll("http://", "");
            website = website.replaceAll("https://", "");
            if (group.getDomain()!=null) {
                domains = group.getDomain().split(";");
                for (String d : domains) {
                    awebsites.add(stripPort(d + website));
                }
                websites = awebsites.toArray(new String[awebsites.size()]);
                //website = group.getDomain() + website;
                furl = url;
            } else {
                furl = stripDomain(url);
            }
            furl = furl.replaceAll("http://", "");
            furl = furl.replaceAll("https://", "");
            furl = furl.replaceAll("www.", "");

//            System.out.println("furl: " + furl + " - website: " + website);
            boolean hasstart = false;
            boolean equalsurl = false;
            for (String ws : websites) {
                if (furl.startsWith(ws)) {
                    hasstart = true;
                    break;
                }
            }
            //System.out.println(rtn + " - " + url);
//            System.out.println("Has Start: " + hasstart + " - " + stripDomain(group.getWebsite().endsWith("/") ? group.getWebsite() : group.getWebsite() + "/") + " - " + stripDomain(rtn.getWebsite().endsWith("/") ? rtn.getWebsite() : rtn.getWebsite() + "/"));
            if (hasstart) {
//                if (url.toLowerCase().indexOf((group.getWebpage()!=null? group.getWebpage() : "null.").toLowerCase())!=-1) {
                if (stripDomain(group.getWebsite().endsWith("/")?group.getWebsite():group.getWebsite()+"/").length()> stripDomain(rtn.getWebsite().endsWith("/")?rtn.getWebsite():rtn.getWebsite()+"/").length()) {
                    rtn = group;
                    for (String ws : websites) {
                        if (furl.equals(ws)) {
                            equalsurl = true;
//                            System.out.println("Yes furl: " + furl + " ws: " + ws + " - " + rtn.getGroupAlphaId());
                            break;
                        } else {
                            //System.out.println("No furl: " + furl + " ws: " + ws + " - " + rtn.getGroupAlphaId());

                        }
                    }
                    if (equalsurl) {
                        break;
                    }
                }
//                break;
            }
        }

//        System.out.println("group: " + rtn.getGroupAlphaId());
        if (rtn.getId()==0) {
            rtn = null;
        }
        return rtn;
    }

    public void delete(AppGroup group) {
        groups.remove(group.getId());


    }

    public AppGroup[] getGroupByParentGroupAlphaId(String groupAlphaId) {
        Hashtable<String, AppGroup> superlist;
        if (groupsByParentGroupAlphaId.containsKey(groupAlphaId)) {
            superlist = groupsByParentGroupAlphaId.get(groupAlphaId);
        } else {
            superlist = new Hashtable<String, AppGroup>();
        }
        AppGroup[] rtn = superlist.values().toArray(new AppGroup[superlist.size()]);
        Arrays.sort(rtn, new Comparator<AppGroup>() {
            public int compare(AppGroup o1, AppGroup o2) {
                return o2.getGroupName().compareTo(o1.getGroupName());
            }
        });
        return rtn;
    }

    public AppGroup[] getGroupByOwner(String owner) {
        Hashtable<String, AppGroup> superlist;
        if (groupsByOwner.containsKey(owner)) {
            superlist = groupsByOwner.get(owner);
        } else {
            superlist = new Hashtable<String, AppGroup>();
        }
        AppGroup[] rtn = superlist.values().toArray(new AppGroup[superlist.size()]);
        Arrays.sort(rtn, new Comparator<AppGroup>() {
            public int compare(AppGroup o1, AppGroup o2) {
                return o2.getGroupName().compareTo(o1.getGroupName());
            }
        });
        return rtn;
    }

    public AppGroup[] getChildren(String groupAlphaId) {
        ArrayList<AppGroup> children = new ArrayList<AppGroup>();
        for (AppGroup child : this.getGroupByParentGroupAlphaId(groupAlphaId)) {
            children.add(child);

            for (AppGroup child_child : this.getChildren(child.getGroupAlphaId())) {
                children.add(child_child);
            }
        }
        return children.toArray(new AppGroup[children.size()]);
    }

}
