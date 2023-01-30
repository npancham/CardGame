package com.example.cardgame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainController {
    @FXML
    private Pane rootLayout;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label infoLabel;
    @FXML
    private Label infoLabelMirrored;
    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private ImageView iv3;
    @FXML
    private ImageView iv4;

    private Rectangle2D screenBounds;

    public void initialize() {
        screenBounds = Screen.getPrimary().getVisualBounds();

        setDimensions();
        drawTable();
        showCardsOnBackground();

        String info = "Select a game" + "\n" + "to play";
        infoLabel.setText(info);
        infoLabelMirrored.setText(info);
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    @FXML
    protected void onBlackjackButtonClick() throws IOException {
        Stage stage = (Stage) rootLayout.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("blackjack/blackjack-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setDimensions() {
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        rootLayout.setMinWidth(screenWidth * 0.95);
        rootLayout.setMaxWidth(screenWidth * 0.95);
        rootLayout.setPrefWidth(screenWidth * 0.95);
        rootLayout.setMinHeight(screenHeight * 0.95);
        rootLayout.setMaxHeight(screenHeight * 0.95);
        rootLayout.setPrefHeight(screenHeight * 0.95);
    }

    private void drawTable() {
        double radiusX = screenBounds.getWidth() * 0.45;
        double radiusY = screenBounds.getHeight() * 0.5;

        Path path = new Path();
        path.setFill(Color.SEAGREEN);
        path.setStroke(Color.SADDLEBROWN);
        path.setStrokeWidth(10);
        path.setFillRule(FillRule.EVEN_ODD);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(radiusX);
        moveTo.setY(0);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(-radiusX);
        arcTo.setY(0);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(radiusX);
        moveTo2.setY(0);

        HLineTo hLineTo = new HLineTo();
        hLineTo.setX(-radiusX);

        path.getElements().add(moveTo);
        path.getElements().add(arcTo);
        path.getElements().add(moveTo2);
        path.getElements().add(hLineTo);

        stackPane.getChildren().add(path);
        path.setTranslateY(screenBounds.getHeight() / 2 - radiusY / 2);
        path.toBack();
    }

    private void showCardsOnBackground() {
        // Show four random cards on the starting screen.
        // Let all four card suits be represented by one of these cards.
        // Let the first card be an Ace, the second a face card, and the third and fourth a numbered card.
        List<Card.Suit> allSuits = Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.CLUBS);
        Collections.shuffle(allSuits);

        Card.Suit suit1 = allSuits.get(0);
        Card.Suit suit2 = allSuits.get(1);
        Card.Suit suit3 = allSuits.get(2);
        Card.Suit suit4 = allSuits.get(3);

        Random random = new Random();

        List<Card.Rank> faceRanks = Arrays.asList(Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING);
        List<Card.Rank> numberedRanks = Arrays.asList(Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE,
                Card.Rank.SIX, Card.Rank.SEVEN, Card.Rank.EIGHT, Card.Rank.NINE, Card.Rank.TEN);

        Card.Rank rank1 = Card.Rank.ACE;
        Card.Rank rank2 = faceRanks.get(random.nextInt(faceRanks.size()));
        Card.Rank rank3 = numberedRanks.get(random.nextInt(numberedRanks.size()));
        Card.Rank rank4 = numberedRanks.get(random.nextInt(numberedRanks.size()));

        iv1.setImage(new Image(CardToImageUrl.toUrl(new Card(suit1, rank1))));
        iv2.setImage(new Image(CardToImageUrl.toUrl(new Card(suit2, rank2))));
        iv3.setImage(new Image(CardToImageUrl.toUrl(new Card(suit3, rank3))));
        iv4.setImage(new Image(CardToImageUrl.toUrl(new Card(suit4, rank4))));

        // Show the cards in their desired position and orientation.
        iv1.getTransforms().add(new Translate(350, 0));
        iv1.getTransforms().add(new Rotate(10, Rotate.Z_AXIS));
        iv1.getTransforms().add(new Rotate(20, Rotate.Y_AXIS));
        iv1.getTransforms().add(new Rotate(0, Rotate.X_AXIS));

        iv2.getTransforms().add(new Translate(450, 300));
        iv2.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
        iv2.getTransforms().add(new Rotate(60, Rotate.Y_AXIS));
        iv2.getTransforms().add(new Rotate(30, Rotate.Z_AXIS));

        iv3.getTransforms().add(new Translate(425, -100));
        iv3.getTransforms().add(new Rotate(15, Rotate.Z_AXIS));
        iv3.getTransforms().add(new Rotate(20, Rotate.Y_AXIS));
        iv3.getTransforms().add(new Rotate(-15, Rotate.X_AXIS));

        iv4.getTransforms().add(new Translate(545, -175));
        iv4.getTransforms().add(new Rotate(25, Rotate.Z_AXIS));
        iv4.getTransforms().add(new Rotate(15, Rotate.Y_AXIS));
        iv4.getTransforms().add(new Rotate(-45, Rotate.X_AXIS));
    }
}