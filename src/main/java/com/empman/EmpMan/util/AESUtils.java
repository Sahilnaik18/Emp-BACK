package com.empman.EmpMan.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {

    private static final String ALGORITHM = "AES";  // AES encryption algorithm

    // Encrypt password
    public static String encryptPassword(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec("this-is-test-key".getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedPassword = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedPassword);
    }

    // Decrypt password
    public static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec("this-is-test-key".getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedPassword = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedPassword = cipher.doFinal(decodedPassword);
        return new String(decryptedPassword);
    }
}
