package com.dxf.parse.analysis;

import com.dxf.generate.utils.DxfUtil;
import com.dxf.parse.enums.DxfEntitiesBaseEnum;
import com.dxf.parse.enums.tables.LayerEnum;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.model.tables.GeometricLayer;
import com.dxf.parse.utils.PropertiesParse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @deps:
 * @date: 2025-06-27
 * @author: chenyisheng
 */
public class DxfTablesParse {

    public static int getLayer(int i, List<String> lineList, Map<String, GeometricModel> map) {
        String str = null;
        GeometricLayer layer = new GeometricLayer();
        while (true) {
            str = lineList.get(++i).trim();
            if (DxfEntitiesBaseEnum.HANDLE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setId(str);
            } else if (LayerEnum.LAYER_NAME.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setName(str);
            } else if (DxfEntitiesBaseEnum.COLOR_16.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setColor(PropertiesParse.convertToHtmlColor(Integer.parseInt(str)));
            } else if (DxfEntitiesBaseEnum.COLOR_CODE.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setColor(PropertiesParse.parseCadColorToHexColorCode(str));
            } else if (DxfEntitiesBaseEnum.ALPHA_2.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.ALPHA.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setAlpha(PropertiesParse.parseAlpha(str));
            } else if (DxfEntitiesBaseEnum.LINE_WIDTH.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setLineWidth(PropertiesParse.parseWeight(str));
            } else if (LayerEnum.LAYER_STATUS.getCode().equals(str)) {
                str = lineList.get(++i).trim();
                layer.setStatus(str);
            } else if (DxfEntitiesBaseEnum.ENTITY_FLAG.getCode().equals(str) && LayerEnum.LAYER_END.getCode().equals(lineList.get(i + 1).trim())) {
                // 实体结束
                break;
            } else {
                //未解析的其他属性
                ++i;
            }
        }
        if (Objects.equals("0", layer.getStatus()) && layer.getName() != null) {
            // 如果图层不可见，则不添加到map中
            map.put(layer.getName(), layer);
        }
        return i;
    }
}
