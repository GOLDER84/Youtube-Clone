package Model;

import java.util.ArrayList;

public class Playlist {
    private static int idCounter = 0;
    private final int playlistLimit;
    private int id;
    private String playlistName;
    private ArrayList<Content> contentList;

    public Playlist(String playlistName) {
        if (playlistName.equals("Liked")||playlistName.equals("Watch Later")||playlistName.equals("All Content")){
            this.id=-1;
        }else {
            idCounter++;
            this.id = idCounter;
        }
        this.playlistLimit = 10;
        this.playlistName = playlistName;
        this.contentList = new ArrayList<>();
    }

    public int getPlaylistLimit() {
        return playlistLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public ArrayList<Content> getContentList() {
        return contentList;
    }
}
