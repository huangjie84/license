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
@TableName("license_download_record")
public class LicenseDownloadRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 下载ID
	 */
	@TableId
	private Integer downloadId;
	/**
	 * licenseID
	 */
	private Integer generateId;
	/**
	 * 下载时间
	 */
	private Date downloadTime;
	/**
	 * 下载状态
	 */
	private Integer downloadResult;

}
