/**
 * <p>Title: DefaultValues</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.server;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppGroupType;
import com.businesshaps.am.businessobjects.AppGroupUser;
import com.businesshaps.am.businessobjects.AppUser;
import com.businesshaps.am.server.group.GroupManager;
import com.businesshaps.am.server.group.grouptype.GroupTypeManager;
import com.businesshaps.am.server.group.groupuser.GroupUserManager;
import com.businesshaps.am.server.user.UserManager;

import java.util.Date;

public class DefaultValues {
    public DefaultValues() {

    }

    public static void setDefaultUser(UserManager userManager, AppProperties properties) {
        System.out.println("#####: " + properties.getApplicationURL() + " --- Inserting Default Users");
        AppUser user = new AppUser();
        user.setActive(true);
        user.setDateCreated(new Date());
        user.setDisplayName("System Admin");
        user.setEmail("sysadmin@admin.com");
        user.setFirstName("System");
        user.setPassword("password");
        user.setStartDate(new Date());
        user.setLastName("Admin");
        user.setUsername("admin");
        user.setPending(false);
        userManager.setUser(user);
    }

    public static void setDefaultGroup(GroupManager groupManager, AppProperties properties) {
        String domain = properties.getDomain();
        if (domain!=null) {
            domain = domain.replaceAll("www.", "");
            domain = domain.replaceAll("http://", "");
            domain = domain.replaceAll("https://", "");
        }
        System.out.println("#####: " + properties.getApplicationURL() + " --- Inserting Default Groups --- " + domain);
        AppGroup group = new AppGroup();
        if (groupManager.getGroupByAlphaId("CMP_ROOT") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_ROOT");
            group.setGroupName("Home");
            group.setWebsite("/");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(true);
            group.setIcon("/images/Silk Icons/house.png");
            group.setOwner("admin");
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        
        if (groupManager.getGroupByAlphaId("CMP_TABLES") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_TABLES");
            group.setGroupName("Page Editor");
            group.setWebsite("" + properties.getRoot() + "/apps/");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(true);
            group.setAllowAnonAccess(false);
            group.setIcon("/images/Silk Icons/application_view_gallery.png");
            group.setOwner("admin");
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        if (groupManager.getGroupByAlphaId("CMP_SIGN_IN_PAGE") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_SIGN_IN_PAGE");
            group.setGroupName("Sign In Page");
            group.setWebsite("" + properties.getRoot() + "/home/signin");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(false);
            group.setIcon("/images/Silk Icons/application_view_gallery.png");
            group.setOwner("admin");
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        if (groupManager.getGroupByAlphaId("CMP_SIGN_UP") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_SIGN_UP");
            group.setGroupName("Sign Up Page");
            group.setWebsite("" + properties.getRoot() + "/apps/signup/submit");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(true);
            group.setIcon("/images/Silk Icons/application_view_gallery.png");
            group.setOwner("admin");
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        if (groupManager.getGroupByAlphaId("CMP_FAVICON") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_FAVICON");
            group.setGroupName("Fav Icon");
            group.setWebsite("" + properties.getRoot() + "/favicon.ico");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(true);
            group.setIcon("/images/Silk Icons/application_view_gallery.png");
            group.setOwner("admin");
            group.setRestrictLayout(true);
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        if (groupManager.getGroupByAlphaId("CMP_404") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_404");
            group.setGroupName("Page Not Found");
            group.setWebsite("" + properties.getRoot() + "/404.page.jsp");
            group.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(true);
            group.setIcon("/images/Silk Icons/exclamation.png");
            group.setOwner("admin");
            group.setDomain(domain);

            group = groupManager.setGroup(group);
        }
        if (groupManager.getGroupByAlphaId("CMP_ADMIN") == null) {
            group = new AppGroup();
            group.setActive(true);
            group.setDateCreated(new Date());
            group.setGroupAlphaId("CMP_ADMIN");
            group.setGroupName("Admin Page");
            group.setWebsite("" + properties.getRoot() + "/admin/");
            group.setGroupTypeAlphaId("GRP_TYPE_USER");
            group.setGroupDesc("");
            group.setRestricted(false);
            group.setAllowAnonAccess(true);
            group.setOwner("admin");
            group.setDomain(domain);
            group.setVirtual(true);
            group.setLayout("canvas");
            group = groupManager.setGroup(group);
        }


    }

    public static void setDefaultGroupUser(GroupUserManager groupUserManager, AppProperties properties) {
        System.out.println("#####: " + properties.getApplicationURL() + " --- Inserting Default Group Users");
        AppGroupUser groupUser = new AppGroupUser();
        groupUser.setDateCreated(new Date());
        groupUser.setGroupAlphaId("CMP_ROOT");
        groupUser.setUserId(1);
        groupUser.setRank(1);
        groupUser.setGroupAccess(false);
        groupUserManager.setGroupUser(groupUser);

        groupUser = new AppGroupUser();
        groupUser.setDateCreated(new Date());
        groupUser.setGroupAlphaId("CMP_PAGE_EDITOR");
        groupUser.setUserId(1);
        groupUser.setRank(1);
        groupUser.setGroupAccess(false);
        groupUserManager.setGroupUser(groupUser);

        groupUser = new AppGroupUser();
        groupUser.setDateCreated(new Date());
        groupUser.setGroupAlphaId("CMP_TABLES");
        groupUser.setUserId(1);
        groupUser.setRank(1);
        groupUser.setGroupAccess(false);
        groupUserManager.setGroupUser(groupUser);

    }

    public static void setDefaultGroupType(GroupTypeManager groupTypeManager, AppProperties properties) {
        System.out.println("#####: " + properties.getApplicationURL() + " --- Inserting Default Group Types");
        AppGroupType groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_DEPT");
        groupType.setGroupTypeName("Department");

        groupTypeManager.setGroupType(groupType);

        groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_GROUP");
        groupType.setGroupTypeName("Group");
        groupTypeManager.setGroupType(groupType);

        groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_JOB_TITLE");
        groupType.setGroupTypeName("Job Title");
        groupTypeManager.setGroupType(groupType);

        groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_APPLICATION");
        groupType.setGroupTypeName("Application");
        groupTypeManager.setGroupType(groupType);

        groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_ORG");
        groupType.setGroupTypeName("Organization");
        groupTypeManager.setGroupType(groupType);

        groupType = new AppGroupType();
        groupType.setGroupTypeAlphaId("GRP_TYPE_USER");
        groupType.setGroupTypeName("User");
        groupTypeManager.setGroupType(groupType);
    }


}
