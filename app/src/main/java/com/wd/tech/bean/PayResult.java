package com.wd.tech.bean;

import java.util.Map;

public class PayResult {
    private String result;
    private String resultStatus;

    public PayResult(Map<String, String> obj) {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
