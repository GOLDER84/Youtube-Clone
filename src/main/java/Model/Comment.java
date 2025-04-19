package Model;

import java.time.LocalDate;
import java.util.Date;

public class Comment {
    private static int idCounter = 0;
    private final int commentId;
    private int userCommenterId;
    private String comment;
    private LocalDate commentDate;

    public Comment(int userCommenterId, String comment , Date commentDate) {
        this.commentId = idCounter++;
        this.userCommenterId = userCommenterId;
        this.comment = comment;
        this.commentDate = LocalDate.now();
    }
    public int getCommentId() {
        return commentId;
    }
    public int getUserCommenterId() {
        return userCommenterId;
    }
    public String getComment() {
        return comment;
    }
    public LocalDate getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
