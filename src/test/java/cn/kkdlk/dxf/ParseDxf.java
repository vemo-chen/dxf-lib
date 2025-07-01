package cn.kkdlk.dxf;

import com.dxf.parse.ParseDXF;
import com.dxf.parse.model.GeometricModel;
import com.dxf.parse.resolver.DxfResolver;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author vemo
 * @DateTime 2023/11/27 16:44
 * @Description 解析dxf
 */
public class ParseDxf {
    @Test
    public void parseDxfFile() {
        try {
            DxfResolver build = ParseDXF.build("D:\\dxf\\dx\\polyline.dxf");
            Map<String, List<GeometricModel>> structureMap = build.getStructureMap();
            System.out.println(structureMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
