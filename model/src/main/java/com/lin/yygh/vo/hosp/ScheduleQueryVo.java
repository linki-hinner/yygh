package com.lin.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "Schedule")
public class ScheduleQueryVo {
	
	@ApiModelProperty(value = "医院编号")
	private String hoscode;

	@ApiModelProperty(value = "医院Id")
	private Long hospitalId;

	@ApiModelProperty(value = "科室编号")
	private String depcode;

	@ApiModelProperty(value = "医院编号")
	private Long departmentId;

	@ApiModelProperty(value = "医生编号")
	private String doccode;

	@ApiModelProperty(value = "安排日期")
	private Date workDate;

	@ApiModelProperty(value = "安排时间（0：上午 1：下午）")
	private Integer workTime;

	@ApiModelProperty(value = "当前页")
	private Integer current;

	@ApiModelProperty(value = "每页记录数")
	private Integer size;
}

