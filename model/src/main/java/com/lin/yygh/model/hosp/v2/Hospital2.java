package com.lin.yygh.model.hosp.v2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lin.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Hospital
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "Hospital")
@TableName("hospital")
public class Hospital2 extends BaseEntity {

	@ApiModelProperty(value = "医院名称")
	@TableField("hosname")
	private String hosname;

	@ApiModelProperty(value = "省code")
	@TableField("province_code")
	private Long provinceCode;

	@ApiModelProperty(value = "市code")
	@TableField("city_code")
	private Long cityCode;

	@ApiModelProperty(value = "区code")
	@TableField("district_code")
	private Long districtCode;

	@ApiModelProperty(value = "医院类型")
	@TableField("hostype")
	private Integer hostype;

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

	@ApiModelProperty(value = "api基础路径")
	@TableField("api_url")
	private String apiUrl;

	@ApiModelProperty(value = "签名秘钥")
	@TableField("sign_key")
	private String signKey;

	@ApiModelProperty(value = "联系人姓名")
	@TableField("contacts_name")
	private String contactsName;

	@ApiModelProperty(value = "联系人手机")
	@TableField("contacts_phone")
	private String contactsPhone;

	@ApiModelProperty(value = "状态 0：未上线 1：已上线")
	@TableField("status")
	private Integer status;
}

