package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.AllgemeinerReturnWert;
import edu.unibw.sse.madn.clientKomm.ClientKomm;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DialogAnmelden extends AnchorPane{
    public DialogAnmelden(ClientKomm komm) {
        TextField serverTextField = new TextField("localhost");
        serverTextField.setPromptText("Server-IP-Adresse");
        serverTextField.setPrefWidth(280);

        TextField usernameTextField = new TextField("Frank");
        usernameTextField.setPromptText("Benutzername");
        usernameTextField.setPrefWidth(280);

        PasswordField passwordField = new PasswordField();
        passwordField.setText("abcdef1!");
        passwordField.setPromptText("Passwort");
        passwordField.setPrefWidth(280);

        Button registrierenButton = new Button("Registrieren");
        registrierenButton.addEventHandler(ActionEvent.ACTION, e -> {
            DialogRegistrieren.dialogRegistrierenStart(komm);
            ((Stage) getScene().getWindow()).close();
        });
        Button abbrechenButton = new Button("Abbrechen");
        abbrechenButton.addEventHandler(ActionEvent.ACTION, e -> System.exit(0));
        Button anmeldenButton = new Button("Anmelden");
        anmeldenButton.addEventHandler(ActionEvent.ACTION, e -> {
            AllgemeinerReturnWert ret = komm.anmelden(serverTextField.getText(), usernameTextField.getText(), passwordField.getText());
            switch (ret) {
                case RET_ERFOLGREICH -> {
                    DialogRaumauswahl.dialogRaumauswahlStart(komm);
                    ((Stage) getScene().getWindow()).close();
                }
                case RET_FEHLER -> Meldungen.zeigeInformation("Login-Daten fehlerhaft", "Die Logindaten sind falsch.");
                case RET_VERBINDUNG_ABGEBROCHEN -> Meldungen.zeigeInformation("Server nicht gefunden", "Unter der angegebenen IP-Adresse konnte kein Server gefunden werden.");
            }
        });

        AnchorPane.setLeftAnchor(serverTextField, 10.0);
        AnchorPane.setTopAnchor(serverTextField, 10.0);
        AnchorPane.setLeftAnchor(usernameTextField, 10.0);
        AnchorPane.setTopAnchor(usernameTextField, 40.0);
        AnchorPane.setLeftAnchor(passwordField, 10.0);
        AnchorPane.setTopAnchor(passwordField, 70.0);
        AnchorPane.setLeftAnchor(registrierenButton, 10.0);
        AnchorPane.setBottomAnchor(registrierenButton, 10.0);
        AnchorPane.setRightAnchor(abbrechenButton, 90.0);
        AnchorPane.setBottomAnchor(abbrechenButton, 10.0);
        AnchorPane.setRightAnchor(anmeldenButton, 10.0);
        AnchorPane.setBottomAnchor(anmeldenButton, 10.0);

        getChildren().addAll(abbrechenButton, serverTextField, usernameTextField, passwordField, registrierenButton, anmeldenButton);
    }

    public static void dialogAnmeldenStart(ClientKomm  komm) {
        DialogAnmelden root = new DialogAnmelden(komm);
        Scene scene = new Scene(root, 300, 150);
        Stage stage = new Stage();

        stage.setTitle("Anmelden");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
