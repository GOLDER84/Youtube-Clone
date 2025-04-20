package Controller;

import Model.*;

import java.util.ArrayList;

public class DatabaseController {
    private static DatabaseController instance;
    private Database database = Database.getInstance();

    public DatabaseController() {
    }

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }
    public ArrayList<User> getUsers() {
        return database.getAllUser();
    }

    public ArrayList<Report> getReports() {
        return database.getAllReport();
    }

    public ArrayList<Content> getContents() {
        return database.getAllContent();
    }

    public ArrayList<Channel> getChannels() {
        return database.getAllChannel();
    }

    public ArrayList<User> getBanUsers() {
        return database.getAllBanUser();
    }

    public User getUserById(int id) {
        return database.getAllUser().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public Content getContentById(int id) {
        return database.getAllContent().stream().filter(content -> content.getId() == id).findFirst().orElse(null);
    }

    public Channel getChannelById(int id) {
        return database.getAllChannel().stream().filter(channel -> channel.getChannelId() == id).findFirst().orElse(null);
    }

    public void addContent(Content content) {
        database.getAllContent().add(content);
    }

    public void removeContent(Content content) {
        database.getAllContent().remove(content);
    }

    public void addUser(User user) {
        database.getAllUser().add(user);
    }
    public void updateUser(User updatedUser) {
        for (int i = 0; i < getUsers().size(); i++) {
            if (getUsers().get(i).getId() == updatedUser.getId()) {
                getUsers().set(i, updatedUser);
                return;
            }
        }
        addUser(updatedUser);
    }
    public void updateChannel(Channel updatedChannel) {
        int index = -1;
        for (int i = 0; i < getChannels().size(); i++) {
            if (getChannels().get(i).getChannelId() == updatedChannel.getChannelId()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            getChannels().set(index, updatedChannel);
        } else {
            getChannels().add(updatedChannel);
        }
    }
    public void removeUser(User user) {
        database.getAllUser().remove(user);
    }

    public Report getReportById(int id) {
        for (Report report : getReports()) {
            if (report.getId() == id) {
                return report;
            }
        }
        return null;
    }
}

