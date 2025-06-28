package com.dxf.generate.model;


import com.dxf.generate.enums.LineWidthEnum;
import com.dxf.generate.model.entities.Color;
import com.dxf.generate.utils.DxfLineBuilder;
import com.dxf.generate.utils.DxfUtil;
import com.dxf.parse.utils.PropertiesParse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DxfLayerEntity implements DxfEntity {

    /**
     * 图元句柄
     */
    protected Long meta;

    /**
     * 图层名称
     */
    private String layerName;
    /**
     * 颜色
     */
    protected Color color = Color.BLACK;
    /**
     * 线宽
     */
    protected LineWidthEnum lineWidth = LineWidthEnum.LW_9;
    /**
     * 图形透明度，取值范围为0-100,当alpha为0时，为不透明，当alpha为100的时候，图形将不可见
     */
    protected int alpha = 0;

    public static DxfLayerEntity build(String name) {
        DxfLayerEntity dxfLayerEntity = new DxfLayerEntity();
        dxfLayerEntity.setLayerName(name);
        return dxfLayerEntity;
    }

    @Override
    public String getDxfStr() {
        return DxfLineBuilder.build(getEntityName())
                .append(5, DxfUtil.formatMeta(meta))
                .append(330, "2")
                .append(100, "AcDbSymbolTableRecord")
                .append(100, "AcDbLayerTableRecord")
                .append(2, layerName)
                .append(70, 0)
                .append(420, PropertiesParse.formatDxfColor(color))
                .append(6, "Continuous")
                .append(370, lineWidth.getCode())
                .append(390, "F")
                .append(347, "EE")
                .append(348, 0)
                .append(1001, "AcAecLayerStandard")
                .append(1000, "")
                .append(1000, "")
                .append(1001, "AcCmTransparency")
                .append(1071, PropertiesParse.parseToCadAlpha(alpha))
                .toString();
    }


    @Override
    public String getEntityName() {
        return "LAYER";
    }

    @Override
    public String getEntityClassName() {
        return "AcDbLayerTableRecord";
    }

    @Override
    public void setReactors(String reactors) {

    }
}
