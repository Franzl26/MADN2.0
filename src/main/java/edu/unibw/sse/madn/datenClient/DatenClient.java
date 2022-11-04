package edu.unibw.sse.madn.datenClient;

import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public interface DatenClient {
    /**
     * @return Bild welches in Spielstatistik die Balken darstellt
     */
    Image balkenBildLaden();

    /**
     * @param name Name des Designs
     * @return null bei Fehler sonst: Spielfeldkonfiguration mit allen Bildern, Koordinaten, Drehungen,
     * Radius in welchem Klicken registriert werden soll
     */
    SpielfeldKonfiguration konfigurationLaden(String name);

    /**
     * Speichert die Konfiguration, wie sie vom Server übermittelt wurde
     *
     * @param konfiguration von Server übermittelte Konfiguration
     * @param name          Name unter dem die Konfiguration abgespeichert werden soll
     */
    void KonfigurationSpeichern(SpielfeldKonfigurationBytes konfiguration, String name);

    /**
     * lädt ein zufälliges Gif
     *
     * @return MP4-Gif oder null, wenn keins geladen werden konnte
     */
    Media zufaelligesGif();
}
