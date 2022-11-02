package edu.unibw.sse.madn.benutzerVerwaltung;

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
     * @param benutzername Benutzername
     */
    void abmelden(String benutzername);
}
