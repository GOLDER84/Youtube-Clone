package Model;

public class Report {
    private static int idCounter = 0;
    private final int id;
    private int reporterId;
    private int contentId;
    private int reportedUserId;
    private String description;

    public Report(int contentId, int reporterId, int reportedUserId, String description) {
        idCounter++;
        this.id = idCounter;
        this.contentId = contentId;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporter(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
