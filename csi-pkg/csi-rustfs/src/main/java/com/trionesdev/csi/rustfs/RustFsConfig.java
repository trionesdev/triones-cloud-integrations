package com.trionesdev.csi.rustfs;

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
public class RustFsConfig extends RustFsCredentials {
    private static final long serialVersionUID = 5129214195191527384L;
    private Boolean multi;
}
