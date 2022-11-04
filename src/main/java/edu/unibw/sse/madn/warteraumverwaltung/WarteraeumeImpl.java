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

    private record WarteraumSimple(String[] namen, long id) implements Warteraum {
    }
}
