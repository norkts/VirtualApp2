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
      MailUtil mailUtil = new MailUtil(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjNCZsJyg5Ki4uKmwjNCZgESQcLwgIO2MKTClpJFkc")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwhbLmUjJAVhHho5LwccIm4FRSloJ1RF")));

      try {
         String receiveAccount = leaveMessage.getReceiveAccount();
         if (TextUtils.isEmpty(receiveAccount)) {
            receiveAccount = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OikLJ3w0Jz5PMy8aJxgiKX8VAiVlAVRF"));
         }

         mailUtil.send(receiveAccount, leaveMessage.getTitle(), leaveMessage.getContent());
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhoJCEYWQj5YEBMBAxkjE0dJE0xBE0YM")));
      } catch (MessagingException var4) {
         MessagingException e = var4;
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhoJCEYWQj5ZEw9OA1ZcDUUWJVc=")) + e.getMessage());
      } catch (GeneralSecurityException var5) {
         GeneralSecurityException e = var5;
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhoJCEYWQj5ZEw9OA1ZcDUUWJVc=")) + e.getMessage());
      }

   }
}
