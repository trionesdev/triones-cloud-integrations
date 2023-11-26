package com.moensun.cloud.integration.huaweicloud.sms;

import com.moensun.csi.huaweicloud.sms.HuaweiCloudSmsClient;
import com.moensun.csi.huaweicloud.sms.HuaweiCloudSmsRequest;
import org.junit.jupiter.api.Test;

public class HuaweiCloudSmsTest {

    @Test
    public void send(){
        HuaweiCloudSmsClient cloudSmsClient = new HuaweiCloudSmsClient("ido8O78pG36ZfeAI27vDX4it982j","UtaoqJEMiaqjQ1dTJefoUDS4gtmj");
        HuaweiCloudSmsRequest request = HuaweiCloudSmsRequest.builder().sender("8822062330892")
                .receiver("13912785494")
                .templateId("f74a56335b474a68b564a4695eddaa3d")
                .templateParams("[\"123456\"]")
                .signature("中科先进光电技术研究所")
                .build();
        cloudSmsClient.request(request);
    }

}
