module com.example.groupingdemo2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.a4 to javafx.fxml;
    exports com.example.a4;
}