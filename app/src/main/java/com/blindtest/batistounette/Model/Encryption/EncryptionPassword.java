package com.blindtest.batistounette.Model.Encryption;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Batistounette on 18/06/2020.
 */

public class EncryptionPassword {

    private static String KEY = "3HyBrlU8gfFV6G8m";
    private static String AES = "AES";

    public static String encrypt(final String PASSWORD) throws Exception {
        SecretKeySpec secretKeySpec = generateKey(KEY);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encVal = c.doFinal(PASSWORD.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    public static String decrypt(final String ENCRYPTED_PASSWORD) throws Exception {
        SecretKeySpec secretKeySpec = generateKey(KEY);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedValue = Base64.decode(ENCRYPTED_PASSWORD, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static SecretKeySpec generateKey(final String key) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = key.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] encryptedKey = digest.digest();
        return new SecretKeySpec(encryptedKey, "AES");
    }

    public static String generateRandomString() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmonpqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int index = 10; index > 0; index--) {randomString.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));}

        return randomString.toString();
    }
}