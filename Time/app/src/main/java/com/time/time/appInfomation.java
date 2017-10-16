package com.time.time;

/**
 * Created by Jerry on 2017/9/25.
 */

public class appInfomation {
    private String appName;//注意，应用名和包名不同
    private String useTime;
    private String useFrequency;
    private String packagename;//这是包名

    public appInfomation(String appName, String useTime, String useFrequency, String packagename) {
        this.appName = appName;
        this.useTime = useTime;
        this.useFrequency = useFrequency;
        this.packagename=packagename;
    }

    public String getAppName() {
        return appName;
    }

    public String getUseTime() {
        return useTime;
    }

    public String getUseFrequency() {
        return useFrequency;
    }

    public String getPackagename(){return  packagename;}

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setUseFrequency(String useFrequency) {
        this.useFrequency = useFrequency;
    }

    public void setPackagename(String packagename){this.packagename=packagename;}
}
