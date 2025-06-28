package cn.kkdlk.dxf;

import com.dxf.generate.DxfDocWriter;
import com.dxf.generate.enums.LineWidthEnum;
import com.dxf.generate.model.Vector3;
import com.dxf.generate.model.entities.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author vemo
 * @DateTime 2023/11/27 16:44
 * @Description 生成dxf
 */
public class GemerateDxf {

    String url = "C:\\Users\\bayod\\Documents\\dxf\\writeTest1.dxf";

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
        dxfLine.setStartPoint(100, 400, 0);
        dxfLine.setEndPoint(500, 400, 0);
        dxfLine.setColor(color);
        dxfLine.setAlpha(22);
        dxfLine.setLineWidth(LineWidthEnum.LW_13);
        dxfLine.setLayerName("ly2");
        dxfDocWriter.addEntity(dxfLine);

        // 创建多段线实体
        DxfLwPolyLine dxfLwPolyLine = new DxfLwPolyLine();
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
        z = 1000;
        point = new ArrayList<>();
        point.add(x);
        point.add(y);
        point.add(z);
        points.add(point);

        x = 200;
        y = 200;
        z = 2500;
        point = new ArrayList<>();
        point.add(x);
        point.add(y);
        point.add(z);
        points.add(point);
        dxfLwPolyLine.addPoints(points);

        dxfLwPolyLine.setHeight(100D);
        dxfLwPolyLine.setColor(color);
        dxfLwPolyLine.setClose(true);
        dxfLwPolyLine.setAlpha(44);
        dxfLwPolyLine.setSolid(true);
        dxfLwPolyLine.setSolidColor(new Color(0, 255, 0));
        dxfLwPolyLine.setSolidAlpha(55);
        dxfLwPolyLine.setLayerName("ly1");
        dxfLwPolyLine.setLineWidth(LineWidthEnum.LW_9);
        dxfDocWriter.addEntity(dxfLwPolyLine);


        // 创建多段线实体
        DxfLwPolyLine dxfLwPolyLine2 = new DxfLwPolyLine();
        List<List<Double>> points2 = new ArrayList<>();
        List<Double> point2 = new ArrayList<>();
        double x2 = 400;
        double y2 = 100;
        double z2 = 100;
        point2.add(x2);
        point2.add(y2);
        point2.add(z2);
        points2.add(point2);

        x2 = 600;
        y2 = 100;
        z2 = 1000;
        point2 = new ArrayList<>();
        point2.add(x2);
        point2.add(y2);
        point2.add(z2);
        points2.add(point2);

        x2 = 500;
        y2 = 200;
        z2 = 2500;
        point2 = new ArrayList<>();
        point2.add(x2);
        point2.add(y2);
        point2.add(z2);
        points2.add(point2);
        dxfLwPolyLine2.addPoints(points2);

        dxfLwPolyLine2.setHeight(100D);
        dxfLwPolyLine2.setColor(color);
        dxfLwPolyLine2.setClose(false);
        dxfLwPolyLine2.setAlpha(44);
        dxfLwPolyLine2.setSolid(false);
        dxfLwPolyLine2.setSolidColor(new Color(0, 255, 0));
        dxfLwPolyLine2.setSolidAlpha(55);
        dxfLwPolyLine.setLayerName("ly1");
        dxfLwPolyLine2.setLineWidth(LineWidthEnum.LW_9);
        dxfDocWriter.addEntity(dxfLwPolyLine2);

        dxfDocWriter.save(url, true);
    }
}
