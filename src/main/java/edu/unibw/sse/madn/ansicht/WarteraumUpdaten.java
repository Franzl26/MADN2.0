package edu.unibw.sse.madn.ansicht;

public interface WarteraumUpdaten {
    /**
     * Aktuelle Spieler/Bot-Namen in Warteraum übermitteln
     *
     * @param namen Namen der Spieler/Bots in Lobby
     */
    void warteraumNamenUpdaten(String[] namen);

    /**
     * mitteilen, dass Spiel gestartet wurde
     *
     * @param design ausgewähltes Design
     */
    void spielStartet(String design);
}
