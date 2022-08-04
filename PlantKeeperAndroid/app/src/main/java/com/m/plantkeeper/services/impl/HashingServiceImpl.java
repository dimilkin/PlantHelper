package com.m.plantkeeper.services.impl;

import com.m.plantkeeper.services.HashingService;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingServiceImpl implements HashingService {

    @Override
    public String hashData(String data) throws NoSuchAlgorithmException {
        String randomAddition = "DoctorMilkin1991";
        String password = data + randomAddition;
        MessageDigest crypt = MessageDigest.getInstance("SHA-512");
        crypt.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = crypt.digest();
        BigInteger bi = new BigInteger(1, bytes);
        String hashedData = String.format("%0" + (bytes.length << 1) + "x", bi);
        return hashedData;
    }
}
