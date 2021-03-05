package com.example.onlinecollegelibrary;

import java.security.NoSuchAlgorithmException;

public class HasingPassword {
    public String hasedPassword(String stringPassword){
        String HasedPassword = null;
        PasswordEncryptionHashGenerator passHash = new PasswordEncryptionHashGenerator();
        try {
            HasedPassword = passHash.generateSHA256(stringPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HasedPassword;
    }
}

