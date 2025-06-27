package com.dxf.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author vemo
 * @DateTime 2023/11/24 16:54
 * @Description DXF实例根模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GeometricModel implements Serializable {


    private static final long serialVersionUID = 154930463320039374L;

    /**
     * dxf 对象id
     */
    private String id;

    /**
     * dxf 对象名称
     */
    private String name;

    /**
     * dxf 对象图层id
     */
    private String layerId;

    /**
     * dxf 对象图层名称
     */
    private String layerName;

    /**
     * 颜色
     */
    private String color;

    /**
     * 线宽
     */
    private Double lineWidth = 1D;

    /**
     * 图形透明度，取值范围为0-100,当alpha为100时，为不透明，当alpha为0的时候，图形将不可见
     */
    private Integer alpha;

    /**
     * 标高
     */
    private Double height = 0.0;

    /**
     * 关联的对象
     */
    private String reactors;

    /**
     * 边框颜色
     */
    private String borderColor;
}
