package com.business.exchange.model;

public class MenuButton {
    private String id;
    private String title;
    private String logo;
    private String description;
    private String href;
    private String memberLink;

    public MenuButton(String id, String title, String logo, String description, String href, String memberLink) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.description = description;
        this.href = href;
        this.memberLink = memberLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMemberLink() {
        return memberLink;
    }

    public void setMemberLink(String memberLink) {
        this.memberLink = memberLink;
    }
}
