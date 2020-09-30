package com.eve.network.volley.model;

public class UploadResponse {
    public String ret;
    public String msg;
    public DataWrapper data;

    public class DataWrapper {
        public String filename;
        public String hash;
        public String readCredential;
        public String ret;
    }
}
