/**
 * <p>Title: UserLogDAO</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.server.userlog;

import com.businesshaps.am.businessobjects.AppUserLog;
import com.businesshaps.am.server.AppProperties;
import com.businesshaps.oi.ObInject;

public class UserLogDAO implements UserLogManager{
    private AppProperties props;
    private ObInject obInject;

    public UserLogDAO(AppProperties props, String jdbc) {
        this.props = props;
        obInject = ObInject.getInstance(jdbc);
    }

    public AppUserLog[] getUserLog() {

//        System.out.println("########UserLogDAO.getUserLog()########");
/*        List list = new Vector();
        Transaction tx = null;
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        tx = session.beginTransaction();
        list = session.createQuery("select u from AppUserLog as u").list();
        tx.commit();
        AppUserLog[] rtn = (AppUserLog[]) list.toArray(new AppUserLog[list.size()]);
        Arrays.sort(rtn, new Comparator<AppUserLog>() {
            public int compare(AppUserLog o1, AppUserLog o2) {
                return o2.getLogDate().compareTo(o1.getLogDate());
            }
        });
*/
        return new AppUserLog[0];
    }

    public AppUserLog[] getUserLogByAccessPoint(String accessPoint) {
//        System.out.println("########UserManagerDAO.getUser()########");
        return new AppUserLog[0];
    }

    public AppUserLog getUserLogByUserLogId(Integer userLogId) {
//        System.out.println("########UserManagerDAO.getUser()########");
        return new AppUserLog();
    }

    public AppUserLog[] getUserLogByUsername(String username) {
        return new AppUserLog[0];
    }

    public AppUserLog setUserLog(AppUserLog userLog) {
        return (AppUserLog) obInject.set( userLog);
    }

    public void reload() {

    }

}
