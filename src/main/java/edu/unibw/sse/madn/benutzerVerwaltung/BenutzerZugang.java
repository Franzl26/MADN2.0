package edu.unibw.sse.madn.benutzerVerwaltung;

import edu.unibw.sse.madn.serverKomm.Sitzung;

public interface BenutzerZugang {
    /**
     * Benutzer anmelden
     * @param benutzername Benutzername
     * @param passwort Passwort Klartext
     * @return true: erfolgreich angemeldet, false: Fehler
     */
    boolean anmelden(String benutzername, String passwort);

    /**
     * Benutzer registrieren
     * @param benutzername Benutzername
     * @param pw1 Passwort Klartext
     * @param pw2 wiederholtes Passwort Klartext
     */
    RegistrierenRueckgabe registrieren(String benutzername, String pw1, String pw2);

    /**
     * meldet Client ab
     * @param sitzung Sitzung
     */
    void abmelden(Sitzung sitzung);
}
