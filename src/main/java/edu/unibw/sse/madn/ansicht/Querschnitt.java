package edu.unibw.sse.madn.ansicht;

import edu.unibw.sse.madn.clientKomm.ClientKomm;
import edu.unibw.sse.madn.datenClient.DatenClient;
import edu.unibw.sse.madn.datenClient.SpielfeldKonfiguration;
import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

public class Querschnitt {
    private static DatenClient datenClient;

    public static void datenClientSetzen(DatenClient datenClient) {
        Querschnitt.datenClient = datenClient;
    }

    static SpielfeldKonfiguration spielfeldKonfigurationLaden(ClientKomm komm, String design) {
        SpielfeldKonfiguration config = datenClient.konfigurationLaden(design);
        if (config == null) {
            SpielfeldKonfigurationBytes configBytes = komm.spielfeldKonfigurationHolen(design);
            if (configBytes == null) return datenClient.konfigurationLaden("Standard");
            datenClient.KonfigurationSpeichern(configBytes, design);
            config = datenClient.konfigurationLaden(design);
        }
        return config != null ? config : datenClient.konfigurationLaden("Standard");
    }

    static void drawBoardAll(GraphicsContext gc, SpielfeldKonfiguration config, FeldBesetztStatus[] board) {
        //gc.setFill(Color.LIGHTSLATEGRAY);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 500, 500);
        gc.drawImage(config.brettBild(), 0, 0, 500, 500);
        for (int i = 0; i < 72; i++) {
            drawBoardSingleFieldAll(gc, config, board[i], i, false);
        }
    }

    static void drawBoardSingleFieldAll(GraphicsContext gc, SpielfeldKonfiguration config, FeldBesetztStatus state, int i, boolean highlight) {
        Image image = switch (i) {
            case 32 -> config.startfeldBild(0);
            case 42 -> config.startfeldBild(1);
            case 52 -> config.startfeldBild(2);
            case 62 -> config.startfeldBild(3);
            default -> config.pfadNormalBild();
        };
        if ((i >= 0 && i <= 3) || (i >= 16 && i <= 19)) image = config.spielerPersBild(0);
        else if ((i >= 4 && i <= 7) || (i >= 20 && i <= 23)) image = config.spielerPersBild(1);
        else if ((i >= 8 && i <= 11) || (i >= 24 && i <= 27)) image = config.spielerPersBild(2);
        else if ((i >= 12 && i <= 15) || (i >= 28 && i <= 31)) image = config.spielerPersBild(3);
        if (highlight) {
            switch (state) {
                case FELD_SPIELER1 -> image = config.figurMarkiertBild(0);
                case FELD_SPIELER2 -> image = config.figurMarkiertBild(1);
                case FELD_SPIELER3 -> image = config.figurMarkiertBild(2);
                case FELD_SPIELER4 -> image = config.figurMarkiertBild(3);
            }
        } else {
            switch (state) {
                case FELD_SPIELER1 -> image = config.figurBild(0);
                case FELD_SPIELER2 -> image = config.figurBild(1);
                case FELD_SPIELER3 -> image = config.figurBild(2);
                case FELD_SPIELER4 -> image = config.figurBild(3);
            }
        }

        ImageView iv = new ImageView(image);
        int[] drehung = config.drehungVonFeld(i);
        if (drehung[1] == 1 && state != FeldBesetztStatus.FELD_LEER) iv.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        image = iv.snapshot(params, null);

        int[] koords = config.koordinatenVonFeld(i);
        Image brett = config.brettBild();
        gc.drawImage(brett, (koords[0] - 20) * config.brettBild().getWidth() / 500, (koords[1] - 20) * brett.getHeight() / 500, 40 * brett.getWidth() / 500, 40 * brett.getHeight() / 500, koords[0] - 20, koords[1] - 20, 40, 40);
        gc.save();
        gc.translate(koords[0], koords[1]);
        if (state != FeldBesetztStatus.FELD_LEER) gc.rotate(drehung[0]);
        gc.drawImage(image, -config.klickRadius(), -config.klickRadius(), 34, 34);
        gc.restore();
    }

    static Media zufaelligesGif() {
        return datenClient.zufaelligesGif();
    }

    static Image balkenBild() {
        return datenClient.balkenBildLaden();
    }
}
