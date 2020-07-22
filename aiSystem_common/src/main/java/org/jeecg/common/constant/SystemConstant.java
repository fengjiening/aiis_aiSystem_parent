package org.jeecg.common.constant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by fengjiening on 2020/6/9.
 */
@Slf4j
public class SystemConstant {

    private static  String AFR_URL;
    private static String AFR_APP_KEY ;
    private static String AFR_DEVELOPER_KEY ;
    private static String AFR_THRESHOLD ;
    private static String AFR_SDK_VERSION = "5.1";
    private static String AFR_CAP_KEY ="afr.cloud.recog";
    public static String DETECT_URL="/afr/detect";
    public static String RECOGNISE_URL="/afr/recognise";


    private static String DATA_PATH_PRE;

    private static  String VPR_URL;
    private static String VPR_APP_KEY ;
    private static String VPR_DEVELOPER_KEY ;
    private static String VPR_THRESHOLD ;
    private static String VPR_AUDIOFORMAT ;
    private static String VPR_PROPERTY ;
    private static String VPR_SDK_VERSION = "5.1";
    private static String VPR_CAP_KEY ="vpr.cloud.recog";
    public static String  VPR_RECOGNISE_URL="/vpr/recognise";

    public static String getDataPathPre() {
        return DATA_PATH_PRE;
    }

    public static void setDataPathPre(String dataPathPre) {
        DATA_PATH_PRE = dataPathPre;
    }

    public static String getVprUrl() {
        return VPR_URL;
    }

    public static String getVprAudioformat() {
        return VPR_AUDIOFORMAT;
    }

    public static void setVprAudioformat(String vprAudioformat) {
        VPR_AUDIOFORMAT = vprAudioformat;
    }

    public static String getVprProperty() {
        return VPR_PROPERTY;
    }

    public static void setVprProperty(String vprProperty) {
        VPR_PROPERTY = vprProperty;
    }

    public static void setVprUrl(String vprUrl) {
        VPR_URL = vprUrl;
    }

    public static String getVprAppKey() {
        return VPR_APP_KEY;
    }

    public static void setVprAppKey(String vprAppKey) {
        VPR_APP_KEY = vprAppKey;
    }

    public static String getVprDeveloperKey() {
        return VPR_DEVELOPER_KEY;
    }

    public static void setVprDeveloperKey(String vprDeveloperKey) {
        VPR_DEVELOPER_KEY = vprDeveloperKey;
    }

    public static String getVprThreshold() {
        return VPR_THRESHOLD;
    }

    public static void setVprThreshold(String vprThreshold) {
        VPR_THRESHOLD = vprThreshold;
    }

    public static String getVprSdkVersion() {
        return VPR_SDK_VERSION;
    }

    public static void setVprSdkVersion(String vprSdkVersion) {
        VPR_SDK_VERSION = vprSdkVersion;
    }

    public static String getVprCapKey() {
        return VPR_CAP_KEY;
    }

    public static void setVprCapKey(String vprCapKey) {
        VPR_CAP_KEY = vprCapKey;
    }

    public static String getAfrUrl() {
        return AFR_URL;
    }

    public static void setAfrUrl(String afrUrl) {
        AFR_URL = afrUrl;
    }

    public static String getAfrAppKey() {
        return AFR_APP_KEY;
    }

    public static void setAfrAppKey(String afrAppKey) {
        AFR_APP_KEY = afrAppKey;
    }

    public static String getAfrDeveloperKey() {
        return AFR_DEVELOPER_KEY;
    }

    public static void setAfrDeveloperKey(String afrDeveloperKey) {
        AFR_DEVELOPER_KEY = afrDeveloperKey;
    }

    public static String getAfrSdkVersion() {
        return AFR_SDK_VERSION;
    }

    public static void setAfrSdkVersion(String afrSdkVersion) {
        AFR_SDK_VERSION = afrSdkVersion;
    }

    public static String getAfrCapKey() {
        return AFR_CAP_KEY;
    }

    public static void setAfrCapKey(String afrCapKey) {
        AFR_CAP_KEY = afrCapKey;
    }

    public static String getAfrThreshold() {
        return AFR_THRESHOLD;
    }

    public static void setAfrThreshold(String afrThreshold) {
        AFR_THRESHOLD = afrThreshold;
    }





}
