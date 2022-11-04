package edu.unibw.sse.madn.warteraumverwaltung;

import edu.unibw.sse.madn.serverKomm.Sitzung;

import java.util.ArrayList;

public class WarteraumImpl implements Warteraum {
    private static long ID_ZAEHLER = 0;

    private final long id = ID_ZAEHLER++;
    private final ArrayList<String> namen = new ArrayList<>(4);
    private final ArrayList<Sitzung> clients = new ArrayList<>(4);
    private int botAnzahl = 0;
    private int spielerAnzahl = 0;
    private String design = "Standard";

    @Override
    public long id() {
        return id;
    }

    @Override
    public String[] namen() {
        return namen.toArray(new String[0]);
    }

    int anzahlSpieler() {
        return spielerAnzahl;
    }

    void spielerHinzufuegen(Sitzung sitzung, String benutzername) {
        clients.add(sitzung);
        namen.add(benutzername);
        spielerAnzahl++;
    }

    void spielerEntfernen(Sitzung sitzung, String benutzername) {
        clients.remove(sitzung);
        namen.remove(benutzername);
        spielerAnzahl--;
    }

    void botHinzufuegen() {
        botAnzahl++;
        namen.add("Bot" + botAnzahl);
    }

    void botEntfernen() {
        namen.remove("Bot" + botAnzahl);
        botAnzahl--;
    }

    int botAnzahl() {
        return botAnzahl;
    }

    Sitzung[] clients() {
        return clients.toArray(new Sitzung[0]);
    }

    public void design(String design) {
        this.design = design;
    }

    public String design() {
        return design;
    }
}
