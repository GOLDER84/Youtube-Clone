package Model;

import java.util.ArrayList;

public abstract class User extends Account{
    private double balance;
    private ArrayList<Playlist> playlists;
    private Channel userChannel;
    private ArrayList<Channel> subscription;
    private ArrayList<Category> favoriteCategories;
    private ArrayList<Content> likedContent;
    private int likedContentsCount;
    public User(String username, String password, String fullName, String email, String phone, String profileCover , double balance) {
        super(username, password, fullName, email, phone, profileCover);
        this.balance = balance;
        this.playlists = new ArrayList<>();
        this.favoriteCategories = new ArrayList<>();
        this.likedContent = new ArrayList<>();
        this.subscription = new ArrayList<>();
        playlists.add(new Playlist("Liked"));
        playlists.add(new Playlist("Watch Later"));
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public Channel getUserChannel() {
        return userChannel;
    }
    public int getUserChannelById(){
        return getUserChannel().getChannelId();
    }

    public void setUserChannel(Channel userChannel) {
        this.userChannel = userChannel;
    }

    public ArrayList<Channel> getSubscription() {
        return subscription;
    }

    public ArrayList<Category> getFavoriteCategories() {
        return favoriteCategories;
    }

    public ArrayList<Content> getLikedContent() {
        return likedContent;
    }

    public void setLikedChannels(ArrayList<Content> likedContent) {
        this.likedContent = likedContent;
    }

    public int getLikedContentsCount() {
        return likedContentsCount;
    }

    public void setLikedContentsCount(int likedContentsCount) {
        this.likedContentsCount = likedContentsCount;
    }
    public void createChannel(String name , String description , String cover) {
        if (this.userChannel == null) {
            this.userChannel = new Channel(name , description , cover , getId());
        }
    }
    public Playlist getPlaylistById(int id) {
        for (Playlist playlist : playlists) {
            if (playlist.getId() == id) {
                return playlist;
            }
        }
        return null;
    }
}
