package org.jeecg.config.thread;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.attendence.entity.AeDailySign;
import org.jeecg.modules.staff.entity.RegisteredInfo;
import org.jeecg.modules.staff.entity.faceUser;

import org.jeecg.modules.staff.service.IRegisteredInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fengjiening on 2020/7/10.
 * 同步用户信息
 *
 */
@Component
@Slf4j
public class SynchUserJob {
    @Autowired
    private IRegisteredInfoService registeredInfoService;

    //@Scheduled(fixedRate = 20000)
    public void testScheduled() {
        log.debug("人员同步 开始...................");
        QueryWrapper<RegisteredInfo> queryWrapper = new QueryWrapper<>();


        List<faceUser> root_mysql = DynamicDBUtil.findListEntities("root_mysql", "select * from registered_info", faceUser.class, new Object[]{});
        RegisteredInfo user = null;
        List<RegisteredInfo> userList = new ArrayList<>();
        Date date = new Date();
        for (faceUser registUser : root_mysql) {

            user = new RegisteredInfo();
            BeanUtils.copyProperties(registUser, user);
            user.setSex("男".equals(registUser.getSex()) ? "1" : "0");
            user.setSysOrgCode("A01");
            user.setCreateBy("admin");
            user.setCreateTime(date);
            user.setIdcard(registUser.getCardNumber());
            user.setId(registUser.getStaffNum());
            //查询天数
            QueryWrapper<RegisteredInfo> queryWrapperDay = new QueryWrapper<>();
            queryWrapperDay.eq("idcard", registUser.getCardNumber());
            RegisteredInfo byId = registeredInfoService.getOne(queryWrapperDay);
            //RegisteredInfo byId = registeredInfoService.getById(registUser.getStaffNum());
            if (byId != null) {
                user.setLoginid(byId.getLoginid());
                user.setKeepDays(byId.getKeepDays());
                user.setIsDel(byId.getIsDel());
                registeredInfoService.removeById(byId.getLoginid());
            } else {
                user.setKeepDays(0);
                user.setLoginid(registUser.getStaffNum());
                user.setIsDel(1);
            }

            //获取人脸数据
            try {
                Map faceUser = (Map) DynamicDBUtil.findOne("root_mysql", "select * from afr_info where ID=?", new Object[]{registUser.getAfrInfo()});
                if (faceUser != null) {
                    user.setUserFace(faceUser.get("AFR_PATH_ONE").toString());
                } else {
                    user.setIsDel(1);
                }
            } catch (Exception e) {
                log.error("未获取头像..................{}", registUser.getAfrInfo());
            }
            userList.add(user);
            user = null;
        }
    }
}
