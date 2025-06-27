package com.dxf.parse.utils;

import com.dxf.parse.constant.EntityNameConstant;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.*;
import com.dxf.parse.transformation.GeometricTransform;
import com.dxf.parse.transformation.TransformBuilder;

import java.util.List;

/**
 * @Author vemo
 * @DateTime 2023/11/27 13:40
 * @Description
 */
public class GeometricTransformUtil {


    /**
     * 转换GeometricObject为GeometricPoint
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricPoint
     */
    public static List<GeometricPoint> transformGeometricPoint(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.POINT_NAME);
        return geometricTransform.transform(objectList);
    }


    /**
     * 转换GeometricObject为GeometricLine
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricLine
     */
    public static List<GeometricLine> transformGeometricLine(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.LINE_NAME);
        return geometricTransform.transform(objectList);
    }

    /**
     * 转换GeometricObject为GeometricArc
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricArc
     */
    public static List<GeometricArc> transformGeometricArc(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.ARC_NAME);
        return geometricTransform.transform(objectList);
    }

    /**
     * 转换GeometricObject为GeometricCircle
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricCircle
     */
    public static List<GeometricCircle> transformGeometricCircle(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.CIRCLE_NAME);
        return geometricTransform.transform(objectList);
    }


    /**
     * 转换GeometricObject为GeometricPolyLine
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricPolyLine
     */
    public static List<GeometricPolyLine> transformGeometricPolyLine(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.POLY_LINE_NAME);
        return geometricTransform.transform(objectList);
    }

    /**
     * 转换GeometricObject为GeometricText
     *
     * @param objectList 需要转换的GeometricObject数据
     * @return 转换GeometricText
     */
    public static List<GeometricText> transformGeometricText(List<GeometricModel> objectList) {
        GeometricTransform geometricTransform = TransformBuilder.builder(EntityNameConstant.TEXT_NAME);
        return geometricTransform.transform(objectList);
    }
}
