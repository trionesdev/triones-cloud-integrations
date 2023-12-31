package com.trionesdev.csi.tencentcloud.ocr;


import com.trionesdev.csi.api.ocr.OcrException;
import com.trionesdev.csi.api.ocr.OcrTemplate;
import com.trionesdev.csi.api.ocr.request.OcrRequest;
import com.trionesdev.csi.api.ocr.response.BusinessLicenseOCRResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.BizLicenseOCRResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TencentCloudOcr implements OcrTemplate {

    private final OcrClient ocrClient;

    public TencentCloudOcr(OcrClient client){
        this.ocrClient = client;
    }

    @Override
    public BusinessLicenseOCRResponse bizLicenseOCR(OcrRequest ocrRequest) {
        BizLicenseOCRRequest bizLicenseOCRRequest = new BizLicenseOCRRequest();
        bizLicenseOCRRequest.setImageUrl(ocrRequest.getFileUrl());
        try {
            BizLicenseOCRResponse response = ocrClient.BizLicenseOCR(bizLicenseOCRRequest);
            return convert(response);
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            throw new OcrException(e);
        }
    }

    private BusinessLicenseOCRResponse convert(BizLicenseOCRResponse response) {
        return BusinessLicenseOCRResponse.builder()
                .regNum(response.getRegNum())
                .name(response.getName()).capital(response.getCapital())
                .person(response.getPerson()).address(response.getAddress())
                .business(response.getBusiness()).type(response.getType())
                .period(response.getPeriod()).composingForm(response.getComposingForm()).build();
    }

}
