package com.dxf.parse.analysis;

import com.dxf.generate.utils.DxfUtil;
import com.dxf.parse.constant.EntityNameConstant;
import com.dxf.parse.constant.PolyLineConstant;
import com.dxf.parse.enums.DxfEntitiesBaseEnum;
import com.dxf.parse.enums.entities.*;
import com.dxf.parse.enums.error.DxfAnalysisErrorEnum;
import com.dxf.parse.exception.DxfAnalysisException;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.entities.*;
import com.dxf.parse.utils.DecimalCheckUtil;
import com.dxf.parse.utils.PropertiesParse;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author vemo
 * @DateTime 2023/11/27 11:00
 * @Description dxf 解析entities 节点
 */
public class DxfEntitiesParse {

    public static final Integer DECIMAL_SIZE = 10;

    /**
     * 获取图元
     *
     * @param i        多线段开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回多线段读完最后行数
     */
    public static int getInsert(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str;
        GeometricPoint point = new GeometricPoint();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setHeight(Double.parseDouble(str));
            } else if (InsertEnum.COORDINATE_X.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_X);
                }
                point.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (InsertEnum.COORDINATE_Y.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Y);
                }
                point.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (InsertEnum.COORDINATE_Z.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Z);
                }
                point.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> pointList = map.get(EntityNameConstant.POINT_NAME);
        if (pointList == null) {
            pointList = Lists.newArrayList();
        }
        pointList.add(point);
        map.put(EntityNameConstant.POINT_NAME, pointList);
        return i;
    }


    /**
     * 获取多文本
     *
     * @param i        多线段开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回多线段读完最后行数
     */
    public static int getMText(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricText text = new GeometricText();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setHeight(Double.parseDouble(str));
            } else if (MTextEnum.COORDINATE_X.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_X);
                }
                text.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (MTextEnum.COORDINATE_Y.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Y);
                }
                text.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (MTextEnum.COORDINATE_Z.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Z);
                }
                text.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (MTextEnum.MTEXT_CONTENT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                String rule = "[{]\\\\fSimSun([|][a-z\\d]+){4};";
                String end = "(?<!\\\\)}";
                String changeLine = "\\P";
                str = str.replaceAll(rule, "");
                str = str.replaceAll(end, "");
                str = str.replace(changeLine, "\n");
                str = str.replace("\\}", "}");
                str = str.replace("\\{", "{");
                str = str.replace("\\\\f", "\\f");
                text.setText(str);
            } else if (MTextEnum.HEIGH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setHigh(Double.parseDouble(str));
            } else if (MTextEnum.WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setWidth(Double.parseDouble(str));
            } else if (MTextEnum.ANGLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setAngle(new BigDecimal(str));
            } else if (MTextEnum.INCLINATION.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setInclination(new BigDecimal(str));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> testList = map.get(EntityNameConstant.TEXT_NAME);
        if (testList == null) {
            testList = Lists.newArrayList();
        }
        testList.add(text);
        map.put(EntityNameConstant.TEXT_NAME, testList);
        return i;
    }

    /**
     * 获取单行文本
     *
     * @param i        多线段开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回多线段读完最后行数
     */
    public static int getText(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricText text = new GeometricText();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setHeight(Double.parseDouble(str));
            } else if (TextEnum.COORDINATE_X.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_X);
                }
                text.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (TextEnum.COORDINATE_Y.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Y);
                }
                text.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (TextEnum.COORDINATE_Z.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Z);
                }
                text.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (TextEnum.TEXT_CONTENT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setText(str);
            } else if (TextEnum.HEIGH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setHigh(Double.parseDouble(str));
            } else if (TextEnum.WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setWidth(Double.parseDouble(str));
            } else if (TextEnum.ANGLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setAngle(new BigDecimal(str));
            } else if (TextEnum.INCLINATION.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                text.setInclination(new BigDecimal(str));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> testList = map.get(EntityNameConstant.TEXT_NAME);
        if (testList == null) {
            testList = Lists.newArrayList();
        }
        testList.add(text);
        map.put(EntityNameConstant.TEXT_NAME, testList);
        return i;
    }

    /**
     * 获取多线段
     *
     * @param i        多线段开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回多线段读完最后行数
     */
    public static int getLWPolyLine(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricPolyLine polyLine = new GeometricPolyLine();
        polyLine.setLogicClose(false);
        List<GeometricVertex> vertices = new ArrayList<>();
        int vertexNum = 0;
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setBorderColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setBorderColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setHeight(Double.parseDouble(str));
            } else if (DxfEntitiesBaseEnum.EXP.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (DxfEntitiesBaseEnum.EXP_FLAG.getCode().equals(str)) {
                    while (true) {
                        str = lineList.get(++i).trim();
                        if (DxfEntitiesBaseEnum.RECORD_ID.getCode().equals(str)) {
                            str = lineList.get(++i).trim();
                            polyLine.setReactors(str);
                        } else if (DxfEntitiesBaseEnum.EXP.getCode().equals(str)) {
                            str = lineList.get(++i).trim();
                            if ("}".equals(str)) {
                                break;
                            }
                        } else {
                            ++i;
                        }
                    }
                }
            } else if (PolyLineEnum.CLOSE.getCode().equals(str)) {
                // 多段线闭合
                str = lineList.get(++i).trim();
                // 多段线的标志，表明这是一个闭合的多段线
                if (str.equals(PolyLineConstant.POLYLINE_LOGIC_CLOSE) || str.equals(PolyLineConstant.POLYLINE_LOGIC_CLOSE_1)) {
                    polyLine.setLogicClose(true);
                }
            } else if (PolyLineEnum.VERTEX_NUM.getCode().equals(str)) {
                // 顶点数量
                str = lineList.get(++i).trim();
                vertexNum = Integer.parseInt(str.trim());
                polyLine.setVertexNum(vertexNum);
            } else if (PolyLineEnum.COORDINATE_X.getCode().equals(str)) {
                GeometricVertex vertex = new GeometricVertex();
                str = lineList.get(++i).trim();
                if (DecimalCheckUtil.check(str.trim())) {
                    vertex.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                    str = lineList.get(++i).trim();
                } else {
                    break;
                }
                if (PolyLineEnum.COORDINATE_Y.getCode().equals(str)) {
                    str = lineList.get(++i).trim();
                    if (DecimalCheckUtil.check(str.trim())) {
                        vertex.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                    }
                }
                // 定制点：LWPolyline 的顶点理论上没有Z，但是为了适配geojson写过来的z值，这里做个解析适配
                if (PolyLineEnum.COORDINATE_Z.getCode().equals(lineList.get(i + 1).trim())){
                    ++i;
                    str = lineList.get(++i).trim();
                    if (DecimalCheckUtil.check(str.trim())) {
                        vertex.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                    }
                }
                vertices.add(vertex);
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        polyLine.setVertexList(vertices);
        List<GeometricModel> polyLines = map.computeIfAbsent(EntityNameConstant.POLY_LINE_NAME, k -> Lists.newArrayList());
        polyLines.add(polyLine);
        return i;

    }


    public static int getHatch(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricPolyLine hatch = new GeometricPolyLine();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                hatch.setHeight(Double.parseDouble(str));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> hatchList = map.computeIfAbsent(EntityNameConstant.HATCH_NAME, k -> Lists.newArrayList());
        hatchList.add(hatch);

        return i;
    }

    /**
     * 解析多段线
     *
     * @param i        多线段开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return: 返回多线段读完最后行数
     **/
    public static int getPolyLine(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricPolyLine polyLine = new GeometricPolyLine();
        polyLine.setLogicClose(false);
        List<GeometricVertex> vertices = new ArrayList<>();
        while (true) {
            str = lineList.get(++i).trim();
            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                polyLine.setHeight(Double.parseDouble(str));
            } else if (PolyLineEnum.CLOSE.getCode().equals(str)) {
                // 多段线闭合
                str = lineList.get(++i).trim();
                // 多段线的标志，表明这是一个闭合的多段线
                if (str.equals(PolyLineConstant.POLYLINE_LOGIC_CLOSE) || str.equals(PolyLineConstant.POLYLINE_LOGIC_CLOSE_1)) {
                    polyLine.setLogicClose(true);
                }
            } else if (PolyLineEnum.BLOCK.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (PolyLineEnum.VERTEX_NAME.getCode().equals(str)) {
                    GeometricVertex vertex = new GeometricVertex();
                    //顶点的x坐标
                    while (true) {
                        str = lineList.get(++i).trim();
                        if (PolyLineEnum.COORDINATE_X.getCode().equals(str)) {
                            str = lineList.get(++i).trim();
                            if (DecimalCheckUtil.check(str.trim())) {
                                vertex.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                            }
                        } else if (PolyLineEnum.COORDINATE_Y.getCode().equals(str)) {
                            str = lineList.get(++i).trim();
                            if (DecimalCheckUtil.check(str.trim())) {
                                vertex.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                            }
                            vertices.add(vertex);
                        } else if (PolyLineEnum.COORDINATE_Z.getCode().equals(str)) {
                            str = lineList.get(++i).trim();
                            if (DecimalCheckUtil.check(str.trim())) {
                                vertex.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
                            }
                            vertices.add(vertex);
                        } else if (PolyLineEnum.BLOCK.getCode().equals(str)) {
                            break;
                        } else {
                            ++i;
                        }
                    }
                    vertices.add(vertex);
                    --i;
                } else if (PolyLineEnum.SEQEND.getCode().equals(str)) {
                    break;
                }
            } else {
                ++i;
            }
        }
        polyLine.setVertexList(vertices);
        List<GeometricModel> polyLines = map.computeIfAbsent(EntityNameConstant.POLY_LINE_NAME, k -> Lists.newArrayList());
        polyLines.add(polyLine);
        return i;
    }

    /**
     * 获取线
     *
     * @param i        线开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回直线读完的最后行数
     */
    public static int getLine(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricLine line = new GeometricLine();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                line.setHeight(Double.parseDouble(str));
            } else if (LineEnum.COORDINATE_X.getCode().equals(str)) {
                //起点x坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_START_NOT_X);
                }
                line.setStartX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (LineEnum.COORDINATE_Y.getCode().equals(str)) {
                //起点y的坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_START_NOT_Y);
                }
                line.setStartY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (LineEnum.COORDINATE_Z.getCode().equals(str)) {
                //起点z的坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_START_NOT_Z);
                }
                line.setStartZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (LineEnum.LINE_END_X_COORDINATES.getCode().equals(str)) {
                //终点的x坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_END_NOT_X);
                }
                line.setEndX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (LineEnum.LINE_END_Y_COORDINATES.getCode().equals(str)) {
                //终点的y坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_END_NOT_Y);
                }
                line.setEndY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (LineEnum.LINE_END_Z_COORDINATES.getCode().equals(str)) {
                //终点的z坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.LINE_END_NOT_Z);
                }
                line.setEndZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> linesList = map.get(EntityNameConstant.LINE_NAME);
        if (linesList == null) {
            linesList = Lists.newArrayList();
        }
        linesList.add(line);
        map.put(EntityNameConstant.LINE_NAME, linesList);
        return i;
    }


    /**
     * 获取圆
     *
     * @param i        圆开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回圆读完的最后行数
     */
    public static int getCircle(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricCircle circle = new GeometricCircle();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                circle.setHeight(Double.parseDouble(str));
            } else if (CircleEnum.COORDINATE_X.getCode().equals(str)) {
                //圆心的x坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.CIRCLE_NOT_X);
                }
                circle.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (CircleEnum.COORDINATE_Y.getCode().equals(str)) {
                //圆心的y坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.CIRCLE_NOT_Y);
                }
                circle.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (CircleEnum.COORDINATE_Z.getCode().equals(str)) {
                //圆心的z坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.CIRCLE_NOT_Z);
                }
                circle.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (CircleEnum.CIRCULAR_RADIUS.getCode().equals(str)) {
                //解析圆的半径
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.CIRCLE_NOT_RADIUS);
                }
                circle.setRadius(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> circleList = map.get(EntityNameConstant.CIRCLE_NAME);
        if (circleList == null) {
            circleList = Lists.newArrayList();
        }
        circleList.add(circle);
        map.put(EntityNameConstant.CIRCLE_NAME, circleList);
        return i;
    }


    /**
     * 解析点
     *
     * @param i        点开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回点读完的最后行数
     */
    public static int getPoint(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricPoint point = new GeometricPoint();

        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                point.setHeight(Double.parseDouble(str));
            } else if (PointEnum.COORDINATE_X.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_X);
                }
                point.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (PointEnum.COORDINATE_Y.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Y);
                }
                point.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (PointEnum.COORDINATE_Z.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.POINT_NOT_Z);
                }
                point.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> pointList = map.get(EntityNameConstant.POINT_NAME);
        if (pointList == null) {
            pointList = Lists.newArrayList();
        }
        pointList.add(point);
        map.put(EntityNameConstant.POINT_NAME, pointList);
        return i;
    }


    /**
     * 获取弧线
     *
     * @param i        弧线开始读取的行数
     * @param lineList 总数据
     * @param map      接收解析的数据map
     * @return 返回弧线读完的最后行数
     */
    public static int getArc(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        GeometricArc arc = new GeometricArc();
        while (true) {
            str = lineList.get(++i).trim();

            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setId(str);
            } else if (DxfEntitiesBaseEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setLayerName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (DxfEntitiesBaseEnum.HEIGHT.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                arc.setHeight(Double.parseDouble(str));
            } else if (ArcEnum.COORDINATE_X.getCode().equals(str)) {
                //圆弧圆心x坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_X);
                }
                arc.setX(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (ArcEnum.COORDINATE_Y.getCode().equals(str)) {
                //圆弧圆心y坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_Y);
                }
                arc.setY(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (ArcEnum.COORDINATE_Z.getCode().equals(str)) {
                //圆弧圆心z坐标
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_Z);
                }
                arc.setZ(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (ArcEnum.ARC_RADIUS.getCode().equals(str)) {
                //圆弧半径
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_RADIUS);
                }
                arc.setRadius(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (ArcEnum.ARC_START_ANGLE.getCode().equals(str)) {
                //圆弧起始角度
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_START_ANGLE);
                }
                arc.setStartArc(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (ArcEnum.ARC_END_ANGLE.getCode().equals(str)) {
                //圆弧中止角度
                str = lineList.get(++i).trim();
                if (!DecimalCheckUtil.check(str.trim())) {
                    throw new DxfAnalysisException(DxfAnalysisErrorEnum.ARC_NOT_END_ANGLE);
                }
                arc.setEndArc(new BigDecimal(str.trim()).setScale(DECIMAL_SIZE, RoundingMode.HALF_UP));
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        List<GeometricModel> arcList = map.get(EntityNameConstant.ARC_NAME);
        if (arcList == null) {
            arcList = Lists.newArrayList();
        }
        arcList.add(arc);
        map.put(EntityNameConstant.ARC_NAME, arcList);
        return i;
    }


    public static int getNotSupported(int i, List<String> lineList, Map<String, List<GeometricModel>> map) {
        String str = null;
        while (true) {
            str = lineList.get(++i).trim();
            if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str)) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        return i;
    }

}
