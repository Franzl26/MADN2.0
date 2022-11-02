package edu.unibw.sse.madn.datenServer;

import edu.unibw.sse.madn.sonstiges.SpielfeldKonfigurationBytes;

public interface SpielDesign {
    /**
     * @return List aller verfügbaren Designs/Spielfeld-Konfigurationen
     */
    String[] designListeHolen();

    /**
     * @param name Name des Designs
     * @return die geladene Spielfeld-Konfiguration
     */
    SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name);
}
