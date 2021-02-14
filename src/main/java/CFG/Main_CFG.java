package CFG;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.util.Arrays;

import java.net.URL;
import java.util.*;


public class Main_CFG implements Initializable {

    @FXML public FlowPane dealer_pane, player_pane;
    @FXML public Button drawButton;
    String[] deck = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    int[] values = {2,3,4,5,6,7,8,9,10,10,10,10};
    public int dealer_score, player_score, card, dealerCounter;
    ArrayList<String> dealerDeck = new ArrayList<>();
    ArrayList<String> playerDeck = new ArrayList<>();
    List<Image> clubs = new ArrayList<>();
    List<Image> spades = new ArrayList<>();
    List<Image> hearts = new ArrayList<>();
    List<Image> diamonds = new ArrayList<>();
    List<Image> chosen = new ArrayList<>();
    List<Image> used;
    Random rnd = new Random();
    ImageView imgView = new ImageView();
    public void init() {
        used = new ArrayList<>();
        imgView.setPreserveRatio(true);
        imgView.setFitHeight(150);
        drawButton.setDisable(false);
        playerDeck.clear();
        dealerDeck.clear();
        dealerCounter = 0;
        dealer_pane.getChildren().clear();
        player_pane.getChildren().clear();
        dealer_score = 0;
        player_score = 0;

        for (int i = 0; i < 2; i++) {
            drawCardPlayer();
        }
        while(dealer_score <= 16) {
            drawCardDealer();
        }
        if (dealerDeck.size() == 2) {
            checkBlackJack();
        }
    }

    public void loadVisuals(){
        for (int i = 0; i < 13; i++) {
            clubs.add(new Image(this.getClass().getResource("/IMG/" + deck[i] + "C.png").toExternalForm()));
            spades.add(new Image(this.getClass().getResource("/IMG/" + deck[i] + "S.png").toExternalForm()));
            hearts.add(new Image(this.getClass().getResource("/IMG/" + deck[i] + "H.png").toExternalForm()));
            diamonds.add(new Image(this.getClass().getResource("/IMG/" + deck[i] + "D.png").toExternalForm()));
        }
    }

    @FXML
    public void drawCardPlayer(){
        card = rnd.nextInt(deck.length);
        if (deck[card] == "A") {
            player_score = checkAce(player_score);
        }
        else {
            player_score += values[card];
        }
        playerDeck.add(deck[card]);
        chosen = chooseColour();
        while(used.contains(chosen.get(card)))
        {
         chosen = chooseColour();
        }
        imgView = new ImageView(); imgView.setFitHeight(150); imgView.setPreserveRatio(true);
        imgView.setImage(chosen.get(card));
        used.add(chosen.get(card));
        player_pane.getChildren().add(imgView);
        if (player_score > 21) {
            end();
        }
    }

    public void checkBlackJack() {
        try {
            String[] blackValues = {"10", "J", "Q", "K"};
            int playerAce = 0;
            if (playerDeck.contains("A")) {
                playerAce++;
            }
            for (String s : blackValues) {
                if (playerDeck.contains(s)) {
                    playerAce++;
                    break;
                }
            }
            int dealerAce = 0;
            if (dealerDeck.contains("A")) {
                dealerAce++;
            }
            for (String s : blackValues) {
                if (dealerDeck.contains(s)) {
                    dealerAce++;
                    break;
                }
            }
            if (playerAce == 2 && dealerAce == 2) {
                drawButton.setDisable(true);
                Thread.sleep(2000);
                blackjackDialog(0);
            } else if (dealerAce == 2) {
                drawButton.setDisable(true);
                Thread.sleep(2000);
                blackjackDialog(-1);
            } else if (playerAce == 2) {
                drawButton.setDisable(true);
                Thread.sleep(2000);
                blackjackDialog(1);
            }
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void blackjackDialog(int x) {
        Dialog<String> dialog = new Dialog();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/CSS/BlackJack.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        dialog.setTitle("Blackjack");
        if (x == 0) {
            dialog.setContentText("BlackJack po obu stronach, remis");
        }
        else if (x == -1) {
            dialog.setContentText("BlackJack, przeciwnik wygrał");
        }
        else {
            dialog.setContentText("BlackJack, wygrałeś");
        }
        ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonType);
        dialog.showAndWait();
        init();
    }

    public List<Image> chooseColour(){
        List<Image> temp = new ArrayList<>();
        switch (rnd.nextInt(4)){
            case 0: temp = clubs;break;
            case 1: temp = hearts;break;
            case 2: temp = diamonds;break;
            case 3: temp = spades;break;
        }
        return temp;
    }

    public void drawCardDealer() {
        card = rnd.nextInt(deck.length);
        if (deck[card] == "A") {
            dealer_score = checkAce(dealer_score);
        } else {
            dealer_score += values[card];
        }
        dealerDeck.add(deck[card]);
        imgView = new ImageView(); imgView.setFitHeight(150); imgView.setPreserveRatio(true);
        if (dealerCounter < 2) {
            chosen = chooseColour();
            while (used.contains(chosen.get(card))) {
                chosen = chooseColour();
            }
            imgView.setImage(chosen.get(card));
            used.add(chosen.get(card));
            dealer_pane.getChildren().add(imgView);
            dealerCounter++;
        }

    }

    public int checkAce(int score) {
        if (score <=10) {
            score += 11;
        }
        else {
            score++;
        }
        return score;
    }

    public void end() {
        try {
            for (int i = 2; i < dealerDeck.size(); i++) {
                imgView = new ImageView();imgView.setFitHeight(150);imgView.setPreserveRatio(true);
                chosen = chooseColour();
                while (used.contains(chosen.get(Arrays.asList(deck).indexOf(dealerDeck.get(i))))) {
                    chosen = chooseColour();
                }
                imgView.setImage(chosen.get(Arrays.asList(deck).indexOf(dealerDeck.get(i))));
                dealer_pane.getChildren().add(imgView);
            }
            drawButton.setDisable(true);
            Thread.sleep(1000);
            Dialog<String> dialog = new Dialog();
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/CSS/BlackJack.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            dialog.setTitle("Koniec gry");
            if (player_score > 21 || (player_score < dealer_score && dealer_score <=21)) dialog.setContentText("Przegrałeś");
            else if (player_score == dealer_score) dialog.setContentText("Remis");
            else dialog.setContentText("Wygrałeś");
            ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonType);
            dialog.showAndWait();
            init();
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadVisuals();
        init();
    }
}
