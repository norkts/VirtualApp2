package com.norkts;

import com.carlos.libcommon.StringFog;

import org.junit.Test;

public class TestStringDesc {

    @Test
    public void testDescrpty(){
        decryptAndPrint("Li5fP2ojBitgHFEzKgccLg==");
        encryptAndPrint("start_application");
    }

    public String decryptAndPrint(String data){
        String res = StringFog.decrypt(com.kook.librelease.StringFog.decrypt(data));

        System.out.println(data + ":" + res);
        return res;

    }

    public String encryptAndPrint(String data){
        String res = StringFog.encrypt(com.kook.librelease.StringFog.encrypt(data));

        System.out.println(data + ":" + res);
        return res;

    }
}
