module java_net {
    requires org.json;
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;
    
    exports mini_project to javafx.graphics, javafx.fxml;
    exports test to javafx.graphics, javafx.fxml;
    exports practice to javafx.graphics;
    exports MiniProject to javafx.graphics, javafx.fxml;
    opens test to javafx.fxml;
    opens mini_project to javafx.fxml;
    opens MiniProject to javafx.fxml;
}