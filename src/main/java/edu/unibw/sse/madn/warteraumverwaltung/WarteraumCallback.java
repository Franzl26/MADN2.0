package edu.unibw.sse.madn.warteraumverwaltung;

import edu.unibw.sse.madn.serverKomm.Sitzung;

public interface WarteraumCallback {
    /**
     * Teilt Warteraumverwaltung mit, dass Spiel beendet ist
     */
    void spielBeendet();

    /**
     * Teilt Warteraumverwaltung mit, dass Spieler fertig ist/Spiel verlassen hat,
     * meldet ihn wieder in für Updates an
     * @param sitzung Sitzung
     */
    void spielerVerlassen(Sitzung sitzung);
}
