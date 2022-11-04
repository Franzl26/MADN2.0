package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.benutzerVerwaltung.RegistrierenRueckgabe;
import edu.unibw.sse.madn.clientKomm.ClientCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface ServerVerbindung extends Remote {
    /**
     * Benutzer anmelden
     *
     * @param client       Rückkanal zum Client
     * @param benutzername Benutzername
     * @param passwort     Passwort verschlüsselt
     * @return null: Fehler, sonst Sitzung
     */
    Sitzung anmelden(ClientCallback client, String benutzername, byte[] passwort) throws RemoteException;

    /**
     * Benutzer registrieren
     *
     * @param benutzername Benutzername
     * @param pw1          Passwort verschlüsselt
     * @param pw2          wiederholtes Passwort verschlüsselt
     * @return Fehler oder Erfolg
     */
    RegistrierenRueckgabe registrieren(String benutzername, byte[] pw1, byte[] pw2) throws RemoteException;

    /**
     * @return öffentlichen Schlüssel zur RSA-Verschlüsselung
     */
    PublicKey oeffenltichenSchluesselHolen() throws RemoteException;
}
