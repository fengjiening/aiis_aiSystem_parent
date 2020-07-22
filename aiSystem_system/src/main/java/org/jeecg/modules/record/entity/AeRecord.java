package org.jeecg.modules.record.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: ae_record
 * @Author: jeecg-boot
 * @Date:   2020-07-10
 * @Version: V1.0
 */
@Data
@TableName("ae_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ae_record对象", description="ae_record")
public class AeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**登录id*/
	@Excel(name = "登录id", width = 15)
    @ApiModelProperty(value = "登录id")
    private String loginid;
	/**1 前台 2 app*/
	@Excel(name = "1 前台 2 app", width = 15)
    @ApiModelProperty(value = "1 前台 2 app")
    private Integer etype;
	/**能力平台token*/
	@Excel(name = "能力平台token", width = 15)
    @ApiModelProperty(value = "能力平台token")
    private String token;
	/**ip*/
	@Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private String ip;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private String latitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private String longitude;
	/**签到签退备注*/
	@Excel(name = "签到签退备注", width = 15)
    @ApiModelProperty(value = "签到签退备注")
    private String remark;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private String udid;
	/**是否警告*/
	@Excel(name = "是否警告", width = 15)
    @ApiModelProperty(value = "是否警告")
    private Integer warning;
	/**异常报警信息*/
	@Excel(name = "异常报警信息", width = 15)
    @ApiModelProperty(value = "异常报警信息")
    private String info;
	/**是否已经使用 0 否 1 是*/
	@Excel(name = "是否已经使用 0 否 1 是", width = 15)
    @ApiModelProperty(value = "是否已经使用 0 否 1 是")
    private Integer state;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
