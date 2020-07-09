package com.orcadt.iot.controller;

import com.orcadt.iot.common.utils.PageUtils;
import com.orcadt.iot.common.utils.R;
import com.orcadt.iot.service.LicenseDownloadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author chenshun
 * @email slayer84@163.com
 * @date 2020-07-06 17:03:34
 */
@RestController
@RequestMapping("/licensedownloadrecord")
public class LicenseDownloadRecordController {
    @Autowired
    private LicenseDownloadRecordService licenseDownloadRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = licenseDownloadRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{downloadId}")
//    public R info(@PathVariable("downloadId") Integer downloadId){
//		LicenseDownloadRecordEntity licenseDownloadRecord = licenseDownloadRecordService.getById(downloadId);
//
//        return R.ok().put("licenseDownloadRecord", licenseDownloadRecord);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody LicenseDownloadRecordEntity licenseDownloadRecord){
//		licenseDownloadRecordService.save(licenseDownloadRecord);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody LicenseDownloadRecordEntity licenseDownloadRecord){
//		licenseDownloadRecordService.updateById(licenseDownloadRecord);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Integer[] downloadIds){
//		licenseDownloadRecordService.removeByIds(Arrays.asList(downloadIds));
//
//        return R.ok();
//    }

}
