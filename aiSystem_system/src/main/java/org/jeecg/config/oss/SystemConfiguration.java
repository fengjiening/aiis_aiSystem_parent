package org.jeecg.config.oss;

import org.jeecg.common.constant.SystemConstant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfiguration {

    @Value("${hcicloud.afr.afr_url}")
    private String afr_url;
    @Value("${hcicloud.afr.devkey}")
    private String afr_devkey;
    @Value("${hcicloud.afr.threshold}")
    private String afr_threshold;
    @Value("${hcicloud.afr.appKey}")
    private String afr_appKey;


    @Value("${hcicloud.vpr.vpr_url}")
    private String vpr_url;
    @Value("${hcicloud.vpr.devkey}")
    private String vpr_devkey;
    @Value("${hcicloud.vpr.threshold}")
    private String vpr_threshold;
    @Value("${hcicloud.vpr.appKey}")
    private String vpr_appKey;
    @Value("${hcicloud.vpr.property}")
    private String vpr_property;
    @Value("${hcicloud.vpr.audioformat}")
    private String  vpr_audioformat;

    @Value("${hcicloud.data.path}")
    private String  data_path;


    @Bean
    public void initAfrBootConfiguration() {
        SystemConstant.setAfrAppKey(afr_appKey);
        SystemConstant.setAfrDeveloperKey(afr_devkey);
        SystemConstant.setAfrUrl(afr_url);
        SystemConstant.setAfrThreshold(afr_threshold);

        SystemConstant.setVprUrl(vpr_url);
        SystemConstant.setVprAppKey(vpr_appKey);
        SystemConstant.setVprDeveloperKey(vpr_devkey);
        SystemConstant.setVprThreshold(vpr_threshold);
        SystemConstant.setVprAudioformat(vpr_audioformat);
        SystemConstant.setVprProperty(vpr_property);
        SystemConstant.setDataPathPre(data_path);

    }
}