package edu.unibw.sse.madn.app.client;

import edu.unibw.sse.madn.ansicht.DialogAnmelden;
import edu.unibw.sse.madn.ansicht.Querschnitt;
import edu.unibw.sse.madn.clientKomm.ClientKomm;
import edu.unibw.sse.madn.clientKomm.ClientKommImpl;
import edu.unibw.sse.madn.datenClient.DatenClient;
import edu.unibw.sse.madn.datenClient.DatenClientImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientStart2 extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        DatenClient datenClient = new DatenClientImpl();
        ClientKomm clientKomm = new ClientKommImpl();
        Querschnitt.datenClientSetzen(datenClient);
        DialogAnmelden.dialogAnmeldenStart(clientKomm);
    }
}
