/**
 * <p>Title: AccessManagerInstanceController</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.client;

import com.businesshaps.am.businessobjects.AppGroup;
import com.businesshaps.am.businessobjects.AppUser;

public interface AccessManagerInstanceController {
    AppGroup getGroupByURL(String url);

    AppUser[] getUserByURL(String url);

    boolean isGroupMember(String username, String groupAlphaId);

    boolean isUserTokenActive(String user_token);

    boolean hasAuthorization(String user_token, String url);

    String getUsernameByToken(String user_token);

    void logUser(String user_token, String access_point, String access_point_addr, String domain, String user_ip, boolean authorized, String browser, String queryString);

    AppUser getUser(String username);

    String getRealm();

    void setRealm(String realm);

    String getApplicationURL();

    void setApplicationURL(String applicationURL);

}
