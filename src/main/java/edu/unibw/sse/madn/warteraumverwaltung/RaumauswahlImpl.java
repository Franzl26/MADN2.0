package edu.unibw.sse.madn.warteraumverwaltung;

import edu.unibw.sse.madn.serverKomm.AnClientSendenRaumauswahl;
import edu.unibw.sse.madn.serverKomm.Sitzung;
import edu.unibw.sse.madn.spielLogik.SpielErstellen;

import java.util.ArrayList;
import java.util.HashMap;

public class RaumauswahlImpl implements Raumauswahl, WarteraumCallback {
    private final static int MAX_RAEUME = 25;

    private AnClientSendenRaumauswahl anClient;
    private SpielErstellen spielErstellen;
    private final ArrayList<Sitzung> clients = new ArrayList<>(); // Clients die sich in Raumauswahl befinden
    private final HashMap<Sitzung, Long> istInRaum = new HashMap<>();
    private final HashMap<Long, WarteraumImpl> idZuRaum = new HashMap<>();
    private int raumAnzahl = 0;

    public void spielErstellenSetzen(SpielErstellen spielErstellen) {
        this.spielErstellen = spielErstellen;
    }

    @Override
    public void kommunikationskanalSetzen(AnClientSendenRaumauswahl raumauswahl) {
        anClient = raumauswahl;
    }

    @Override
    public void fuerUpdatesAnmelden(Sitzung sitzung) {
        clients.add(sitzung);
    }

    @Override
    public synchronized boolean warteraumErstellen(Sitzung sitzung) {
        if (!clients.contains(sitzung)) return false;
        if (raumAnzahl >= MAX_RAEUME) return false;
        WarteraumImpl neuerRaum = new WarteraumImpl();
        neuerRaum.spielerHinzufuegen(sitzung);
        idZuRaum.put(neuerRaum.id(), neuerRaum);
        istInRaum.put(sitzung, neuerRaum.id());
        raumAnzahl++;
        clients.remove(sitzung);
        updateClients();
        updateRaum(neuerRaum);
        return true;
    }

    @Override
    public synchronized boolean warteraumBeitreten(Sitzung sitzung, long raumId) {
        WarteraumImpl raum = idZuRaum.get(raumId);
        if (raum == null) return false;
        if (raum.anzahlSpieler() >= 4) return false;
        raum.spielerHinzufuegen(sitzung);
        istInRaum.put(sitzung, raum.id());
        clients.remove(sitzung);
        updateClients();
        updateRaum(raum);
        return true;
    }


    // in Warteraum
    @Override
    public synchronized void warteraumVerlassen(Sitzung sitzung) {
        Long id = istInRaum.get(sitzung);
        if (id == null) return;
        WarteraumImpl raum = idZuRaum.get(id);
        raum.spielerEntfernen(sitzung);
        if (raum.anzahlSpieler() <= 0) {
            idZuRaum.remove(id);
            raumAnzahl--;
            alleInRaumEntfernen(id);
        }
        clients.add(sitzung);
        istInRaum.remove(sitzung);
        updateClients();
        updateRaum(raum);
    }

    @Override
    public synchronized boolean botHinzufuegen(Sitzung sitzung) {
        Long id = istInRaum.get(sitzung);
        if (id == null) return false;
        WarteraumImpl raum = idZuRaum.get(id);
        if (raum.anzahlSpieler() >= 4) return false;
        raum.botHinzufuegen();
        updateClients();
        updateRaum(raum);
        return true;
    }

    @Override
    public synchronized boolean botEntfernen(Sitzung sitzung) {
        Long id = istInRaum.get(sitzung);
        if (id == null) return false;
        WarteraumImpl raum = idZuRaum.get(id);
        if (raum.botAnzahl() <= 0) return false;
        raum.botEntfernen();
        updateClients();
        updateRaum(raum);
        return true;
    }

    @Override
    public synchronized boolean spielStarten(Sitzung sitzung) {
        Long id = istInRaum.get(sitzung);
        if (id == null) return false;
        WarteraumImpl raum = idZuRaum.get(id);
        if (raum.anzahlSpieler() <= 1) return false;
        spielErstellen.spielErstellen(this, raum.clients(), raum.botAnzahl(), raum.anzahlSpieler() - raum.botAnzahl());
        alleInRaumEntfernen(id);
        idZuRaum.remove(id);
        updateClients();
        startenFuerRaum(raum);
        return false;
    }

    @Override
    public synchronized void designAnpassen(Sitzung sitzung, String design) {
        Long id = istInRaum.get(sitzung);
        if (id == null) return;
        WarteraumImpl raum = idZuRaum.get(id);
        raum.design(design);
    }

    @Override
    public synchronized void spielBeendet() {
        raumAnzahl--;
    }


    private void updateRaum(WarteraumImpl raum) {
        Sitzung[] clients = raum.clients().clone();
        String[] namen = raum.namen().clone();
        for (Sitzung s : clients) {
            new Thread(() -> {
                boolean ret = anClient.warteraumNamenUpdaten(s, namen);
                if (!ret) warteraumVerlassen(s);
            }).start();
        }
    }

    private void startenFuerRaum(WarteraumImpl raum) {
        Sitzung[] clients = raum.clients().clone();
        String design = raum.design();
        for (Sitzung s : clients) {
            new Thread(() -> {
                boolean ret = anClient.spielStartet(s, design);
                if (!ret) warteraumVerlassen(s);
            }).start();
        }
    }

    private void updateClients() {
        WarteraeumeImpl raeume = new WarteraeumeImpl();
        idZuRaum.values().forEach(r -> raeume.raumHinzufuegen(r.namen(), r.id()));
        Sitzung[] clients = this.clients.toArray(new Sitzung[0]);
        for (Sitzung s : clients) {
            new Thread(() -> {
                boolean ret = anClient.raeumeUpdaten(s, raeume);
                if (!ret) clientEntfernen(s);
            }).start();
        }
    }

    private synchronized void alleInRaumEntfernen(long id) {
        istInRaum.keySet().forEach(e -> {
            if (istInRaum.get(e).equals(id)) istInRaum.remove(e);
        });
    }

    private synchronized void clientEntfernen(Sitzung sitzung) {
        clients.remove(sitzung);
    }
}
