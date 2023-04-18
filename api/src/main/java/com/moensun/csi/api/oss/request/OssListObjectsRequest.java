package com.moensun.csi.api.oss.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OssListObjectsRequest extends BucketInfo {
    private String prefix;
    private Integer maxKeys;
    private String startAfter;
    private String delimiter;
}
