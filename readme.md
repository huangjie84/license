# license设计概要
- server工程负责授权文件的生成。部署在公司内部
- client工程负责实现授权的校验。部署在客户的物理机器上
- checker上一个jar包类型的插件。用来提供向client发送验证请求


##  lincense-server
> 负责license.lic 文件生成。文件地址在application.properties可配置
调用接口 {ip}/license/generateLicense

## lincense-client

1. 提供了 {ip}/main  界面.提供授权信息查看.提供导入替换license功能.
2. 提供了 {ip}/license/check 接口提供授权信息校验及查询
- [x] 压测结果约3w kps.完全满足校验需求.


## lincenseCheck
*主要提供license校验.* 

> 1.要在启动类上加上注解,com.orcadt.license为license包
```
@SpringBootApplication(scanBasePackages = {"com.orcadt.license","com.orcadt.iot"})
```
> 2.需要在配置文件内添加

```
license.check.url=http://{ip}/license/check
```

> 3.在项目内部获取授权信息 直接调用 LicenseAuthHolder.authModels 即可


```
    @GetMapping("test")
    public Object getLicenseResult(){
     return LicenseAuthHolder.authModels;
    }
```

> 4.可参考 license-demo工程