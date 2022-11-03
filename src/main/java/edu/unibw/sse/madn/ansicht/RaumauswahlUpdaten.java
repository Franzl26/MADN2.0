package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

import java.rmi.RemoteException;

public interface RaumauswahlUpdaten {
    /**
     * Alle Warteräume übermitteln
     * @param warteraeume Alle aktuell verfügbaren Warteräume
     */
    void raeumeUpdaten(Warteraeume warteraeume);
}
