package edu.unibw.sse.madn.datenServer;

public interface SpielDesign {
    /**
     * @return List aller verfügbaren Designs/Spielfeld-Konfigurationen
     */
    String[] designListeHolen();

    /**
     * @param name Name des Designs
     * @return die geladene Spielfeld-Konfiguration oder null bei Fehler
     */
    SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name);
}
