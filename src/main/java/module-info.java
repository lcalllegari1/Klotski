module application.klotski {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.klotski to javafx.fxml;
    opens application.klotski.Model to javafx.fxml;
    opens application.klotski.View to javafx.fxml;
    opens application.klotski.Controller to javafx.fxml;

    exports application.klotski;
    exports application.klotski.Model;
    exports application.klotski.View;
    exports application.klotski.Controller;
}