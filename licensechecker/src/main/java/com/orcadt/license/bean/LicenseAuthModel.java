package com.orcadt.license.bean;

import java.io.Serializable;

/**
 * @author jie.huang
 * @date 2020/7/1
 **/

public class LicenseAuthModel implements Serializable {


    /**
     * 授权key
     */
    private String key;
    /**
     * 授权名称
     */
    private String name;
    /**
     * 授权的值
     */
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
