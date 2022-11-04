package edu.unibw.sse.madn.clientKomm;

import edu.unibw.sse.madn.ansicht.RaumauswahlUpdaten;
import edu.unibw.sse.madn.ansicht.SpielUpdaten;
import edu.unibw.sse.madn.ansicht.WarteraumUpdaten;
import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {
    RaumauswahlUpdaten raumauswahl;
    WarteraumUpdaten warteraum;
    SpielUpdaten spiel;

    protected ClientCallbackImpl() throws RemoteException {
    }

    @Override
    public void raeumeUpdaten(Warteraeume warteraeume) throws RemoteException {
        if (raumauswahl == null) return;
        raumauswahl.raeumeUpdaten(warteraeume);
    }

    @Override
    public void warteraumNamenUpdaten(String[] namen) throws RemoteException {
        if (warteraum == null) return;
        warteraum.warteraumNamenUpdaten(namen);
    }

    @Override
    public void spielStartet(String design) throws RemoteException {
        if (warteraum == null) return;
        warteraum.spielStartet(design);
    }

    @Override
    public void spielfeldUpdaten(FeldBesetztStatus[] feld, int[] geandert) throws RemoteException {
        if (spiel == null) return;
        spiel.spielfeldUpdaten(feld, geandert);
    }

    @Override
    public void spielNamenUpdaten(String[] namen) throws RemoteException {
        if (spiel == null) return;
        spiel.spielNamenUpdaten(namen);
    }

    @Override
    public void aktuellenSpielerSetzen(int spieler) throws RemoteException {
        if (spiel == null) return;
        spiel.aktuellenSpielerSetzen(spieler);
    }

    @Override
    public void wuerfelUpdaten(int wert) throws RemoteException {
        if (spiel == null) return;
        spiel.wuerfelUpdaten(wert);
    }

    @Override
    public void wuerfelnVorbei() throws RemoteException {
        if (spiel == null) return;
        spiel.wuerfelnVorbei();
    }

    @Override
    public void ziehenVorbei() throws RemoteException {
        if (spiel == null) return;
        spiel.ziehenVorbei();
    }

    @Override
    public void gifAnzeigen() throws RemoteException {
        if (spiel == null) return;
        spiel.gifAnzeigen();
    }

    @Override
    public void spielVorbei(SpielStatistik statistik) throws RemoteException {
        if (spiel == null) return;
        spiel.spielVorbei(statistik);
    }
}
