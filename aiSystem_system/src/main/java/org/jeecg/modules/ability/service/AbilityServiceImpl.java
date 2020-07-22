package org.jeecg.modules.ability.service;


import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.UuidUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SystemConstant;

import org.jeecg.common.util.CommonMethod;
import org.jeecg.common.util.CommonReturnMethod;

import org.jeecg.common.util.DateTimeUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.ability.util.AbilityCallMethod;
import org.jeecg.modules.ability.util.Recognise_Result;
import org.jeecg.modules.ability.vo.FaceResult;

import org.jeecg.modules.ability.vo.UserFaceInfo;
import org.jeecg.modules.ability.vo.UserVprInfo;
import org.jeecg.modules.attendence.entity.AeDailySign;
import org.jeecg.modules.attendence.service.IAeDailySignService;
import org.jeecg.modules.record.entity.AeRecord;
import org.jeecg.modules.record.service.IAeRecordService;
import org.jeecg.modules.staff.entity.RegisteredInfo;
import org.jeecg.modules.staff.service.IRegisteredInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengjiening on 2020/6/9.
 */
@Service
@Slf4j
public class AbilityServiceImpl implements AbilityService {
    //人脸图像地址
    private final String  BASE_PIC_URL="http://10.0.1.47:8081/";

    @Autowired
    private IAeDailySignService aeDailySignService;

    @Autowired
    private IRegisteredInfoService registeredInfoService;
    @Autowired
    private IAeRecordService aeRecordService;

    /**
     * 人脸探测
     * @return
     */
    @Override
    public FaceResult afrDetect( InputStream inputFile) {

        try{
            FaceResult faceResult = AbilityCallMethod.callAfrDetectAbility(SystemConstant.getAfrUrl() + SystemConstant.DETECT_URL, SystemConstant.getAfrAppKey(), SystemConstant.getAfrCapKey(), SystemConstant.getAfrSdkVersion(), SystemConstant.getAfrDeveloperKey(), "no", inputFile);
            return faceResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 人脸认证
     * @return
     */

    @Override
    public UserFaceInfo afrRecg (FaceResult faceResult,String faceData){
        CommonReturnMethod<List<Recognise_Result>> a = AbilityCallMethod.callAFRRecognizeAbility(SystemConstant.getAfrUrl() + SystemConstant.RECOGNISE_URL, SystemConstant.getAfrAppKey(),
                SystemConstant.getAfrCapKey(), SystemConstant.getAfrSdkVersion(), SystemConstant.getAfrDeveloperKey(), faceResult, Integer.parseInt(SystemConstant.getAfrThreshold()));

        if (a.getErrorCode()==0&&a.getResult().size()>0){
            String userID=a.getResult().get(0).getUserId();
            //todo  数据查询用户信息
            RegisteredInfo user = getUserInfo("afr",userID);
            //match 3 平台识别到 ，但是考勤没有这个人
            //match 2  识别到了这个人
            //match 1   平台未找到这个人
            //match 0   人脸探测失败
            //match 4 用户被禁用
            if(user==null){
                faceResult.setResult(null);
                faceResult.setStable(false);
                faceResult.setMatch(3);
            }else if(user!=null&& user.getIsDel()==1){
                //用户被禁用
                faceResult.getResult().setBirth(user.getBirthday());
                faceResult.getResult().setGender(user.getSex());
                faceResult.getResult().setName(user.getName());
                faceResult.getResult().setSysTime(String.valueOf(DateUtils.getMillis()));
                faceResult.getResult().setId(user.getLoginid());
                faceResult.setStable(true);
                faceResult.getResult().setScore(String.valueOf( a.getResult().get(0).getSocre()));
                faceResult.setMatch(4);
            }else{
                faceResult.getResult().setBirth(user.getBirthday());
                faceResult.getResult().setGender(user.getSex());
                faceResult.getResult().setName(user.getName());
                faceResult.getResult().setSysTime(String.valueOf(DateUtils.getMillis()));
                faceResult.getResult().setId(user.getLoginid());
                //保存数据
                InputStream newvStream = CommonMethod.decoderBase64ToStream(faceData);
                String newPath = genToPathById(user.getLoginid(),"afr",newvStream);
                faceResult.getResult().setFaceDataPath(newPath);
                faceResult.getResult().setToken(a.getResult().get(0).getToken());
                faceResult.getResult().setHeadPic(BASE_PIC_URL+user.getUserFace());
                faceResult.setStable(true);
                faceResult.getResult().setScore(String.valueOf( a.getResult().get(0).getSocre()));
                faceResult.setMatch(2);
                //插入ID
                AeRecord ac =new AeRecord();
                ac.setIp("0.0.0.0");
                ac.setEtype(3);
                ac.setToken(a.getResult().get(0).getToken());
                ac.setLoginid(user.getLoginid());
                ac.setRemark("智慧打卡");
                ac.setState(0);
                ac.setCreateTime(new Date(Long.parseLong(faceResult.getTimestamp())));
                aeRecordService.save(ac);
            }

        }else{
            faceResult.setResult(null);
            faceResult.setStable(false);
            faceResult.setMatch(1);
        }
        return null;
     }
    @Override
    public Result<?> callVprRecognizeAbility( String vStreamstr) throws Exception {
        InputStream vStream = CommonMethod.decoderBase64ToStream(vStreamstr);

//        //测试下载音频
//        InputStream vStream1 = CommonMethod.decoderBase64ToStream(vStreamstr);
//        CommonMethod.writeToLocal("c://test.pcm",vStream1);

        CommonReturnMethod<List<Recognise_Result>> listCommonReturnMethod = AbilityCallMethod.callVPRRecognizeAbility(SystemConstant.getVprUrl() + SystemConstant.VPR_RECOGNISE_URL, SystemConstant.getVprAppKey(), SystemConstant.getVprCapKey(), SystemConstant.getVprSdkVersion(), SystemConstant.getVprDeveloperKey(), vStream, SystemConstant.getVprThreshold(), SystemConstant.getVprAudioformat(), SystemConstant.getVprProperty());
        UserVprInfo info =new UserVprInfo();
        if (listCommonReturnMethod.getErrorCode()==0&&listCommonReturnMethod.getResult().size()>0){
            String token =listCommonReturnMethod.getResult().get(0).getToken();
            info.setToken(token);
            info.setScore(String.valueOf(listCommonReturnMethod.getResult().get(0).getSocre()));


            //todo  数据查询用户信息
            RegisteredInfo user = getUserInfo("vpr",listCommonReturnMethod.getResult().get(0).getUserId());

            if(user==null){
                return Result.error("识别失败");
            }else if(user!=null&& user.getIsDel()==1){
                //用户被禁用
                return  Result.error("用户状态异常");
            }

            info.setBirth(user.getBirthday());
            info.setGender("1".equals(user.getSex())?"男":"女");
            info.setName(user.getName());
            info.setSysTime(String.valueOf(DateUtils.getMillis()));
            info.setId(user.getLoginid());
            info.setHeadPic(BASE_PIC_URL+user.getUserFace());


            //中间记录
            AeRecord ac =new AeRecord();
            ac.setIp("0.0.0.0");
            ac.setEtype(3);
            ac.setToken(token);
            ac.setLoginid(user.getLoginid());
            ac.setRemark("智慧打卡");
            ac.setState(0);
            ac.setCreateTime(new Date());
            aeRecordService.save(ac);


            //保存数据
            InputStream newvStream = CommonMethod.decoderBase64ToStream(vStreamstr);
            String newPath = genToPathById(user.getLoginid(),"vpr",newvStream);
            info.setVoiceDataPath(newPath);

            return  Result.ok(info);
        }else return Result.error("识别失败");

    }

    /**
     * 签到接口
     * @param path
     * @param type
     * @param message
     * @param id
     * @param sysTime
     * @param temperature
     * @return
     */
    @Override
    @Transactional()
    public Result<?> sign(String token ,String path, String type, String message, String id, String sysTime, String temperature) {
        HashMap map=new HashMap(1);
        boolean signType=true;
        //获取考勤记录
        Date signTime= new Date(Long.parseLong(sysTime));
        String today= DateTimeUtils.dateToStr(signTime);
        String todayTime= DateTimeUtils.dateToStrLong(signTime);

        QueryWrapper<AeDailySign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginid",id );
        queryWrapper.apply("UNIX_TIMESTAMP(sign_date) >= UNIX_TIMESTAMP('" + today+" 00:00:00" + "')");
        queryWrapper.apply("UNIX_TIMESTAMP(sign_date) <= UNIX_TIMESTAMP('" + today+" 23:59:59" + "')");
        AeDailySign one = aeDailySignService.getOne(queryWrapper);

        //查询是否人脸认证过来的

        QueryWrapper<AeRecord> queryWrapperAeRecord = new QueryWrapper<>();
        queryWrapperAeRecord.eq("token",token );
        AeRecord aeRecord = aeRecordService.getOne(queryWrapperAeRecord);
        if(aeRecord==null){
            return  Result.error("token不一致，非法请求！！");
        }
        if(one==null) return  Result.error("数据错误，打卡失败");
        //判断是否为早上
        if(one.getStartTime()==null){
            int signStatu=0;
            try {
                long time = DateUtils.get2Time(today + " 09:00:00",todayTime);
                if(time<=0){
                    //未迟到
                    signStatu=0;
                }else if(0<time && time <=15){
                    signStatu=1;
                }else if(15<time && time <=30){
                    signStatu=2;
                }else if(30<time && time <=60){
                    signStatu=3;
                }else{
                    signStatu=4;
                }
                one.setStartTime(signTime);
                one.setRemark("签到备注："+message);
                one.setIsLate(signStatu);
                one.setIsAbsent(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            signType=true;
            map.put("signType","signIn");
        }else{
            //签退操作
            int signStatu=0;
            try {
                long time = DateUtils.get2Time(today + " 18:00:00", todayTime);
                if(time<0){
                    //早退
                    signStatu=-1;
                }else if(time==0){
                    signStatu=0;
                }else if (time<=3*60+30){
                    signStatu=1;
                }else if (time<=4*60){
                    signStatu=2;
                }else if (time<=5*60){
                    signStatu=3;
                }else if (time<=6*60){
                    signStatu=4;
                }

                one.setEndTime(signTime);
                String perMess=one.getRemark();
                one.setRemark(perMess==null?"签退备注："+message:perMess+"\n签退备注："+message);
                one.setIsOvertime(signStatu);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            signType=false;
            map.put("signType","signOut");
        }
        if("afr".equals(type)){
            one.setFace(path);
        }else{
            one.setVoice(path);
        }
        one.setUpdateTime(signTime);
        one.setTemperature(temperature);


        aeRecord.setState(1);
        aeRecordService.updateById(aeRecord);
        aeDailySignService.updateById(one);
        Result<Object> result = signType? Result.ok("签到成功"): Result.ok("签退成功");
        result.setResult(map);
        return result;
    }

    /**
     * 温度检察接口
     * @param id
     * @param temperature
     * @return
     */
    @Override
    public Result<?> check(String id,String sysTime, String temperature) {
        //获取考勤记录
        Date signTime= new Date(Long.parseLong(sysTime));
        String today= DateTimeUtils.dateToStr(signTime);
        String todayTime= DateTimeUtils.dateToStrLong(signTime);

        QueryWrapper<AeDailySign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginid",id );
        queryWrapper.apply("UNIX_TIMESTAMP(sign_date) >= UNIX_TIMESTAMP('" + today+" 00:00:00" + "')");
        queryWrapper.apply("UNIX_TIMESTAMP(sign_date) <= UNIX_TIMESTAMP('" + today+" 23:59:59" + "')");
        AeDailySign one = aeDailySignService.getOne(queryWrapper);

        if(one==null) return  Result.error("数据错误，打卡失败");
        if(one.getStartTime()!=null && one.getEndTime()==null){
            //未签退 就高温打卡
            int signStatu=0;
            try {
                long time = DateUtils.get2Time(today + " 18:00:00", todayTime);
                if(time<0){
                    //早退
                    signStatu=-1;
                }else if(time==0){
                    signStatu=0;
                }else if (time<=3*60+30){
                    signStatu=1;
                }else if (time<=4*60){
                    signStatu=2;
                }else if (time<=5*60){
                    signStatu=3;
                }else if (time<=6*60){
                    signStatu=4;
                }
                one.setEndTime(signTime);
                one.setRemark("签退备注：高温签退，"+temperature);
                one.setIsOvertime(signStatu);
                aeDailySignService.updateById(one);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return Result.ok("高温补卡成功");
        }
        return Result.error("未打卡");
    }

    private RegisteredInfo getUserInfo(String type,String id){
        RegisteredInfo RegisteredInfo=null;
        if("afr".equals(type)){
            QueryWrapper<RegisteredInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("AFR_INFO",id);
            return registeredInfoService.getOne(queryWrapper);
        }else if("vpr".equals(type)){
            QueryWrapper<RegisteredInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("VPR_INFO",id);
            return registeredInfoService.getOne(queryWrapper);
        }
        return null;
    }

    /**
     * 生成数据文件
     * @param id
     * @param type
     * @param vStream
     */
    private String  genToPathById(String id,String type, InputStream vStream ){
        String path =SystemConstant.getDataPathPre()+"/";
        String newDay =  DateUtils.formatDate();
        System.out.println(newDay);
//        File dir = new File(path+newDay);
//        if (!dir.isDirectory()) {
//            System.out.println("用户ID索引文件不存在 to 创建");
//            dir.mkdirs();
//            System.out.println("创建文件夹");
//        }
        String newPath= path+newDay+"/"+id+"/";
        File dir1 = new File(newPath);
        if (!dir1.isDirectory()) {
            System.out.println("时间索引文件不存在 to 创建");
            //dir1.setWritable(true, false);
            dir1.mkdirs();
        }
        String newFileName="afr".equals(type)?id+"_"+type+".png":id+"_"+type+".pcm";
        //保存音頻
        String filePath =newPath +newFileName;
        CommonMethod.writeToLocal(filePath,vStream);

        return filePath;
    }


}
