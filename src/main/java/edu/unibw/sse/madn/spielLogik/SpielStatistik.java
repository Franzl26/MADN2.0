package edu.unibw.sse.madn.spielLogik;

public interface SpielStatistik {
    /**
     * @return die gewürfelten Zahlen nach Spieler und 1-6
     */
    int[][] zahlenGewuerfelt();

    /**
     * @return die Anzahl, wie oft die Spieler geschlagen haben
     */
    int[] andereGeschlagen();

    /**
     * @return die Anzahl, wie oft die Spieler geschlagen worden
     */
    int[] geschlagenWorden();

    /**
     * @return die Anzahl, wie oft die Spieler einen Prio-Zug missachtet haben
     */
    int[] prioZugFalsch();

    /**
     * @return die Namen der Spieler in der Reihenfolge ihrer Platzierung
     */
    String[] platzierung();

    /**
     * @return die Zeit zu der das Spiel gestartet wurde
     */
    long startZeit();

    /**
     * @return die Namen in der Reihenfolge der Spieler entspricht der Reihenfolge von gewürfelt/geschlagen
     */
    String[] namen();
}
