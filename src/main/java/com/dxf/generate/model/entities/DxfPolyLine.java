package com.dxf.generate.model.entities;

import com.dxf.generate.model.BaseDxfEntity;
import com.dxf.generate.model.Vector3;
import com.dxf.generate.utils.DxfLineBuilder;
import com.dxf.generate.utils.DxfUtil;
import com.dxf.parse.utils.PropertiesParse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 多段线图元
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class DxfPolyLine extends BaseDxfEntity {

    /**
     * 各个点的集合
     */
    private List<Vector3> points;
    /**
     * 多线段是否闭合
     */
    private boolean isClose = false;

    /**
     * 各个点的对象集合
     */
    private List<DxfVertex> vertices;

    public DxfPolyLine() {
        points = new ArrayList<>();
        vertices = new ArrayList<>();
        height = null;
    }

    /**
     * 向多线段中添加一个坐标点
     *
     * @param p 坐标点
     */
    public void addPoint(Vector3 p) {
        points.add(p);
    }

    /**
     * 根据点位信息向多线段中添加一个坐标点
     **/
    public void addPoint(String x, String y, String z) {
        points.add(new Vector3(x, y, z));
    }

    /**
     * 根据点位信息向多线段中添加一个坐标点
     * 支持int、double、long、float、BigDecimal
     **/
    public <T extends Number> void addPoint(T x, T y, T z) {
        addPoint(x.toString(), y.toString(), z.toString());
    }

    /**
     * 添加点列表
     * 支持double、BigDecimal
     **/
    public <T extends Number> void addPoint(List<T> point) {
        addPoint(point.get(0), point.get(1), point.get(2));
    }

    /**
     * 根据列表添加点支持基本类型
     * 支持double、BigDecimal
     **/
    public <T extends Number> void addPoints(List<List<T>> points) {
        for (List<T> point : points) {
            addPoint(point);
        }
    }

    /**
     * 根据点位信息移除点位
     **/
    public <T extends Number> void removePoint(T x, T y, T z) {
        BigDecimal bx = new BigDecimal(x.toString());
        BigDecimal by = new BigDecimal(y.toString());
        BigDecimal bz = new BigDecimal(z.toString());
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX().equals(bx) && points.get(i).getY().equals(by) && points.get(i).getZ().equals(bz)) {
                points.remove(points.get(i));
            }
        }
    }

    /**
     * 移除点
     **/
    public <T extends Number> void removePoint(List<T> point) {
        removePoint(point.get(0), point.get(1), point.get(2));
    }

    public <T extends Number> void removePoints(List<List<T>> points) {
        for (List<T> point : points) {
            removePoint(point);
        }
    }

    /***
     * 从多线段中移除一个坐标点
     * @param p 坐标点
     */
    public void removePoint(Vector3 p) {
        points.remove(p);
    }

    /**
     * 多线段图元中内容是否为空
     *
     * @return 若图元空不包含任何坐标点返回true
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * 构造vertex
     *
     * @param dxfEntity 基础entity
     * @param maxMeta   句柄
     */
    public void buildVertex(BaseDxfEntity dxfEntity, long maxMeta) {
        for (Vector3 point : points) {
            DxfVertex dxfVertex = DxfVertex.buildVertexBy(dxfEntity, point, maxMeta);
            maxMeta++;
            vertices.add(dxfVertex);
        }
    }

    @Override
    protected String getChildDxfStr() {
        DxfLineBuilder lineBuilder = DxfLineBuilder.build();
        lineBuilder.append(66, 1);
        lineBuilder.append(10, 0.0);
        lineBuilder.append(20, 0.0);
        lineBuilder.append(30, 0.0);
        lineBuilder.append(70, isClose ? 9 : 8);

        for (DxfVertex dxfVertex : vertices) {
            lineBuilder.append(dxfVertex.getDxfStr());
        }

        lineBuilder
                .append(0, "SEQEND")
                .append(5, DxfUtil.formatMeta(meta + 1))
                .append(330, DxfUtil.formatMeta(meta))
                .append(100, "AcDbEntity")
                .append(8, layerName == null ? "0" : layerName)
                .append(6, "Continuous")
                .append(420, PropertiesParse.formatDxfColor(color))
                .append(440, PropertiesParse.parseToCadAlpha(alpha))
                .append(370, lineWidth.getCode());

        return lineBuilder.toString();
    }

    @Override
    public String getEntityName() {
        return "POLYLINE";
    }

    @Override
    public String getEntityClassName() {
        return "AcDb3dPolyline";
    }
}
