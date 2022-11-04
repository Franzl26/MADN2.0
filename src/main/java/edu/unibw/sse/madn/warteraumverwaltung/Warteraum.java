package edu.unibw.sse.madn.warteraumverwaltung;

import java.io.Serializable;

public interface Warteraum extends Serializable {
    /**
     * @return id des Warteraums
     */
    long id();

    /**
     * @return Array der Spielernamen im Warteraum, Länge muss Spieleranzahl+Botanzahl entsprechen
     */
    String[] namen();
}
