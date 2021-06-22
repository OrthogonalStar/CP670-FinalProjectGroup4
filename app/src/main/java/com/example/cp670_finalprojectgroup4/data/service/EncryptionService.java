package com.example.cp670_finalprojectgroup4.data.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.spec.PBEKeySpec;

public class EncryptionService {
    private static final Random RANDOM = new SecureRandom();

    public static String getNextSalt() {
        byte[] array = new byte[64];
        new Random().nextBytes(array);
        return android.util.Base64.encodeToString(array, android.util.Base64.NO_WRAP);
    }

    public static String hash(String password, String salt) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(android.util.Base64.decode(salt, android.util.Base64.NO_WRAP));

            byte[] hash = digest.digest(password.getBytes());

            return android.util.Base64.encodeToString(hash, android.util.Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
