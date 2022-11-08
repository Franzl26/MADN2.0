package edu.unibw.sse.madn.app.server;

import edu.unibw.sse.madn.benutzerVerwaltung.BenutzerZugang;
import edu.unibw.sse.madn.benutzerVerwaltung.BenutzerZugangImpl;
import edu.unibw.sse.madn.datenServer.BenutzerDaten;
import edu.unibw.sse.madn.datenServer.BenutzerDatenImpl;
import edu.unibw.sse.madn.datenServer.SpielDesign;
import edu.unibw.sse.madn.datenServer.SpielDesignImpl;
import edu.unibw.sse.madn.serverKomm.AnClientSendenImpl;
import edu.unibw.sse.madn.serverKomm.ServerVerbindung;
import edu.unibw.sse.madn.serverKomm.ServerVerbindungImpl;
import edu.unibw.sse.madn.spielLogik.SpielImpl;
import edu.unibw.sse.madn.warteraumverwaltung.RaumauswahlImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerStart {
    public static void main(String[] args) {

        try {
            BenutzerDaten benutzerDaten = new BenutzerDatenImpl();
            SpielDesign spielDesign = new SpielDesignImpl();
            BenutzerZugang benutzerZugang = new BenutzerZugangImpl(benutzerDaten);
            RaumauswahlImpl raumauswahl = new RaumauswahlImpl();
            SpielImpl spiel = new SpielImpl(raumauswahl);
            raumauswahl.spielErstellenSetzen(spiel);
            ServerVerbindung serverVerbindung = new ServerVerbindungImpl(spielDesign, benutzerZugang, raumauswahl, spiel);
            AnClientSendenImpl anClientSenden = new AnClientSendenImpl();
            raumauswahl.kommunikationskanalSetzen(anClientSenden);
            spiel.kommunikationskanalSetzen(anClientSenden);


            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("//localhost/MADNLogin", serverVerbindung);
            System.out.println("Server gestartet");

        } catch (RemoteException | MalformedURLException e) {
            System.err.println("Server konnte nicht gestartet werden");
            e.printStackTrace();
        }
    }
}
