package com.backend.connectable.global.util;

public class OpenseaCollectionNamingUtil {

    private static final String OPENSEA_COLLECTION_URL = "https://opensea.io/collection/";

    private OpenseaCollectionNamingUtil() {}

    public static String toOpenseaCollectionUrl(String contractName) {
        contractName = convertToOpenseaCollectionFormat(contractName);
        return OPENSEA_COLLECTION_URL + contractName;
    }

    private static String convertToOpenseaCollectionFormat(String contractName) {
        return contractName.toLowerCase().replaceAll("\\s+", "-");
    }
}
