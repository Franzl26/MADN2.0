module ourproject.srccode {
    requires javafx.controls;
    requires javafx.media;
    requires java.rmi;

    exports edu.unibw.sse.madn.ansicht;
    exports edu.unibw.sse.madn.benutzerVerwaltung;
    exports edu.unibw.sse.madn.datenClient;
    exports edu.unibw.sse.madn.datenServer;
    exports edu.unibw.sse.madn.clientKomm;
    exports edu.unibw.sse.madn.serverKomm;
    exports edu.unibw.sse.madn.spielLogik;
    exports edu.unibw.sse.madn.warteraumverwaltung;
    exports edu.unibw.sse.madn.app.client;

}