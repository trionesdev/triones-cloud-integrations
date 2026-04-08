# 云服务集成

> 对多个云服务平台的 对象存储，短信，等通用业务进行统一抽象定义，满足在切换不同平台服务的时候，只需要更新配置文件，而不需要改动代码。

## 模块

- 阿里云
    - [OSS 对象存储](csi-pkg/csi-aliyun-oss)
    - [SMS 短信服务](csi-pkg/csi-aliyun-sms)
- 腾讯云
    - [COS 对象存储](csi-pkg/csi-tencentcloud-cos)
    - [SMS 短信服务](csi-pkg/csi-tencentcloud-sms)
    - [SES 邮件服务](csi-pkg/csi-tencentcloud-ses)
    - [OCR 文本识别](csi-pkg/csi-tencentcloud-ocr)
- 华为云
    - [OBS 对象存储](csi-pkg/csi-huaweicloud-obs)
    - [SMS 短信服务](csi-pkg/csi-huaweicloud-sms)
- 七牛云
    - [KODO 对象存储](csi-pkg/csi-qiniu-kodo)
- 微软云
    - [BLOB 块存储](csi-pkg/csi-azure-blob)
- 谷歌云
    - [STORAGE 存储](csi-pkg/csi-googlecloud-storage)
- 自部署服务
    - [minio](csi-pkg/csi-minio)


## 使用
### 添加依赖管理
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.trionesdev.csi</groupId>
            <artifactId>triones-csi-dependencies</artifactId>
            <version>版本号</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```


---

### 关注我们，一起交流

> 留言回复不及时，可以通过关注公众号联系我们
<div style="text-align: center">
<img src="images/shuque_wx.jpg" width="200px" alt="">
</div>