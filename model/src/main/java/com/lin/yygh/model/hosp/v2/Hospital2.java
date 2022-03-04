package com.lin.yygh.model.hosp.v2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lin.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * Hospital
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "Hospital")
@Document("Hospital")
public class Hospital2 extends BaseEntity {

	@ApiModelProperty(value = "医院编号")
	@TableField("hoscode")
	private String hoscode;

	@ApiModelProperty(value = "详细地址")
	@TableField("address")
	private String address;

	@ApiModelProperty(value = "坐车路线")
	@TableField("route")
	private String route;

	@ApiModelProperty(value = "医院logo")
	@TableField("logoData")
	private String logoData;

	@ApiModelProperty(value = "医院简介")
	@TableField("intro")
	private String intro;

	@ApiModelProperty(value = "状态 0：未上线 1：已上线")
	@TableField("status")
	private Integer status;
}

