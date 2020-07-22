package org.jeecg.modules.ability.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fengjiening on 2020/6/18.
 */
@Data
public class UserBaseInfo implements Serializable {

    private String score;
    private String token;
    private String headPic;
    private String name;
    private Date birth;
    private String gender;
    private String id;
    private String sysTime;

}
