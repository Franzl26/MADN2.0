package edu.unibw.sse.madn.spielLogik;

import edu.unibw.sse.madn.serverKomm.Sitzung;
import edu.unibw.sse.madn.warteraumverwaltung.WarteraumCallback;

public interface SpielErstellen {
    /**
     * Erstellt ein neues Spiel
     *
     * @param sitzungen alle Sitzungen von Nutzern in Lobby
     * @param bots      Anzahl Bots
     * @param spieler   Anzahl Spieler
     */
    void spielErstellen(Sitzung[] sitzungen, int bots, int spieler);
}
