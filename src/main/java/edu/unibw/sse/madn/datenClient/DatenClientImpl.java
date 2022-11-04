package edu.unibw.sse.madn.datenClient;

import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;
import java.nio.file.Paths;

public class DatenClientImpl implements DatenClient {
    private final Image balkenImage;
    private final File[] gifList;

    public DatenClientImpl() {
        File f = new File("./resources/client/gradient.png");
        balkenImage = new Image(Paths.get(f.getAbsolutePath()).toUri().toString());
        f = new File("./resources/client/waiting/gifs/");
        gifList = f.listFiles();
    }

    @Override
    public Image balkenBildLaden() {
        return balkenImage;
    }

    @Override
    public SpielfeldKonfiguration konfigurationLaden(String name) {
        return SpielfeldKonfigurationImpl.loadBoardKonfiguration("./resources/client/designs/" + name + "/");
    }

    @Override
    public void KonfigurationSpeichern(SpielfeldKonfigurationBytes konfiguration, String name) {
        File f = new File("./resources/client/designs/" + name + "/");
        //noinspection ResultOfMethodCallIgnored
        f.mkdir();
        konfiguration.konfigurationSpeichern(f.getAbsolutePath());
    }

    @Override
    public Media zufaelligesGif() {
        if (gifList == null) return null;
        File f = gifList[(int) (Math.random() * gifList.length)];
        return new Media(f.toURI().toString());
    }
}
