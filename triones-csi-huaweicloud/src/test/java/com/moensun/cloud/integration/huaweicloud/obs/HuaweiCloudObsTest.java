package com.moensun.cloud.integration.huaweicloud.obs;

import com.moensun.csi.api.oss.request.OssPutObjectRequest;
import com.moensun.csi.huaweicloud.obs.HuaweiCloudObs;
import com.moensun.csi.huaweicloud.obs.HuaweiCloudObsConfig;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HuaweiCloudObsTest {

    @Test
    public void upload() {
        ObsClient obsClient = new ObsClient("ACEZDK8LNIZEXLEIHQDQ", "GfGPIls9uaBahk21rTB8JNZAvfPUbPmMpXFYvhPs", "obs.cn-east-3.myhuaweicloud.com");
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName("jscoe");
        request.setObjectKey("22.jpg");
        request.setFile(new File("C:\\Users\\fengx\\Downloads\\1.jpg"));
        obsClient.putObject(request);
    }

    @Test
    public void upload1() throws FileNotFoundException {
        HuaweiCloudObsConfig cloudObsConfig = new HuaweiCloudObsConfig();
        cloudObsConfig.setBucket("jscoe");
        cloudObsConfig.setUrlPrefix("https://jscoe.obs.cn-east-3.myhuaweicloud.com");
        ObsClient obsClient = new ObsClient("ACEZDK8LNIZEXLEIHQDQ", "GfGPIls9uaBahk21rTB8JNZAvfPUbPmMpXFYvhPs", "obs.cn-east-3.myhuaweicloud.com");


        HuaweiCloudObs cloudObs =  new HuaweiCloudObs(obsClient,cloudObsConfig);
        OssPutObjectRequest ossPutObjectRequest = OssPutObjectRequest.builder()
                .objectName("2.jpg")
                .inputStream(new FileInputStream("C:\\Users\\fengx\\Downloads\\1.jpg"))
                .build();
        cloudObs.putObject(ossPutObjectRequest);
    }

}
