package Model;

import java.util.ArrayList;
import java.util.Date;

public abstract class Content {
    private static int idCounter = 0;
    private final int id;
    private int ownerId;
    private int channelId;
    private String name;
    private boolean isExclusive;
    private String description;
    private String duration;
    private int views;
    private int likes;
    private Date publishDate;
    private Category category;
    private String fileLink;
    private String cover;
    private ArrayList<Comment> commentList;
    public Content(int ownerId , int channelId ,String name , boolean isExclusive, String description, String duration,
                   Category category, String fileLink, String cover) {
        idCounter++;
        this.id = idCounter;
        this.ownerId = ownerId;
        this.channelId = channelId;
        this.name = name;
        this.isExclusive = isExclusive;
        this.isExclusive = false;
        this.description = description;
        this.duration = duration;
        this.views = 0;
        this.likes = 0;
        this.publishDate = new Date();
        this.category = category;
        this.fileLink = fileLink;
        this.cover = cover;
        this.commentList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

}

