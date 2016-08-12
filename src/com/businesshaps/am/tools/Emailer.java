/**
 * <p>Title: Emailer</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.tools;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Emailer {
    public static String server = "";
    public static String auth = "";
    public static String user = "";
    public static String pass = "";
    public static String port = "";

    public Emailer() {

    }

    public static void main(String[] args) {

        sendMail("someone@schedbed.com", "bradleygude@gmail.com", "test", "<b>test</b>");
    }

    public static void sendMail(String from, String emailTo, String emailSubject,
                                String emailContent) {


        try {


            Properties props = System.getProperties();

            props.put("mail.smtp.host", server);
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.port", port);

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });


            // -- Create a new message --

            javax.mail.Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(emailTo));
//            msg.setRecipient(javax.mail.Message.RecipientType.BCC,                  new InternetAddress(emailBcc));
            msg.setSubject(emailSubject);
            msg.setSentDate(new Date());
            msg.setContent(emailContent, "text/html");

            final Message tmsg = msg;
            Thread thread = (new Thread() {
                public void run() {
                    try {
                        Transport.send(tmsg);
                    } catch(Exception e) {
                        e.printStackTrace();;
                    }
                }
            });
            thread.start();


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("sendMail: Unable to process email.Check email: " + emailSubject);
            //sendMail(emailFrom, "Email Failure : " + emailSubject, e.toString());
        }

    }


}
