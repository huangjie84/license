package com.orcadt.iot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.orcadt.iot.common.utils.PageUtils;
import com.orcadt.iot.entity.LicenseGenerateRecordEntity;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email slayer84@163.com
 * @date 2020-07-06 17:03:34
 */
public interface LicenseGenerateRecordService extends IService<LicenseGenerateRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

