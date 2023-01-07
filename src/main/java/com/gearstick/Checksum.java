package com.gearstick;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Checksum {
    /**
     * Calculates checksum of a file in MD5 and SHA-256 algorithms.
     */
    public static String getChecksum(String digestName, File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(digestName);

        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in parts
        byte[] byteArray = new byte[1024];
        int bytesCount;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //Convert bytes[] to hexadecimal from decimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
