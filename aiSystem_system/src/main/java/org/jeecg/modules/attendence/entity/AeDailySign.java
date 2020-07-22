package org.jeecg.modules.attendence.entity;

import java.io.Serializable;
import java.util.Date;

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
 * @Description: 考勤数据
 * @Author: jeecg-boot
 * @Date:   2020-06-28
 * @Version: V1.0
 */
@Data
@TableName("ae_daily_sign")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ae_daily_sign对象", description="考勤数据")
public class AeDailySign implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private String name;
	/**工号*/
	@Excel(name = "工号", width = 15)
    @ApiModelProperty(value = "工号")
    private String loginid;
	/**打卡日期*/
	@Excel(name = "打卡日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "打卡日期")
    private Date signDate;
	/**签到时间*/
	@Excel(name = "签到时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签到时间")
    private Date startTime;
	/**签到状态*/
	@Excel(name = "签到状态", width = 15, dicCode = "sign_status")
	@Dict(dicCode = "sign_status")
    @ApiModelProperty(value = "签到状态")
    private Integer isLate;
	/**签退时间*/
	@Excel(name = "签退时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签退时间")
    private Date endTime;
	/**签退状态*/
	@Excel(name = "签退状态", width = 15, dicCode = "end_status")
	@Dict(dicCode = "end_status")
    @ApiModelProperty(value = "签退状态")
    private Integer isOvertime;
	/**出勤状态*/
	@Excel(name = "出勤状态", width = 15, dicCode = "absent_status")
	@Dict(dicCode = "absent_status")
    @ApiModelProperty(value = "出勤状态")
    private Integer isAbsent;
	/**请假状态*/
	@Excel(name = "请假状态", width = 15, dicCode = "leave_status")
	@Dict(dicCode = "leave_status")
    @ApiModelProperty(value = "请假状态")
    private Integer isLeave;
	/**打卡备注*/
	@Excel(name = "打卡备注", width = 15)
    @ApiModelProperty(value = "打卡备注")
    private String remark;
	/**考勤备注*/
	@Excel(name = "考勤备注", width = 15)
    @ApiModelProperty(value = "考勤备注")
    private String extendedInfo;
	/**人脸图像*/
	@Excel(name = "人脸图像", width = 15)
    @ApiModelProperty(value = "人脸图像")
    private String face;
	/**声纹声音*/
	@Excel(name = "声纹声音", width = 15)
    @ApiModelProperty(value = "声纹声音")
    private String voice;
	/**体温*/
	@Excel(name = "体温", width = 15)
    @ApiModelProperty(value = "体温")
    private String temperature;
	/**是否修改过*/
	@Excel(name = "是否修改过", width = 15, dicCode = "modify_status")
	@Dict(dicCode = "modify_status")
    @ApiModelProperty(value = "是否修改过")
    private Integer hasModify;
	/**是否工作日*/
	@Excel(name = "是否工作日", width = 15, dicCode = "workday_status")
	@Dict(dicCode = "workday_status")
    @ApiModelProperty(value = "是否工作日")
    private Integer isWorkday;
	/**迟到分钟*/
	@Excel(name = "迟到分钟", width = 15)
    @ApiModelProperty(value = "迟到分钟")
    private Integer lateTime;
}
