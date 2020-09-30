package com.eve.network;

import android.content.Context;

import java.util.HashMap;

public class NetworkConfig {
    private static Context context;
    private static HashMap<UrlTypeEnum, String> urlMap;

    private static final EnvironmentEnum ENVIRONMENT = EnvironmentEnum.BETA;

    public enum EnvironmentEnum {
        DEV, UAT, BETA, LIVE
    }

    public enum UrlTypeEnum {
        HOST, API, SOCKET
    }

    public static String getUrl(UrlTypeEnum type) {
        if (urlMap != null && urlMap.containsKey(type))
            return urlMap.get(type);

        urlMap = new HashMap<>();
        switch (ENVIRONMENT) {
            case DEV:
                urlMap.put(UrlTypeEnum.API, "https://dev-meds-admin-api.usdp.io");
                urlMap.put(UrlTypeEnum.HOST, "https://dev-meds-admin.usdp.io");
                break;
//            case UAT:
//                urlMap.put(UrlTypeEnum.API, "https://uat-grx-api.usdp.io");
//                break;
            case BETA:
                urlMap.put(UrlTypeEnum.API, "https://meds-pri-api.maxonrow.com");
                urlMap.put(UrlTypeEnum.HOST, "https://dev-meds-admin.usdp.io");
                break;
//            case LIVE:
//                urlMap.put(UrlTypeEnum.API, "https://api.grxtrade.com");
//                break;
        }

        return urlMap.get(type);
    }

    public static EnvironmentEnum getEnvironment() {
        return ENVIRONMENT;
    }

    public static void setContext(Context context) {
        NetworkConfig.context = context;
    }

    public static Context getContext() {
        return NetworkConfig.context;
    }

}
