package edu.unibw.sse.madn.datenClient;

import javafx.scene.image.Image;

public interface SpielfeldKonfiguration {
    /**
     * @param i Feld
     * @return [x, y] Koordinate für Feld
     */
    int[] koordinatenVonFeld(int i);

    /**
     * @param i Feld
     * @return [Grad, gespiegelt]
     */
    int[] drehungVonFeld(int i);

    /**
     * @return Bild von Spielbrett
     */
    Image brettBild();

    /**
     * @return Bild von Pfad Normal
     */
    Image pfadNormalBild();

    /**
     * @param wurf Zahl des Würfels 0-6
     * @return Bild mit Würfelzahl
     */
    Image wuerfelBild(int wurf);

    /**
     * @param spieler Spieler 0-3
     * @return Bild
     */
    Image startfeldBild(int spieler);

    /**
     * @param spieler Spieler 0-3
     * @return Bild
     */
    Image spielerPersBild(int spieler);

    /**
     * @param spieler Spieler 0-3
     * @return Bild
     */
    Image figurBild(int spieler);

    /**
     * @param spieler Spieler 0-3
     * @return Bild
     */
    Image figurMarkiertBild(int spieler);

    /**
     * @return klick-Radius
     */
    int klickRadius();
}
