package com.haoweixiu.dto;

import java.io.Serializable;

public class RoleItem implements Serializable {
	private String f_use;
    private String f_cnt;
    private String f_date_from;
    private String f_date_to;

    public String getF_use() {
        return f_use;
    }

    public void setF_use(String f_use) {
        this.f_use = f_use;
    }

    public String getF_cnt() {
        return f_cnt;
    }

    public void setF_cnt(String f_cnt) {
        this.f_cnt = f_cnt;
    }

    public String getF_date_from() {
        return f_date_from;
    }

    public void setF_date_from(String f_date_from) {
        this.f_date_from = f_date_from;
    }

    public String getF_date_to() {
        return f_date_to;
    }

    public void setF_date_to(String f_date_to) {
        this.f_date_to = f_date_to;
    }
}