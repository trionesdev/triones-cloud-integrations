package com.trionesdev.csi.azure.blob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AzureBlobCredentials {
    private String connectionString;
    private String containerName;
    private String urlPrefix;
}
