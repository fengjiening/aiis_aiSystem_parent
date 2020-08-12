package org.jeecg.modules.ability.util;


import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeecg.common.util.CommonMethod;
import org.jeecg.common.util.CommonReturnMethod;
import org.jeecg.common.util.MD5;
import org.jeecg.modules.ability.vo.FaceBox;
import org.jeecg.modules.ability.vo.FaceResult;
import org.jeecg.modules.ability.vo.UserFaceInfo;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class AbilityCallMethod {

    public static String getNowDateString() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        return dateString;
    }

    @SuppressWarnings("unchecked")
    public static CommonReturnMethod<String> callAFRDetectAbility(String url,
                                                                  String appKey, String sdkVersion, String develoerKey,
                                                                  InputStream inputStream, String capKey) {
        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey + ",gender=no";
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-udid", "101:1234567890");
        requestHeaderMap.put("x-tid", "");
        requestHeaderMap.put("x-eid", "");
        requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {

            result = CommonMethod.doPostQuery(url, inputStream,
                    requestHeaderMap, 30000);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
            Document doc = null;
            doc = DocumentHelper.parseText(result);
            Element rootElt = doc.getRootElement(); // 获取根节点
            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                return commonReturnMethod.retError(errorNo,
                        rootElt.elementText("ResMessage"));
            }
            List<Element> faceElements = rootElt.element("Result")
                    .element("Faces").elements("Face");
            if (faceElements == null || faceElements.size() == 0) {
                return commonReturnMethod.retError("没有识别到人脸");
            }
            return commonReturnMethod.retSucc(faceElements.get(0).elementText(
                    "FaceId"));

        } catch (Exception e) {

            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError("人脸探测时获取识别信息失败");
        }

    }

    @SuppressWarnings("unchecked")
    public static CommonReturnMethod<List<Recognise_Result>> callAFRRecognizeAbility(
            String url, String appKey,String capKey, String sdkVersion, String develoerKey,
            FaceResult faceResult, int threshold) {


        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<List<Recognise_Result>> commonReturnMethod = new CommonReturnMethod<List<Recognise_Result>>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey + ",groupid=" + appKey + ",threshold=" + threshold
                + ",faceid=" + faceResult.getResult().getFaceId() + ",candnum=5";
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-udid", "101:1234567890");
        //requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {
            result = CommonMethod.doPostQuery(url, null, requestHeaderMap,
                    30000);
            System.out.println(result);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
            Document doc = null;
            doc = DocumentHelper.parseText(result);
            Element rootElt = doc.getRootElement(); // 获取根节点
            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                return commonReturnMethod.retError(errorNo,
                        rootElt.elementText("ResMessage"));
            }
            List<Element> userIds = rootElt.element("Result")
                    .elements("UserId");

            String token = rootElt.element("Result_Token").getText();
            if (userIds == null) {
                return commonReturnMethod.retError("匹配结果错误");
            }
            List<Recognise_Result> recognise_Results = new ArrayList<Recognise_Result>();
            for (int i = 0; i < userIds.size(); i++) {
                Element userIdElement = userIds.get(i);
                Recognise_Result recognise_Result = new Recognise_Result();
                recognise_Result.setSocre(Double.parseDouble(userIdElement
                        .attribute("Score").getStringValue()));
                recognise_Result.setType("ability");
                recognise_Result.setUserId(userIdElement.getText());
                recognise_Result.setToken(token);
                recognise_Results.add(recognise_Result);

            }
            return commonReturnMethod.retSucc(recognise_Results);

        } catch (Exception e) {

            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError("人脸识别获取识别信息失败");
        }
    }

    /**
     * 人脸探测能力调用
     *
     * @param @param  url
     * @param @param  appKey
     * @param @param  sdkVersion
     * @param @param  capKey
     * @param @param  gender
     * @param @param  develoerKey
     * @param @param  inputFile
     * @param @return 设定文件
     * @return CommonReturnMethod<String> 返回类型
     * @throws
     * @Title: detectAbility
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static CommonReturnMethod<String> detectAbility(String url,
                                                           String appKey, String sdkVersion, String capKey, String params,
                                                           String develoerKey, InputStream inputFile) {
        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey+params;
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-udid", "101:1234567890");
        //requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {
            result = CommonMethod.doPostQuery(url, inputFile, requestHeaderMap,
                    30000);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
        } catch (Exception e) {
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError(e.getMessage());
        }

        return commonReturnMethod.retSucc(result);
    }

    /**
     * 人脸注册
     *
     * @param @param  url
     * @param @param  appKey
     * @param @param  capKey
     * @param @param  sdkVersion
     * @param @param  develoerKey
     * @param @param  cardId
     * @param @param  faceId
     * @param @return 设定文件
     * @return CommonReturnMethod<String> 返回类型
     * @throws
     * @Title: callAfrRegistAbility
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static CommonReturnMethod<String> callAfrRegistAbility(String url,
                                                                  String appKey, String capKey, String sdkVersion,
                                                                  String develoerKey, String cardId, String faceId) {
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        CommonReturnMethod<String> retResut = registAbility(url, appKey,
                sdkVersion, capKey, develoerKey, cardId, faceId);
        if (retResut.isError()) {
            return commonReturnMethod.retError(retResut.getErrorMsg());
        }
        String resultString = retResut.getResult();
        try {
            Document doc = null;
            doc = DocumentHelper.parseText(resultString);
            Element rootElt = doc.getRootElement(); // 获取根节点

            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                return commonReturnMethod.retError(errorNo,
                        rootElt.elementText("ResMessage"));
            }
            // Document resultDocument
            // =DocumentHelper.parseText(rootElt.elementText("Result"));
            Element resultElement = rootElt.element("Result");
            String userId = resultElement.elementText("UserId");
            return commonReturnMethod.retSucc(userId);
        } catch (Exception e) {
            return commonReturnMethod.retError(1, e.getMessage());
        }
    }


    /**
     * 人脸注册能力调用
     *
     * @param @param  url
     * @param @param  appKey
     * @param @param  sdkVersion
     * @param @param  capKey
     * @param @param  develoerKey
     * @param @param  cardId
     * @param @param  faceId
     * @param @return 设定文件
     * @return CommonReturnMethod<String> 返回类型
     * @throws
     * @Title: registAbility
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private static CommonReturnMethod<String> registAbility(String url,
                                                            String appKey, String sdkVersion, String capKey,
                                                            String develoerKey, String cardId, String faceId) {
        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey + ",faceid=" + faceId
                + ",userid=" + cardId + ",groupid=" + appKey;
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-udid", "101:1234567890");
        requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        InputStream inputFile = null;
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {
            result = CommonMethod.doPostQuery(url, inputFile, requestHeaderMap,
                    30000);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
        } catch (Exception e) {
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError(e.getMessage());
        }

        return commonReturnMethod.retSucc(result);
    }

    public static CommonReturnMethod<String> callAfrVerifyAbility(String url,
                                                                  String appKey, String capKey, String sdkVersion,
                                                                  String develoerKey, String faceId, int threshold) {
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        CommonReturnMethod<String> retResut = VerifyAbility(url, appKey,
                sdkVersion, capKey, develoerKey, faceId, threshold);
        if (retResut.isError()) {
            return commonReturnMethod.retError(retResut.getErrorMsg());
        }
        String resultString = retResut.getResult();
        try {
            Document doc = null;
            doc = DocumentHelper.parseText(resultString);
            Element rootElt = doc.getRootElement(); // 获取根节点

            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                return commonReturnMethod.retError(errorNo,
                        rootElt.elementText("ResMessage"));
            }
            // Document resultDocument
            // =DocumentHelper.parseText(rootElt.elementText("Result"));
            Element resultElement = rootElt.element("Result");
            String verifyResult = resultElement.elementText("VerifyResult");
            return commonReturnMethod.retSucc(verifyResult);
        } catch (Exception e) {
            return commonReturnMethod.retError(1, e.getMessage());
        }
    }

    private static CommonReturnMethod<String> VerifyAbility(String url,
                                                            String appKey, String sdkVersion, String capKey,
                                                            String develoerKey, String faceId, int threshold) {
        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<String> commonReturnMethod = new CommonReturnMethod<String>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey + ",faceid=" + faceId
                + ",threshold=" + threshold;
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-udid", "101:1234567890");
        requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        InputStream inputFile = null;
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {
            result = CommonMethod.doPostQuery(url, inputFile, requestHeaderMap,
                    30000);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
        } catch (Exception e) {
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError(e.getMessage());
        }

        return commonReturnMethod.retSucc(result);
    }

    /**
     * 人脸探测
     *
     * @param @param  url
     * @param @param  appKey
     * @param @param  capKey
     * @param @param  sdkVersion
     * @param @param  develoerKey
     * @param @param  gender
     * @param @param  inputFile
     * @param @return 设定文件
     * @return CommonReturnMethod<AfrDetectReturn> 返回类型
     * @throws
     * @Title: callAfrDetectAbility
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */

   static volatile FaceBox faceBox = new FaceBox();
   static volatile UserFaceInfo userInfo = new UserFaceInfo();
   static volatile FaceResult faceResultVo=new FaceResult(faceBox,userInfo);

    @SuppressWarnings("unchecked")
    public static FaceResult callAfrDetectAbility(
            String url, String appKey, String capKey, String sdkVersion,
            String develoerKey, String params, InputStream inputFile) {
        CommonReturnMethod<String> retResut = detectAbility(url, appKey,
                sdkVersion, capKey, params, develoerKey, inputFile);
        //初始一定值
        faceResultVo.setResult(userInfo);
        faceResultVo.setFaceBox(faceBox);
        long cTime = System.currentTimeMillis();
        faceResultVo.setTimestamp(String.valueOf(cTime));
        if (retResut.isError()) {
            setNoFaceVo(faceResultVo);
            return faceResultVo;
        }
        String resultString = retResut.getResult();
        try {
            Document doc = null;
            doc = DocumentHelper.parseText(resultString);
            Element rootElt = doc.getRootElement(); // 获取根节点

            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                setNoFaceVo(faceResultVo);
                return faceResultVo;
            }
            Element resultElement = doc.getRootElement().element("Result");
            Element facesElement = resultElement.element("Faces");
            Element faceElement = facesElement.element("Face");
            String faceId = faceElement.elementText("FaceId");
            String genderResult = faceElement.elementText("Gender");

            String left = faceElement.element("FaceBox").attributeValue("Left");
            String top = faceElement.element("FaceBox").attributeValue("Top");
            String right = faceElement.element("FaceBox").attributeValue("Right");
            String bottom = faceElement.element("FaceBox").attributeValue("Bottom");
             faceBox.setX1(left);
            faceBox.setY1(top);
             faceBox.setX2(right);
             faceBox.setY2(bottom);
            userInfo.setFaceId(faceId);
            faceResultVo.setStable(true);
            userInfo.setGender(genderResult);
            return faceResultVo;
        } catch (Exception e) {
            e.printStackTrace();
            setNoFaceVo(faceResultVo);
            return faceResultVo;
        }

    }

    /**
     * 声纹识别
     * @param url
     * @param appKey
     * @param capKey
     * @param sdkVersion
     * @param develoerKey
     * @param threshold
     * @return
     */
    @SuppressWarnings("unchecked")
    public static CommonReturnMethod<List<Recognise_Result>> callVPRRecognizeAbility(
            String url, String appKey,String capKey, String sdkVersion, String develoerKey,
            InputStream inputStream, String threshold,String audioformat,String property) {


        String uuidString = UUID.randomUUID().toString();
        CommonReturnMethod<List<Recognise_Result>> commonReturnMethod = new CommonReturnMethod<List<Recognise_Result>>();
        Map<String, String> requestHeaderMap = new HashMap<String, String>();
        String requestionDate = getNowDateString();
        String xtaskconfig = "capkey=" + capKey + ",groupid=" + appKey + ",threshold=" + threshold
                + ",audioformat=" + audioformat  + ",property=" + property + ",candnum=5";
        String xsessionkey = MD5.getMD5Code(requestionDate + develoerKey);
        requestHeaderMap.put("x-app-key", appKey);
        requestHeaderMap.put("x-sdk-version", sdkVersion);
        requestHeaderMap.put("x-request-date", requestionDate);
        requestHeaderMap.put("x-task-config", xtaskconfig);
        requestHeaderMap.put("x-session-key", xsessionkey);
        requestHeaderMap.put("x-udid", "101:1234567890");
        //requestHeaderMap.put("request-uuidKey", uuidString);
        long time = new Date().getTime();
        log.debug("发送请求" + uuidString + "：" + requestHeaderMap.toString());
        String result = null;
        try {
            result = CommonMethod.doPostQuery(url, inputStream, requestHeaderMap,
                    30000);
            System.out.println(result);
            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "获取值" + result);
            Document doc = null;
            doc = DocumentHelper.parseText(result);
            Element rootElt = doc.getRootElement(); // 获取根节点
            int errorNo = Integer.parseInt(rootElt.elementText("ErrorNo"));
            if (errorNo != 0) {
                return commonReturnMethod.retError(errorNo,
                        rootElt.elementText("ResMessage"));
            }
            List<Element> userIds = rootElt.element("Result")
                    .elements("UserId");
            if (userIds == null) {
                return commonReturnMethod.retError("匹配结果错误");
            }
            String token = rootElt.element("Result_Token").getText();

            List<Recognise_Result> recognise_Results = new ArrayList<Recognise_Result>();
            for (int i = 0; i < userIds.size(); i++) {
                Element userIdElement = userIds.get(i);
                Recognise_Result recognise_Result = new Recognise_Result();
                recognise_Result.setSocre(Double.parseDouble(userIdElement
                        .attribute("Score").getStringValue()));
                recognise_Result.setType("ability");
                recognise_Result.setToken(token);
                recognise_Result.setUserId(userIdElement.getText());
                recognise_Results.add(recognise_Result);
            }
            return commonReturnMethod.retSucc(recognise_Results);

        } catch (Exception e) {

            log.debug("接收请求" + uuidString + "：耗时："
                    + (new Date().getTime() - time) + "异常" + e.getMessage());
            return commonReturnMethod.retError("人脸识别获取识别信息失败");
        }
    }

    private static void setNoFaceVo(FaceResult faceResultVo) {
        faceResultVo.getFaceBox().setX1(null);
        faceResultVo.getFaceBox().setY1(null);
        faceResultVo.getFaceBox().setX2(null);
        faceResultVo.getFaceBox().setY2(null);
        faceResultVo.setMatch(0);
        faceResultVo.setStable(false);
        faceResultVo.getResult().setFaceId(null);
        faceResultVo.getResult().setGender(null);
    }

}
