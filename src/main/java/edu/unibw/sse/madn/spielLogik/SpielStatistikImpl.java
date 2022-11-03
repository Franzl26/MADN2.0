package edu.unibw.sse.madn.spielLogik;

public class SpielStatistikImpl implements SpielStatistik{
    private String[] names;
    private final int[][] zahlenGewuerfelt;
    private final int[] andereGeschlagen;
    private final int[] geschlagenWorden;
    private final int[] prioZugFalsch;
    private final String[] platzierungen;
    private final long startZeit;

    public SpielStatistikImpl() {
        this.names = new String[4];
        zahlenGewuerfelt = new int[4][6];
        andereGeschlagen = new int[4];
        geschlagenWorden = new int[4];
        platzierungen = new String[4];
        prioZugFalsch = new int[4];
        startZeit = System.currentTimeMillis();
    }

    public void namenSetzen(String[] names) {
        this.names = names.clone();
    }

    public void incZahlGewuerfelt(int spieler, int zahl) {
        zahlenGewuerfelt[spieler][zahl]++;
    }

    public void incAndereGeschlagen(int spieler) {
        andereGeschlagen[spieler]++;
    }

    public void incGeschlagenWorden(int spieler) {
        geschlagenWorden[spieler]++;
    }

    public void incPrioZugIgnoriert(int spieler) {
        prioZugFalsch[spieler]++;
    }

    public void platzierungSetzen(int platz, String name) {
        platzierungen[platz] = name;
    }


    @Override
    public int[][] zahlenGewuerfelt() {
        return zahlenGewuerfelt;
    }

    @Override
    public int[] andereGeschlagen() {
        return andereGeschlagen;
    }

    @Override
    public int[] geschlagenWorden() {
        return geschlagenWorden;
    }

    @Override
    public int[] prioZugFalsch() {
        return prioZugFalsch;
    }

    @Override
    public String[] platzierung() {
        return platzierungen;
    }

    @Override
    public long startZeit() {
        return startZeit;
    }

    @Override
    public String[] namen() {
        return new String[0];
    }
}
