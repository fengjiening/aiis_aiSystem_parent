package org.jeecg.modules.ability.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.CommonMethod;
import org.jeecg.common.util.MD5Util;
import org.jeecg.modules.ability.service.AbilityService;
import org.jeecg.modules.ability.util.AbilityCallMethod;
import org.jeecg.modules.attendence.service.IAeDailySignService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fengjiening on 2020/6/17.
 */
@RestController
@Slf4j
public class AbilityController{

    @Autowired
    private AbilityService abilityService;

    /**
     * 声纹识别
     * @param invalidata
     * @return
     */
    @RequestMapping(value = "/vpr/{invalidata}")
    public Result<?> vprRecognize(@PathVariable("invalidata") String invalidata,String voiceData,String dataFormat ) {
        log.info("声纹识别 HTTP接口进入");
/*
//测试代码
        String savePath = "c:/pcm/34796210.pcm";

        try {
            String s = CommonMethod.encodeBase64File(savePath);
            return abilityService.callVprRecognizeAbility(s );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;*/

        //系统校验
        if (StringUtils.isEmpty(invalidata)) return Result.error("验证信息错误");
        if (StringUtils.isEmpty(voiceData)) return Result.error("音频数据为空");
        if (StringUtils.isEmpty(dataFormat)) return Result.error("音频格式不能为空");
        try{
            String [] inStr = invalidata.split("-");
            if (MD5Util.MD5Encode(inStr[2]+MD5Util.SECRET,null).equals(inStr[1])){
                Result<?> result = abilityService.callVprRecognizeAbility(voiceData);
                log.info("声纹识别 HTTP接口结束");
                return result;
            }else  return  Result.error("非法请求！！");

        }catch (JeecgBootException e){
            return   Result.error(e.getMessage());
        }catch (Exception e){
             return   Result.error("非法请求！！");
        }

    }


    /**
     * 签到接口
     * @param invalidata
     * @return
     */
    @RequestMapping(value = "/sign/{invalidata}")
    public Result<?> sign(@PathVariable("invalidata") String invalidata,String path,String type,String message,String id ,String sysTime,String temperature,String name ) {
        log.info("签到 HTTP接口进入");
        try{
             String [] inStr = invalidata.split("-");
            if (MD5Util.MD5Encode(inStr[2]+MD5Util.SECRET,null).equals(inStr[1])){
                return abilityService.sign(inStr[0] ,path,type,message,id,sysTime,temperature,name);
            }else  return  Result.error("非法请求！！");

        }catch (JeecgBootException e){
            return   Result.error(e.getMessage());
        }catch (Exception e){
            return   Result.error("非法请求！！");
        }


    }
    /**
     * 高温检测接口
     * @param invalidata
     * @return
     */
    @RequestMapping(value = "/check/{invalidata}")
    public Result<?> check(@PathVariable("invalidata") String invalidata,String id,String sysTime,String temperature ) {
        log.info("签到 HTTP接口进入");
        try{
            String [] inStr = invalidata.split("-");
            if (MD5Util.MD5Encode(inStr[2]+MD5Util.SECRET,null).equals(inStr[1])){
                return abilityService.check(id,sysTime,temperature);
            }else  return  Result.error("非法请求！！");

        }catch (JeecgBootException e){
            return   Result.error(e.getMessage());
        }catch (Exception e){
            return   Result.error("非法请求！！");
        }


    }
}
