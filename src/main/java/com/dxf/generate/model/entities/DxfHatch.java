package com.dxf.generate.model.entities;


import com.dxf.generate.model.BaseDxfEntity;
import com.dxf.generate.utils.DxfLineBuilder;
import lombok.Getter;

/**
 * Hatch
 */
@Getter
public class DxfHatch extends BaseDxfEntity {

    private DxfSolid dxfSolid;

    public static DxfHatch buildHatchBy(BaseDxfEntity baseDxfEntity) {
        DxfHatch dxfHatch = new DxfHatch();
        dxfHatch.dxfSolid = new DxfSolid();
        dxfHatch.dxfSolid.setDxfEntity(baseDxfEntity);
        dxfHatch.color = baseDxfEntity.getSolidColor() == null ? baseDxfEntity.getColor() : baseDxfEntity.getSolidColor();
        dxfHatch.alpha = baseDxfEntity.getSolidAlpha();
        return dxfHatch;
    }

    @Override
    protected String getChildDxfStr() {
        return DxfLineBuilder.build()
                .append(10, 0.0)
                .append(20, 0.0)
                .append(30, 0.0)
                .append(210, 0.0)
                .append(220, 0.0)
                .append(230, 1.0)
                .append(dxfSolid.getDxfStr())
                .toString();
    }

    @Override
    public String getEntityName() {
        return "HATCH";
    }

    @Override
    public String getEntityClassName() {
        return "AcDbHatch";
    }

    public enum HatchType {
        /**
         * Solid
         */
        SOLID;
    }

}
