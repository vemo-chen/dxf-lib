package com.dxf.parse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author vemo
 * @DateTime 2023/11/27 17:21
 * @Description entities的基础信息枚举
 */

@Getter
@AllArgsConstructor
public enum DxfEntitiesBaseEnum {

    ENTITY_FLAG("0", "开始结束标记"),

    COLOR_16("420", "颜色16进制"),

    COLOR_CODE("62", "颜色编码"),

    ALPHA("440", "图形透明度"),

    LINE_WIDTH("370", "线宽"),

    HEIGHT("38", "标高"),

    HANDLE("5", "句柄"),

    LAYER_NAME("8", "图层名称"),

    EXP("102", "拓展组码"),

    EXP_FLAG("{ACAD_REACTORS", "{ACAD_XDICTIONARY”表示扩展词典组的开始。"),

    RECORD_ID("330", "所有者 BLOCK_RECORD 对象的软指针 ID/句柄"),

    ALPHA_2("1071", "图形透明度"),
    ;

    /**
     * 组码
     */
    private String code;
    /**
     * 组码名称
     */
    private String fieldName;

}
