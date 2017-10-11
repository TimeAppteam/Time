package com.time.time;

/**
 * Created by Jerry on 2017/9/25.
 */

public class appInfomation {
    private String appName;
    private String useTime;
    private String useFrequency;
    private int appIcon;

    public appInfomation(String appName, String useTime, String useFrequency, int appIcon) {
        this.appName = appName;
        this.useTime = useTime;
        this.useFrequency = useFrequency;
        this.appIcon = appIcon;
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

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setUseFrequency(String useFrequency) {
        this.useFrequency = useFrequency;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }
}
