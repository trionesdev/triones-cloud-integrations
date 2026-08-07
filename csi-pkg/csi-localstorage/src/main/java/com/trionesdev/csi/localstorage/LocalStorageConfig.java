package com.trionesdev.csi.localstorage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LocalStorageConfig {
    private String dir;
    private String bucket;
    private String urlPrefix;
}
