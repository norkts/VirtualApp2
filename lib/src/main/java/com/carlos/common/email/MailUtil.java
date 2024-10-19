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
      properties.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWoJBgNgAQosOj0ADWoKBlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4ILG8JBgFhClk5Ki1XVg==")));
      properties.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWoJBgNgAQosOj0iLWUzFlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMI2gVSFo=")));
      MailSSLSocketFactory sf = new MailSSLSocketFactory();
      sf.setTrustAllHosts(true);
      properties.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWoJBgNgAQosOj4qL283MCtlNzgpLAguVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMI2gVSFo=")));
      properties.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+CWoJBgNgAQosOj4qL283MANlJzAiLhcqBH0KND9sJygy")), sf);
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
