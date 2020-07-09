package com.orcadt.iot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author chenshun
 * @email slayer84@163.com
 * @date 2020-07-06 17:03:34
 */
@Data
@TableName("license_generate_record")
public class LicenseGenerateRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录主键
	 */
	@TableId
	private Long recordId;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 秘钥
	 */
	private String keyPass;
	/**
	 * 公钥
	 */
	private String storePass;
	/**
	 * license文件地址
	 */
	private String licensePath;
	/**
	 * 私钥地址
	 */
	private String privateKeysStorePath;
	/**
	 * 生效时间
	 */
	private String issuedTime;
	/**
	 * 过期时间
	 */
	private String expiryTime;
	/**
	 * 消费者类型
	 */
	private String consumerType;
	/**
	 * 消费者数量
	 */
	private Integer consumerAmount;
	/**
	 * 证书描述
	 */
	private String description;
	/**
	 * 证书授权项
	 */
	private String licenseCheckModel;

}
