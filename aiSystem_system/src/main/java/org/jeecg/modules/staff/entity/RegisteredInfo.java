package org.jeecg.modules.staff.entity;

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
 * @Description: 考勤员工
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Data
@TableName("ae_user_map")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ae_user_map对象", description="考勤员工")
public class RegisteredInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
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
    @TableId(type = IdType.ID_WORKER_STR)
	@Excel(name = "工号", width = 15)
    @ApiModelProperty(value = "工号")
    private String loginid;
	/**身份证号码*/
	@Excel(name = "身份证号码", width = 15)
    @ApiModelProperty(value = "身份证号码")
    private String idcard;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birthday;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String address;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private String sex;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
    private String nation;
	/**卡保存路径*/
	@Excel(name = "卡保存路径", width = 15)
    @ApiModelProperty(value = "卡保存路径")
    private String cardSavePath;
	/**人脸*/
	@Excel(name = "人脸", width = 15)
    @ApiModelProperty(value = "人脸")
    private Long afrInfo;
	/**指纹*/
	@Excel  (name = "指纹", width = 15)
    @ApiModelProperty(value = "指纹")
    private Long fprInfo;
	/**声纹*/
	@Excel(name = "声纹", width = 15)
    @ApiModelProperty(value = "声纹")
    private Long vprInfo;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "stuff_status")
	@Dict(dicCode = "stuff_status")
    @ApiModelProperty(value = "状态")
    private Integer isDel;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
    private String udid;
	/**排班方式*/
	@Excel(name = "排班方式", width = 15)
    @ApiModelProperty(value = "排班方式")
    private String schedule;

    /**基本人脸*/
    @Excel(name = "基本人脸", width = 15)
    @ApiModelProperty(value = "基本人脸")
    private String userFace;

    @Excel(name = "坚持天数", width = 15)
    @ApiModelProperty(value = "坚持天数")
    private Integer keepDays;



}
