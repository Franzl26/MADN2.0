package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.benutzerVerwaltung.BenutzerZugang;
import edu.unibw.sse.madn.clientKomm.ClientCallback;
import edu.unibw.sse.madn.datenServer.SpielDesign;
import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import edu.unibw.sse.madn.spielLogik.Spiel;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.spielLogik.WuerfelnRueckgabe;
import edu.unibw.sse.madn.spielLogik.ZiehenRueckgabe;
import edu.unibw.sse.madn.warteraumverwaltung.Raumauswahl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SitzungImpl extends UnicastRemoteObject implements Sitzung {
    private final ClientCallback clientCallback;
    private final String benutzername;
    private final SpielDesign spielDesign;
    private final BenutzerZugang benutzerZugang;
    private final Raumauswahl raumauswahl;
    private final Spiel spiel;

    public SitzungImpl(ClientCallback clientCallback, String benutzername, SpielDesign spielDesign, BenutzerZugang benutzerZugang, Raumauswahl raumauswahl, Spiel spiel) throws RemoteException {
        super();
        this.clientCallback = clientCallback;
        this.benutzername = benutzername;
        this.spielDesign = spielDesign;
        this.benutzerZugang = benutzerZugang;
        this.raumauswahl = raumauswahl;
        this.spiel = spiel;
    }

    @Override
    public void abmelden() throws RemoteException {
        benutzerZugang.abmelden(benutzername);
    }

    @Override
    public String[] designListeHolen() throws RemoteException {
        return spielDesign.designListeHolen();
    }

    @Override
    public SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name) throws RemoteException {
        return spielDesign.spielfeldKonfigurationHolen(name);
    }

    @Override
    public boolean fuerWarteraumUpdatesAnmelden() {
        raumauswahl.fuerUpdatesAnmelden(this);
        return true;
    }

    @Override
    public boolean warteraumErstellen() throws RemoteException {
        return raumauswahl.warteraumErstellen(this);
    }

    @Override
    public boolean warteraumBeitreten(long raumId) throws RemoteException {
        return raumauswahl.warteraumBeitreten(this, raumId);
    }

    @Override
    public void warteraumVerlassen() throws RemoteException {
        raumauswahl.warteraumVerlassen(this);
    }

    @Override
    public boolean botHinzufuegen() throws RemoteException {
        return raumauswahl.botHinzufuegen(this);
    }

    @Override
    public boolean botEntfernen() throws RemoteException {
        return raumauswahl.botEntfernen(this);
    }

    @Override
    public boolean spielStarten() throws RemoteException {
        return raumauswahl.spielStarten(this);
    }

    @Override
    public void designAnpassen(String design) throws RemoteException {
        if (!spielDesign.existiertDesign(design)) return;
        raumauswahl.designAnpassen(this, design);
    }

    @Override
    public ZiehenRueckgabe figurZiehen(int von, int nach) throws RemoteException {
        return spiel.figurZiehen(this, von, nach);
    }

    @Override
    public WuerfelnRueckgabe wuerfeln() throws RemoteException {
        return spiel.wuerfeln(this);
    }

    @Override
    public SpielStatistik spielVerlassen() throws RemoteException {
        return spiel.spielVerlassen(this);
    }

    @Override
    public String benutzername() {
        return benutzername;
    }

    @Override
    public ClientCallback clientCallback() {
        return clientCallback;
    }
}
