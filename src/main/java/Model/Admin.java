package Model;

public class Admin extends Account{
    private static Admin instance;

    public Admin() {
        super("Admin", "123", "System Admin",
                "admin123@gmail.com", "1234567890", "default_admin_cover.jpg");
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }
}
