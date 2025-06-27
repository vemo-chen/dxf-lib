package com.dxf.parse.enums.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LayerEnum {

    LAYER_FLAG("LAYER", "图层标识"),

    LAYER_NAME("2", "图层名"),

    LAYER_STATUS("70", "标准标记,0为正常"),

    LAYER_END("ENDTAB", "标准标记,结束table"),

    ;
    /**
     * 组码
     */
    private final String code;

    /**
     * 组码名称
     */
    private final String fieldName;

}
