package com.example.cardgame.blackjack;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebView;

import java.util.Objects;

public class HelpController {
    public static final String WHAT_FILE = "what-html.html";
    public static final String VALUES_FILE = "values-html.html";
    public static final String GLOSSARY_FILE = "glossary-html.html";

    public static final Tooltip WHAT_TOOLTIP = new Tooltip("What is Blackjack?");
    public static final Tooltip VALUES_TOOLTIP = new Tooltip("Card values");
    public static final Tooltip GLOSSARY_TOOLTIP = new Tooltip("Glossary");

    @FXML
    public WebView webView;

    @FXML
    public Button previousButton;

    @FXML
    public Button nextButton;

    @FXML
    protected void onPrevious() {
        String url = webView.getEngine().getLocation();
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        if (fileName.equals(VALUES_FILE)) {
            showWhatIsBlackjack();
        } else if (fileName.equals(GLOSSARY_FILE)) {
            showCardValues();
        }
    }

    @FXML
    protected void onNext() {
        String url = webView.getEngine().getLocation();
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        if (fileName.equals(WHAT_FILE)) {
            showCardValues();
        } else if (fileName.equals(VALUES_FILE)) {
            showGlossary();
        }
    }

    public void showWhatIsBlackjack() {
        loadHtml(WHAT_FILE);

        previousButton.setDisable(true);
        nextButton.setDisable(false);
        nextButton.setTooltip(VALUES_TOOLTIP);
    }

    public void showCardValues() {
        loadHtml(VALUES_FILE);

        previousButton.setDisable(false);
        previousButton.setTooltip(WHAT_TOOLTIP);
        nextButton.setDisable(false);
        nextButton.setTooltip(GLOSSARY_TOOLTIP);
    }

    public void showGlossary() {
        loadHtml(GLOSSARY_FILE);

        previousButton.setDisable(false);
        previousButton.setTooltip(VALUES_TOOLTIP);
        nextButton.setDisable(true);
    }

    private void loadHtml(String file) {
        webView.getEngine().load(Objects.requireNonNull(getClass().getResource(file)).toString());
    }
}
