package com.wiggins.okhttp3.http;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.utils.StringUtil;
import com.wiggins.okhttp3.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @Description 数据解析
 * @Author 一花一世界
 */

public class DataAnalysis {

    public enum JSON_TYPE {
        /**
         * JSONObject
         */
        JSON_OBJECT,
        /**
         * JSONArray
         */
        JSON_ARRAY,
        /**
         * 不是JSON格式的字符串
         */
        JSON_ERROR
    }

    /**
     * @param result 返回数据
     * @Description 获取result数据格式
     */
    private static JSON_TYPE getJSONType(String result) {
        if (StringUtil.isEmpty(result)) {
            return JSON_TYPE.JSON_ERROR;
        }

        final char[] strChar = result.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if (firstChar == '{') {
            return JSON_TYPE.JSON_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_ARRAY;
        } else {
            return JSON_TYPE.JSON_ERROR;
        }
    }

    /**
     * @param result 请求返回字符串
     * @Description 返回数据解析
     */
    public static ResultDesc getReturnData(String result) {
        ResultDesc resultDesc = null;

        if (StringUtil.isEmpty(result)) {
            //返回数据为空
            resultDesc = dataRestructuring(-1, UIUtils.getString(R.string.back_abnormal_results), "");
            return resultDesc;
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            //返回码
            int error_code = jsonObject.getInt("error_code");
            //返回说明
            String reason = jsonObject.getString("reason");
            //返回数据
            String resultData = jsonObject.getString("result");

            resultDesc = dataRestructuring(error_code, reason, resultData);
        } catch (JSONException e) {
            resultDesc = dataRestructuring(-1, UIUtils.getString(R.string.back_abnormal_results), "");
        }

        return resultDesc;
    }

    /**
     * @param error_code 返回码
     * @param reason     返回说明
     * @param result     返回数据
     * @Description 数据重组
     */
    private static ResultDesc dataRestructuring(int error_code, String reason, String result) {
        ResultDesc resultDesc = new ResultDesc(error_code, reason, result);
        return resultDesc;
    }
}
