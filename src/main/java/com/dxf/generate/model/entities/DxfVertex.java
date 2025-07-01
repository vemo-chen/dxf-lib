package com.dxf.generate.model.entities;


import com.dxf.generate.model.BaseDxfEntity;
import com.dxf.generate.model.Vector3;
import com.dxf.generate.utils.DxfLineBuilder;
import lombok.Getter;

/**
 * Hatch
 */
@Getter
public class DxfVertex extends BaseDxfEntity {

    Vector3 point;

    public static DxfVertex buildVertexBy(BaseDxfEntity dxfEntity, Vector3 point, long maxMeta) {
        DxfVertex dxfVertex = new DxfVertex();
        dxfVertex.color = dxfEntity.getColor();
        dxfVertex.alpha = dxfEntity.getAlpha();
        dxfVertex.setBlockRecord(dxfEntity.getMeta());
        dxfVertex.point = point;
        dxfVertex.height = null;
        dxfVertex.setMeta(maxMeta);
        return dxfVertex;
    }

    @Override
    protected String getChildDxfStr() {
        return DxfLineBuilder.build()
                .append(100, "AcDb3dPolylineVertex")
                .append(10, point.getX())
                .append(20, point.getY())
                .append(30, point.getZ())
                .append(70, 32)
                .toString();
    }

    @Override
    public String getEntityName() {
        return "VERTEX";
    }

    @Override
    public String getEntityClassName() {
        return "AcDbVertex";
    }


}
