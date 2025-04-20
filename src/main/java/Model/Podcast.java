package Model;

public class Podcast extends Content{
    private boolean isExclusive;

    public Podcast(int ownerId, int channelId, String name, String description, String duration, Category category, String fileLink, String cover , boolean isExclusive) {
        super(ownerId, channelId, name, isExclusive,description, duration, category, fileLink, cover);
        this.isExclusive = isExclusive;
    }
    public boolean isExclusive() {
        return isExclusive;
    }
    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }
}
