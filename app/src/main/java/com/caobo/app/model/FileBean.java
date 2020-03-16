package com.caobo.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cb
 */
@DatabaseTable(tableName = "t_filebean")
public class FileBean extends BaseModel{
    @DatabaseField
    private String url;
    @DatabaseField
    private String time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
