package com.dxf.parse.exception;

import com.dxf.parse.enums.error.DxfFileErrorEnum;

/**
 * @Author vemo
 * @DateTime 2023/11/24 16:28
 * @Description DXF 文件类型异常
 */

public class DxfFileException extends DxfException {


    public DxfFileException(String message) {
        super(message);
    }

    public DxfFileException(Throwable cause) {
        super(cause);
    }

    public DxfFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public DxfFileException(DxfFileErrorEnum errorEnum) {
        super("ERROR CODE: " + errorEnum.getCode() + "; ERROR MESSAGE: " + errorEnum.getMessage());
    }
}
