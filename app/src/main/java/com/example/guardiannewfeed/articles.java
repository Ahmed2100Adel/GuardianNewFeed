package com.example.guardiannewfeed;

public class articles {

    private String id;
    private String title;
    private String type;
    private String sectinName;
    private String url;

    public articles(String id, String title, String type, String sectinName, String url) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.sectinName = sectinName;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getSectinName() {
        return sectinName;
    }

    public String getUrl() {
        return url;
    }
}
