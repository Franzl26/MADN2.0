package edu.unibw.sse.madn.clientKomm;

import edu.unibw.sse.madn.ansicht.RaumauswahlUpdaten;
import edu.unibw.sse.madn.ansicht.SpielUpdaten;
import edu.unibw.sse.madn.ansicht.WarteraumUpdaten;
import edu.unibw.sse.madn.benutzerVerwaltung.RegistrierenRueckgabe;
import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.spielLogik.WuerfelnRueckgabe;
import edu.unibw.sse.madn.spielLogik.ZiehenRueckgabe;

public interface ClientKomm {
    /**
     * @return Benutzernamen des Clients der bei der Anmeldung verwendet wurde
     */
    String benutzernamenHolen();

    // Anmelden / Registrieren / Abmelden

    /**
     * Benutzer anmelden
     *
     * @param ip           Server IP
     * @param benutzername Benutzername
     * @param passwort     Passwort verschlüsselt
     * @return erfolgreich: true, sonst false
     */
    AllgemeinerReturnWert anmelden(String ip, String benutzername, String passwort);

    /**
     * Benutzer registrieren
     *
     * @param ip           Server IP
     * @param benutzername Benutzername
     * @param pw1          Passwort verschlüsselt
     * @param pw2          wiederholtes Passwort verschlüsselt
     * @return entsprechend des Anwendungsfalls
     */
    RegistrierenRueckgabe registrieren(String ip, String benutzername, String pw1, String pw2);

    /**
     * meldet Client ab
     */
    void abmelden();


    // Designs

    /**
     * @return List aller verfügbaren Designs/Spielfeld-Konfigurationen oder null bei Fehler
     */
    String[] designListeHolen();

    /**
     * @param name Name des Designs
     * @return die geladene Spielfeld-Konfiguration oder null bei Fehler
     */
    SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name);


    // Warteraum

    /**
     * meldet Client für die aktualisierung der Warteräume an
     *
     * @return angemeldet: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert fuerWarteraumUpdatesAnmelden();

    /**
     * Warteraum erstellen
     *
     * @return Warteraum erstellt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert warteraumErstellen();

    /**
     * Warteraum beitreten
     *
     * @param raumId Raum-ID
     * @return Warteraum beigetreten: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert warteraumBeitreten(long raumId);

    /**
     * Warteraum verlassen
     */
    void warteraumVerlassen();

    /**
     * Bot hinzufügen
     *
     * @return Bot hinzugefügt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert botHinzufuegen();

    /**
     * Bot Entfernen
     *
     * @return Bot entfernt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert botEntfernen();

    /**
     * Spiel starten
     *
     * @return Spiel gestartet: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert spielStarten();

    /**
     * Spieldesign ändern
     *
     * @param design Design
     */
    void designAnpassen(String design);


    // Spiel

    /**
     * Spielzug einreichen
     *
     * @param von  Feld von
     * @param nach Feld nach
     */
    ZiehenRueckgabe figurZiehen(int von, int nach);

    /**
     * Würfeln
     */
    WuerfelnRueckgabe wuerfeln();

    /**
     * Spiel Verlassen
     *
     * @return Spielstatistik oder null bei Fehler
     */
    SpielStatistik spielVerlassen();

    /**
     * Setzt für ClientCallback den Callback in den Dialog Raumauswahl
     *
     * @param update UpdateInterface
     */
    void raumauswahlUpdaterSetzen(RaumauswahlUpdaten update);

    /**
     * Setzt für ClientCallback den Callback in den Dialog Warteraum
     *
     * @param update UpdateInterface
     */
    void warteraumUpdaterSetzen(WarteraumUpdaten update);

    /**
     * Setzt für ClientCallback den Callback in den Dialog Spiel
     *
     * @param update UpdateInterface
     */
    void spielUpdaterSetzen(SpielUpdaten update);
}
