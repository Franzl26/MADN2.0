package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

public interface AnClientSendenRaumauswahl {
    // Raumauswahl

    /**
     * Alle Warteräume übermitteln
     *
     * @param sitzung     Sitzung des Clients
     * @param warteraeume Alle aktuell verfügbaren Warteräume
     * @return true: wenn gesendet, false: wenn keine Verbindung möglich
     */
    boolean raeumeUpdaten(Sitzung sitzung, Warteraeume warteraeume);


    // Warteraum

    /**
     * Aktuelle Spieler in Warteraum übermitteln
     *
     * @param sitzung Sitzung des Clients
     * @param namen   Namen der Spieler/Bots in Lobby
     * @return true: wenn gesendet, false: wenn keine Verbindung möglich
     */
    boolean warteraumNamenUpdaten(Sitzung sitzung, String[] namen);

    /**
     * mitteilen, dass Spiel gestartet wurde
     *
     * @param sitzung Sitzung des Clients
     * @param design  ausgewähltes Design
     * @return true: wenn gesendet, false: wenn keine Verbindung möglich
     */
    boolean spielStartet(Sitzung sitzung, String design);
}
