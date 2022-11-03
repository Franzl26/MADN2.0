package edu.unibw.sse.madn.spielLogik;

import edu.unibw.sse.madn.serverKomm.AnClientSendenSpiel;
import edu.unibw.sse.madn.serverKomm.Sitzung;
import edu.unibw.sse.madn.warteraumverwaltung.WarteraumCallback;

import java.util.HashMap;

public class SpielImpl implements Spiel, SpielErstellen {
    private AnClientSendenSpiel anClient;
    private final HashMap<Sitzung, SpielObjekt> istInSpiel = new HashMap<>();
    private WarteraumCallback warteraumCallback;

    @Override
    public synchronized void spielErstellen(WarteraumCallback warteraum, Sitzung[] sitzungen, int bots, int spieler) {
        warteraumCallback = warteraum;
        Sitzung[] neueSitzungen = new Sitzung[bots + spieler];
        if (spieler >= 0) System.arraycopy(sitzungen, 0, neueSitzungen, 0, spieler);
        SpielObjekt spiel = new SpielObjekt(this, anClient, neueSitzungen, bots + spieler);
        for (Sitzung s : sitzungen) {
            istInSpiel.put(s, spiel);
        }
    }

    @Override
    public synchronized void kommunikationskanalSetzen(AnClientSendenSpiel spiel) {
        anClient = spiel;
    }

    @Override
    public synchronized ZiehenRueckgabe figurZiehen(Sitzung sitzung, int von, int nach) {
        SpielObjekt spiel = istInSpiel.get(sitzung);
        if (spiel == null) return ZiehenRueckgabe.ZIEHEN_VERBINDUNG_ABGEBROCHEN;
        return spiel.submitMove(sitzung, von, nach);
    }

    @Override
    public synchronized WuerfelnRueckgabe wuerfeln(Sitzung sitzung) {
        SpielObjekt spiel = istInSpiel.get(sitzung);
        if (spiel == null) return WuerfelnRueckgabe.WUERFELN_VERBINDUNG_ABGEBROCHEN;
        return spiel.throwDice(sitzung);
    }

    @Override
    public synchronized SpielStatistik spielVerlassen(Sitzung sitzung) {
        SpielObjekt spiel = istInSpiel.get(sitzung);
        if (spiel == null) return null;
        return spiel.leaveGame(sitzung);
    }

    public synchronized void spielBeendet(SpielObjekt spielObjekt) {
        istInSpiel.keySet().forEach(s -> {
            if (istInSpiel.get(s).equals(spielObjekt)) istInSpiel.remove(s);
        });
        warteraumCallback.spielBeendet();
    }
}
