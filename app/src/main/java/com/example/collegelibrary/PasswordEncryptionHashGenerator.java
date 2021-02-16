package com.example.collegelibrary;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptionHashGenerator {
    public static final String HEXADECIMALS = "0123456789abcdef";
    public static String generateSHA256(String textToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(textToHash.getBytes(Charset.defaultCharset()));
        final String HexText = convertByteArrayToHexString(md.digest());
        return HexText;
    }
    public static String convertByteArrayToHexString(byte[] byteArray){
        final StringBuilder hexText = new StringBuilder(2 * byteArray.length);

        for (byte byteElement : byteArray) {
            hexText
                    .append(HEXADECIMALS.charAt((byteElement & 0xF0) >> 4))
                    .append(HEXADECIMALS.charAt((byteElement & 0x0F)));
        }

        return hexText.toString();
    }
}
