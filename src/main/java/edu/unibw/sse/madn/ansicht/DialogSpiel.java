package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.ClientKomm;
import edu.unibw.sse.madn.datenClient.SpielfeldKonfiguration;
import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.spielLogik.WuerfelnRueckgabe;
import edu.unibw.sse.madn.spielLogik.ZiehenRueckgabe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;

import static edu.unibw.sse.madn.ansicht.Querschnitt.drawBoardAll;
import static edu.unibw.sse.madn.ansicht.Querschnitt.drawBoardSingleFieldAll;

public class DialogSpiel extends AnchorPane implements SpielUpdaten {
    private final SpielfeldKonfiguration config;
    private final GraphicsContext gcBoard;
    private final GraphicsContext gcDice;
    private final GraphicsContext gcName;
    private final Canvas gifCanvas;
    private final MediaView gifView;
    private final ClientKomm komm;
    private final FigurZiehen figurZiehen = new FigurZiehen();
    private FeldBesetztStatus[] brettStatus = new FeldBesetztStatus[72];
    private String[] namen = new String[0];
    private int aktiverSpieler = 0;

    public DialogSpiel(ClientKomm komm, String design) {
        this.komm = komm;
        komm.spielUpdaterSetzen(this);
        Arrays.fill(brettStatus, FeldBesetztStatus.FELD_LEER);

        config = Querschnitt.spielfeldKonfigurationLaden(komm, design);

        setBackground(Background.fill(Color.LIGHTSLATEGRAY));
        Canvas nameCanvas = new Canvas(980, 50);
        gcName = nameCanvas.getGraphicsContext2D();

        Canvas diceCanvas = new Canvas(100, 100);
        gcDice = diceCanvas.getGraphicsContext2D();
        diceCanvas.setOnMouseClicked(e -> {
            WuerfelnRueckgabe ret = komm.wuerfeln();
            switch (ret) { // todo texte
                case WUERFELN_NICHT_DRAN -> Meldungen.zeigeInformation("", "");
                case WUERFELN_FALSCHE_PHASE -> Meldungen.zeigeInformation("", "");
                case WUERFELN_ERFOLGREICH, WUERFELN_KEIN_ZUG_MOEGLICH -> {
                }
                case WUERFELN_VERBINDUNG_ABGEBROCHEN -> {
                    Meldungen.kommunikationAbgebrochen();
                    System.exit(0);
                }
            }
        });

        Canvas boardCanvas = new Canvas(500, 500);
        gcBoard = boardCanvas.getGraphicsContext2D();
        boardCanvas.setOnMouseClicked(e -> figurZiehen.onMouseClickedField(e.getX(), e.getY()));

        gifCanvas = new Canvas(360, 360);
        GraphicsContext gcGif = gifCanvas.getGraphicsContext2D();
        gcGif.setFill(Color.LIGHTSLATEGRAY);
        gcGif.fillRect(0, 0, 360, 360);

        gifView = new MediaView();
        gifView.setFitWidth(360);
        gifView.setFitHeight(360);

        Button spielVerlassenButton = new Button("Spiel verlassen");
        spielVerlassenButton.addEventHandler(ActionEvent.ACTION, e -> spielVerlassen());

        AnchorPane.setLeftAnchor(nameCanvas, 10.0);
        AnchorPane.setTopAnchor(nameCanvas, 10.0);
        AnchorPane.setLeftAnchor(diceCanvas, 10.0);
        AnchorPane.setBottomAnchor(diceCanvas, 210.0);
        AnchorPane.setLeftAnchor(boardCanvas, 120.0);
        AnchorPane.setBottomAnchor(boardCanvas, 10.0);
        AnchorPane.setRightAnchor(gifCanvas, 10.0);
        AnchorPane.setTopAnchor(gifCanvas, 90.0);
        AnchorPane.setRightAnchor(gifView, 10.0);
        AnchorPane.setTopAnchor(gifView, 90.0);
        AnchorPane.setRightAnchor(spielVerlassenButton, 10.0);
        AnchorPane.setBottomAnchor(spielVerlassenButton, 10.0);

        getChildren().addAll(nameCanvas, boardCanvas, diceCanvas, gifCanvas, gifView, spielVerlassenButton);
    }

    public void drawDice(int number) {
        gcDice.drawImage(config.wuerfelBild(number), 0, 0, 100, 100);
    }

    public void drawBoard(FeldBesetztStatus[] board) {
        drawBoardAll(gcBoard, config, board);
    }

    public void drawBoardSingleField(FeldBesetztStatus state, int i, boolean highlight) {
        drawBoardSingleFieldAll(gcBoard, config, state, i, highlight);
    }

    public void drawNames(String[] players, int turn) {
        gcName.setFill(Color.LIGHTSLATEGRAY);
        gcName.fillRect(0, 0, 980, 50);
        gcName.setFont(Font.font(40));
        gcName.setFill(Color.BLACK);
        for (int i = 0; i < players.length; i++) {
            gcName.drawImage(config.figurBild((players.length == 2 ? (i == 0 ? 0 : 2) : i)), 5 + 245 * i, 5, 40, 40);
            String p = players[i];
            gcName.fillText(p, i * 245 + 50, 40, 190);
            if (i == turn) gcName.fillRect(i * 245 + 5, 46, 235, 47);
        }
    }

    public void showGif() {
        gifView.toFront();
        Media media = Querschnitt.zufaelligesGif();
        MediaPlayer player = new MediaPlayer(media);
        gifView.setMediaPlayer(player);
        player.setAutoPlay(true);
        player.setCycleCount(50);
    }

    public void hideGif() {
        gifCanvas.toFront();
    }

    private void spielVerlassen() {
        if (Meldungen.frageBestaetigung("Spiel verlassen", "Willst du das Spiel wirklich verlassen?")) {
            SpielStatistik statistik = komm.spielVerlassen();
            if (statistik == null) {
                Meldungen.kommunikationAbgebrochen();
                System.exit(0);
            }
            DialogSpielstatistik.dialogSpielstatistikStart(komm, statistik);
            ((Stage) getScene().getWindow()).close();
        }
    }

    private void setOnClose() {
        getScene().getWindow().setOnCloseRequest(e -> {
            spielVerlassen();
            e.consume();
        });
    }

    public static void dialogSpielStart(ClientKomm komm, String design) {
        DialogSpiel root = new DialogSpiel(komm, design);
        Scene scene = new Scene(root, 1000, 600);
        Stage stage = new Stage();

        stage.setTitle("Mensch Ärgere dich nicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        root.setOnClose();
    }

    @Override
    public void spielfeldUpdaten(FeldBesetztStatus[] feld, int[] geandert) {
        brettStatus = feld;
        Platform.runLater(() -> {
            hideGif();
            drawDice(0);
            if (geandert == null) drawBoard(feld);
            else for (int i : geandert) if (i != -1) drawBoardSingleField(feld[i], i, false);
        });
    }

    @Override
    public void spielNamenUpdaten(String[] namen) {
        this.namen = namen;
        Platform.runLater(() -> drawNames(namen, aktiverSpieler));
    }

    @Override
    public void aktuellenSpielerSetzen(int spieler) {
        aktiverSpieler = spieler;
        Platform.runLater(() -> drawNames(namen, aktiverSpieler));
    }

    @Override
    public void wuerfelUpdaten(int wert) {
        Platform.runLater(() -> drawDice(wert));
    }

    @Override
    public void wuerfelnVorbei() {
        Platform.runLater(() -> Meldungen.zeigeInformation("Zeit abgelaufen", "Deine Zeit zum Würfeln ist abgelaufen, der Server hat für dich gewürfelt und gezogen."));
    }

    @Override
    public void ziehenVorbei() {
        Platform.runLater(() -> Meldungen.zeigeInformation("Zeit abgelaufen", "Deine Zeit zum Ziehen ist abgelaufen, der Server hat für dich gezogen."));
    }

    @Override
    public void gifAnzeigen() {
        Platform.runLater(this::showGif);
    }

    @Override
    public void spielVorbei(SpielStatistik statistik) {
        Platform.runLater(() -> {
            DialogSpielstatistik.dialogSpielstatistikStart(komm, statistik);
            ((Stage) getScene().getWindow()).close();
        });
    }

    private class FigurZiehen {
        private boolean highlighted = false;
        private int highlightedField = -1;

        public void onMouseClickedField(double x, double y) {
            int clickRadius = config.klickRadius();
            for (int i = 0; i < 72; i++) {
                int[] koords = config.koordinatenVonFeld(i);
                if (Math.hypot(x - koords[0], y - koords[1]) < clickRadius - 2) {
                    //System.out.println("Field clicked: " + i);
                    if (!highlighted && brettStatus[i] == FeldBesetztStatus.FELD_LEER) return;
                    if (!highlighted) {
                        highlighted = true;
                        highlightedField = i;
                        drawBoardSingleField(brettStatus[i], i, true);
                    } else {
                        if (highlightedField == i) {
                            highlighted = false;
                            highlightedField = -1;
                            drawBoardSingleField(brettStatus[i], i, false);
                        } else {
                            ZiehenRueckgabe ret = komm.figurZiehen(highlightedField, i);
                            switch (ret) { // todo texte
                                case ZIEHEN_BESTRAFT ->
                                        Meldungen.zeigeInformation("Prio Zug missachtet", "Du hast einen PrioZug missachtet, die entsprechende Figur wurde geschlagen");
                                case ZIEHEN_NICHT_DRAN ->
                                        Meldungen.zeigeInformation("Nicht an der Reihe", "Du bist nicht dran du Pflaume");
                                case ZIEHEN_ERFOLGREICH -> {
                                }
                                case ZIEHEN_ZUG_FEHLERHAFT ->
                                        Meldungen.zeigeInformation("Fehlerhafter Zug", "Fehlerhafter Zug. Bitte nochmal setzen.");
                                case ZIEHEN_NICHT_GEWUERFELT ->
                                        Meldungen.zeigeInformation("Nicht gewürfelt", "Du hast noch nicht gewürfelt");
                                case ZIEHEN_VERBINDUNG_ABGEBROCHEN -> {
                                    Meldungen.kommunikationAbgebrochen();
                                    System.exit(0);
                                }
                            }
                            highlighted = false;
                            highlightedField = -1;
                        }
                    }
                    return;
                }
            }
        }
    }
}
