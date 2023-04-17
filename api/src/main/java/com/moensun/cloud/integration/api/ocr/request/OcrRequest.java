package com.moensun.cloud.integration.api.ocr.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OcrRequest {
    private String fileUrl;
    private String base64Content;
}
