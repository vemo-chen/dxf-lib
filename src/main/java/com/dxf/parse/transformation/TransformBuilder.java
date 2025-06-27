package com.dxf.parse.transformation;


import com.dxf.parse.constant.EntityNameConstant;
import com.dxf.parse.transformation.impl.*;

import java.util.HashMap;
import java.util.Map;

public class TransformBuilder {

    /**
     * 容器
     */
    private final Map<String, GeometricTransform> map;

    public TransformBuilder() {
        map = new HashMap<>();
        map.put(EntityNameConstant.ARC_NAME, GeometricArcTransformImpl.getSingleInstance());
        map.put(EntityNameConstant.CIRCLE_NAME, GeometricCircleTransformImpl.getSingleInstance());
        map.put(EntityNameConstant.LINE_NAME, GeometricLineTransformImpl.getSingleInstance());
        map.put(EntityNameConstant.POINT_NAME, GeometricPointTransformImpl.getSingleInstance());
        map.put(EntityNameConstant.TEXT_NAME, GeometricTextTransformImpl.getSingleInstance());
        map.put(EntityNameConstant.POLY_LINE_NAME, GeometricPolyLineTransformImpl.getSingleInstance());
    }

    /**
     * 构建方法
     *
     * @param type 类型
     * @return GeometricTransform
     */
    public static GeometricTransform builder(String type) {
        return Holder.transformBuilder.map.get(type);
    }

    /**
     * 静态内部类
     */
    public static class Holder {
        public static TransformBuilder transformBuilder = new TransformBuilder();
    }

}
