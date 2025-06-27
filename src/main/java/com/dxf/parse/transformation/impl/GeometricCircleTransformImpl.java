package com.dxf.parse.transformation.impl;

import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.GeometricCircle;
import com.dxf.parse.transformation.GeometricTransform;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class GeometricCircleTransformImpl implements GeometricTransform<GeometricCircle> {

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
            synchronized (GeometricCircleTransformImpl.class) {
                if (geometricTransform == null) {
                    geometricTransform = new GeometricCircleTransformImpl();
                }
            }
        }
        return geometricTransform;
    }


    /**
     * 转换几何圆
     *
     * @param objectList 转换的数据
     * @return List
     */
    @Override
    public List<GeometricCircle> transform(List<GeometricModel> objectList) {
        List<GeometricCircle> circleList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(objectList)) {
            for (GeometricModel object : objectList) {
                if (object instanceof GeometricCircle) {
                    circleList.add((GeometricCircle) object);
                }
            }
        }
        return circleList;
    }
}
