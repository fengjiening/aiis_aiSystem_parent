package org.jeecg.modules.ability.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SystemConstant;
import org.jeecg.common.util.CommonMethod;

import org.jeecg.common.util.CommonReturnMethod;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.ability.util.AbilityCallMethod;
import org.jeecg.modules.ability.util.Recognise_Result;
import org.jeecg.modules.ability.vo.FaceBox;
import org.jeecg.modules.ability.vo.FaceResult;
import org.jeecg.modules.ability.vo.PicDataInput;

import org.jeecg.modules.ability.vo.UserFaceInfo;
import org.jeecg.modules.staff.entity.RegisteredInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.websocket.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.List;
import java.util.concurrent.BlockingQueue;


/**
 * Created by fengjiening on 2020/6/12.
 * 核心检测人脸稳定性
 */
@Component
@Slf4j
public class  FaceTask {

    @Autowired
    private AbilityService abilityService;
    static ThreadLocal<FaceBox> faceBoxThread=new ThreadLocal<FaceBox>();
    static ThreadLocal<UserFaceInfo> UserFaceInfoThread=new ThreadLocal<UserFaceInfo>();
    static ThreadLocal<FaceResult> FaceResultThread=new ThreadLocal<FaceResult>();
    private static final int   recgWidth = 780 / 2;
//    static  FaceBox faceBox = new FaceBox();
//    static  UserFaceInfo userInfo = new UserFaceInfo();
//    static  FaceResult faceResultVo=new FaceResult(faceBox,userInfo);
    /**
     * todo 需要改进，针对 会话session管理
     * @param queue
     */
    @Async
    public void delFace(Session session, BlockingQueue queue, List<Float> frameXList, List<Float> frameYList){
        log.info(Thread.currentThread().getName()+"delFace 线程开始启动");
         FaceBox faceBox = new FaceBox();
        UserFaceInfo userInfo = new UserFaceInfo();
        faceBoxThread.set(faceBox);
        UserFaceInfoThread.set(userInfo);
        FaceResultThread.set(new FaceResult(faceBox,userInfo));
        PicDataInput input=null;
        while (session.isOpen()){
            try {
                //log.error(Thread.currentThread().getName()+" 开始获取图片数据总数【{}】",queue.size());

                if (queue.size()==0){
                    Thread.sleep(1000);
                    continue;
                }
                //take 线程阻塞，后面客户端断开 会导致资源消耗
                // PicDataInput input = (PicDataInput)queue.take();
                input = (PicDataInput)queue.poll();
                //测试 当前上传人脸图片
//                InputStream faceStreamc =  CommonMethod.decoderBase64ToStream(input.getPicData());
//                String savePath = "c://face.png";
//                File file = new File(savePath);
//                InputStream inputFile = new FileInputStream(file);
//                String s = CommonMethod.encodeBase64File(savePath);
//                InputStream faceStreamc =  CommonMethod.decoderBase64ToStream(s);

                //人脸探测
                 FaceResult faceResult = afrDetect(CommonMethod.decoderBase64ToStream(input.getPicData()));

                //log.error(Thread.currentThread().getName()+" 返回结果【{}】", JSONObject.toJSONString(faceResult,SerializerFeature.WriteNullStringAsEmpty));
                //参数校验
                //未获取坐标数据 不去计算
                if (faceResult.getFaceBox()==null ||faceResult.getFaceBox().getX1()==null ||faceResult.getFaceBox().getX2()==null||
                faceResult.getFaceBox().getY1()==null||faceResult.getFaceBox().getY2()==null || input.getTotalFrame()<=0){
                    faceResult.setStable(false);
                    faceResult.setCenter(false);
                    sendOneMessage(session, JSONObject.toJSONString(faceResult,SerializerFeature.WriteNullStringAsEmpty));
                    continue;
                };

                //人脸坐标
                float x1=Float.valueOf(faceResult.getFaceBox().getX1());
                float x2=Float.valueOf(faceResult.getFaceBox().getX2());
                float y1=Float.valueOf(faceResult.getFaceBox().getY1());
                float y2=Float.valueOf(faceResult.getFaceBox().getY2());

                //获取中心点
                float x= Math.abs(x2-x1)/2+x1 ;
                float y= Math.abs(y1-y2)/2+y2;

                //判断是否未中心，再去返回人脸坐标
                int i1 = input.getPicWidth() / 2;


                //判断 人脸中心 是否位于中性点
                if((i1-recgWidth)<x && (i1+recgWidth)>x){
                    //实时返回人脸坐标
                    faceResult.setCenter(true);
                    sendOneMessage(session, JSONObject.toJSONString(faceResult,SerializerFeature.WriteNullStringAsEmpty));
                    log.debug(Thread.currentThread().getName()+"【居中】【 min：{} x:{} max：{}】",(i1-recgWidth),x,(i1+recgWidth) );
                }else{
                   log.debug(Thread.currentThread().getName()+" 【不居中】【 min：{} x:{} max：{}】",(i1-recgWidth),x,(i1+recgWidth) );
                    faceResult.setStable(false);
                    faceResult.setCenter(false);
                    sendOneMessage(session, JSONObject.toJSONString(faceResult,SerializerFeature.WriteNullStringAsEmpty));
                    continue;
                }

                //计算人脸大小符合最小宽度MinHeadSize
                if ( Math.abs(x2-x1)>=input.getMinHeadSize()&&Math.abs(y1-y2)>=input.getMinHeadSize()){
                    //是否满足总帧数
                    if (frameXList.size()==input.getTotalFrame()&&frameXList.size()==frameYList.size()){
                        float [] xChangeArr=new float[frameXList.size()-1]; //变化范围listx
                        float [] yChangeArr=new float[frameXList.size()-1]; //变化范围listy


                        // 判断指定帧数内 人脸中心点的位移幅度是否满足条件3
                        for(int i=frameYList.size();frameYList.size()-i<frameXList.size()-1;i--){
                            xChangeArr[frameYList.size()-i] =Math.abs(frameXList.get(i-1)-frameXList.get(i-2));
                            yChangeArr[frameYList.size()-i] =Math.abs(frameYList.get(i-1)-frameYList.get(i-2));
                        }
                        log.debug(Thread.currentThread().getName()+" 人脸 X坐标 集合【{}】【{}】", ArrayUtils.toString(frameXList),input.getXRange());
                        log.debug(Thread.currentThread().getName()+" 人脸 Y坐标 集合【{}】【{}】",ArrayUtils.toString(frameYList),input.getYRange());
                        //判断条件
                        log.debug(Thread.currentThread().getName()+" 人脸判断条件X方向变化率【{}】【{}】", ArrayUtils.toString(xChangeArr),input.getXRange());
                        log.debug(Thread.currentThread().getName()+" 人脸判断条件Y方向变化率【{}】【{}】",ArrayUtils.toString(xChangeArr),input.getYRange());
                        if(isAt(input.getXRange(),xChangeArr)&&isAt(input.getYRange(),yChangeArr)){
                            if (faceResult.getResult()!=null){
                                //认证
                                abilityService.afrRecg(faceResult,input.getPicData());
                                log.info(Thread.currentThread().getName()+"检查成功，开始进行人脸对比..............");
                                sendOneMessage(session, JSONObject.toJSONString(faceResult,SerializerFeature.WriteNullStringAsEmpty));
                                clearResult(faceResult,queue);
                            }
                            frameXList.clear();
                            frameYList.clear();
                            Thread.sleep(500);


                        }else{
                            frameXList.remove(0);
                            frameYList.remove(0);

                        }
                    }else{
                        frameXList.add(x);
                        frameYList.add(y);
                    }

                }

            } catch (InterruptedException e) {
                log.error("InterruptedException 获取队列信息出错");
                e.printStackTrace();
            }catch (Exception e){
                log.error("Exception 获取队列信息出错");
                e.printStackTrace();
            }finally {
                //释放 输入数据
                if(input!=null){
                    input.setPicData(null);
                    input=null;
                }

            }



        }

        //#释放资源 防止内存移除
        if(queue!=null){
            queue.clear();
            queue=null;
        }
        if(frameYList!=null){
            frameYList.clear();
            frameYList=null;
        }
        if(frameXList!=null){
            frameXList.clear();
            frameXList=null;
        }
        faceBoxThread.remove();
        UserFaceInfoThread.remove();
        FaceResultThread.remove();
        log.info(Thread.currentThread().getName()+"delFace 线程完成");

    }

    private void clearResult(FaceResult face,BlockingQueue queue) {
        UserFaceInfo result=face.getResult();
        //清除队列信息
        if(queue!=null) queue.clear();
        face.setStable(false);
        face.setMatch(0);
       if(result!=null){
           result.setFaceDataPath("");
           result.setScore("");
           result.setFaceId("");
           result.setBirth(null);

           result.setGender("");
           result.setName("");
           result.setSysTime("");
           result.setId("");
           result.setToken("");
       }



    }

    private  void sendOneMessage(   Session session , String message) {
        if (session != null&&session.isOpen()) {
            try {
                log.debug("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAt(float a,float [] b){
        for(int i=0;i<b.length;i++)
            if(b[i]>a) return false;
        return true;
    }

    private FaceResult afrDetect( InputStream inputFile) {
        try{
            FaceResult faceResult = AbilityCallMethod.callAfrDetectAbility(SystemConstant.getAfrUrl() + SystemConstant.DETECT_URL,
                    SystemConstant.getAfrAppKey(), SystemConstant.getAfrCapKey(),
                    SystemConstant.getAfrSdkVersion(), SystemConstant.getAfrDeveloperKey(),
                    ",minfacewidth=20,minfaceheight=20,autoscale=no,gender=yes,detectThreshold=0.9", inputFile
            , faceBoxThread.get(),UserFaceInfoThread.get(),FaceResultThread.get());
            return faceResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}