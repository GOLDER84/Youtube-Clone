package Controller;

import Model.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class UserController {

    private User user;
    private Database database = Database.getInstance();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private ChannelController channelController = ChannelController.getInstance();
    private static UserController instance;
    public User lastSignedUpUser;
    public UserController() {
    }
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public String createAccount(String username, String password, String fullName, String email, String phone, String profileCover) {
        String validation = checkSignupInputs(username, password, fullName, email, phone, profileCover);
        if (!validation.equals("Valid")) {
            return validation;
        }
        if (database.getAllUser().stream().anyMatch(user -> user.getUsername().equals(username))) {
            return "Username already exists";
        }
        User newUser = new NormalUser(username, password, fullName, email, phone , profileCover);
        databaseController.getUsers().add(newUser);
        lastSignedUpUser = newUser;
        return "Account created successfully";
    }

    public String login(String username, String password) {
        User foundUser = database.getAllUser().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);
        if (foundUser != null) {
            boolean isBanned = database.getAllBanUser().stream().anyMatch(user -> user.getUsername().equals(username));
            if (isBanned) {
                return "You are banned";
            }
            else {
                lastSignedUpUser = foundUser;
                this.user = lastSignedUpUser;
                return "Logged in successfully.Welcome" + user.getUsername();
            }
        }
        return "Invalid username or password";
    }

    public String logout() {
        if (this.lastSignedUpUser == null) {
            return "You are not logged in";
        }
        String username = this.user.getUsername();
        this.user = null;
        lastSignedUpUser = null;
        return "Logged out successfully.Goodbye" + username;
    }

    public String checkSignupInputs(String userName, String password, String name, String email, String phoneNumber, String profileCover) {
        if (userName == null || userName.isEmpty()) return "Username cannot be empty";
        if (password == null || password.isEmpty()) return "Password cannot be empty";
        if (!checkPasswordStrength(password)) return "Password is too weak";
        if (name == null || name.isEmpty()) return "Name cannot be empty";
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Invalid email format";
        }
        if (phoneNumber == null || !phoneNumber.matches("\\d{10,15}")) {
            return "Invalid phone number";
        }
        if (profileCover == null) {
            return "Invalid profile cover";
        }
        return "Valid";
    }
    public String editPassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) return "Password cannot be empty";
        if (newPassword.equals(this.user.getPassword())) {
            return "Password Should be different";
        }
        if (!checkPasswordStrength(newPassword)) return "Password is too weak";
        this.user.setPassword(newPassword);
        return "Password updated successfully";
    }
    public boolean checkPasswordStrength(String password) {
        return password.length() >= 6 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    public String showUserInfo() {
        return String.format( "User Info:\nUsername: %s\nName: %s\nEmail: %s\nPhone: %s\nType: %s\nYourChannel: %s\nYourBalance: %s",
                user.getUsername(), user.getFullName(), user.getEmail(), user.getPhone(),
                (user instanceof PremiumUser) ? "Premium" : "Normal" , user.getUserChannel() , user.getBalance());
    }

    public String editProfile(String newFullName) {
        if (newFullName == null || newFullName.isEmpty()) return "Full Name cannot be empty";
        if (newFullName.equals(this.user.getFullName())) {
            return "New Full Name should be different";
        }
        user.setFullName(newFullName);
        return "Profile updated successfully";
    }

    public String createPlaylist(String playlistName) {
        if (user instanceof NormalUser && ((NormalUser) user).getMaxPlaylists() <= user.getPlaylists().size()) {
            return "You Have reached the maximum number of playlists";
        }
        Playlist newPlaylist = new Playlist(playlistName);
        user.getPlaylists().add(newPlaylist);
        return "Playlist created successfully";
    }

    public String chooseFavouriteCategory(ArrayList<Category> categories) {
        if (lastSignedUpUser == null) {
            return "User not found";
        }
        if (categories == null || categories.size() > 4 || categories.isEmpty()){
            return "You must select 1 to 4 categories!";
        }
        for (int i = 0; i < categories.size(); i++) {
            for (int j = i + 1; j < categories.size(); j++) {
                if (categories.get(i) == categories.get(j)) {
                    return "Duplicate categories are not allowed!";
                }
            }
        }
        lastSignedUpUser.getFavoriteCategories().addAll(categories);
        databaseController.updateUser(lastSignedUpUser);
        return "Favorite categories updated successfully!";
    }

    public String addContentToPlaylist(int playlistId, int contentId) {
        Playlist playlist = user.getPlaylistById(playlistId);
        if (playlist == null) {
            playlist=channelController.getPlaylistById(String.valueOf(playlistId));
            if (playlist == null) {
                return "Playlist does not exist";
            }
        }
        Content content = database.getAllContent().stream().filter(c -> c.getId() == contentId).findFirst().orElse(null);
        if (content == null) {
            return "Content does not exist";
        }

        if (user instanceof NormalUser) {
            NormalUser normalUser = (NormalUser) user;
            if (playlist.getContentList().size() >= normalUser.getMaxPlaylistContent()) {
                return "You Have reached the maximum number of playlists";
            }
        }
        if (!(playlist.getContentList().contains(content))) {
            playlist.getContentList().add(content);
            return "Content added successfully";
        }
        return "Content already exists";
    }

    public String playContent(int contentId) {
        Content content = databaseController.getContents().stream().filter(c -> c.getId() == contentId).findFirst().orElse(null);
        if (content == null) {
            return "Content does not exist";
        }
        if (!(user instanceof PremiumUser) && content.isExclusive()) {
            return "This content is for premium users";
        }
        content.setViews(content.getViews() + 1);
        return String.format("Streaming: \nName: %s\nDescription: %s\nCategory: %s\nTime: %s" , content.getName() ,content.getDescription() , content.getCategory() , content.getDuration());
    }

    public String likeContent(int contentId) {
        Content content = database.getAllContent().stream().filter(content1 -> content1.getId() == contentId).findFirst().orElse(null);
        if (content == null) {
            return "Content does not exist";
        }
        if (user.getLikedContent().contains(content)) {
            return "content already liked";
        }
        content.setLikes(content.getLikes() + 1);
        user.getLikedContent().add(content);
        user.setLikedContentsCount(user.getLikedContentsCount() + 1);
        return "Content liked. Total likes: " + user.getLikedContentsCount();
    }

    public String searchContent(String query) {
        ArrayList<Object> results = new ArrayList<>();
        for (Content content : database.getAllContent()) {
            if (content.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(content.getName());
            }
        }
        for (Channel channel : database.getAllChannel()){
            if (channel.getChannelName().toLowerCase().contains(query.toLowerCase())) {
                results.add(channel.getChannelName());
            }
        }
        return results.toString();
    }

    public String reportContent(int contentId , String reason) {
        Content content = database.getAllContent().stream().filter(content1 -> content1.getId() == contentId).findFirst().orElse(null);
        if (content == null) {
            return "Content does not exist";
        }
        Report report = new Report(user.getId() , contentId , content.getOwnerId() , reason);
        databaseController.getReports().add(report);
        return "Report submitted successfully";
    }

    public String subscribeChannel(int channelId) {
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) {
            return "Channel does not exist";
        }
        user.getSubscription().add(channel);
        channel.getSubscribers().add(user);
        return "Subscribed to channel: " + channel.getChannelName();
    }

    public String showPlaylistContents() {
        Playlist playlist = user.getPlaylists().stream().findAny().orElse(null);
        if (playlist == null) {
            return "Playlist does not exist";
        }
        StringBuilder result = new StringBuilder("Playlist: \n");
        for (Playlist playlist1 : user.getPlaylists()) {
            result.append(playlist1.toString()).append("\n");
            result.append("Contents:\n");
            for (Content content : playlist.getContentList()) {
                result.append(" - ").append(content.getName()).append("\n");
            }
        }
        return result.toString();
    }

    public String showSuggestedContents() {
        ArrayList<Content> suggestedContents = databaseController.getContents().stream()
                .filter(content ->
                        user.getFavoriteCategories().contains(content.getCategory()) ||
                                user.getLikedContent().contains(content) ||
                                user.getSubscription().stream()
                                        .anyMatch(ch -> ch.getContentId().contains(content.getId())))
                .sorted(Comparator.comparingInt(Content::getViews).reversed())
                .limit(10)
                .collect(Collectors.toCollection(ArrayList::new));
        return suggestedContents.toString();
    }

    public ArrayList<Channel> showSubscribedChannels() {
        if (user.getSubscription().isEmpty()) {
            return null;
        }
        return user.getSubscription();
    }

    public ArrayList<Channel> showChannels() {
        if (databaseController.getChannels().isEmpty()) {
            return null;
        }
        return databaseController.getChannels();
    }

    public String showChannelInfoAndContents(int id) {
        Channel channel = databaseController.getChannelById(id);
        if (channel == null) {
            return "Channel does not exist";
        }
        StringBuilder result = new StringBuilder(String.format("Channel: %s\nDescription: %s\nSubscribers:%s\n", channel.getChannelName() , channel.getChannelDescription() , channel.getSubscribers().size()));
        return result.toString();
    }

    public String addComment(int contentId , String description) {
        Content content = database.getAllContent().get(contentId);
        if (content == null) {
            return "Content does not exist";
        }
        Comment newComment = new Comment(user.getId() , description , new Date());
        content.getCommentList().add(newComment);
        return "Comment submitted successfully";
    }

    public String buySubscription(PremiumSubscriptionPackages requestedSubscription) {
        if (user instanceof PremiumUser) {
            return "You are already a premium user";
        }
        double price = requestedSubscription.getPrice();
        if (user.getBalance() < price) {
            return "You don't have enough money";
        }
        user.setBalance(user.getBalance() - price);
        LocalDate expirationDate = LocalDate.now().plusDays(requestedSubscription.getDays());
        PremiumUser premiumUser = new PremiumUser(user.getUsername() , user.getPassword() , user.getFullName() , user.getEmail() , user.getPhone() , user.getProfileCover() , expirationDate);
        databaseController.removeUser(user);
        databaseController.addUser(premiumUser);
        this.user = premiumUser;
        return String.format("Subscription purchased successfully! Expires on %s", expirationDate);
    }

    public String createChannel(String channelName , String description , String cover) {
        if (user.getUserChannel() != null) {
            return "You already have a channel";
        }

        user.createChannel(channelName , description , cover);
        channelController.setChannel(user.getUserChannel());
        database.getAllChannel().add(user.getUserChannel());
        return "Channel created successfully";
    }

    public String addBalance(double amount) {
        if (amount <= 0){
            return "Amount must be greater than 0";
        }
        user.setBalance(user.getBalance() + amount);
        return String.format("%s credits added to your account", amount);
    }

    public String showChannelContentAndInfo() {
        if (user.getUserChannel() == null) {
            return "You do not have a channel";
        }
        Channel channel = user.getUserChannel();
        StringBuilder result = new StringBuilder(String.format("Channel: %s\nContents:\n", channel.getChannelName()));
        for (Content content : channel.getPlaylists().getFirst().getContentList()) {
            result.append("- ").append(content.getName()).append("\n");
        }
        return result.toString();
    }

    public String showChannelSubscribers() {
//        if (user.getUserChannel() == null) {
//            return "You do not have a channel";
//        }
//        StringBuilder result = new StringBuilder("Subscribers:\n");
//        for (int subscriberId : user.getUserChannel().getSubscribersList()){
//            String subscriber = channelController.showSubscribersList();
//            if (subscriber != null) {
//                result.append("- ").append(subscriber.getUsername()).append("\n");
//            }
//        }
//        return result.toString();
        return channelController.showSubscribersList();
    }

    public int getUserChannelId(){
        return user.getUserChannelById();
    }
}

