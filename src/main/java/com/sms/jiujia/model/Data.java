package com.sms.jiujia.model;

import java.util.List;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Data {
    private List<Xiaoxi> xiaoxi;
    private Website website;
    private Calldata calldata;
    private Onsitedate onsitedate;

    public List<Xiaoxi> getXiaoxi() {
        return xiaoxi;
    }

    public void setXiaoxi(List<Xiaoxi> xiaoxi) {
        this.xiaoxi = xiaoxi;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Calldata getCalldata() {
        return calldata;
    }

    public void setCalldata(Calldata calldata) {
        this.calldata = calldata;
    }

    public Onsitedate getOnsitedate() {
        return onsitedate;
    }

    public void setOnsitedate(Onsitedate onsitedate) {
        this.onsitedate = onsitedate;
    }

    @Override
    public String toString() {
        return "Data{" +
                "xiaoxi=" + xiaoxi +
                ", website=" + website +
                ", calldata=" + calldata +
                ", onsitedate=" + onsitedate +
                '}';
    }
}
