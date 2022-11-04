package edu.unibw.sse.madn.datenServer;

import java.io.Serializable;

public interface SpielfeldKonfigurationBytes extends Serializable {
    /**
     * Speichert Bilder in Verzeichnis ab (muss schon angelegt sein)
     *
     * @param verzeichnis Verzeichnis
     */
    void konfigurationSpeichern(String verzeichnis);
}
