package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;

public interface SpielUpdaten {
    /**
     * Spielfeld aktualisieren
     *
     * @param feld     Feld
     * @param geandert array der geänderten Felder, wenn null komplett neu zeichnen
     */
    void spielfeldUpdaten(FeldBesetztStatus[] feld, int[] geandert);

    /**
     * Namen der Spieler übermitteln
     *
     * @param namen Namen
     */
    void spielNamenUpdaten(String[] namen);

    /**
     * aktuellen Spieler übermitteln
     *
     * @param spieler Spieler
     */
    void aktuellenSpielerSetzen(int spieler);

    /**
     * Würfelwert übermitteln
     *
     * @param wert Würfelwert
     */
    void wuerfelUpdaten(int wert);

    /**
     * Anzeigen, dass Zeit fürs Würfeln abgelaufen
     */
    void wuerfelnVorbei();

    /**
     * Anzeigen, dass Zeit fürs Ziehen abgelaufen
     */
    void ziehenVorbei();

    /**
     * Gif Anzeigen, wenn Zeit für Ziehen fortgeschritten
     */
    void gifAnzeigen();

    /**
     * Teilt Client mit, dass Spiel vorbei und übermittelt ihm Spielstatistik
     *
     * @param statistik Spielstatistik
     */
    void spielVorbei(SpielStatistik statistik);
}
