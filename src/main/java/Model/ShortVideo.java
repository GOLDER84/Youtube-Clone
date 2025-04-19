package Model;

public class ShortVideo extends Video {
    private String referenceMusic;

    public ShortVideo(int ownerId, int channelId, String name , boolean isExclusive, String description, String duration, Category category, String fileLink, String cover, String subtitles , String referenceMusic) {
        super(ownerId, channelId, name, isExclusive ,description, duration, category, fileLink, cover, subtitles);
        this.referenceMusic = referenceMusic;
    }
    public String getReferenceMusic() {
        return referenceMusic;
    }
    public void setReferenceMusic(String referenceMusic) {
        this.referenceMusic = referenceMusic;
    }
}
