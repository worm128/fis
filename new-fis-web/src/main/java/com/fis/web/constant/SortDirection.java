package com.fis.web.constant;


public enum SortDirection {
    /// <summary>
    /// 倒叙
    /// </summary>
    Desc("D", "倒序"),
    /// <summary>
    /// 顺叙
    /// </summary>
    Asc("A", "顺序");

    private final String code;
    private final String desc;

    SortDirection(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
