package org.jeecg.modules.staff.entity;

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
 * @Description: registered_info
 * @Author: jeecg-boot
 * @Date:   2020-07-10
 * @Version: V1.0
 */
@Data
@TableName("registered_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="registered_info对象", description="registered_info")
public class faceUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**cardNumber*/
	@Excel(name = "cardNumber", width = 15)
    @ApiModelProperty(value = "cardNumber")
    private String cardNumber;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**birthday*/
	@Excel(name = "birthday", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "birthday")
    private Date birthday;
	/**address*/
	@Excel(name = "address", width = 15)
    @ApiModelProperty(value = "address")
    private String address;
	/**sex*/
	@Excel(name = "sex", width = 15)
    @ApiModelProperty(value = "sex")
    private String sex;
	/**nation*/
	@Excel(name = "nation", width = 15)
    @ApiModelProperty(value = "nation")
    private String nation;
	/**cardSavePath*/
	@Excel(name = "cardSavePath", width = 15)
    @ApiModelProperty(value = "cardSavePath")
    private String cardSavePath;
	/**afrInfo*/
	@Excel(name = "afrInfo", width = 15)
    @ApiModelProperty(value = "afrInfo")
    private Long afrInfo;
	/**fprInfo*/
	@Excel(name = "fprInfo", width = 15)
    @ApiModelProperty(value = "fprInfo")
    private Long fprInfo;
	/**vprInfo*/
	@Excel(name = "vprInfo", width = 15)
    @ApiModelProperty(value = "vprInfo")
    private Long vprInfo;
	/**isDel*/
	@Excel(name = "isDel", width = 15)
    @ApiModelProperty(value = "isDel")
    private Integer isDel;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private Date updateTime;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
	/**staffNum*/
	@Excel(name = "staffNum", width = 15)
    @ApiModelProperty(value = "staffNum")
    private String staffNum;

    @Excel(name = "坚持天数", width = 15)
    @ApiModelProperty(value = "坚持天数")
    private Integer keepDays;

}
