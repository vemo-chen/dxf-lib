package com.dxf.generate.utils;

import com.dxf.generate.model.entities.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class DxfUtil {


    public static boolean isPairNameEquals(String[] pair, String name) {
        if (pair == null) {
            return false;
        }
        return Objects.equals(name, pair[1].trim());
    }

    /**
     * 格式化图元句柄
     *
     * @param meta
     * @return
     */
    public static String formatMeta(long meta) {
        return StringUtil.appendStart('0', 3, Long.toHexString(meta)).toUpperCase();
    }

    /**
     * 读取最大图元句柄
     *
     * @param dxfFilePath
     * @return
     */
    public static long readMaxMeta(String dxfFilePath) {
        long maxMeta = 0;
        try (BufferedReader br = StreamUtil.getFileReader(dxfFilePath)) {
            while (true) {
                String[] pair = StreamUtil.readNextPair(br);
                if (pair == null) {
                    break;
                }
                if ("5".equals(pair[0].trim())) {
                    maxMeta = Math.max(maxMeta, Long.parseLong(pair[1].trim(), 16));
                }
            }
        } catch (IOException e) {
            System.err.println("read maxMeta error : " + e.getMessage());
        }
        return maxMeta;
    }




}
