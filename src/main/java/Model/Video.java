package Model;

public abstract class Video extends Content{
    protected String subtitles;

    public Video(int ownerId, int channelId, String name , boolean isExclusive, String description, String duration, Category category, String fileLink, String cover , String subtitles) {
        super(ownerId, channelId, name, isExclusive ,description, duration, category, fileLink, cover);
        this.subtitles = subtitles;
    }
    public String getSubtitles() {
        return subtitles;
    }
    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }
}
