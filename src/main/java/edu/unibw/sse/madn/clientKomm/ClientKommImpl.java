package edu.unibw.sse.madn.clientKomm;

import edu.unibw.sse.madn.ansicht.RaumauswahlUpdaten;
import edu.unibw.sse.madn.ansicht.SpielUpdaten;
import edu.unibw.sse.madn.ansicht.WarteraumUpdaten;
import edu.unibw.sse.madn.benutzerVerwaltung.RegistrierenRueckgabe;
import edu.unibw.sse.madn.datenServer.SpielfeldKonfigurationBytes;
import edu.unibw.sse.madn.serverKomm.ServerVerbindung;
import edu.unibw.sse.madn.serverKomm.Sitzung;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.spielLogik.WuerfelnRueckgabe;
import edu.unibw.sse.madn.spielLogik.ZiehenRueckgabe;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static edu.unibw.sse.madn.clientKomm.AllgemeinerReturnWert.*;

public class ClientKommImpl implements ClientKomm {
    private final ClientCallbackImpl clientCallback;
    private String benutzername;
    private Sitzung sitzung;

    public ClientKommImpl() {
        clientCallback = new ClientCallbackImpl();
    }

    @Override
    public String benutzernamenHolen() {
        return benutzername;
    }

    @Override
    public AllgemeinerReturnWert anmelden(String ip, String benutzername, String passwort) {
        try {
            ServerVerbindung verbindung = (ServerVerbindung) Naming.lookup("//" + ip + "/" + "MADNLogin");
            PublicKey key = verbindung.oeffenltichenSchluesselHolen();
            byte[] pw = passwortVerschluesseln(passwort, key);
            if (pw == null) return RET_VERBINDUNG_ABGEBROCHEN;
            sitzung = verbindung.anmelden(clientCallback, benutzername, pw);
            if (sitzung == null) {
                return RET_FEHLER;
            }
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
        this.benutzername = benutzername;
        return RET_ERFOLGREICH;
    }

    @Override
    public RegistrierenRueckgabe registrieren(String ip, String benutzername, String passwort1, String passwort2) {
        try {
            ServerVerbindung verbindung = (ServerVerbindung) Naming.lookup("//" + ip + "/" + "MADNLogin");
            PublicKey key = verbindung.oeffenltichenSchluesselHolen();
            byte[] pw1 = passwortVerschluesseln(passwort1, key);
            byte[] pw2 = passwortVerschluesseln(passwort2, key);
            if (pw1 == null || pw2 == null) return RegistrierenRueckgabe.REG_VERBINDUNG_ABGEBROCHEN;
            return verbindung.registrieren(benutzername, pw1, pw2);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            return RegistrierenRueckgabe.REG_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public void abmelden() {
        try {
            sitzung.abmelden();
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public String[] designListeHolen() {
        try {
            return sitzung.designListeHolen();
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name) {
        try {
            return sitzung.spielfeldKonfigurationHolen(name);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public AllgemeinerReturnWert fuerWarteraumUpdatesAnmelden() {
        try {
            return sitzung.fuerWarteraumUpdatesAnmelden() ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public AllgemeinerReturnWert warteraumErstellen() {
        try {
            return sitzung.warteraumErstellen() ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public AllgemeinerReturnWert warteraumBeitreten(long raumId) {
        try {
            return sitzung.warteraumBeitreten(raumId) ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public void warteraumVerlassen() {
        try {
            sitzung.warteraumVerlassen();
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public AllgemeinerReturnWert botHinzufuegen() {
        try {
            return sitzung.botHinzufuegen() ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public AllgemeinerReturnWert botEntfernen() {
        try {
            return sitzung.botEntfernen() ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public AllgemeinerReturnWert spielStarten() {
        try {
            return sitzung.spielStarten() ? RET_ERFOLGREICH : RET_FEHLER;
        } catch (RemoteException e) {
            return RET_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public void designAnpassen(String design) {
        try {
            sitzung.designAnpassen(design);
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public ZiehenRueckgabe figurZiehen(int von, int nach) {
        try {
            return sitzung.figurZiehen(von, nach);
        } catch (RemoteException e) {
            return ZiehenRueckgabe.ZIEHEN_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public WuerfelnRueckgabe wuerfeln() {
        try {
            return sitzung.wuerfeln();
        } catch (RemoteException e) {
            return WuerfelnRueckgabe.WUERFELN_VERBINDUNG_ABGEBROCHEN;
        }
    }

    @Override
    public SpielStatistik spielVerlassen() {
        try {
            return sitzung.spielVerlassen();
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public void raumauswahlUpdaterSetzen(RaumauswahlUpdaten update) {
        clientCallback.raumauswahl = update;
    }

    @Override
    public void warteraumUpdaterSetzen(WarteraumUpdaten update) {
        clientCallback.warteraum = update;
    }

    @Override
    public void spielUpdaterSetzen(SpielUpdaten update) {
        clientCallback.spiel = update;
    }

    private byte[] passwortVerschluesseln(String passwort, PublicKey key) {
        byte[] chiffrat;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            chiffrat = cipher.doFinal(passwort.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeyException e) {
            return null;
        }
        return chiffrat;
    }
}
