package com.moensun.cloud.integration.api.ocr;

import com.moensun.cloud.integration.api.ocr.request.OcrRequest;
import com.moensun.cloud.integration.api.ocr.response.BusinessLicenseOCRResponse;

public interface OcrTemplate {

    BusinessLicenseOCRResponse bizLicenseOCR(OcrRequest ocrRequest);

}
