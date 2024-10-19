package com.carlos.common.email;

import android.text.TextUtils;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.security.GeneralSecurityException;
import javax.mail.MessagingException;

public class EmailReceive {
   public static EmailReceive mEmailReceive = new EmailReceive();

   public static EmailReceive getInstance() {
      return mEmailReceive;
   }

   public void sendCode(LeaveMessage leaveMessage) {
      MailUtil mailUtil = new MailUtil("serven_scorpion@foxmail.com", "mkvjauphcaixcbcc");

      try {
         String receiveAccount = leaveMessage.getReceiveAccount();
         if (TextUtils.isEmpty(receiveAccount)) {
            receiveAccount = "329716228@qq.com";
         }

         mailUtil.send(receiveAccount, leaveMessage.getTitle(), leaveMessage.getContent());
         HVLog.d("邮件发送成功");
      } catch (MessagingException var4) {
         MessagingException e = var4;
         HVLog.d("邮件错误：" + e.getMessage());
      } catch (GeneralSecurityException var5) {
         GeneralSecurityException e = var5;
         HVLog.d("邮件错误：" + e.getMessage());
      }

   }
}
