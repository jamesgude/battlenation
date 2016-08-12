/**
 * <p>Title: GEncrypt</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.tools;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class GEncrypt {
    public GEncrypt() {

    }
    private static SecretKey getKey() {
        DESKeySpec keySpec = null;
        SecretKeyFactory keyFactory = null;
        SecretKey key = null;

        try {
            keySpec = new DESKeySpec("GEnCrypter 1979".getBytes("UTF8"));
            keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static String encrypt(String str) {
        try {
            sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();

            byte[] cleartext = str.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            str = base64encoder.encode(cipher.doFinal(cleartext));
            str = str.replaceAll("\\+", "%2B");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String decrypt(String str) {

        str = str.replaceAll("%2B", "+");
        try {
            sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(str);
            Cipher cipherd = Cipher.getInstance("DES");
            cipherd.init(Cipher.DECRYPT_MODE, getKey());
            byte[] d = (cipherd.doFinal(encrypedPwdBytes));
            str = new String(d);
        } catch (Exception e) {

        }
        return str;
    }
}
