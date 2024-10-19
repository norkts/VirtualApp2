package com.carlos.common.email;

import com.carlos.libcommon.StringFog;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
   private String senderAccount;
   private String senderPassword;

   public MailUtil(String senderAccount, String senderPassword) {
      this.senderAccount = senderAccount;
      this.senderPassword = senderPassword;
   }

   public void send(String senderObject, String title, String content) throws MessagingException, GeneralSecurityException {
      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", "smtp.qq.com");
      properties.put("mail.smtp.auth", "true");
      MailSSLSocketFactory sf = new MailSSLSocketFactory();
      sf.setTrustAllHosts(true);
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.smtp.ssl.socketFactory", sf);
      Session session = Session.getDefaultInstance(properties, new Authenticator() {
         public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(MailUtil.this.senderAccount, MailUtil.this.senderPassword);
         }
      });
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(this.senderAccount));
      message.addRecipient(RecipientType.TO, new InternetAddress(senderObject));
      message.setSubject(title);
      message.setText(content);
      Transport.send(message);
   }
}
