package com.lin.yygh.model.hosp.v2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lin.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * RegisterRule
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "预约规则")
@TableName("booking_rule")
public class BookingRule2 extends BaseEntity {
	@ApiModelProperty(value = "预约周期")
	@TableField("hospital_id")
	private Long hospitalId;

	@ApiModelProperty(value = "预约周期")
	@TableField("cycle")
	private Integer cycle;

	@ApiModelProperty(value = "放号时间")
	@TableField("release_time")
	private String releaseTime;

	@ApiModelProperty(value = "停挂时间")
	@TableField("stop_time")
	private String stopTime;

	@ApiModelProperty(value = "退号截止天数（如：就诊前一天为-1，当天为0）")
	@TableField("quit_day")
	private Integer quitDay;

	@ApiModelProperty(value = "退号时间")
	@TableField("quit_time")
	private String quitTime;

	@ApiModelProperty(value = "预约规则")
	@TableField("rule")
	private String rule;


}

