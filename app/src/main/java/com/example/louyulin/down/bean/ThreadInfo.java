package com.example.louyulin.down.bean;

public class ThreadInfo {
    private int id;
    private String url;
    private long filelength;
    private int state;
    private String title;

    public ThreadInfo() {

    }

    public ThreadInfo(int id, String url, long filelength, int state, String title) {
        this.id = id;
        this.url = url;
        this.filelength = filelength;
        this.state = state;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFilelength() {
        return filelength;
    }

    public void setFilelength(long filelength) {
        this.filelength = filelength;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
