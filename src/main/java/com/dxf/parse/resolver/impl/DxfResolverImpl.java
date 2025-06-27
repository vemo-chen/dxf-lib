package com.dxf.parse.resolver.impl;

import com.dxf.parse.analysis.DxfAnalysis;
import com.dxf.parse.constant.EntityNameConstant;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.*;
import com.dxf.parse.model.headers.HeaderModel;
import com.dxf.parse.resolver.DxfResolver;
import com.dxf.parse.transformation.model.DxfLine;
import com.dxf.parse.utils.DxfLineTransformUtil;
import com.dxf.parse.utils.GeometricTransformUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author vemo
 * @DateTime 2023/11/24 16:37
 * @Description DXF 解析器默认实现
 */
public class DxfResolverImpl implements DxfResolver {


    /**
     * dxf 文件流
     */
    private final InputStream inputStream;

    /**
     * dxf 编码
     */
    private final String fileChartSet = "UTF-8";

    /**
     * dxf 文件头信息
     */
    private HeaderModel dxfHeaderModel;


    /**
     * 初步解析的数据
     */
    private Map<String, List<GeometricModel>> baseStructureMap;

    public DxfResolverImpl(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.getBaseStructure();
    }

    /**
     * 获取dxf基础结构
     */
    private void getBaseStructure() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = this.inputStream.read(bytes)) > -1) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        byteArrayOutputStream.flush();

        try (InputStream headerStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
            // 1. 根据utf-8，读取字节流，解析系统变量生成dxf系统模型存储
            dxfHeaderModel = DxfAnalysis.parseDxfHeaderModel(headerStream, this.fileChartSet);
        }
        try (InputStream geometricStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
            // 2. 根据系统变量解析出的字符编码，读取字节流，解析其他数据
            baseStructureMap = DxfAnalysis.parseDxfGeometricList(geometricStream, this.dxfHeaderModel);
        }

        //关闭文件流
        this.inputStream.close();
        byteArrayOutputStream.close();
    }

    /**
     * 获取dxf几何数据（初步）
     * map的key: 几何图像名称，比如：‘POINT’
     * map的value:几何图像集合，比如：List
     *
     * @return 返回map数据
     */
    @Override
    public Map<String, List<GeometricModel>> getStructureMap() {
        return this.baseStructureMap;
    }

    @Override
    public List<DxfLine> getDxfLineList() {
        //几何数据转换点线数据
        return DxfLineTransformUtil.geometricTransform(this.baseStructureMap);
    }

    @Override
    public List<GeometricPoint> getGeometricPointList() {
        //从map中把所有的GeometricPoint拿出来
        List<GeometricModel> geometricModels = this.baseStructureMap.get(EntityNameConstant.POINT_NAME);
        //把GeometricPoint转换为GeometricPoint
        return GeometricTransformUtil.transformGeometricPoint(geometricModels);
    }

    @Override
    public List<GeometricLine> getGeometricLineList() {
        //从map中把所有的GeometricLine拿出来
        List<GeometricModel> objectList = this.baseStructureMap.get(EntityNameConstant.LINE_NAME);
        //把GeometricPoint转换为GeometricLine
        return GeometricTransformUtil.transformGeometricLine(objectList);
    }

    @Override
    public List<GeometricArc> getGeometricArcList() {
        //从map中把所有的GeometricArc拿出来
        List<GeometricModel> objectList = this.baseStructureMap.get(EntityNameConstant.ARC_NAME);
        //把GeometricPoint转换为GeometricArc
        return GeometricTransformUtil.transformGeometricArc(objectList);
    }

    @Override
    public List<GeometricCircle> getGeometricCircleList() {
        List<GeometricModel> objectList = this.baseStructureMap.get(EntityNameConstant.CIRCLE_NAME);
        return GeometricTransformUtil.transformGeometricCircle(objectList);
    }

    @Override
    public List<GeometricPolyLine> getGeometricPolyLineList() {
        List<GeometricModel> objectList = this.baseStructureMap.get(EntityNameConstant.POLY_LINE_NAME);
        return GeometricTransformUtil.transformGeometricPolyLine(objectList);
    }

    @Override
    public List<GeometricText> getGeometricTextList() {
        List<GeometricModel> objectList = this.baseStructureMap.get(EntityNameConstant.TEXT_NAME);
        return GeometricTransformUtil.transformGeometricText(objectList);
    }
}
