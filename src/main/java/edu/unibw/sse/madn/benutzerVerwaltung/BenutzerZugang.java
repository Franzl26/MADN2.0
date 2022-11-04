package edu.unibw.sse.madn.benutzerVerwaltung;

import java.io.Serializable;

public interface BenutzerZugang extends Serializable {
    /**
     * Benutzer anmelden
     *
     * @param benutzername Benutzername
     * @param passwort     Passwort im Klartext
     * @return true: erfolgreich angemeldet, false: Fehler
     */
    boolean anmelden(String benutzername, String passwort);

    /**
     * Benutzer registrieren
     *
     * @param benutzername Benutzername
     * @param pw1          Passwort Klartext
     * @param pw2          wiederholtes Passwort Klartext
     * @return entsprechend des Anwendungsfalls
     */
    RegistrierenRueckgabe registrieren(String benutzername, String pw1, String pw2);

    /**
     * meldet Client ab
     *
     * @param benutzername Benutzername aus Sitzung
     */
    void abmelden(String benutzername);
}
