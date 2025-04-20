package Model;

import java.time.LocalDate;

public class LiveStream extends Video{
    private int currentViewers;
    private LocalDate scheduledTime;

    public LiveStream(int ownerId, int channelId, String name , boolean isExclusive, String description, String duration, Category category, String fileLink, String cover, String subtitles , int currentViewers, LocalDate scheduledTime) {
        super(ownerId, channelId, name, isExclusive,description, duration, category, fileLink, cover, subtitles);
        this.currentViewers = currentViewers;
        this.scheduledTime = scheduledTime;
    }
    public int getCurrentViewers() {
        return currentViewers;
    }
    public void setCurrentViewers(int currentViewers) {
        this.currentViewers = currentViewers;
    }
    public LocalDate getScheduledTime() {
        return scheduledTime;
    }
    public void setScheduledTime(LocalDate scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
