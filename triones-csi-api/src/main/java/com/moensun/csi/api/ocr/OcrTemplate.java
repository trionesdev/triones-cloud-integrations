package com.moensun.csi.api.ocr;

import com.moensun.csi.api.ocr.request.OcrRequest;
import com.moensun.csi.api.ocr.response.BusinessLicenseOCRResponse;

public interface OcrTemplate {

    BusinessLicenseOCRResponse bizLicenseOCR(OcrRequest ocrRequest);

}
