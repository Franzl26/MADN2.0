package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.AllgemeinerReturnWert;
import edu.unibw.sse.madn.clientKomm.ClientKomm;
import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;
import edu.unibw.sse.madn.warteraumverwaltung.Warteraum;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Collection;

public class DialogRaumauswahl extends AnchorPane implements RaumauswahlUpdaten {
    private final ListView<HBox> roomsList;
    private final ClientKomm komm;

    public DialogRaumauswahl(ClientKomm komm) {
        this.komm = komm;

        Canvas nameCanvas = new Canvas(300, 40);
        GraphicsContext gcName = nameCanvas.getGraphicsContext2D();
        gcName.setFont(Font.font(30));
        gcName.fillText(komm.benutzernamenHolen(), 5, 30, 300);

        roomsList = new ListView<>();
        roomsList.setPrefWidth(800);

        Button newGameButton = new Button("Neuen Warteraum erstellen");
        newGameButton.addEventHandler(ActionEvent.ACTION, e -> {
            AllgemeinerReturnWert ret = komm.warteraumErstellen();
            switch (ret) {
                case RET_FEHLER ->
                        Meldungen.zeigeInformation("Maximale Raumanzahl bereits erreicht", "Die maximale Anzahl an Warteräumen ist erreicht, es kann kein neuer erstellt werden.");
                case RET_ERFOLGREICH -> {
                    DialogWarteraum.dialogWarteraumStart(komm);
                    ((Stage) getScene().getWindow()).close();
                }
                case RET_VERBINDUNG_ABGEBROCHEN -> {
                    Meldungen.kommunikationAbgebrochen();
                    System.exit(-1);
                }
            }
        });
        Button exitButton = new Button("Beenden");
        exitButton.addEventHandler(ActionEvent.ACTION, e -> beenden(komm));

        AnchorPane.setLeftAnchor(nameCanvas, 10.0);
        AnchorPane.setTopAnchor(nameCanvas, 10.0);
        AnchorPane.setLeftAnchor(roomsList, 10.0);
        AnchorPane.setTopAnchor(roomsList, 60.0);
        AnchorPane.setLeftAnchor(newGameButton, 10.0);
        AnchorPane.setBottomAnchor(newGameButton, 10.0);
        AnchorPane.setRightAnchor(exitButton, 10.0);
        AnchorPane.setBottomAnchor(exitButton, 10.0);

        getChildren().addAll(nameCanvas, roomsList, newGameButton, exitButton);
        komm.raumauswahlUpdaterSetzen(this);
        AllgemeinerReturnWert retAn = komm.fuerWarteraumUpdatesAnmelden();
        switch (retAn) {
            case RET_ERFOLGREICH, RET_FEHLER -> {
            }
            case RET_VERBINDUNG_ABGEBROCHEN -> {
                Meldungen.kommunikationAbgebrochen();
                System.exit(-1);
            }
        }
    }

    public void displayRooms(Warteraeume raume) {
        roomsList.getItems().clear();
        Collection<Warteraum> rooms = raume.warteraeume();
        for (Warteraum r : rooms) {
            HBox box = new HBox();
            Canvas canvas = new Canvas(700, 30);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            Button button = new Button("Beitreten");
            button.addEventHandler(ActionEvent.ACTION, e -> {
                AllgemeinerReturnWert ret = komm.warteraumBeitreten(r.id());
                switch (ret) {
                    case RET_FEHLER ->
                            Meldungen.zeigeInformation("Warteraum ist voll", "Der Warteraum ist bereits voll, du kannst diesem leider nicht beitreten");
                    case RET_ERFOLGREICH -> {
                        DialogWarteraum.dialogWarteraumStart(komm);
                        ((Stage) getScene().getWindow()).close();
                    }
                    case RET_VERBINDUNG_ABGEBROCHEN -> {
                        Meldungen.kommunikationAbgebrochen();
                        System.exit(-1);
                    }
                }
            });

            String[] players = r.namen();
            StringBuilder build = new StringBuilder();
            build.append(players.length).append("/4    ");
            for (String player : players) {
                build.append(player).append("  ");
            }
            gc.setLineWidth(1.0);
            gc.setFont(Font.font(20));
            gc.fillText(build.toString(), 5, 20, 700);
            button.setAlignment(Pos.CENTER_RIGHT);
            box.getChildren().addAll(canvas, button);

            roomsList.getItems().add(box);
        }
    }

    private void setOnClose(ClientKomm komm) {
        getScene().getWindow().setOnCloseRequest(e -> {
            beenden(komm);
            e.consume();
        });

    }

    private void beenden(ClientKomm komm) {
        if (Meldungen.frageBestaetigung("Spiel beenden", "Willst du das Spiel wirklich beenden?")) {
            komm.abmelden();
            System.exit(0);
        }
    }

    public static void dialogRaumauswahlStart(ClientKomm komm) {
        DialogRaumauswahl root = new DialogRaumauswahl(komm);
        Scene scene = new Scene(root, 820, 500);
        Stage stage = new Stage();

        stage.setTitle("Raumauswahl");
        stage.setScene(scene);
        stage.setResizable(false);
        root.setOnClose(komm);
        stage.show();
    }

    @Override
    public void raeumeUpdaten(Warteraeume warteraeume) {
        Platform.runLater(() -> displayRooms(warteraeume));
    }
}
