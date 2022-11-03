package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

public interface AnClientSendenRaumauswahl {
    // Raumauswahl
    /**
     * Alle Warteräume übermitteln
     * @param sitzung Sitzung des Clients
     * @param warteraeume Alle aktuell verfügbaren Warteräume
     */
    boolean raeumeUpdaten(Sitzung sitzung, Warteraeume warteraeume);


    // Warteraum
    /**
     * Aktuelle Spieler in Warteraum übermitteln
     * @param sitzung Sitzung des Clients
     * @param namen Namen der Spieler/Bots in Lobby
     */
    boolean warteraumNamenUpdaten(Sitzung sitzung, String[] namen);

    /**
     * mitteilen, dass Spiel gestartet wurde
     * @param sitzung Sitzung des Clients
     * @param design ausgewähltes Design
     */
    boolean spielStartet(Sitzung sitzung, String design);
}
