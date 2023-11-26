package com.moensun.csi.api.ocr.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessLicenseOCRResponse {
    /**
     * 注册号
     */
    private String regNum;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 注册资本
     */
    private String capital;
    /**
     * 法定代表人
     */
    private String person;
    /**
     * 地址
     */
    private String address;
    /**
     * 经营范围
     */
    private String business;
    /**
     * 主体类型
     */
    private String type;
    /**
     * 营业期限
     */
    private String period;
    /**
     * 组成形式
     */
    private String composingForm;
}
