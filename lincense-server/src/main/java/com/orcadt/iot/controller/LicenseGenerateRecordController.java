package com.orcadt.iot.controller;

import com.orcadt.iot.common.utils.PageUtils;
import com.orcadt.iot.common.utils.R;
import com.orcadt.iot.service.LicenseGenerateRecordService;
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
@RequestMapping("/licensegeneraterecord")
public class LicenseGenerateRecordController {
    @Autowired
    private LicenseGenerateRecordService licenseGenerateRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = licenseGenerateRecordService.queryPage(params);

        return R.ok().put("page", page);
    }

//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{recordId}")
//    @RequiresPermissions(":licensegeneraterecord:info")
//    public R info(@PathVariable("recordId") Integer recordId){
//		LicenseGenerateRecordEntity licenseGenerateRecord = licenseGenerateRecordService.getById(recordId);
//
//        return R.ok().put("licenseGenerateRecord", licenseGenerateRecord);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions(":licensegeneraterecord:save")
//    public R save(@RequestBody LicenseGenerateRecordEntity licenseGenerateRecord){
//		licenseGenerateRecordService.save(licenseGenerateRecord);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions(":licensegeneraterecord:update")
//    public R update(@RequestBody LicenseGenerateRecordEntity licenseGenerateRecord){
//		licenseGenerateRecordService.updateById(licenseGenerateRecord);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions(":licensegeneraterecord:delete")
//    public R delete(@RequestBody Integer[] recordIds){
//		licenseGenerateRecordService.removeByIds(Arrays.asList(recordIds));
//
//        return R.ok();
//    }

}
