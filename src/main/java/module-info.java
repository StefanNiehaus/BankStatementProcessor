module BankStatementProcessor {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens com.niehaus to javafx.fxml;
    exports com.niehaus;
}