package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;

public interface AnClientSendenSpiel {
    // im Spiel
    /**
     * Spielfeld aktualisieren
     * @param sitzung Sitzung des Clients
     * @param feld Feld
     * @param geandert array der geänderten Felder, wenn null komplett neu zeichnen
     */
    boolean spielfeldUpdaten(Sitzung sitzung, FeldBesetztStatus[] feld, int[] geandert);

    /**
     * Namen der Spieler übermitteln
     * @param sitzung Sitzung des Clients
     * @param namen Namen
     */
    boolean spielNamenUpdaten(Sitzung sitzung, String[] namen);

    /**
     * aktuellen Spieler setzen
     * @param sitzung Sitzung des Clients
     * @param spieler Spieler
     */
    boolean aktuellenSpielerSetzen(Sitzung sitzung, int spieler);

    /**
     * Würfelwert übermitteln
     * @param sitzung Sitzung des Clients
     * @param wert Würfelwert
     */
    boolean wuerfelUpdaten(Sitzung sitzung, int wert);

    /**
     * Anzeigen, dass Zeit fürs Würfeln abgelaufen
     * @param sitzung Sitzung des Clients
     */
    boolean wuerfelnVorbei(Sitzung sitzung);

    /**
     * Anzeigen, dass Zeit fürs Ziehen abgelaufen
     * @param sitzung Sitzung des Clients
     */
    boolean ziehenVorbei(Sitzung sitzung);

    /**
     * Gif Anzeigen, wenn Zeit für Ziehen fortgeschritten
     * @param sitzung Sitzung des Clients
     */
    boolean gifAnzeigen(Sitzung sitzung);

    /**
     * Teilt Client mit, dass Spiel vorbei und übermittelt ihm Spielstatistik
     * @param sitzung Sitzung des Clients
     * @param statistik Spielstatistik
     */
    boolean spielVorbei(Sitzung sitzung, SpielStatistik statistik);
}
