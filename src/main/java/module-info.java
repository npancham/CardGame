module com.example.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.cardgame to javafx.fxml;
    opens com.example.cardgame.blackjack to javafx.fxml;
    exports com.example.cardgame;
    exports com.example.cardgame.blackjack;
}