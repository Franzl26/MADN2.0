package edu.unibw.sse.madn.warteraumverwaltung;

public interface Warteraum {
    /**
     * @return id des Warteraums
     */
    int id();

    /**
     * @return Array der Spielernamen im Warteraum, Länge muss Spieleranzahl+Botanzahl entsprechen
     */
    String[] namen();
}
