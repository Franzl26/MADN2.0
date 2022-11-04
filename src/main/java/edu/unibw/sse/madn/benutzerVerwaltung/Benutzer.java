package edu.unibw.sse.madn.benutzerVerwaltung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Benutzer implements Serializable {
    private static final long NUTZER_LOESCHEN_NACH = 259200000; // 72h

    private final HashMap<String, Nutzer> user = new HashMap<>();

    synchronized void nutzerHinzufuegen(String benutzername, byte[] pwHash) {
        if (user.containsKey(benutzername)) return;
        user.put(benutzername, new Nutzer(pwHash));
    }

    synchronized boolean nutzernameExistent(String benutzername) {
        return user.containsKey(benutzername);
    }

    synchronized boolean passwortPruefen(String benutzername, byte[] pwHash) {
        Nutzer nutzer = user.get(benutzername);
        if (nutzer == null) return false;
        if (!Arrays.equals(pwHash, nutzer.passwordHash)) return false;
        nutzer.letzterLogin = System.currentTimeMillis();
        return true;
    }

    synchronized void alteNutzerLoeschen() {
        ArrayList<String> entfernen = new ArrayList<>();
        user.keySet().forEach(n -> {
            if (user.get(n).letzterLogin + NUTZER_LOESCHEN_NACH < System.currentTimeMillis()) entfernen.add(n);
        });
        entfernen.forEach(user::remove);
    }


    private static class Nutzer implements Serializable {
        private final byte[] passwordHash;
        private long letzterLogin;

        public Nutzer(byte[] passwordHash) {
            this.passwordHash = passwordHash;
            letzterLogin = System.currentTimeMillis();
        }
    }
}
