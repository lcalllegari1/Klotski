module com.klotski {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.swing;


    opens com.klotski to javafx.fxml;
    opens com.klotski.Model to javafx.fxml;
    opens com.klotski.View to javafx.fxml;
    opens com.klotski.Controller to javafx.fxml;

    exports com.klotski;
    exports com.klotski.Model;
    exports com.klotski.View;
    exports com.klotski.Controller;
}