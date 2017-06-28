package com.wxzd.efcs.business.scheduler;

/**
 * Created by zhouzh on 2017/5/16.
 */
public class ChargeResult {

    public ChargeResult(int code, String msg, Boolean isFinish) {
        this.code = code;
        this.errorMessage = msg;
        this.isFinish = isFinish;
    }

    private int code = 0;

    private String errorMessage;

    private Boolean isFinish;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }
}
