package com.dxf.parse.exception;

/**
 * @Author vemo
 * @DateTime 2023/11/24 16:23
 * @Description DXF 根异常
 */
public class DxfException extends RuntimeException {

    public DxfException(String message) {
        super(message);
    }

    public DxfException(Throwable cause) {
        super(cause);
    }

    public DxfException(String message, Throwable cause) {
        super(message, cause);
    }

}
