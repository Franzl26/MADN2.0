package edu.unibw.sse.madn.warteraumverwaltung;

import java.io.Serializable;
import java.util.Collection;

public interface Warteraeume extends Serializable {
    /**
     * @return Eine Collection aller Warteräume
     */
    Collection<Warteraum> warteraeume();
}
