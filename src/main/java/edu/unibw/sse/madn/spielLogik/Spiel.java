package edu.unibw.sse.madn.spielLogik;

import edu.unibw.sse.madn.serverKomm.Sitzung;

public interface Spiel {
    /**
     * Spielzug einreichen
     * @param sitzung Sitzung
     * @param von Feld von
     * @param nach Feld nach
     */
    ZiehenRueckgabe figurZiehen(Sitzung sitzung, int von, int nach);

    /**
     * Würfeln
     * @param sitzung Sitzung
     */
    WuerfelnRueckgabe wuerfeln(Sitzung sitzung);

    /**
     * Spiel Verlassen
     * @param sitzung Sitzung
     * @return Spielstatistik
     */
    SpielStatistik spielVerlassen(Sitzung sitzung);
}
