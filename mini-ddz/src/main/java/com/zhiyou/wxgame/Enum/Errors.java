package com.zhiyou.wxgame.Enum;


public enum Errors {
	
    OK(200,"OK"),
    Error(201,"error"),
    
    ;
    
    private int code;
    private String msg;
    
    Errors(int code, String msg){
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
    
}
