package com.dxf.parse.transformation.impl;

import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.GeometricLine;
import com.dxf.parse.transformation.GeometricTransform;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class GeometricLineTransformImpl implements GeometricTransform<GeometricLine> {

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
            synchronized (GeometricLineTransformImpl.class) {
                if (geometricTransform == null) {
                    geometricTransform = new GeometricLineTransformImpl();
                }
            }
        }
        return geometricTransform;
    }

    /**
     * 几何线的转换
     *
     * @param objectList 转换的数据
     * @return List<GeometricLine>
     */
    @Override
    public List<GeometricLine> transform(List<GeometricModel> objectList) {
        List<GeometricLine> lineList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(objectList)) {
            for (GeometricModel object : objectList) {
                if (object instanceof GeometricLine) {
                    lineList.add((GeometricLine) object);
                }
            }
        }
        return lineList;
    }
}
