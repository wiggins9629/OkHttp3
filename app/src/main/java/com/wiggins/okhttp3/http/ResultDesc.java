package com.wiggins.okhttp3.http;

/**
 * @Description 接口返回数据封装
 * @Author 一花一世界
 */

public class ResultDesc {

    private int error_code;//返回码
    private String reason;//返回说明
    private String result;//返回数据

    public ResultDesc(int error_code, String reason, String result) {
        this.error_code = error_code;
        this.reason = reason;
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultDesc{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
