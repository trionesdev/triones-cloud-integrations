package com.trionesdev.csi.api.ocr;

import com.trionesdev.csi.api.ocr.request.OcrRequest;
import com.trionesdev.csi.api.ocr.response.BusinessLicenseOCRResponse;

public interface OcrTemplate {

    BusinessLicenseOCRResponse bizLicenseOCR(OcrRequest ocrRequest);

}
