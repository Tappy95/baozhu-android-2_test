package com.micang.baozhu.module.information;

public class NewsChannelTable {


    private String newsChannelName;

    private String newsChannelId;

    public NewsChannelTable() {
    }

    public NewsChannelTable(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }

    public NewsChannelTable(String newsChannelName, String newsChannelId) {
        this.newsChannelName = newsChannelName;
        this.newsChannelId = newsChannelId;
    }


    public String getNewsChannelName() {
        return newsChannelName;
    }


    public void setNewsChannelName(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }


    public String getNewsChannelId() {
        return newsChannelId;
    }


    public void setNewsChannelId(String newsChannelId) {
        this.newsChannelId = newsChannelId;
    }


}
