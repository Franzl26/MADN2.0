package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.ClientKomm;
import edu.unibw.sse.madn.datenClient.SpielfeldKonfiguration;
import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class DialogDesignauswahl extends AnchorPane {
    private boolean last = false;

    public DialogDesignauswahl(String[] designs, ClientKomm komm) {

        setBackground(Background.fill(Color.LIGHTSLATEGRAY));
        Canvas boardCanvas = new Canvas(500, 500);
        GraphicsContext gcBoard = boardCanvas.getGraphicsContext2D();
        Canvas diceCanvas = new Canvas(100, 100);
        GraphicsContext gcDice = diceCanvas.getGraphicsContext2D();

        gcBoard.setFill(Color.LIGHTSLATEGRAY);
        gcBoard.fillRect(0, 0, 500, 500);
        gcDice.setFill(Color.LIGHTSLATEGRAY);
        gcDice.fillRect(0, 0, 100, 100);

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setPrefWidth(80);
        cancelButton.addEventHandler(ActionEvent.ACTION, e -> abbrechen());
        ChoiceBox<String> boardChoice = new ChoiceBox<>();
        boardChoice.setOnHiding(e -> {
            if (last) {
                last = false;
                return;
            }
            last = true;
            SpielfeldKonfiguration config = Querschnitt.spielfeldKonfigurationLaden(komm, boardChoice.getValue());
            FeldBesetztStatus[] state = new FeldBesetztStatus[72];
            Arrays.fill(state,0,4,FeldBesetztStatus.FELD_SPIELER1);
            Arrays.fill(state,4,8,FeldBesetztStatus.FELD_SPIELER2);
            Arrays.fill(state,8,12,FeldBesetztStatus.FELD_SPIELER3);
            Arrays.fill(state,12,16,FeldBesetztStatus.FELD_SPIELER4);
            Arrays.fill(state,16,72,FeldBesetztStatus.FELD_LEER);
            Querschnitt.drawBoardAll(gcBoard, config, state);
            gcDice.setFill(Color.LIGHTSLATEGRAY);
            gcDice.fillRect(0, 0, 100, 100);
            gcDice.drawImage(config.wuerfelBild(5), 0, 0, 100, 100);
        });
        boardChoice.setPrefWidth(170);
        boardChoice.getItems().addAll(designs);
        boardChoice.setValue("Standard");

        Button selectButton = new Button("Auswählen");
        selectButton.setPrefWidth(80);
        selectButton.addEventHandler(ActionEvent.ACTION, e -> {
            komm.designAnpassen(boardChoice.getValue());
            ((Stage) getScene().getWindow()).close();
        });

        AnchorPane.setLeftAnchor(boardCanvas, 10.0);
        AnchorPane.setTopAnchor(boardCanvas, 10.0);
        AnchorPane.setLeftAnchor(diceCanvas, 540.0);
        AnchorPane.setTopAnchor(diceCanvas, 50.0);
        AnchorPane.setRightAnchor(selectButton, 10.0);
        AnchorPane.setBottomAnchor(selectButton, 10.0);
        AnchorPane.setRightAnchor(cancelButton, 100.0);
        AnchorPane.setBottomAnchor(cancelButton, 10.0);
        AnchorPane.setRightAnchor(boardChoice, 10.0);
        AnchorPane.setBottomAnchor(boardChoice, 150.0);

        getChildren().addAll(boardCanvas, diceCanvas, cancelButton, selectButton, boardChoice);
        boardChoice.hide();
    }

    private void setOnClose() {
        getScene().getWindow().setOnCloseRequest(e -> abbrechen());
    }

    private void abbrechen() {
        if (Meldungen.frageBestaetigung("Designauswahl verlassen?","Möchtest du die Designauswahl wirklich verlassen")) {
            ((Stage)getScene().getWindow()).close();
        }
    }

    public static void dialogDesignauswahlStart(String[] designs, ClientKomm komm) {
        DialogDesignauswahl root = new DialogDesignauswahl(designs, komm);
        Scene scene = new Scene(root, 700, 520);
        Stage stage = new Stage();

        stage.setTitle("Design Auswählen");
        stage.setScene(scene);
        stage.setResizable(false);
        root.setOnClose();
        stage.show();
    }
}
