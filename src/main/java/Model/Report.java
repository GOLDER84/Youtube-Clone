package Model;

public class Report {
    private static int idCounter = 0;
    private final int reportId;
    private int reporterId;
    private int contentId;
    private int reportedUserId;
    private String description;

    public Report(int reporterId, int contentId, int reportedUserId, String description) {
        this.reportId = ++idCounter;
        this.reporterId = reporterId;
        this.contentId = contentId;
        this.reportedUserId = reportedUserId;
        this.description = description;
    }

    public int getReportId() { return reportId; }
    public int getReporterId() { return reporterId; }
    public int getContentId()  { return contentId; }
    public int getReportedUserId() { return reportedUserId; }
    public String getDescription() { return description; }
}
