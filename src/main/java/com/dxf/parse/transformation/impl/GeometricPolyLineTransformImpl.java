package com.dxf.parse.transformation.impl;

import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.GeometricPolyLine;
import com.dxf.parse.transformation.GeometricTransform;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class GeometricPolyLineTransformImpl implements GeometricTransform<GeometricPolyLine> {

    /**
     * 实例对象
     */
    private static GeometricTransform geometricTransform;

    /**
     * 采用单例模式
     *
     * @return 返回实例对象
     */
    public static GeometricTransform getSingleInstance() {
        if (geometricTransform == null) {
            synchronized (GeometricPolyLineTransformImpl.class) {
                if (geometricTransform == null) {
                    geometricTransform = new GeometricPolyLineTransformImpl();
                }
            }
        }
        return geometricTransform;
    }

    /**
     * 几何多线段转换
     *
     * @param objectList 转换的数据
     * @return List<GeometricPolyLine>
     */
    @Override
    public List<GeometricPolyLine> transform(List<GeometricModel> objectList) {
        List<GeometricPolyLine> polyLineList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(objectList)) {
            for (GeometricModel object : objectList) {
                if (object instanceof GeometricPolyLine) {
                    polyLineList.add((GeometricPolyLine) object);
                }
            }
        }
        return polyLineList;
    }
}
