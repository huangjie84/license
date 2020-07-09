package com.orcadt.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orcadt.iot.common.utils.PageUtils;
import com.orcadt.iot.common.utils.Query;
import com.orcadt.iot.dao.LicenseGenerateRecordDao;
import com.orcadt.iot.entity.LicenseGenerateRecordEntity;
import com.orcadt.iot.service.LicenseGenerateRecordService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("licenseGenerateRecordService")
public class LicenseGenerateRecordServiceImpl extends ServiceImpl<LicenseGenerateRecordDao, LicenseGenerateRecordEntity> implements LicenseGenerateRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LicenseGenerateRecordEntity> page = this.page(
                new Query<LicenseGenerateRecordEntity>().getPage(params),
                new QueryWrapper<LicenseGenerateRecordEntity>()
                        .select("record_id,subject,issued_time,expiry_time,description,license_check_model")
                        .orderByDesc("record_id")
        );

        return new PageUtils(page);
    }

}
