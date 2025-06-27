package com.dxf.parse.model.entities;

import com.dxf.parse.model.GeometricModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author vemo
 * @DateTime 2023/11/27 9:10
 * @Description 点
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GeometricPoint extends GeometricModel implements Serializable {

    private static final long serialVersionUID = 6202012001043042778L;
    /**
     * 点的x坐标
     */
    private BigDecimal x;

    /**
     * 点的y坐标
     */
    private BigDecimal y;

    /**
     * 点的z坐标
     */
    private BigDecimal z;
}
