package edu.unibw.sse.madn.datenServer;

import edu.unibw.sse.madn.benutzerVerwaltung.Benutzer;

public interface BenutzerDaten {
    /**
     * Speichert Benutzerdaten ab
     *
     * @param benutzer alle Benutzer
     */
    void benutzerSpeichern(Benutzer benutzer);

    /**
     * Lädt Benutzerdaten
     *
     * @return geladene Benutzer oder null, wenn keine Datei gefunden/Fehler aufgetreten
     */
    Benutzer benutzerLaden();
}
