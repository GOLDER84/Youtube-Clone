package Controller;

import Model.Channel;
import Model.*;
import java.util.ArrayList;

public class ChannelController {
    private Channel channel;
    private DatabaseController databaseController = DatabaseController.getInstance();

    private static ChannelController instance;
    public ChannelController() {
    }
    public static ChannelController getInstance() {
        if (instance == null) {
            instance = new ChannelController();
        }
        return instance;
    }
    public String showSubscribersList() {
        StringBuilder result = new StringBuilder("Subscribers:\n");
        for (User subscriber : channel.getSubscribers()) {
            if (subscriber != null) {
                result.append("- ").append(subscriber.getUsername()).append("\n");
            }
        }
        return result.toString();
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public String viewChannel() {
        return String.format(
                "Channel Info:\nName: %s\nDescription: %s\nSubscribers: %d\n", channel.getChannelName(), channel.getChannelDescription(), channel.getSubscribersList().size()
        );
    }

//    public String editChannel(String name, String description, String cover) {
//        channel.setChannelName(name);
//        channel.setChannelDescription(description);
//        channel.setChannelCover(cover);
//        return "Channel updated successfully";
//    }

    //    public String addSubscriber(int userId) {
//        User user = databaseController.getUserById(userId);
//        if (user == null) {
//            return "User not found";
//        }
//        channel.getSubscribersList().add(userId);
//        return "Subscriber added";
//    }
    public String addContentToChannel(int contentId) {
        Content content = databaseController.getContentById(contentId);
        if (content == null) {
            return "Content not found";
        }
        channel.getContentId().add(contentId);
        channel.getPlaylistById(channel.getAllContentPlaylistId()).getContentList().add(content);
        return "Content added successfully";
    }
    public String createChannelPlaylist(String playlistName){
        if (channel.getPlaylists().stream().anyMatch(playlist -> playlist.getPlaylistName().equals(playlistName))) {
            return "Playlist already exists";
        }
        Playlist newPlaylist = new Playlist(playlistName);
        channel.getPlaylists().add(newPlaylist);
        databaseController.updateChannel(channel);
        return "Playlist '" + playlistName + "' created successfully in channel";
    }
    public int getChannelById(){
        return channel.getChannelId();
    }
    public ArrayList<Playlist> getAllPlaylists(){
        return channel.getPlaylists();
    }
    public Playlist getPlaylistById(String playlistId){
        for (Playlist playlist : channel.getPlaylists()) {
            if (playlist.getId() == Integer.parseInt(playlistId)) {
                return playlist;
            }
        }
        return null;
    }
}

