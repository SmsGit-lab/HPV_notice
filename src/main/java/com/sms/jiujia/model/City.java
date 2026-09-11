package com.sms.jiujia.model;

/**
 * @author songmingsong
 * @date 2023/5/6
 */
public class City {
    private String name; // 城市的名称。
    private String code; // 城市的代码。
    private String province; // 城市所属的省份。

    // 构造函数用于设置城市的属性。
    public City(String name, String code, String province) {
        this.name = name;
        this.code = code;
        this.province = province;
    }

    // 获取对象属性的getter方法。
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
