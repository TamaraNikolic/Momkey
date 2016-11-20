package com.momkey.alen.momkey.data;

/**
 * Created by Tamara on 8/3/2016.
 */
public class Record {

    private long id;
    private String name;
    private String path;
    private String date;
    private String size;
    private long time;
    public  boolean isUpdateLayoutVisible=false;// for adapter save state for update
    public  boolean isUpdateLayoutVisiblePlay=false;// for adapter save state for play
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Record(String name, String path, String date, String size, long time) {

        this.name = name;
        this.path = path;
        this.date = date;
        this.size = size;
        this.time = time;
    }
    public Record(){

    }
}

