package edu.unibw.sse.madn.datenClient;

import edu.unibw.sse.madn.sonstiges.SpielfeldKonfigurationBytes;
import javafx.scene.image.Image;

public interface DatenClient {
    /**
     *
     * @return Bild welches in Spielstatistik die Balken darstellt
     */
    Image balkenBildLaden();

    /**
     *
     * @param name Name des Designs
     * @return Spielfeldkonfiguration mit allen Bildern, Koordinaten, Drehungen, Radius in welchem Klicken registriert
     */
    SpielfeldKonfiguration konfigurationLaden(String name);

    /**
     * Speichert die Konfiguration, wie sie vom Server übermittelt wurde
     * @param konfiguration von Server übermittelte Konfiguration
     */
    void KonfigurationSpeichern(SpielfeldKonfigurationBytes konfiguration);
}
