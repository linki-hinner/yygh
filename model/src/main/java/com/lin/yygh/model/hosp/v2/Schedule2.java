package com.lin.yygh.model.hosp.v2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lin.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * Schedule
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "Schedule")
@TableName("schedule")
public class Schedule2 extends BaseEntity {

	@ApiModelProperty(value = "科室编号")
	@TableField(value = "hospital_id")
	private Long hospitalId;

	@ApiModelProperty(value = "科室编号")
	@TableField(value = "hospital_id")
	private Long departmentId;

	@ApiModelProperty(value = "职称")
	@TableField(value = "title")
	private String title;

	@ApiModelProperty(value = "医生名称")
	@TableField(value = "docname")
	private String docname;

	@ApiModelProperty(value = "擅长技能")
	@TableField(value = "skill")
	private String skill;

	@ApiModelProperty(value = "排班日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "work_date")
	private Date workDate;

	@ApiModelProperty(value = "排班时间（0：上午 1：下午）")
	@TableField(value = "work_time")
	private Integer workTime;

	@ApiModelProperty(value = "可预约数")
	@TableField(value = "reserved_number")
	private Integer reservedNumber;

	@ApiModelProperty(value = "剩余预约数")
	@TableField(value = "available_number")
	private Integer availableNumber;

	@ApiModelProperty(value = "挂号费")
	@TableField(value = "amount")
	private BigDecimal amount;

	@ApiModelProperty(value = "排班状态（-1：停诊 0：停约 1：可约）")
	@TableField(value = "status")
	private Integer status;

	@ApiModelProperty(value = "排班编号（医院自己的排班主键）")
	@TableField(value = "hosScheduleId")
	private String hosScheduleId;

}

