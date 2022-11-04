package edu.unibw.sse.madn.datenServer;

import edu.unibw.sse.madn.benutzerVerwaltung.Benutzer;

import java.io.*;

public class BenutzerDatenImpl implements BenutzerDaten {
    private final static String datei = "./resources/server/benutzer";

    @Override
    public void benutzerSpeichern(Benutzer benutzer) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(datei))) {
            os.writeObject(benutzer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Benutzer benutzerLaden() {
        File file = new File(datei);
        if (!file.isFile()) {
            System.err.println("Keine gespeicherten Benutzer gefunden");
            return null;
        }
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(file))) {
            return (Benutzer) os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Benutzer konnten nicht gelesen werden");
        }
        return null;
    }
}
