package edu.unibw.sse.madn.clientKomm;

import edu.unibw.sse.madn.benutzerVerwaltung.RegistrierenRueckgabe;
import edu.unibw.sse.madn.serverKomm.Sitzung;
import edu.unibw.sse.madn.sonstiges.SpielfeldKonfigurationBytes;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.spielLogik.WuerfelnRueckgabe;
import edu.unibw.sse.madn.spielLogik.ZiehenRueckgabe;

import java.rmi.RemoteException;

public interface ClientKomm {
    // Anmelden / Registrieren
    /**
     * Benutzer anmelden
     * @param client Rückkanal zum Client
     * @param benutzername Benutzername
     * @param passwort Passwort verschlüsselt
     * @return erfolgreich: true, sonst false
     */
    AllgemeinerReturnWert anmelden(ClientCallback client, String benutzername, String passwort) throws RemoteException;

    /**
     * Benutzer registrieren
     * @param benutzername Benutzername
     * @param pw1 Passwort verschlüsselt
     * @param pw2 wiederholtes Passwort verschlüsselt
     * @return Fehler oder Erfolg
     */
    RegistrierenRueckgabe registrieren(String benutzername, String pw1, String pw2) throws RemoteException;


    // Designs
    /**
     * @return List aller verfügbaren Designs/Spielfeld-Konfigurationen
     */
    String[] designListeHolen() throws RemoteException;

    /**
     * @param name Name des Designs
     * @return die geladene Spielfeld-Konfiguration
     */
    SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name) throws RemoteException;


    // Warteraum
    /**
     * Warteraum erstellen
     * @return Warteraum erstellt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert warteraumErstellen();

    /**
     * Warteraum beitreten
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
     * @return Bot hinzugefügt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert botHinzufuegen();

    /**
     * Bot Entfernen
     * @return Bot entfernt: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert botEntfernen();

    /**
     * Spiel starten
     * @return Spiel gestartet: RET_ERFOLGREICH, Verbindungsfehler RET_VERBINDUNG_ABGEBROCHEN, sonst RET_FEHLER
     */
    AllgemeinerReturnWert spielStarten();

    /**
     * Spieldesign ändern
     * @param design Design
     */
    void designAnpassen(String design);


    // Spiel
    /**
     * Spielzug einreichen
     * @param sitzung Sitzung
     * @param von Feld von
     * @param nach Feld nach
     */
    ZiehenRueckgabe figurZiehen(Sitzung sitzung, int von, int nach);

    /**
     * Würfeln
     * @param sitzung Sitzung
     */
    WuerfelnRueckgabe wuerfeln(Sitzung sitzung);

    /**
     * Spiel Verlassen
     * @param sitzung Sitzung
     * @return Spielstatistik
     */
    SpielStatistik spielVerlassen(Sitzung sitzung);
}
