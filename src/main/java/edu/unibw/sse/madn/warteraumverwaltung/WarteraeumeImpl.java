package edu.unibw.sse.madn.warteraumverwaltung;

import java.util.ArrayList;
import java.util.Collection;

public class WarteraeumeImpl implements Warteraeume {
    private final ArrayList<Warteraum> raeume = new ArrayList<>();

    void raumHinzufuegen(String[] namen, long id) {
        raeume.add(new WarteraumSimple(namen, id));
    }

    @Override
    public Collection<Warteraum> warteraeume() {
        return raeume;
    }

    private static class WarteraumSimple implements Warteraum {
        private final String[] namen;
        private final long id;

        public WarteraumSimple(String[] namen, long id) {
            this.namen = namen;
            this.id = id;
        }

        @Override
        public long id() {
            return id;
        }

        @Override
        public String[] namen() {
            return namen;
        }
    }
}
