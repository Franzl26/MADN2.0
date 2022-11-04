package edu.unibw.sse.madn.spielLogik;

import edu.unibw.sse.madn.serverKomm.AnClientSendenSpiel;
import edu.unibw.sse.madn.serverKomm.Sitzung;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import static edu.unibw.sse.madn.spielLogik.FeldBesetztStatus.*;
import static edu.unibw.sse.madn.spielLogik.SpielMethoden.*;
import static java.lang.Thread.sleep;

public class SpielObjekt {
    private static final int BOT_WAIT_WUERFELN = 1000;
    private static final int BOT_WAIT_ZIEHEN = 2000;
    private static final int DELAY_WUERFELN = 10000;
    private static final int DELAY_SPIELZUG = 15000;
    private static final int DELAY_WAITING = 5000;

    private final SpielStatistikImpl spielStatistik;
    private final AnClientSendenSpiel anClient;
    private final SpielImpl spielImpl;

    private final Sitzung[] clients;
    private final String[] namen;
    private final FeldBesetztStatus[] felderVonSpieler;
    private final boolean[] finished;
    private int anzahlFinished = 0;

    private final int spielerAnzahl;
    private final FeldBesetztStatus[] boardState;
    private int aktiverSpieler = -1;
    private int zahlGewuerfelt = -5;
    private int anzahlWuerfeln = -1;

    private Timer timerWuerfeln = new Timer();
    private Timer timerZiehen = new Timer();
    private Timer timerWaiting = new Timer();

    protected SpielObjekt(SpielImpl spielImpl, AnClientSendenSpiel anClient, Sitzung[] sitzungen, int spielerUndBotsAnz) {
        this.spielImpl = spielImpl;
        this.anClient = anClient;
        spielerAnzahl = spielerUndBotsAnz;
        clients = sitzungen.clone();

        namen = new String[spielerAnzahl];
        felderVonSpieler = new FeldBesetztStatus[spielerAnzahl];
        finished = new boolean[spielerAnzahl];

        if (spielerAnzahl == 2) {
            namen[0] = (clients[0] == null ? "Bot0" : namenHolen(clients[0]));
            namen[1] = (clients[1] == null ? "Bot1" : namenHolen(clients[1]));
            felderVonSpieler[0] = FELD_SPIELER1;
            felderVonSpieler[1] = FELD_SPIELER3;
        } else {
            for (int i = 0; i < spielerAnzahl; i++) {
                namen[i] = (clients[i] == null ? "Bot" + i : namenHolen(clients[i]));
                felderVonSpieler[i] = switch (i) {
                    case 0 -> FELD_SPIELER1;
                    case 1 -> FELD_SPIELER2;
                    case 2 -> FELD_SPIELER3;
                    default -> FELD_SPIELER4;
                };
            }
        }
        spielStatistik = new SpielStatistikImpl();
        spielStatistik.namenSetzen(namen);
        boardState = feldFuellen(spielerAnzahl);

        Timer t = new Timer("startGame");
        t.schedule(new StartGame(), 1000);

        displayNewStateAll(boardState, null);
        aktivenSpielerSendenAlle(-1);
    }

    public synchronized ZiehenRueckgabe submitMove(Sitzung sitzung, int from, int to) {
        int i = isInGame(sitzung);
        if (i == -1 || aktiverSpieler != i) return ZiehenRueckgabe.ZIEHEN_NICHT_DRAN;
        FeldBesetztStatus field = felderVonSpieler[aktiverSpieler];
        if (zahlGewuerfelt == -1) return ZiehenRueckgabe.ZIEHEN_NICHT_GEWUERFELT;
        if (!checkMoveValid(boardState, field, from, to, zahlGewuerfelt)) return ZiehenRueckgabe.ZIEHEN_ZUG_FEHLERHAFT;
        int[] prio = checkForPrioMove(boardState, field, zahlGewuerfelt);
        if (prio != null && prio[0] < 16 && from > 15) return ZiehenRueckgabe.ZIEHEN_ZUG_FEHLERHAFT;
        return submitMoveIntern(from, to);
    }

    /**
     * move muss erlaubt sein, wird nur noch gesetzt + ggf Punish + Spieler informiert
     */
    private synchronized ZiehenRueckgabe submitMoveIntern(int from, int to) {
        if (aktiverSpieler < 0) return ZiehenRueckgabe.ZIEHEN_NICHT_DRAN;
        timerZiehen.cancel();
        timerWaiting.cancel();
        //System.out.println("Submit " + namen[aktiverSpieler] + ": " + from + " -> " + to);
        int[] changed = new int[]{from, to, -1, -1, -1}; // from, to, strafe in loch, jmd geschlagen, strafe Figur die weg
        // wenn geschlagen zurücksetzen
        FeldBesetztStatus fieldStateFrom = boardState[from];
        if (boardState[to] != FELD_LEER) {
            spielStatistik.incAndereGeschlagen(aktiverSpieler);
            for (int i = 0; i < spielerAnzahl; i++) {
                if (felderVonSpieler[i] == boardState[to]) {
                    spielStatistik.incGeschlagenWorden(i);
                    break;
                }
            }
            changed[3] = figurZurueckAufStartpositionen(to);
        }
        int[] bestrafung = checkBestrafen(fieldStateFrom, from);
        changed[2] = bestrafung[0];
        changed[4] = bestrafung[1];
        if (changed[2] != -1) spielStatistik.incPrioZugIgnoriert(aktiverSpieler);

        boardState[to] = fieldStateFrom;
        boardState[from] = FELD_LEER;

        checkFinished(fieldStateFrom);

        displayNewStateAll(boardState, changed);
        nextMove(false);
        return (changed[2] == -1 ? ZiehenRueckgabe.ZIEHEN_ERFOLGREICH : ZiehenRueckgabe.ZIEHEN_BESTRAFT);
    }

    private void checkFinished(FeldBesetztStatus fieldStateFrom) {
        // gewonnen Erkennung
        if (!finished[aktiverSpieler] && fieldStateFrom == FELD_SPIELER1 && boardState[16] == FELD_SPIELER1 && boardState[17] == FELD_SPIELER1 && boardState[18] == FELD_SPIELER1 && boardState[19] == FELD_SPIELER1) {
            finished[aktiverSpieler] = true;
            spielStatistik.platzierungSetzen(anzahlFinished, namen[aktiverSpieler]);
            anzahlFinished++;
        }
        if (!finished[aktiverSpieler] && fieldStateFrom == FELD_SPIELER2 && boardState[20] == FELD_SPIELER2 && boardState[21] == FELD_SPIELER2 && boardState[22] == FELD_SPIELER2 && boardState[23] == FELD_SPIELER2) {
            finished[aktiverSpieler] = true;
            spielStatistik.platzierungSetzen(anzahlFinished, namen[aktiverSpieler]);
            anzahlFinished++;
        }
        if (!finished[aktiverSpieler] && fieldStateFrom == FELD_SPIELER3 && boardState[24] == FELD_SPIELER3 && boardState[25] == FELD_SPIELER3 && boardState[26] == FELD_SPIELER3 && boardState[27] == FELD_SPIELER3) {
            finished[aktiverSpieler] = true;
            spielStatistik.platzierungSetzen(anzahlFinished, namen[aktiverSpieler]);
            anzahlFinished++;
        }
        if (!finished[aktiverSpieler] && fieldStateFrom == FELD_SPIELER4 && boardState[28] == FELD_SPIELER4 && boardState[29] == FELD_SPIELER4 && boardState[30] == FELD_SPIELER4 && boardState[31] == FELD_SPIELER4) {
            finished[aktiverSpieler] = true;
            spielStatistik.platzierungSetzen(anzahlFinished, namen[aktiverSpieler]);
            anzahlFinished++;
        }
        if (anzahlFinished == spielerAnzahl - 1) {
            for (int i = 0; i < spielerAnzahl; i++) {
                if (!finished[i]) {
                    finished[i] = true;
                    spielStatistik.platzierungSetzen(anzahlFinished, namen[i]);
                }
            }
            anzahlFinished++;
        }
    }

    /**
     * @return [-1, -1] wenn keine Strafe, sonst Felder, die neu gezeichnet werden müssen
     */
    private int[] checkBestrafen(FeldBesetztStatus aktuellerSpieler, int from) {
        int[] ret = new int[]{-1, -1};
        // bestrafen
        // abrücken
        if (from != 32 && aktuellerSpieler == FELD_SPIELER1 && boardState[32] == FELD_SPIELER1 && boardState[32 + zahlGewuerfelt] != FELD_SPIELER1 && (boardState[0] == FELD_SPIELER1 || boardState[1] == FELD_SPIELER1 || boardState[2] == FELD_SPIELER1 || boardState[3] == FELD_SPIELER1)) {
            ret[0] = figurZurueckAufStartpositionen(32);
            ret[1] = 32;
        }
        if (from != 42 && aktuellerSpieler == FELD_SPIELER2 && boardState[42] == FELD_SPIELER2 && boardState[42 + zahlGewuerfelt] != FELD_SPIELER2 && (boardState[4] == FELD_SPIELER2 || boardState[5] == FELD_SPIELER2 || boardState[6] == FELD_SPIELER2 || boardState[7] == FELD_SPIELER2)) {
            ret[0] = figurZurueckAufStartpositionen(42);
            ret[1] = 42;
        }
        if (from != 52 && aktuellerSpieler == FELD_SPIELER3 && boardState[52] == FELD_SPIELER3 && boardState[52 + zahlGewuerfelt] != FELD_SPIELER3 && (boardState[8] == FELD_SPIELER3 || boardState[9] == FELD_SPIELER3 || boardState[10] == FELD_SPIELER3 || boardState[11] == FELD_SPIELER3)) {
            ret[0] = figurZurueckAufStartpositionen(52);
            ret[1] = 52;
        }
        if (from != 62 && aktuellerSpieler == FELD_SPIELER4 && boardState[62] == FELD_SPIELER4 && boardState[62 + zahlGewuerfelt] != FELD_SPIELER4 && (boardState[12] == FELD_SPIELER4 || boardState[13] == FELD_SPIELER4 || boardState[14] == FELD_SPIELER4 || boardState[15] == FELD_SPIELER4)) {
            ret[0] = figurZurueckAufStartpositionen(62);
            ret[1] = 62;
        }
        // schlagen
        if (from > 31 && ret[0] != -1) {
            for (int i = 39; i >= 0; i--) {
                int intFieldTo = (i + zahlGewuerfelt) % 40 + 32;
                if ((i + 32) != from && boardState[i + 32] == aktuellerSpieler && boardState[intFieldTo] != aktuellerSpieler && boardState[intFieldTo] != FELD_LEER) {
                    // nicht über Start beachten
                    if (aktuellerSpieler == FELD_SPIELER1 && (i + zahlGewuerfelt) >= 40) continue;
                    if (aktuellerSpieler == FELD_SPIELER2 && (i + zahlGewuerfelt) >= 10 && i < 10) continue;
                    if (aktuellerSpieler == FELD_SPIELER3 && (i + zahlGewuerfelt) >= 20 && i < 20) continue;
                    if (aktuellerSpieler == FELD_SPIELER4 && (i + zahlGewuerfelt) >= 30 && i < 30) continue;
                    ret[0] = figurZurueckAufStartpositionen(i + 32);
                    ret[1] = i + 32;
                }
            }
        }
        return ret;
    }

    public synchronized WuerfelnRueckgabe throwDice(Sitzung sitzung) {
        int i = isInGame(sitzung);
        if (i == -1 || i != aktiverSpieler || zahlGewuerfelt < -1) return WuerfelnRueckgabe.WUERFELN_NICHT_DRAN;
        return throwDiceIntern();
    }

    private synchronized WuerfelnRueckgabe throwDiceIntern() {
        timerWuerfeln.cancel();
        if (zahlGewuerfelt > 0) {
            if (getValidMove(boardState, felderVonSpieler[aktiverSpieler], zahlGewuerfelt)[0] != -1)
                return WuerfelnRueckgabe.WUERFELN_FALSCHE_PHASE;
        }
        if (anzahlWuerfeln == 0) return WuerfelnRueckgabe.WUERFELN_NICHT_DRAN;

        zahlGewuerfelt = (int) (Math.random() * 6 + 1);

        spielStatistik.incZahlGewuerfelt(aktiverSpieler, zahlGewuerfelt - 1);
        //System.out.println("throw diceIntern: " + namen[aktiverSpieler] + " : " + zahlGewuerfelt);
        anzahlWuerfeln--;
        displayDiceAll(zahlGewuerfelt);
        if (zahlGewuerfelt == 6) {
            anzahlWuerfeln = 1;
        }

        if (getValidMove(boardState, felderVonSpieler[aktiverSpieler], zahlGewuerfelt)[0] == -1) { // kein Zug möglich
            nextMove(false);
            return WuerfelnRueckgabe.WUERFELN_KEIN_ZUG_MOEGLICH;
        }
        if (clients[aktiverSpieler] != null) {
            timerZiehen = new Timer("TimerZiehen");
            timerZiehen.schedule(new ZiehenEnde(aktiverSpieler), DELAY_SPIELZUG);
            timerWaiting = new Timer("TimerWaiting");
            timerWaiting.schedule(new Waiting(aktiverSpieler), DELAY_WAITING);
        }
        return WuerfelnRueckgabe.WUERFELN_ERFOLGREICH;
    }

    private void nextMove(boolean naechstenErzwingen) {
        zahlGewuerfelt = -5;
        new Thread(() -> {
            try {
                if (anzahlFinished == spielerAnzahl) {
                    beenden();
                    Thread.currentThread().interrupt();
                    return;
                }
                if (anzahlWuerfeln < 1 || naechstenErzwingen) {
                    //System.out.println("set next");
                    aktiverSpieler = (aktiverSpieler + 1) % spielerAnzahl;
                    while (finished[aktiverSpieler]) aktiverSpieler = (aktiverSpieler + 1) % spielerAnzahl;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.err.println("schlafen unterbrochen");
                    }
                    aktivenSpielerSendenAlle(aktiverSpieler);
                    displayDiceAll(0);
                    anzahlWuerfeln = getAnzahlWuerfelnNext(aktiverSpieler);
                    //displayNewStateAll(boardState, new int[]{});
                }
                zahlGewuerfelt = -1;
                if (clients[aktiverSpieler] == null) {
                    doBotMove(aktiverSpieler, true);
                } else {
                    timerWuerfeln = new Timer("TimerWurf");
                    timerWuerfeln.schedule(new WuerfelnEnde(aktiverSpieler), DELAY_WUERFELN);
                }
            } catch (RuntimeException e) {
                System.err.println("beendet");
            }
        }).start();
    }

    private int getAnzahlWuerfelnNext(int spielerNext) {
        FeldBesetztStatus fieldState = felderVonSpieler[spielerNext];
        if (fieldState == FELD_SPIELER1) {
            int countFehlen = 0;
            for (int i = 0; i < 4; i++) if (boardState[i] == FELD_LEER) countFehlen++;
            for (int i = 0; i < countFehlen; i++) if (boardState[19 - i] == FELD_LEER) return 1;
            return 3;
        }
        if (fieldState == FELD_SPIELER2) {
            int countFehlen = 0;
            for (int i = 4; i < 8; i++) if (boardState[i] == FELD_LEER) countFehlen++;
            for (int i = 0; i < countFehlen; i++) if (boardState[23 - i] == FELD_LEER) return 1;
            return 3;
        }
        if (fieldState == FELD_SPIELER3) {
            int countFehlen = 0;
            for (int i = 8; i < 12; i++) if (boardState[i] == FELD_LEER) countFehlen++;
            for (int i = 0; i < countFehlen; i++) if (boardState[27 - i] == FELD_LEER) return 1;
            return 3;
        }
        if (fieldState == FELD_SPIELER4) {
            int countFehlen = 0;
            for (int i = 12; i < 16; i++) if (boardState[i] == FELD_LEER) countFehlen++;
            for (int i = 0; i < countFehlen; i++) if (boardState[31 - i] == FELD_LEER) return 1;
            return 3;
        }
        System.err.println("Fehler oder ich bin doof");
        return -1;
    }

    private int figurZurueckAufStartpositionen(int feld) {
        FeldBesetztStatus field = boardState[feld];
        boardState[feld] = FELD_LEER;
        if (field == FELD_SPIELER1) {
            for (int i = 0; i < 4; i++) {
                if (boardState[i] == FELD_LEER) {
                    boardState[i] = FELD_SPIELER1;
                    return i;
                }
            }
        }
        if (field == FELD_SPIELER2) {
            for (int i = 4; i < 8; i++) {
                if (boardState[i] == FELD_LEER) {
                    boardState[i] = FELD_SPIELER2;
                    return i;
                }
            }
        }
        if (field == FELD_SPIELER3) {
            for (int i = 8; i < 12; i++) {
                if (boardState[i] == FELD_LEER) {
                    boardState[i] = FELD_SPIELER3;
                    return i;
                }
            }
        }
        if (field == FELD_SPIELER4) {
            for (int i = 12; i < 16; i++) {
                if (boardState[i] == FELD_LEER) {
                    boardState[i] = FELD_SPIELER4;
                    return i;
                }
            }
        }
        System.err.println("Fehler oder ich bin doof");
        return -1;
    }

    public SpielStatistik leaveGame(Sitzung sitzung) {
        return removePlayerIntern(sitzung);
    }

    private synchronized SpielStatistik removePlayerIntern(Sitzung sitzung) {
        int i = isInGame(sitzung);
        if (i == -1) return null;
        clients[i] = null;
        namen[i] = "Bot" + i;
        for (int j = 0; j < spielerAnzahl; j++) {
            if (clients[j] != null) break;
            if (j == spielerAnzahl - 1) {
                beenden();
                break;
            }
        }
        SpielStatistik spielStatistik1 = spielStatistik.holeZumSenden();
        //System.out.println("Spiel verlassen: " + namenHolen(sitzung));
        spielStatistik.namenSetzen(namen);
        neueNamenSendenAlle(namen.clone());
        return spielStatistik1;
    }

    private void beenden() {
        System.err.println("beendet");
        aktiverSpieler = -1;
        zahlGewuerfelt = -1;
        anzahlWuerfeln = -1;
        spielImpl.spielBeendet(this);
        spielVorbeiSendenAlle();
    }

    private void doBotMove(int spieler, boolean sleep) {
        new Thread(() -> {
            if (zahlGewuerfelt < 1 && spieler == aktiverSpieler) { // schon gewürfelt, falls Spieler ersetzt / nicht gezogen
                try {
                    if (sleep) sleep(BOT_WAIT_WUERFELN);
                } catch (InterruptedException e) {
                    System.err.println("schlafen unterbrochen");
                }
                if (throwDiceIntern() == WuerfelnRueckgabe.WUERFELN_KEIN_ZUG_MOEGLICH) return; // kein Zug möglich
            }
            int[] move = getValidMove(boardState, felderVonSpieler[aktiverSpieler], zahlGewuerfelt);
            if (move[0] != -1 && spieler == aktiverSpieler) {
                try {
                    if (sleep) sleep(BOT_WAIT_ZIEHEN);
                } catch (InterruptedException e) {
                    System.err.println("schlafen unterbrochen");
                }
                submitMoveIntern(move[0], move[1]);
            }
        }).start();
    }

    private void displayDiceAll(int wurf) {
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] != null) displayDice(clients[i], wurf);
        }
    }

    private void displayDice(Sitzung sitzung, int wurf) {
        new Thread(() -> {
            boolean ret = anClient.wuerfelUpdaten(sitzung, wurf);
            if (!ret) removePlayerIntern(sitzung);
        }).start();
    }

    private void displayNewStateAll(FeldBesetztStatus[] state, int[] changed) {
        FeldBesetztStatus[] statuses = state.clone();
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] != null) displayNewStateIntern(clients[i], statuses, changed);
        }
    }

    private void displayNewStateIntern(Sitzung sitzung, FeldBesetztStatus[] state, int[] changed) {
        new Thread(() -> {
            boolean ret = anClient.spielfeldUpdaten(sitzung, state, changed);
            if (!ret) removePlayerIntern(sitzung);
        }).start();
    }

    private void neueNamenSendenAlle(String[] namen) {
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] != null) neuenNamenSenden(clients[i], namen);
        }
    }

    private void neuenNamenSenden(Sitzung sitzung, String[] namen) {
        new Thread(() -> {
            boolean ret = anClient.spielNamenUpdaten(sitzung, namen);
            if (!ret) removePlayerIntern(sitzung);
        }).start();
    }

    private void aktivenSpielerSendenAlle(int aktiverSpieler) {
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] != null) aktivenSpielerSenden(clients[i], aktiverSpieler);
        }
    }

    private void aktivenSpielerSenden(Sitzung sitzung, int aktiverSpieler) {
        new Thread(() -> {
            boolean ret = anClient.aktuellenSpielerSetzen(sitzung, aktiverSpieler);
            if (!ret) removePlayerIntern(sitzung);
        }).start();
    }

    private void spielVorbeiSendenAlle() {
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] != null) spielVorbeiSenden(clients[i]);
        }
    }

    private void spielVorbeiSenden(Sitzung sitzung) {
        SpielStatistik zumSenden = spielStatistik.holeZumSenden();
        new Thread(() -> anClient.spielVorbei(sitzung, zumSenden)).start();
    }

    private int isInGame(Sitzung sitzung) {
        for (int i = 0; i < spielerAnzahl; i++) {
            if (clients[i] == sitzung) return i;
        }
        return -1;
    }

    private class StartGame extends TimerTask {
        @Override
        public void run() {
            neueNamenSendenAlle(namen);
            displayNewStateAll(boardState, null);
            //aktiverSpieler = spielerAnzahl - 1;
            nextMove(true);
        }
    }

    private class WuerfelnEnde extends TimerTask {
        private final int spieler;

        public WuerfelnEnde(int spieler) {
            this.spieler = spieler;
        }

        @Override
        public void run() {
            if (zahlGewuerfelt < 1 && spieler == aktiverSpieler) {
                doBotMove(spieler, false);
                boolean ret = anClient.wuerfelnVorbei(clients[spieler]);
                if (!ret) removePlayerIntern(clients[spieler]);
            }
        }
    }

    private class ZiehenEnde extends TimerTask {
        private final int spieler;

        public ZiehenEnde(int spieler) {
            this.spieler = spieler;
        }

        @Override
        public void run() {
            if (spieler == aktiverSpieler) {
                doBotMove(spieler, false);
                boolean ret = anClient.ziehenVorbei(clients[spieler]);
                if (!ret) removePlayerIntern(clients[spieler]);
            }
        }
    }

    private class Waiting extends TimerTask {
        private final int spieler;

        private Waiting(int spieler) {
            this.spieler = spieler;
        }

        @Override
        public void run() {
            if (spieler == aktiverSpieler) {
                boolean ret = anClient.gifAnzeigen(clients[spieler]);
                if (!ret) removePlayerIntern(clients[spieler]);
            }
        }
    }

    private String namenHolen(Sitzung sitzung) {
        try {
            return sitzung.benutzername();
        } catch (RemoteException e) {
            System.err.println("Benutzername konnte nicht geholt werden");
            return "ERROR";
        }
    }
}
