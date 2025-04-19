package Model;

import java.util.ArrayList;

public class Channel {
    private static int idCounter = 0;
    private int channelId;
    private String channelName;
    private String channelDescription;
    private String channelCover;
    private int channelOwnerId;
    private ArrayList<Playlist> playlists;
    private int allContentPlaylistId;
    private ArrayList<User> subscribers;
    private ArrayList<Integer> subscribersList;
    private ArrayList<Integer> contentId;

    public Channel(String channelName, String channelDescription, String channelCover, int channelOwnerId) {
        idCounter++;
        this.channelId= idCounter;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.channelCover = channelCover;
        this.channelOwnerId = channelOwnerId;
        this.playlists = new ArrayList<>();
        Playlist allContent = new Playlist("All Content");
        this.playlists.add(allContent);
        this.allContentPlaylistId = allContent.getId();
        this.subscribers = new ArrayList<>();
        this.subscribersList = new ArrayList<>();
        this.contentId = new ArrayList<>();
    }

    public int getAllContentPlaylistId() {
        return allContentPlaylistId;
    }

    public int getChannelId() {
        return channelId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getChannelDescription() {
        return channelDescription;
    }
    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }
    public String getChannelCover() {
        return channelCover;
    }
    public void setChannelCover(String channelCover) {
        this.channelCover = channelCover;
    }
    public int getChannelOwnerId() {
        return channelOwnerId;
    }
    public void setChannelOwnerId(int channelOwnerId) {
        this.channelOwnerId = channelOwnerId;
    }
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
    public ArrayList<User> getSubscribers() {
        return subscribers;
    }
    public ArrayList<Integer> getSubscribersList() {
        return subscribersList;
    }
    public ArrayList<Integer> getContentId() {
        return contentId;
    }
    public Playlist getPlaylistById(int playlistId) {
        return playlists.stream().filter(playlist -> playlist.getId() == playlistId).findFirst().orElse(null);
    }
}

