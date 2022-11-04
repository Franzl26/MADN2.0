package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

public interface RaumauswahlUpdaten {
    /**
     * Alle Warteräume übermitteln damit diese angezeigt werden können
     *
     * @param warteraeume Alle aktuell verfügbaren Warteräume
     */
    void raeumeUpdaten(Warteraeume warteraeume);
}
