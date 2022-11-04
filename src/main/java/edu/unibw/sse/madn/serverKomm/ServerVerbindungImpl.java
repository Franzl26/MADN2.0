package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.benutzerVerwaltung.BenutzerZugang;
import edu.unibw.sse.madn.benutzerVerwaltung.RegistrierenRueckgabe;
import edu.unibw.sse.madn.clientKomm.ClientCallback;
import edu.unibw.sse.madn.datenServer.SpielDesign;
import edu.unibw.sse.madn.spielLogik.Spiel;
import edu.unibw.sse.madn.warteraumverwaltung.Raumauswahl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;

public class ServerVerbindungImpl extends UnicastRemoteObject implements ServerVerbindung {
    private final SpielDesign spielDesign;
    private final BenutzerZugang benutzerZugang;
    private final Raumauswahl raumauswahl;
    private final Spiel spiel;
    private KeyPair keyPair;

    public ServerVerbindungImpl(SpielDesign spielDesign, BenutzerZugang benutzerZugang, Raumauswahl raumauswahl, Spiel spiel) throws RemoteException {
        this.spielDesign = spielDesign;
        this.benutzerZugang = benutzerZugang;
        this.raumauswahl = raumauswahl;
        this.spiel = spiel;
        try {
            keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Neues Schlüsselpaar konnte nicht erstellt werden");
            System.exit(-1);
        }
    }

    @Override
    public Sitzung anmelden(ClientCallback client, String benutzername, byte[] passwort) throws RemoteException {
        if (client == null || benutzername == null || passwort == null) return null;
        String pw = passwortEntschluesseln(passwort, keyPair.getPrivate());
        if (pw == null) return null;
        if (!benutzerZugang.anmelden(benutzername, pw)) return null;
        return new SitzungImpl(client, benutzername, spielDesign, benutzerZugang, raumauswahl, spiel);
    }

    @Override
    public RegistrierenRueckgabe registrieren(String benutzername, byte[] passwort1, byte[] passwort2) throws RemoteException {
        if (benutzername == null || passwort1 == null || passwort2 == null) return null;
        String pw1 = passwortEntschluesseln(passwort1, keyPair.getPrivate());
        String pw2 = passwortEntschluesseln(passwort2, keyPair.getPrivate());
        if (pw1 == null || pw2 == null) return RegistrierenRueckgabe.REG_VERBINDUNG_ABGEBROCHEN;
        return benutzerZugang.registrieren(benutzername, pw1, pw2);
    }

    @Override
    public PublicKey oeffenltichenSchluesselHolen() throws RemoteException {
        return keyPair.getPublic();
    }

    private String passwortEntschluesseln(byte[] pw, PrivateKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] ent = cipher.doFinal(pw);
            return new String(ent);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            System.err.println("Passwort entschlüsseln nicht möglich");
            return null;
        }
    }

    public SpielDesign getSpielDesign() {
        return spielDesign;
    }

    public BenutzerZugang getBenutzerZugang() {
        return benutzerZugang;
    }

    public Raumauswahl getRaumauswahl() {
        return raumauswahl;
    }

    public Spiel getSpiel() {
        return spiel;
    }
}
