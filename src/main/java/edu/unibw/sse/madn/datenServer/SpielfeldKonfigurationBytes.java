package edu.unibw.sse.madn.datenServer;

public interface SpielfeldKonfigurationBytes {
    /**
     * Speichert Bilder in Verzeichnis ab (muss schon angelegt sein)
     * @param verzeichnis Verzeichnis
     */
    void konfigurationSpeichern(String verzeichnis);
}
