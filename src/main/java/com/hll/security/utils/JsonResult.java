package com.hll.security.utils;/**
 * @author Le
 * @date 2019/5/521:21
 */

/**
 * @author hll
 * @date 2019/5/5 21:21
 */
public class JsonResult {

    /**
     * code值
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 布尔值
     */
    private boolean flag;

    public JsonResult() {
    }

    public JsonResult(String message, boolean flag) {
        this.message = message;
        this.flag = flag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
