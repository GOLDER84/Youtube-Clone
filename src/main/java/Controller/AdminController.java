package Controller;

import Model.*;
import java.util.ArrayList;

public class AdminController {
    private Admin admin = Admin.getInstance();
    private Database database = Database.getInstance();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private static AdminController instance;
    public AdminController() {
    }
    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }

    public String login(String username, String password) {
        Admin admin = Admin.getInstance();
        if (!(admin.getUsername().equals(username))){
            return "Invalid username";
        }
        if (!(admin.getPassword().equals(password))){
            return "Invalid password";
        }
        this.admin = admin;
        return "Admin logged in.";
    }

    public String logout() {
        if (this.admin == null) {
            return "You are not logged in";
        }
        this.admin = null;
        return "Logged out successfully";
    }
    public String adminInfo(){
        if (this.admin == null) {
            return "You are not logged in";
        }
        return String.format(
                "=== Admin Account Information ===\n" +
                        "ID: %d\n" +
                        "Username: %s\n" +
                        "Full Name: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Profile Cover: %s\n" +
                        "===============================",
                admin.getId(),
                admin.getUsername(),
                admin.getFullName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getProfileCover()
        );
    }

    public String showPopularChannelOnSubscribers() {
        ArrayList<Channel> channels = database.getAllChannel();
        if (channels.isEmpty()) return "No channels available";

        channels.sort((c1, c2) -> Integer.compare(c2.getSubscribersList().size(), c1.getSubscribersList().size()));

        StringBuilder result = new StringBuilder("Most popular channels:\n");
        for (int i = 0; i < Math.min(5, channels.size()); i++) {
            result.append(i + 1).append(". ").append(channels.get(i).getChannelName()).append(" (").append(channels.get(i).getSubscribersList().size()).append(" subscribers)\n");
        }
        return result.toString();
    }

    public String showPopularContentOnLikes() {
        ArrayList<Content> contents = database.getAllContent();
        if (contents.isEmpty()) return "No content available";

        contents.sort((c1, c2) -> Integer.compare(c2.getLikes(), c1.getLikes()));

        StringBuilder result = new StringBuilder("Most popular content:\n");
        for (int i = 0; i < Math.min(5, contents.size()); i++) {
            result.append(i+1).append(". ").append(contents.get(i).getName()).append(" (").append(contents.get(i).getLikes()).append(" likes)\n");
        }
        return result.toString();
    }

    public String showAllContentInfo() {
        ArrayList<Content> contents = database.getAllContent();
        if (contents.isEmpty()) return "No content available";

        StringBuilder result = new StringBuilder("All Content:\n");
        for (Content content : contents) {
            result.append(String.format("ID: %d, Name: %s, Likes: %d", content.getId(), content.getName(), content.getLikes()));
        }
        return result.toString();
    }

    public String showAllUserAccountInfo() {
        ArrayList<User> users = databaseController.getUsers();
        if (users.isEmpty()) return "No users available";

        StringBuilder result = new StringBuilder("All Users:\n");
        for (User user : users) {
            result.append(String.format("ID: %d, Username: %s, Name: %s, Type: %s\n", user.getId(), user.getUsername(), user.getUsername(), (user instanceof PremiumUser) ? "Premium" : "Normal"));
        }
        return result.toString();
    }

    //    public String showChannelAndContent(int channelId) {
//        Channel channel = databaseController.getChannelById(channelId);
//        if (channel == null) return "Channel not found";
//
//        StringBuilder result = new StringBuilder(String.format("Channel Info:\nName: %s\nDescription: %s\nSubscribers: %d\n", channel.getChannelName(), channel.getChannelDescription(), channel.getSubscribersList().size()));
//
//        result.append("Contents:\n");
//        for (int contentId : channel.getContentId()) {
//            Content content = databaseController.getContentById(contentId);
//            if (content != null) {
//                result.append("- ").append(content.getName()).append("\n");
//            }
//        }
//        return result.toString();
//    }
    public String showAllReport(){
        ArrayList<Report> reports = database.getAllReport();
        if (reports.isEmpty()) return "No reports available";
        StringBuilder result = new StringBuilder("All reports:\n");
        for (Report report : reports) {
            result.append(String.format("Reporter:%s\nReportedContent: %s\nUserReported: %s\nDescription: %s\n" , databaseController.getUserById(report.getReporterId()).getUsername() ,databaseController.getContentById(report.getContentId()).getName() ,databaseController.getUserById(report.getReportedUserId()).getUsername() , report.getDescription()));
        }
        return result.toString();
    }
    public String showAllChannelsAndContents() {
        ArrayList<Channel> channels = databaseController.getChannels();
        if (channels.isEmpty()) return "No channels available";

        StringBuilder result = new StringBuilder("All Channels:\n");
        for (Channel channel : channels) {
            result.append(String.format("Channel: %s (ID: %d)\nContents:\n", channel.getChannelName(), channel.getChannelId()));

            for (int contentId : channel.getContentId()) {
                Content content = databaseController.getContentById(contentId);
                if (content != null) {
                    result.append("- ").append(content.getName()).append("\n");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String acceptReport(int reportId) {
        Report report = databaseController.getReportById(reportId);
        if (report == null) return "Report not found";
        Content content = databaseController.getContentById(report.getContentId());
        if (content == null) return "Content not found";
        databaseController.getReports().remove(report);
        databaseController.removeContent(content);
        banUser(report.getReportedUserId());
        return "Report accepted and content removed and user banned";
    }

    public void banUser(int userId) {
        User user = databaseController.getUserById(userId);
        if (user == null) return;
        database.getAllBanUser().add(user);
    }

    public String unbanUser(int userId) {
        ArrayList<User> bannedUsers = database.getAllBanUser();
        User user = bannedUsers.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);

        if (user == null) return "User not found in banned list";
        bannedUsers.remove(user);
        return "User unbanned successfully";
    }

}

