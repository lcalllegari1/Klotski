module application.klotski {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.klotski to javafx.fxml;
    exports application.klotski;
    exports application.klotski.View;
    opens application.klotski.View to javafx.fxml;
}