package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.AllgemeinerReturnWert;
import edu.unibw.sse.madn.clientKomm.ClientKomm;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DialogWarteraum extends AnchorPane implements WarteraumUpdaten {
    private final GraphicsContext gcName;
    private final ClientKomm komm;
    public DialogWarteraum(ClientKomm komm) {
        this.komm = komm;

        Canvas nameCanvas = new Canvas(200, 150);
        gcName = nameCanvas.getGraphicsContext2D();

        Button botAddButton = new Button("Bot hinzufügen");
        botAddButton.setPrefWidth(100);
        botAddButton.addEventHandler(ActionEvent.ACTION, e -> {
            AllgemeinerReturnWert ret = komm.botHinzufuegen();
            switch (ret) {
                case RET_ERFOLGREICH -> {}
                case RET_FEHLER -> Meldungen.zeigeInformation("Warteraum voll", "Der Warteraum ist bereits voll, du kannst keinen Bot hinzufügen.");
                case RET_VERBINDUNG_ABGEBROCHEN -> {
                    Meldungen.kommunikationAbgebrochen();
                    System.exit(-1);
                }
            }
        });
        Button botRemoveButton = new Button("Bot entfernen");
        botRemoveButton.setPrefWidth(100);
        botRemoveButton.addEventHandler(ActionEvent.ACTION, e -> {
            AllgemeinerReturnWert ret = komm.botEntfernen();
            switch (ret) {
                case RET_ERFOLGREICH -> {}
                case RET_FEHLER -> Meldungen.zeigeInformation("Kein Bot im Warteraum", "Es befindet sich kein Bot im Warteraum, der entfernt werden kann.");
                case RET_VERBINDUNG_ABGEBROCHEN -> {
                    Meldungen.kommunikationAbgebrochen();
                    System.exit(-1);
                }
            }
        });
        Button designButton = new Button("Spieldesign auswählen");
        designButton.setPrefWidth(140);
        designButton.addEventHandler(ActionEvent.ACTION, e -> {
            String[] liste = komm.designListeHolen();
            if (liste == null) {
                Meldungen.kommunikationAbgebrochen();
                System.exit(-1);
            }
            DialogDesignauswahl.dialogDesignauswahlStart(liste, komm);
        });
        Button startButton = new Button("Spiel starten");
        startButton.setPrefWidth(140);
        startButton.addEventHandler(ActionEvent.ACTION, e -> {
            AllgemeinerReturnWert ret = komm.spielStarten();
            switch (ret) {
                case RET_ERFOLGREICH -> {}
                case RET_FEHLER -> Meldungen.zeigeInformation("Nicht genug Spieler im Warteraum", "Es sind weniger als 2 Spieler im Warteraum, dass Spiel kann nicht gestartet werden.");
                case RET_VERBINDUNG_ABGEBROCHEN -> {
                    Meldungen.kommunikationAbgebrochen();
                    System.exit(-1);
                }
            }
        });
        Button exitButton = new Button("Warteraum verlassen");
        exitButton.setPrefWidth(140);
        exitButton.addEventHandler(ActionEvent.ACTION, e -> verlassen());

        komm.warteraumUpdaterSetzen(this);

        AnchorPane.setLeftAnchor(nameCanvas, 10.0);
        AnchorPane.setTopAnchor(nameCanvas, 10.0);
        AnchorPane.setLeftAnchor(botAddButton, 10.0);
        AnchorPane.setBottomAnchor(botAddButton, 10.0);
        AnchorPane.setLeftAnchor(botRemoveButton, 120.0);
        AnchorPane.setBottomAnchor(botRemoveButton, 10.0);
        AnchorPane.setRightAnchor(exitButton, 10.0);
        AnchorPane.setBottomAnchor(exitButton, 10.0);
        AnchorPane.setRightAnchor(startButton, 10.0);
        AnchorPane.setBottomAnchor(startButton, 40.0);
        AnchorPane.setRightAnchor(designButton, 10.0);
        AnchorPane.setBottomAnchor(designButton, 100.0);

        getChildren().addAll(nameCanvas, botAddButton, botRemoveButton, designButton, startButton, exitButton);
    }

    private void setOnClose() {
        getScene().getWindow().setOnCloseRequest(e -> {
            verlassen();
            e.consume();
        });
    }

    private void verlassen() {
        if (Meldungen.frageBestaetigung("Warteraum verlassen", "Willst du den Warteraum wirklich verlassen und zur Raumauswahl zurückkehren?")) {
            komm.warteraumVerlassen();
            DialogRaumauswahl.dialogRaumauswahlStart(komm);
            ((Stage) getScene().getWindow()).close();
        }
    }

    public void drawNames(String[] names) {
        gcName.clearRect(0, 0, 200, 150);
        gcName.setLineWidth(1.0);
        gcName.setFont(Font.font(20));
        gcName.setFill(Color.BLACK);
        for (int i = 0; i < names.length; i++) {
            gcName.fillText(names[i], 5, i * 30 + 20, 190);
        }
    }

    public void spielStarten(String design) {
        Platform.runLater(() -> {
            DialogSpiel.dialogSpielStart(komm, design);
            ((Stage) getScene().getWindow()).close();
        });
    }

    public static void dialogWarteraumStart(ClientKomm komm) {
        DialogWarteraum root = new DialogWarteraum(komm);
        Scene scene = new Scene(root, 400, 200);
        Stage stage = new Stage();

        stage.setTitle("Warteraum");
        stage.setScene(scene);
        stage.setResizable(false);
        root.setOnClose();
        stage.show();
    }

    @Override
    public void warteraumNamenUpdaten(String[] namen) {
        Platform.runLater(() -> drawNames(namen));
    }

    @Override
    public void spielStartet(String design) {
        spielStarten(design);
    }
}
