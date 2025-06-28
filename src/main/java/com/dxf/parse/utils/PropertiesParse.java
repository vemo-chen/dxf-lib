package com.dxf.parse.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.dxf.generate.model.entities.Color;
import com.dxf.generate.utils.StreamUtil;
import com.dxf.generate.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author vemo
 * @DateTime 2023/11/24 10:05
 * @Description 多文本样式解析
 */
public class PropertiesParse {

    /**
     * cad默认颜色ACI编码
     */
    private static final String DEFAULT_COLOR_ACI_CODE = "7";

    /**
     * 256= BYLAYER
     */
    public static final String DEFAULT_COLOR_BY_LAYER = "256";


    /**
     * cad默认颜色与16进制颜色转换文件
     */
    private static final String DEFAULT_COLOR_JSON_FILE_PATH = "dxf/color.json";

    /**
     * 完全透明对应的alpha值
     */
    private static final Integer DEFAULT_ALPHA_00 = 33554432;

    /**
     * CAD颜色列表
     */
    private static Map<String, JSONObject> colorMap = new HashMap<>();

    static {
        try (
                InputStream resourceStream = StreamUtil.getResourceStream(DEFAULT_COLOR_JSON_FILE_PATH);
                JSONReader reader = new JSONReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8))
        ) {
            JSONArray colorReflection = reader.readObject(JSONArray.class);
            for (int i = 0; i < colorReflection.size(); i++) {
                colorMap.put(colorReflection.getJSONObject(i).getString("code"), colorReflection.getJSONObject(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 解析CAD ACI色号为16进制颜色标识
     *
     * @param cadCode CAD ACI色号
     * @return #FFFFFF 十六进制颜色代码
     */
    public static String parseCadColorToHexColorCode(String cadCode) {
        JSONObject json = colorMap.get(cadCode);
        if (json != null) {
            return json.getString("color");
        }
        if (Objects.equals(DEFAULT_COLOR_BY_LAYER, cadCode)) {
            return DEFAULT_COLOR_BY_LAYER;
        }
        return colorMap.get(DEFAULT_COLOR_ACI_CODE).getString("color");
    }

    /**
     * 格式颜色为DXF类型，大小写颜色十六进制代码为整数
     *
     * @param color
     * @return
     */
    public static int formatDxfColor(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }
        return (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
    }

    /**
     * 解析透明度
     *
     * @param alpha 32位整数，真彩色透明度
     * @return 0-100透明度
     */
    public static Integer parseAlpha(String alpha) {
        if (alpha == null || StringUtil.isEmpty(alpha)) {
            return 0;
        }
        int a = Integer.parseInt(alpha) - DEFAULT_ALPHA_00;
        return Math.round((a / 255f) * 100);
    }

    /**
     * 解析透明度
     *
     * @param alpha 0-100透明度
     * @return 32位整数，真彩色透明度
     */
    public static Integer parseToCadAlpha(int alpha) {
        return DEFAULT_ALPHA_00 + (alpha * 255 / 100);
    }


    /**
     * 解析线宽
     *
     * @param weight 线宽
     * @return 线宽值
     */
    public static Double parseWeight(String weight) {
        if (weight == null || StringUtil.isEmpty(weight)) {
            return 2.0;
        }
        try {
            double v = Double.parseDouble(weight);

            if (v < 0) {
                v = 20;
            } else if (v > 200) {
                v = 200;
            }
            return v / 10;
        } catch (NumberFormatException e) {
            return 2.0;
        }
    }

    /**
     * 将整数的颜色值，转为16进制 类似html16进制颜色表示形式
     *
     * @param colorValue 真彩色
     * @return #FFFFFF 十六进制颜色代码
     */
    public static String convertToHtmlColor(int colorValue) {
        // 将整数值转为16进制字符串
        StringBuilder hexString = new StringBuilder(Integer.toHexString(colorValue));

        // 补足到6位
        while (hexString.length() < 6) {
            hexString.insert(0, "0");
        }

        return "#" + hexString.toString().toUpperCase();
    }


}
