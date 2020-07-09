package com.orcadt.iot.service.impl;

import com.orcadt.iot.common.utils.PageUtils;
import com.orcadt.iot.common.utils.Query;
import com.orcadt.iot.dao.LicenseDownloadRecordDao;
import com.orcadt.iot.entity.LicenseDownloadRecordEntity;
import com.orcadt.iot.service.LicenseDownloadRecordService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("licenseDownloadRecordService")
public class LicenseDownloadRecordServiceImpl extends ServiceImpl<LicenseDownloadRecordDao, LicenseDownloadRecordEntity> implements LicenseDownloadRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LicenseDownloadRecordEntity> page = this.page(
                new Query<LicenseDownloadRecordEntity>().getPage(params),
                new QueryWrapper<LicenseDownloadRecordEntity>()
        );

        return new PageUtils(page);
    }

}
