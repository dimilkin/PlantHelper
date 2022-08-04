package com.m.plantkeeper.services;

import java.security.NoSuchAlgorithmException;

public interface HashingService {

    String hashData(String data) throws NoSuchAlgorithmException;
}
