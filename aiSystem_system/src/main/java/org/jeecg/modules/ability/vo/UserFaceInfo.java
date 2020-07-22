package org.jeecg.modules.ability.vo;

import lombok.Data;
import org.apache.ibatis.annotations.ConstructorArgs;

import java.io.Serializable;

/**
 * Created by fengjiening on 2020/6/11.
 */
@Data
public class UserFaceInfo extends UserBaseInfo{

    private String faceId;
    private String faceDataPath;

}
