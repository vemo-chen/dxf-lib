package com.dxf.parse.analysis;

import com.dxf.parse.constant.EntityNameConstant;
import com.dxf.parse.enums.CadVersionEnum;
import com.dxf.parse.enums.DxfSystemEnum;
import com.dxf.parse.enums.entities.*;
import com.dxf.parse.enums.tables.LayerEnum;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.headers.HeaderModel;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @AUTHOR vemo
 * @DATE 2023/11/25
 * <p/>
 * 概要：DXF 文件解析
 */
public class DxfAnalysis {


    /**
     * 读取所有的行
     *
     * @param reader BufferedReader
     * @return 返回文件的所有数据，以每一行数据为一个item
     * @throws IOException IO异常
     */
    private static List<String> readAllLine(BufferedReader reader) throws IOException {
        List<String> list = Lists.newArrayList();
        String line = null;
        while ((line = reader.readLine()) != null) {
            list.add(line.trim());
        }
        return list;
    }

    /**
     * 解析dxf文件头信息，形成对象模型
     *
     * @param inputStream 文件流
     * @param charset     字符编码 （UTF-8  GBK）
     * @return
     * @throws IOException
     */
    public static HeaderModel parseDxfHeaderModel(InputStream inputStream, String charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            List<String> dxfAllLine = readAllLine(reader);
            return DxfHeaderParse.getHeaderParse(dxfAllLine);
        }
    }


    /**
     * 返回几何图像解析数据
     *
     * @param inputStream    dxf文件流
     * @param dxfHeaderModel dxf解析出的头信息
     * @return
     * @throws IOException
     */
    public static Map<String, List<GeometricModel>> parseDxfGeometricList(InputStream inputStream, HeaderModel dxfHeaderModel) throws IOException {
        /**
         * 从cad版本信息中获取字符编码
         */
        CadVersionEnum cadVersionEnum = CadVersionEnum.getCadVersion(dxfHeaderModel.getCadVersion());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            Map<String, List<GeometricModel>> map = new HashMap<>();
            //读取dxf所有的数据
            List<String> lineList = readAllLine(reader);
            // 解析dxf文件结构
            parseFile(lineList, map, dxfHeaderModel);
            return map;
        }
    }

    /**
     * 解析dxf文件结构
     *
     * @param lineList 总数据
     * @param map      接收解析的数据map
     */
    private static void parseFile(List<String> lineList, Map<String, List<GeometricModel>> map, HeaderModel dxfHeaderModel) {
        if (CollectionUtils.isEmpty(lineList)) {
            return;
        }
        Map<String, GeometricModel> layerMap = new HashMap<>();
        int i = 0;
        String str = lineList.get(i).trim();
        //未到文件结束标志
        while (!DxfSystemEnum.FILE_END.getCode().equals(str)) {
            str = lineList.get(++i).trim();

            // 解析图层
            if (DxfSystemEnum.TABLES_START.getCode().equals(str)) {
                parseTables(i, lineList, layerMap);
            }

            //实体段开始
            if (DxfSystemEnum.ENTITIES_START.getCode().equals(str)) {
                //解析实体
                parseEntities(i, lineList, map, dxfHeaderModel);
                //处理实体里的特殊信息
                handleEntities(map, layerMap);
            }
            // 文件循环语句结束
        }

        // 解析函数结束

    }

    /**
     * 解析实体
     *
     * @param i              实体开始读取的行数
     * @param lineList       总数据
     * @param map            接收解析的数据map
     * @param dxfHeaderModel dxf文件的头信息模型
     */
    private static void parseEntities(int i, List<String> lineList, Map<String, List<GeometricModel>> map, HeaderModel dxfHeaderModel) {
        String str = null;
        while (true) {
            str = lineList.get(++i).trim();

            //点开始
            if (PointEnum.POINT_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getPoint(i, lineList, map);
            }
            //直线开始
            if (LineEnum.LINE_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getLine(i, lineList, map);
            }
            //多线段
            if (PolyLineEnum.LWPOLYLINE_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getLWPolyLine(i, lineList, map);
            }
            // 填充块
            if (PolyLineEnum.HATCH_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getHatch(i, lineList, map);
            }
            // 多线段
            if (PolyLineEnum.POLYLINE_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getPolyLine(i, lineList, map);
            }
            //圆开始
            if (CircleEnum.CIRCLE_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getCircle(i, lineList, map);
            }
            // 椭圆开始
            if (EllipseEnum.ELLIPSE_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getNotSupported(i, lineList, map);
            }
            // 圆弧开始
            if (ArcEnum.ARC_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getArc(i, lineList, map);
            }
            // 文本
            if (TextEnum.TEXT_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getText(i, lineList, map);
            }
            // 多文本
            if (MTextEnum.MTEXT_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getMText(i, lineList, map);
            }
            // 插入图元
            if (InsertEnum.INSERT_NAME.getCode().equals(str)) {
                i = DxfEntitiesParse.getInsert(i, lineList, map);
            }
            //实体结束
            if (DxfSystemEnum.END_SEC.getCode().equals(str)) {
                break;
            }
        }
    }

    private static void parseTables(int i, List<String> lineList, Map<String, GeometricModel> map) {
        String str = null;
        while (true) {
            str = lineList.get(++i).trim();

            if (LayerEnum.LAYER_FLAG.getCode().equals(str)) {
                DxfTablesParse.getLayer(i, lineList, map);
            }
            //实体结束
            if (DxfSystemEnum.END_SEC.getCode().equals(str)) {
                break;
            }
        }
    }

    private static void handleEntities(Map<String, List<GeometricModel>> map, Map<String, GeometricModel> layerMap) {

        Map<String, GeometricModel> hatchMap = Optional.ofNullable(map.get(PolyLineEnum.HATCH_NAME.getCode()))
                .orElse(Collections.emptyList())
                .stream()
                .filter(model -> model.getId() != null)
                .collect(Collectors.toMap(
                        GeometricModel::getId,
                        model -> model,
                        (existing, replacement) -> existing
                ));


        for (Map.Entry<String, List<GeometricModel>> entry : map.entrySet()) {
            for (GeometricModel model : entry.getValue()) {
                GeometricModel layer = layerMap.get(model.getLayerName());
                if (model.getLayerId() == null && layer != null) {
                    model.setLayerId(layer.getId());
                }
                if (model.getColor() == null && layer != null) {
                    model.setColor(layer.getColor());
                }
                if (model.getBorderColor() == null && layer != null) {
                    model.setBorderColor(layer.getColor());
                }
                if (model.getAlpha() == null && layer != null) {
                    model.setAlpha(layer.getAlpha() == null ? 100 : layer.getAlpha());
                }
                if (model.getLineWidth() == null && layer != null) {
                    model.setLineWidth(layer.getLineWidth() == null ? 2 : layer.getLineWidth());
                }

                if (Objects.equals(EntityNameConstant.POLY_LINE_NAME, entry.getKey()) && model.getReactors() != null) {
                    GeometricModel hatch = hatchMap.get(model.getReactors());
                    if (hatch != null) {
                        model.setColor(hatch.getColor());
                        model.setAlpha(hatch.getAlpha());
                    }
                }
            }
        }
    }


}
