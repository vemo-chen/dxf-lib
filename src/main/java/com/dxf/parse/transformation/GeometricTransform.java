package com.dxf.parse.transformation;

import com.dxf.parse.model.GeometricModel;

import java.util.List;

/**
 * @Author vemo
 * @DateTime 2023/11/27 13:44
 * @Description
 */
public interface GeometricTransform<T> {

    /**
     * 将GeometricObject转换为GeometricLine
     *
     * @param objectList 转换的数据
     * @return List
     */
    List<T> transform(List<GeometricModel> objectList);
}
