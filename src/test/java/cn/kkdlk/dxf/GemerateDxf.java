package cn.kkdlk.dxf;

import com.dxf.generate.DxfDocWriter;
import com.dxf.generate.enums.LineWidthEnum;
import com.dxf.generate.model.Vector3;
import com.dxf.generate.model.entities.Color;
import com.dxf.generate.model.entities.DxfLine;
import com.dxf.generate.model.entities.DxfPoint;
import com.dxf.generate.model.entities.DxfPolyLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author vemo
 * @DateTime 2023/11/27 16:44
 * @Description 生成dxf
 */
public class GemerateDxf {

    String url = "D:\\dxf\\0630\\0701-1.dxf";

    @Test
    public void testWrite() {
        DxfDocWriter dxfDocWriter = new DxfDocWriter();
        Color color = new Color(255, 0, 0);

        // 创建点
        DxfPoint dxfPoint = new DxfPoint();
        dxfPoint.setPoint(new Vector3(300.00, 300.0, 300.0));
        dxfPoint.setHeight(300D);
        dxfPoint.setColor(color);
        dxfPoint.setAlpha(22);
        dxfPoint.setLayerName("ly1");
        dxfPoint.setLineWidth(LineWidthEnum.LW_5);
        dxfDocWriter.addEntity(dxfPoint);


        // 创建线段
        DxfLine dxfLine = new DxfLine();
        dxfLine.setStartPoint(100, 400, 500);
        dxfLine.setEndPoint(500, 450, 400);
        dxfLine.setColor(color);
        dxfLine.setAlpha(22);
        dxfLine.setLineWidth(LineWidthEnum.LW_13);
        dxfLine.setLayerName("ly2");
        dxfDocWriter.addEntity(dxfLine);

        // 创建三维多段线实体
        DxfPolyLine dxfPolyLine = new DxfPolyLine();
        List<List<Double>> points = new ArrayList<>();
        List<Double> point = new ArrayList<>();
        double x = 100;
        double y = 100;
        double z = 100;
        point.add(x);
        point.add(y);
        point.add(z);
        points.add(point);

        x = 300;
        y = 100;
        z = 300;
        point = new ArrayList<>();
        point.add(x);
        point.add(y);
        point.add(z);
        points.add(point);

        x = 200;
        y = 200;
        z = 500;
        point = new ArrayList<>();
        point.add(x);
        point.add(y);
        point.add(z);
        points.add(point);
        dxfPolyLine.addPoints(points);

        dxfPolyLine.setColor(color);
        dxfPolyLine.setClose(true);
        dxfPolyLine.setAlpha(44);
        dxfPolyLine.setLayerName("ly3");
        dxfPolyLine.setLineWidth(LineWidthEnum.LW_9);
        dxfDocWriter.addEntity(dxfPolyLine);

        dxfDocWriter.save(url, true);
    }
}
