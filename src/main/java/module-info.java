module com.example.youtube_graphic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.youtube_graphic to javafx.fxml;
    opens Controller to javafx.fxml;
    opens Model to javafx.fxml;

    exports com.example.youtube_graphic;
    exports Controller;
    exports Model;
}
