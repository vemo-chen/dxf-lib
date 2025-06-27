package com.dxf.parse.model.tables;

import com.dxf.parse.model.GeometricModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author vemo
 * @DateTime 2023/11/27 9:16
 * @Description 弧度
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GeometricLayer extends GeometricModel implements Serializable {

    private static final long serialVersionUID = 1685556296528470644L;

    /**
     * 状态
     */
    private String status;

}
