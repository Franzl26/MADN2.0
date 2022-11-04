package edu.unibw.sse.madn.datenServer;

import java.io.Serializable;

public interface SpielDesign extends Serializable {
    /**
     * @return List aller verfügbaren Designs/Spielfeld-Konfigurationen
     */
    String[] designListeHolen();

    /**
     * @param name Name des Designs
     * @return die geladene Spielfeld-Konfiguration oder null bei Fehler
     */
    SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name);

    /**
     * Prüft, ob es das Design gibt
     *
     * @param design Design
     * @return true: wenn es das Design gibt, false: wenn nicht
     */
    boolean existiertDesign(String design);
}
