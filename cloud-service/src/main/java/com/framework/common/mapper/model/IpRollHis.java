package com.framework.common.mapper.model;

import java.io.Serializable;

public class IpRollHis implements Serializable {
    private Integer id;

    private String ip;

    private String rollDate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRollDate() {
        return rollDate;
    }

    public void setRollDate(String rollDate) {
        this.rollDate = rollDate == null ? null : rollDate.trim();
    }
}