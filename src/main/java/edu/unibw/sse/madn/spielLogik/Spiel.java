package edu.unibw.sse.madn.spielLogik;

import edu.unibw.sse.madn.serverKomm.AnClientSendenSpiel;
import edu.unibw.sse.madn.serverKomm.Sitzung;

import java.io.Serializable;

public interface Spiel extends Serializable {
    /**
     * setzt Kanal mit dem der Server Nachrichten an den Client senden kann
     *
     * @param spiel AnClientSendenSpiel
     */
    void kommunikationskanalSetzen(AnClientSendenSpiel spiel);

    /**
     * Spielzug einreichen
     *
     * @param sitzung Sitzung
     * @param von     Feld von
     * @param nach    Feld nach
     */
    ZiehenRueckgabe figurZiehen(Sitzung sitzung, int von, int nach);

    /**
     * Würfeln
     *
     * @param sitzung Sitzung
     */
    WuerfelnRueckgabe wuerfeln(Sitzung sitzung);

    /**
     * Spiel Verlassen
     *
     * @param sitzung Sitzung
     * @return Spielstatistik oder null bei Fehler
     */
    SpielStatistik spielVerlassen(Sitzung sitzung);
}
