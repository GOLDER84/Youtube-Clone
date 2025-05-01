package com.example.youtube_graphic;

import Controller.AdminController;
import Model.Channel;
import Model.Content;
import Model.User;
import Model.PremiumUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminPanel implements Initializable {

    @FXML private Button adminInfoBtn;
    @FXML private Button popularChannelsBtn;
    @FXML private Button popularContentsBtn;
    @FXML private Button allContentsBtn;
    @FXML private Button allUsersBtn;
    @FXML private Button allReportsBtn;
    @FXML private Button manageReportsBtn;
    @FXML private Button acceptReportBtn;
    @FXML private Button unbanUserBtn;
    @FXML private Button logoutBtn;

    @FXML private Label outputLabel;
    @FXML private BarChart<String, Number> barChart;
    @FXML private ListView<String> userListView;
    @FXML private ListView<String> contentListView;

    private final AdminController adminController = AdminController.getInstance();
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminController.refresh();
        stage = HelloApplication.primaryStage;
        refreshUserList();
        refreshContentList();
    }

    @FXML
    void adminInfoClicked(MouseEvent e) {
        outputLabel.setText(adminController.adminInfo());
    }

    @FXML
    void popularChannelsClicked(MouseEvent e) {
        outputLabel.setText(adminController.showPopularChannelOnSubscribers());
        updateBarChartWithChannels(adminController.getTopChannels(5));
    }

    @FXML
    void popularContentsClicked(MouseEvent e) {
        outputLabel.setText(adminController.showPopularContentOnLikes());
        updateBarChartWithContents(adminController.getTopContents(5));
    }

    @FXML
    void allContentsClicked(MouseEvent e) {
        outputLabel.setText(adminController.showAllContentInfo());
    }

    @FXML
    void allUsersClicked(MouseEvent e) {
        outputLabel.setText(adminController.showAllUserAccountInfo());
    }

    @FXML
    void allReportsClicked(MouseEvent e) {
        outputLabel.setText(adminController.showAllReport());
    }

    @FXML
    void manageReportsClicked(MouseEvent e) {
        outputLabel.setText(adminController.showAllReport());
    }

    @FXML
    void acceptReportBtn(MouseEvent e) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Accept Report");
        dlg.setHeaderText("Enter Report ID to accept:");
        dlg.setContentText("Report ID:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                String out = adminController.acceptReport(id);
                showAlert("Result", out);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid report ID!");
            }
        });
    }

    @FXML
    void unbanUserClicked(MouseEvent e) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Unban User");
        dlg.setHeaderText("Enter User ID to unban:");
        dlg.setContentText("User ID:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                String out = adminController.unbanUser(id);
                showAlert("Result", out);
                refreshUserList();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid user ID!");
            }
        });
    }

    @FXML
    void logoutClicked(MouseEvent e) throws IOException {
        adminController.logout();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpOrLogin.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ========== لیست‌های کلیک‌شونده ==========

    @FXML
    void userSelected(MouseEvent e) {
        String username = userListView.getSelectionModel().getSelectedItem();
        if (username != null) {
            // پیدا کردن شی User از نام کاربری
            User u = adminController.getAllUsers().stream()
                    .filter(x -> x.getUsername().equals(username))
                    .findFirst().orElse(null);
            if (u != null) {
                String type = (u instanceof PremiumUser) ? "Premium" : "Normal";
                String detail = String.format(
                        "ID: %d\nUsername: %s\nFull Name: %s\nEmail: %s\nPhone: %s\nType: %s\nBalance: %.2f",
                        u.getId(), u.getUsername(), u.getFullName(),
                        u.getEmail(), u.getPhone(), type, u.getBalance()
                );
                outputLabel.setText(detail);
            }
        }
    }

    @FXML
    void contentSelected(MouseEvent e) {
        String name = contentListView.getSelectionModel().getSelectedItem();
        if (name != null) {
            Content c = adminController.getAllContents().stream()
                    .filter(x -> x.getName().equals(name))
                    .findFirst().orElse(null);
            if (c != null) {
                String detail = String.format(
                        "Name: %s\nDescription: %s\nLikes: %d\nViews: %d",
                        c.getName(), c.getDescription(),
                        c.getLikes(), c.getViews()
                );
                outputLabel.setText(detail);
            }
        }
    }

    // ========== کمکی‌ها ==========

    private void refreshUserList() {
        ObservableList<String> names = FXCollections.observableArrayList();
        adminController.getAllUsers().forEach(u -> names.add(u.getUsername()));
        userListView.setItems(names);
    }

    private void refreshContentList() {
        ObservableList<String> names = FXCollections.observableArrayList();
        adminController.getAllContents().forEach(c -> names.add(c.getName()));
        contentListView.setItems(names);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }

    private void updateBarChartWithChannels(List<Channel> top) {
        barChart.getData().clear();
        barChart.getXAxis().setTickLabelRotation(45);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Subscribers");
        top.forEach(ch -> series.getData().add(
                new XYChart.Data<>(ch.getChannelName(), ch.getSubscribersList().size())
        ));
        barChart.getData().add(series);
    }

    private void updateBarChartWithContents(List<Content> top) {
        barChart.getData().clear();
        barChart.getXAxis().setTickLabelRotation(45);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Likes");
        top.forEach(ct -> series.getData().add(
                new XYChart.Data<>(ct.getName(), ct.getLikes())
        ));
        barChart.getData().add(series);
    }
}
