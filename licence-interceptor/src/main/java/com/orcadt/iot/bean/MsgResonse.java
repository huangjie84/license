package com.orcadt.iot.bean;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
public class MsgResonse {

    private int code;
    private String msg;
    private Object data;

    public MsgResonse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MsgResonse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgResonse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public String toJsonString(){
        return "{" +
                "\"code\":" + code +
                ", \"msg\":'" + msg + '\'' +
                '}';
    }
}
