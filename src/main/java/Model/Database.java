package Model;

import java.util.ArrayList;

public class Database {
    private static Database instance;
    private ArrayList<User> allUser;
    private ArrayList<Content> allContent;
    private ArrayList<Report> allReport;
    private ArrayList<Channel> allChannel;
    private ArrayList<User> allBanUser;

    private Database() {
        this.allUser = new ArrayList<>();
        this.allContent = new ArrayList<>();
        this.allReport = new ArrayList<>();
        this.allChannel = new ArrayList<>();
        this.allBanUser = new ArrayList<>();
    }
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<User> getAllUser() {
        return allUser;
    }

    public ArrayList<Content> getAllContent() {
        return allContent;
    }

    public ArrayList<Report> getAllReport() {
        return allReport;
    }

    public ArrayList<Channel> getAllChannel() {
        return allChannel;
    }

    public ArrayList<User> getAllBanUser() {
        return allBanUser;
    }
}
