package Model;

public class NormalUser extends User {
    private static final int MaxPlaylistContent = 10;
    private static final int MaxPlaylists = 5;

    public NormalUser(String username, String password, String fullName, String email, String phone , String profileCover) {
        super(username, password, fullName, email, phone, profileCover);
    }
    public int getMaxPlaylists() {
        return MaxPlaylists;
    }
    public int getMaxPlaylistContent() {
        return MaxPlaylistContent;
    }
}
