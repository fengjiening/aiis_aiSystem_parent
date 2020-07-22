package org.jeecg.modules.ability.service;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.ability.vo.FaceResult;
import org.jeecg.modules.ability.vo.UserFaceInfo;
import org.jeecg.modules.ability.vo.UserVprInfo;

import java.io.InputStream;

/**
 * Created by fengjiening on 2020/6/9.
 */

public interface AbilityService {

    public FaceResult afrDetect (InputStream inputFile);
    public UserFaceInfo afrRecg (FaceResult faceResult,String faceData);
    public Result<?> callVprRecognizeAbility( String  vStreamStr) throws Exception;

    Result<?> sign(String token ,String path, String type, String message, String id, String sysTime, String temperature);

    Result<?> check(String id,String sysTime, String temperature);
}
