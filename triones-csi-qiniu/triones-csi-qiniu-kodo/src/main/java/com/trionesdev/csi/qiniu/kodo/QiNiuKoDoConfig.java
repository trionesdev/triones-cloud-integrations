package com.trionesdev.csi.qiniu.kodo;

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
public class QiNiuKoDoConfig extends QiNiuKuDoCredentials {
    private static final long serialVersionUID = 2200810031665639938L;
    private Boolean multi;
}
