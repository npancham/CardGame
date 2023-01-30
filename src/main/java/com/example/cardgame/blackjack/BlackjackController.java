package com.example.cardgame.blackjack;

import com.example.cardgame.Card;
import com.example.cardgame.CardToImageUrl;
import com.example.cardgame.Deck;
import com.example.cardgame.MainApplication;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlackjackController {
    @FXML
    private BorderPane rootPane;
    @FXML
    private GridPane mainGrid;
    @FXML
    private GridPane dealerGrid;
    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane runner;
    @FXML
    private Button playButton;
    @FXML
    private Button minus1Button;
    @FXML
    private Button plus1Button;
    @FXML
    private Button minus5Button;
    @FXML
    private Button plus5Button;
    @FXML
    private Button minus25Button;
    @FXML
    private Button plus25Button;
    @FXML
    private Button minus100Button;
    @FXML
    private Button plus100Button;
    @FXML
    private Button minus500Button;
    @FXML
    private Button plus500Button;
    @FXML
    private Button startRoundButton;
    @FXML
    private Button standButton;
    @FXML
    private Button hitButton;
    @FXML
    private Button doubleButton;
    @FXML
    private Button splitButton;
    @FXML
    private Button surrenderButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button quitButton;
    @FXML
    private Label dealerLabel;
    @FXML
    private Label playerLabel;
    @FXML
    private Label infoLabel1;
    @FXML
    private Label infoLabel2;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label wagerLabel;

    private Rectangle2D screenBounds;
    private Stage stage;
    private Stage helpStage;
    private HelpController helpController;
    private Deck deck;
    private Dealer dealer;
    private Player player;
    private double balance;
    private double wager;
    private Card splitCardLeft;
    private Card splitCardRight;
    private List<Card> splitHandLeft;
    private List<Card> splitHandRight;
    private int splitHandLeftValue;
    private int splitHandRightValue;
    private Phase phase;

    public enum Phase {
        BET,
        INITIAL,
        FOLLOW_UP,
        DOUBLE_DOWN,
        SPLIT1,
        SPLIT2,
        END
    }

    public void initialize() {
        // A reference to the stage can only be gotten after initialize() has finished. Therefore, a change listener is
        // added to the window property of the scene, which perceives when the window corresponding to the scene
        // transits from null to the window shown on screen. However, to get the scene, first a change listener needs to
        // be added to the scene property of the root node.
        rootPane.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene oldScene, Scene newScene) {
                if (newScene != null) {
                    newScene.windowProperty().addListener(new ChangeListener<Window>() {
                        @Override
                        public void changed(ObservableValue<? extends Window> observableValue, Window oldWindow, Window newWindow) {
                            if (newWindow != null) {
                                stage = (Stage) rootPane.getScene().getWindow();
                            }
                        }
                    });
                }
            }
        });

        screenBounds = Screen.getPrimary().getVisualBounds();

        infoLabel1.setWrapText(true);
        infoLabel2.setWrapText(true);

        // During play, the style class initialized here will change depending on the outcome of a round
        infoLabel1.getStyleClass().add("");
        infoLabel2.getStyleClass().add("");

        setDimensions();
        prepareGame();
    }

    private void setDimensions() {
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        rootPane.setMinSize(screenWidth * 0.95, screenHeight * 0.95);
        rootPane.setMaxSize(screenWidth * 0.95, screenHeight * 0.95);
        rootPane.setPrefSize(screenWidth * 0.95, screenHeight * 0.95);

        mainGrid.setPrefWidth(rootPane.getPrefWidth());
        mainGrid.setMinWidth(rootPane.getMinWidth());
        mainGrid.setMaxWidth(rootPane.getMaxWidth());

        runner.setPrefWidth(mainGrid.getPrefWidth() - mainGrid.getPadding().getLeft() - mainGrid.getPadding().getRight());
        runner.setMinWidth(runner.getPrefWidth());
        runner.setMaxWidth(runner.getPrefWidth());
        runner.setMinHeight(screenHeight / 4);
        runner.setMaxHeight(screenHeight / 2);

        playerGrid.setPrefWidth(runner.getPrefWidth());
        playerGrid.setMinWidth(runner.getMinWidth());
        playerGrid.setMaxWidth(runner.getMaxWidth());
        playerGrid.setPrefHeight(screenHeight / 4);
        playerGrid.setMinHeight(screenHeight / 4);
        playerGrid.setMaxHeight(screenHeight / 3);

        dealerGrid.setMinSize(playerGrid.getMinWidth(), playerGrid.getMinHeight());
        dealerGrid.setMaxSize(playerGrid.getMaxWidth(), playerGrid.getMaxHeight());
        dealerGrid.setPrefSize(playerGrid.getPrefWidth(), playerGrid.getPrefHeight());
    }

    private void prepareGame() {
        deck = new Deck();
        deck.shuffle();
        dealer = new Dealer();
        player = new Player();
        balance = 1000;
        wager = 100;
    }

    private void setupHelpStageAndController() throws IOException {
        helpStage = new Stage();
        helpStage.initOwner(stage);
        helpStage.setResizable(false);
        helpStage.getIcons().add(new Image("file:src/main/resources/com/example/cardgame/images/questionMark.png"));
        helpStage.setTitle("Help");
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("blackjack/help-view.fxml"));
        double helpSceneWidth = screenBounds.getWidth() * 0.56;
        double helpSceneHeight = screenBounds.getHeight() * 0.72;
        Scene scene = new Scene(fxmlLoader.load(), helpSceneWidth, helpSceneHeight);
        helpStage.setScene(scene);
        helpController = fxmlLoader.getController();
    }

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    @FXML
    protected void onNewGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("blackjack/blackjack-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void onQuit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Card Games");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void onWhatIsBlackjack() throws IOException {
        if (helpStage == null || helpController == null) {
            setupHelpStageAndController();
        }

        helpController.showWhatIsBlackjack();
        helpStage.show();
    }

    @FXML
    protected void onCardValues() throws IOException {
        if (helpStage == null || helpController == null) {
            setupHelpStageAndController();
        }

        helpController.showCardValues();
        helpStage.show();
    }

    @FXML
    protected void onGlossary() throws IOException {
        if (helpStage == null || helpController == null) {
            setupHelpStageAndController();
        }

        helpController.showGlossary();
        helpStage.show();
    }

    @FXML
    protected void onPlay() {
        phase = Phase.BET;

        playButton.setVisible(false);
        startRoundButton.setVisible(true);

        toggleWagerButtonsVisibility(true);
        disableInvalidWagerButtons();

        printInfo("Place your wager");
        dealerLabel.setText("");
        playerLabel.setText("");

        printBalance();
        printWager();
        clearCardsFromGrid();
    }

    @FXML
    protected void onPlusMinus(ActionEvent event) {
        String sourceId = ((Button) event.getSource()).getId();
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(sourceId);

        if (matcher.find()) {
            int iStart = matcher.start();
            int iEnd = matcher.end();

            String operation = sourceId.substring(0, iStart);
            int amount = Integer.parseInt(sourceId.substring(iStart, iEnd));

            if (operation.equals("plus")) {
                wager += amount;
            } else if (operation.equals("minus")) {
                wager -= amount;
            }

            printBalance();
            printWager();
            disableInvalidWagerButtons();
        }
    }

    @FXML
    protected void onStartRound() {
        phase = Phase.INITIAL;
        balance -= wager;

        startRoundButton.setVisible(false);

        toggleWagerButtonsVisibility(false);
        toggleDecisionButtonsVisibility(true);

        doubleButton.setDisable(false);
        surrenderButton.setDisable(false);

        printInfo("");
        printBalance();
        printWager();

        playRound();
    }

    @FXML
    protected void onStand() {
        if (phase != Phase.SPLIT1 && phase != Phase.SPLIT2) {
            concludeRound();
        } else if (phase == Phase.SPLIT1) {
            concludeSplitRound(1);
        } else if (phase == Phase.SPLIT2) {
            concludeSplitRound(2);
        }
    }

    @FXML
    protected void onHit() {
        doubleButton.setDisable(true);
        splitButton.setDisable(true);
        surrenderButton.setDisable(true);

        player.hit(deck);

        if (phase != Phase.SPLIT1 && phase != Phase.SPLIT2) {
            phase = Phase.FOLLOW_UP;
            ImageView iv = displayNextCardFor(player);
            TranslateTransition tt = translateCardEffect(iv, player);
            tt.play();

            tt.setOnFinished(event -> {
                printHandValueFor(player);

                if (player.hasTwentyOne() || player.hasBusted()) {
                    concludeRound();
                }
            });
        } else if (phase == Phase.SPLIT1) {
            splitHandLeftValue = player.evaluateHand();
            ImageView iv = displayNextSplitCard(1);
            TranslateTransition tt = translateCardEffect(iv, player);
            tt.play();

            tt.setOnFinished(event -> {
                printSplitHandValue(1);

                if (player.hasTwentyOne() || player.hasBusted()) {
                    concludeSplitRound(1);
                }
            });
        } else if (phase == Phase.SPLIT2) {
            splitHandRightValue = player.evaluateHand();
            ImageView iv = displayNextSplitCard(2);
            TranslateTransition tt = translateCardEffect(iv, player);
            tt.play();

            tt.setOnFinished(event -> {
                printSplitHandValue(2);

                if (player.hasTwentyOne() || player.hasBusted()) {
                    concludeSplitRound(2);
                }
            });
        }
    }

    @FXML
    protected void onDouble() {
        phase = Phase.DOUBLE_DOWN;
        balance -= wager;

        printBalance();
        printWager();

        player.hit(deck);

        ImageView iv = displayNextCardFor(player);
        TranslateTransition tt = translateCardEffect(iv, player);
        tt.play();

        tt.setOnFinished(event -> {
            printHandValueFor(player);
            concludeRound();
        });
    }

    @FXML
    protected void onSplit() {
        phase = Phase.SPLIT1;
        balance -= wager;

        printBalance();
        printWager();

        doubleButton.setDisable(true);
        splitButton.setDisable(true);
        surrenderButton.setDisable(true);

        splitHandLeft = new ArrayList<>();
        splitHandRight = new ArrayList<>();

        splitCardLeft = player.getHand().get(0);
        splitCardRight = player.getHand().get(1);

        playSplitRound(1);
    }

    @FXML
    protected void onSurrender() {
        balance += (wager / 2);

        toggleDecisionButtonsVisibility(false);

        playButton.setVisible(true);

        printInfo("Surrendered");
        printBalance();

        phase = Phase.END;
    }

    public void playRound() {
        // With a single deck, the maximum number of cards a player's hand can contain is 11. For the dealer, this
        // number is 10. Therefore, a new deck needs to be used if the cards remaining in the current deck is less than
        // 21. This number needs to be altered when multiple decks are implemented for play.
        if (deck.getCards().size() < 21) {
            deck = new Deck();
            deck.shuffle();
        }

        dealer.newHand(deck);
        displayNthCardFor(dealer, 0);
        printHandValueFor(dealer);

        player.newHand(deck);
        displayNthCardFor(player, 0);
        displayNthCardFor(player, 1);
        printHandValueFor(player);

        doubleButton.setDisable(wager > balance);
        splitButton.setDisable(!player.getHand().get(0).getRank().equals(player.getHand().get(1).getRank()) || wager > balance);

        if (player.hasBlackjack()) {
            blackjackGlow(player);
            concludeRound();
        }
    }

    public void concludeRound() {
        dealer.resolvePlay(deck);

        List<Card> dealerHand = dealer.getHand();

        // A sequential transition is used to show each card added to the dealer's hand one at a time, instead of
        // simultaneously
        SequentialTransition st = new SequentialTransition();

        // The dealer's first card (index 0) does not need to be shown, since it is already on screen
        for (int i = 1; i < dealerHand.size(); i++) {
            ImageView iv = displayNthCardFor(dealer, i);
            st.getChildren().add(translateCardEffect(iv, dealer));
        }

        st.play();

        BlackjackRules.Outcome outcome = BlackjackRules.evaluateOutcome(dealer, player);

        double payout = payout(outcome);

        toggleDecisionButtonsVisibility(false);

        // Only proceed with the results on screen when all the dealer's cards have been shown
        st.setOnFinished(event -> {
            printHandValueFor(dealer);

            if (dealer.hasBlackjack()) {
                blackjackGlow(dealer);
            }

            printOutcome(outcome);
            printBalance(payout);

            balance += payout;

            if (balance >= 1) {
                playButton.setVisible(true);
            } else {
                gameOver();
            }
        });

        phase = Phase.END;
    }

    private void playSplitRound(int splitRound) {
        if (splitRound == 1) {
            playerGrid.getChildren().clear();
            player.newHand(splitCardLeft, deck);
            splitHandLeftValue = player.evaluateHand();
        } else if (splitRound == 2) {
            darkenLeftHand();
            player.newHand(splitCardRight, deck);
            splitHandRightValue = player.evaluateHand();
        } else {
            return;
        }

        displayNthSplitCard(0, splitRound);
        displayNthSplitCard(1, splitRound);
        printSplitHandValue(splitRound);

        if (player.hasTwentyOne()) {
            concludeSplitRound(splitRound);
        }
    }

    private void concludeSplitRound(int splitRound) {
        if (splitRound == 1) {
            splitCardLeft = null;
            splitHandLeft = new ArrayList<>(player.getHand());
            phase = Phase.SPLIT2;
            playSplitRound(2);
        } else if (splitRound == 2) {
            splitCardRight = null;
            splitHandRight = new ArrayList<>(player.getHand());
            dealer.resolvePlay(deck);

            List<Card> dealerHand = dealer.getHand();

            // A sequential transition is used to show each card added to the dealer's hand one at a time, instead of
            // simultaneously
            SequentialTransition st = new SequentialTransition();

            // The dealer's first card (index 0) does not need to be shown, since it is already on screen
            for (int i = 1; i < dealerHand.size(); i++) {
                ImageView iv = displayNthCardFor(dealer, i);
                st.getChildren().add(translateCardEffect(iv, dealer));
            }

            st.play();

            player.setHand(splitHandLeft);
            BlackjackRules.Outcome outcome1 = BlackjackRules.evaluateSplitOutcome(dealer, player);

            player.setHand(splitHandRight);
            BlackjackRules.Outcome outcome2 = BlackjackRules.evaluateSplitOutcome(dealer, player);

            double payout1 = payout(outcome1);
            double payout2 = payout(outcome2);

            toggleDecisionButtonsVisibility(false);

            // Only proceed with the results on screen when all the dealer's cards have been shown
            st.setOnFinished(event -> {
                printHandValueFor(dealer);

                if (dealer.hasBlackjack()) {
                    blackjackGlow(dealer);
                }

                printSplitOutcome(outcome1, outcome2);
                printBalance(payout1, payout2);

                balance += payout1;
                balance += payout2;

                if (balance >= 1) {
                    playButton.setVisible(true);
                } else {
                    gameOver();
                }
            });

            phase = Phase.END;
        }
    }

    private void gameOver() {
        PauseTransition pt = new PauseTransition(Duration.millis(2500));
        pt.play();
        pt.setOnFinished(event -> {
            infoLabel1.setText("Game Over");
            infoLabel1.getStyleClass().set(1, "gameOverMessage");
            infoLabel2.setText("");
            infoLabel2.getStyleClass().set(1, "gameOverMessage");

            dealerLabel.setText("");
            playerLabel.setText("");

            wager = 0;
            printWager();

            clearCardsFromGrid();

            newGameButton.setVisible(true);
            quitButton.setVisible(true);
        });
    }

    private double payout(BlackjackRules.Outcome outcome) {
        double payout = 0;
        int doubleModifier = (phase == Phase.DOUBLE_DOWN) ? 2 : 1;

        if (outcome == BlackjackRules.Outcome.PLAYER_BLACKJACK) {
            payout = (wager + wager * 3 / 2) * doubleModifier;
        } else if (outcome == BlackjackRules.Outcome.DEALER_BUST || outcome == BlackjackRules.Outcome.PLAYER_NORMAL_WIN) {
            payout = (wager + wager) * doubleModifier;
        } else if (outcome == BlackjackRules.Outcome.PUSH) {
            payout = wager * doubleModifier;
        }

        return payout;
    }

    private String[] outcomeToString(BlackjackRules.Outcome outcome) {
        if (outcome == BlackjackRules.Outcome.PLAYER_BLACKJACK) {
            return new String[] {"Blackjack!", "You win!"};
        } else if (outcome == BlackjackRules.Outcome.DEALER_BUST) {
            return new String[]{"Dealer exceeded 21.", "You win!"};
        } else if (outcome == BlackjackRules.Outcome.PLAYER_NORMAL_WIN) {
            return new String[]{"You win!", ""};
        } else if (outcome == BlackjackRules.Outcome.DEALER_BLACKJACK) {
            return new String[]{"Dealer Blackjack!", "You lose..."};
        } else if (outcome == BlackjackRules.Outcome.PLAYER_BUST) {
            return new String[]{"You exceeded 21.", "You lose..."};
        } else if (outcome == BlackjackRules.Outcome.DEALER_NORMAL_WIN) {
            return new String[]{"You lose...", ""};
        } else if (outcome == BlackjackRules.Outcome.PUSH) {
            return new String[]{"It's a tie.", ""};
        }

        return null;
    }

    private void printInfo(String text) {
        infoLabel1.setText(text);
        infoLabel1.getStyleClass().set(1, "");
        infoLabel2.setText("");
        infoLabel2.getStyleClass().set(1, "");
    }

    private void printBalance() {
        String text;
        String formattedBalance = String.format(Locale.US, "%.2f", balance);

        if (phase == Phase.BET) {
            String formattedWager = String.format(Locale.US, "%.2f", wager);
            text = "Balance: " + formattedBalance + " (-" + formattedWager + ")";
        } else  {
            text = "Balance: " + formattedBalance;
        }

        balanceLabel.setText(text);
    }

    private void printBalance(double payout) {
        String text;
        String formattedBalance = String.format(Locale.US, "%.2f", balance);

        if (payout == 0) {
            text = "Balance: " + formattedBalance;
        } else if (phase == Phase.DOUBLE_DOWN) {
            String times = Character.toString(215);
            String formattedHalfPayout = String.format(Locale.US, "%.2f", (payout / 2));
            text = "Balance: " + formattedBalance + " (+" + formattedHalfPayout + times + "2)";
        } else {
            String formattedPayout = String.format(Locale.US, "%.2f", payout);
            text = "Balance: " + formattedBalance + " (+" + formattedPayout + ")";
        }

        balanceLabel.setText(text);
    }

    private void printBalance(double payout1, double payout2) {
        String text;
        String FormattedBalance = String.format(Locale.US, "%.2f", balance);

        if (payout1 == 0 && payout2 == 0) {
            text = "Balance: " + FormattedBalance;
        } else {
            String formattedPayout1 = String.format(Locale.US, "%.2f", payout1);
            String formattedPayout2 = String.format(Locale.US, "%.2f", payout2);
            text = "Balance: " + FormattedBalance + " (+" + formattedPayout1 + ")" + " (+" + formattedPayout2 + ")";
        }

        balanceLabel.setText(text);
    }

    private void printWager() {
        String text;
        String formattedWager = String.format(Locale.US, "%.2f", this.wager);

        if (phase == Phase.DOUBLE_DOWN) {
            String times = Character.toString(215);
            text = "Wager: " + formattedWager + " (" + times + "2)";
        } else if (phase == Phase.SPLIT1) {
            text = "Wager: " + formattedWager + " + " + formattedWager;
        } else {
            text = "Wager: " + formattedWager;
        }

        wagerLabel.setText(text);
    }

    private void printOutcome(BlackjackRules.Outcome outcome) {
        String[] strings = outcomeToString(outcome);
        infoLabel1.setText(strings[0]);
        infoLabel2.setText(strings[1]);

        if (outcome == BlackjackRules.Outcome.PLAYER_BLACKJACK) {
            infoLabel1.getStyleClass().set(1, "playerBlackjackMessage");
            infoLabel2.getStyleClass().set(1, "playerBlackjackMessage");
        } else if (outcome == BlackjackRules.Outcome.DEALER_BLACKJACK) {
            infoLabel1.getStyleClass().set(1, "dealerBlackjackMessage");
            infoLabel2.getStyleClass().set(1, "dealerBlackjackMessage");
        } else {
            infoLabel1.getStyleClass().set(1, "");
            infoLabel2.getStyleClass().set(1, "");
        }
    }

    private void printSplitOutcome(BlackjackRules.Outcome outcome1, BlackjackRules.Outcome outcome2) {
        String[] strings1 = outcomeToString(outcome1);
        String[] strings2 = outcomeToString(outcome2);

        if (outcome1 == BlackjackRules.Outcome.DEALER_BLACKJACK) {
            infoLabel1.setText(strings1[0]);
            infoLabel2.setText(strings1[1]);
            infoLabel1.getStyleClass().set(1, "dealerBlackjackMessage");
            infoLabel2.getStyleClass().set(1, "dealerBlackjackMessage");
        } else {
            infoLabel1.setText("Left hand: " + strings1[0] + "\n" + strings1[1]);
            infoLabel2.setText("Right hand: " + strings2[0] + "\n" + strings2[1]);
            infoLabel1.getStyleClass().set(1, "splitOutcomeMessage");
            infoLabel2.getStyleClass().set(1, "splitOutcomeMessage");
        }
    }

    private void printHandValueFor(Participant participant) {
        if (participant.getClass().equals(Player.class)) {
            playerLabel.setText("You: " + participant.evaluateHand());
        } else if (participant.getClass().equals(Dealer.class)) {
            dealerLabel.setText("Dealer: " + participant.evaluateHand());
        }
    }

    private void printSplitHandValue(int splitRound) {
        String text = "You: left hand: " + splitHandLeftValue;

        if (splitRound == 2) {
            text += ", right hand: " + splitHandRightValue;
        }

        playerLabel.setText(text);
    }

    private ImageView displayCardOnGrid(Card card, Participant participant, int gridPaneColumn) {
        GridPane gridPane;
        double imageRotation;

        if (participant.getClass().equals(Player.class)) {
            gridPane = playerGrid;
            imageRotation = 0;
        } else if (participant.getClass().equals(Dealer.class)) {
            gridPane = dealerGrid;
            imageRotation = 180;
        } else {
            return null;
        }

        Image image = new Image(CardToImageUrl.toUrl(card));
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitHeight(gridPane.getPrefHeight());
        iv.setPreserveRatio(true);
        iv.setRotate(imageRotation);

        GridPane.setConstraints(iv, gridPaneColumn, 0);
        GridPane.setHalignment(iv, HPos.CENTER);
        gridPane.getChildren().add(iv);

        return iv;
    }

    private ImageView displayNextCardFor(Participant participant) {
        int handSize = participant.getHand().size();
        return displayNthCardFor(participant, handSize - 1);
    }

    private ImageView displayNthCardFor(Participant participant, int n) {
        if (n >= 0) {
            if (participant.getClass().equals(Player.class)) {
                return displayCardOnGrid(participant.getHand().get(n), participant, n + 1);
            } else if (participant.getClass().equals(Dealer.class)) {
                return displayCardOnGrid(participant.getHand().get(n), participant, 8 - n);
            }
        }

        return null;
    }

    private ImageView displaySplitCardOnGrid(Card card, int gridPaneColumn, int splitRound) {
        // Where displayCardOnGrid() simply adds a card's Image to an ImageView in the corresponding GridPane cell,
        // displaySplitCardOnGrid() creates a StackPane in the cell as an intermediate step, which allows for showing
        // both hands after splitting in a single GridPane row
        StackPane stackPane = new StackPane();
        GridPane.setConstraints(stackPane, gridPaneColumn, 0);
        GridPane.setHalignment(stackPane, HPos.CENTER);
        playerGrid.getChildren().add(stackPane);

        Image image = new Image(CardToImageUrl.toUrl(card));
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitHeight(playerGrid.getPrefHeight());
        iv.setPreserveRatio(true);
        iv.setRotate(0);

        if (splitRound == 1) {
            StackPane.setMargin(iv, new Insets(0, 0, 25, 0));
            iv.setTranslateX(-10);
        } else if (splitRound == 2) {
            StackPane.setMargin(iv, new Insets(25, 0, 0, 0));
            iv.setTranslateX(10);
        }

        stackPane.getChildren().add(iv);

        return iv;
    }

    private ImageView displayNextSplitCard(int splitRound) {
        int handSize = player.getHand().size();
        return displayNthSplitCard(handSize - 1, splitRound);
    }

    private ImageView displayNthSplitCard(int n, int splitRound) {
        return displaySplitCardOnGrid(player.getHand().get(n), n + 1, splitRound);
    }

    private void clearCardsFromGrid() {
        dealerGrid.getChildren().clear();
        playerGrid.getChildren().clear();
    }

    private void toggleDecisionButtonsVisibility(boolean visible) {
        standButton.setVisible(visible);
        hitButton.setVisible(visible);
        doubleButton.setVisible(visible);
        splitButton.setVisible(visible);
        surrenderButton.setVisible(visible);
    }

    private void toggleWagerButtonsVisibility(boolean visible) {
        minus1Button.setVisible(visible);
        plus1Button.setVisible(visible);
        minus5Button.setVisible(visible);
        plus5Button.setVisible(visible);
        minus25Button.setVisible(visible);
        plus25Button.setVisible(visible);
        minus100Button.setVisible(visible);
        plus100Button.setVisible(visible);
        minus500Button.setVisible(visible);
        plus500Button.setVisible(visible);
    }

    private void disableInvalidWagerButtons() {
        plus1Button.setDisable(balance - wager < 1);
        plus5Button.setDisable(balance - wager < 5);
        plus25Button.setDisable(balance - wager < 25);
        plus100Button.setDisable(balance - wager < 100);
        plus500Button.setDisable(balance - wager < 500);

        minus1Button.setDisable(wager < 1);
        minus5Button.setDisable(wager < 5);
        minus25Button.setDisable(wager < 25);
        minus100Button.setDisable(wager < 100);
        minus500Button.setDisable(wager < 500);

        startRoundButton.setDisable(wager <= 0 || wager > balance);
    }

    private TranslateTransition translateCardEffect(ImageView iv, Participant participant) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), iv);
        double amountY = 150;

        if (participant.getClass().equals(Dealer.class)) {
            amountY *= -1;
        }

        tt.setFromY(iv.getY() - amountY);
        tt.setByY(amountY);

        return tt;
    }

    private void darkenCard(ImageView iv) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.8);
        iv.setEffect(colorAdjust);
    }

    private void darkenLeftHand() {
        for (Node node: playerGrid.getChildren()) {
            if (node.getClass().equals(StackPane.class)) {
                StackPane stackPane = (StackPane) node;

                if (stackPane.getChildren().get(0).getClass().equals(ImageView.class)) {
                    ImageView iv = (ImageView) stackPane.getChildren().get(0);
                    darkenCard(iv);
                }
            }
        }
    }

    private void cardGlow(ImageView iv, Color color) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(color);
        borderGlow.setWidth(100);
        borderGlow.setHeight(100);
        iv.setEffect(borderGlow);
    }

    private void blackjackGlow(Participant participant) {
        GridPane gridPane;
        Color color;

        if (participant.getClass().equals(Player.class)) {
            gridPane = playerGrid;
            color = Color.GOLD;
        } else if (participant.getClass().equals(Dealer.class)) {
            gridPane = dealerGrid;
            color = Color.SILVER;
        } else {
            return;
        }

        List<Node> childNodes = gridPane.getChildren();

        for (Node node: childNodes) {
            if (node.getClass().equals(ImageView.class)) {
                ImageView iv = (ImageView) node;
                cardGlow(iv, color);
            }
        }
    }
}
