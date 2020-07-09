使用JDK自带的 keytool 工具生成公私钥证书库：

假如我们设置公钥库密码为： public_password1234 ，私钥库密码为： private_password1234 ，则生成命令如下：

#生成命令
keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_orcadt_key_654321" -keypass "private_orcadt_key_123456" -dname "CN=orcadt.com, OU=orcadt.com, O=orcadt.com, L=SH, ST=SH, C=CN"

#导出命令
keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_orcadt_key_654321" -file "certfile.cer"

#导入命令
keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "public_orcadt_key_654321"
复制代码
上述命令执行完成之后，会在当前路径下生成三个文件，分别是：privateKeys.keystore、publicCerts.keystore、certfile.cer。 其中文件certfile.cer不再需要可以删除，文件privateKeys.keystore用于当前的 ServerDemo 项目给客户生成license文件，而文件publicCerts.keystore则随应用代码部署到客户服务器，用户解密license文件并校验其许可信息 。
