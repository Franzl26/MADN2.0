package edu.unibw.sse.madn.datenServer;


import java.io.File;

public class SpielDesignImpl implements SpielDesign {
    @Override
    public String[] designListeHolen() {
        File f = new File("./resources/server/designs/");
        return f.list();
    }

    @Override
    public SpielfeldKonfigurationBytes spielfeldKonfigurationHolen(String name) {
        return SpielfeldKonfigurationBytesImpl.loadBoardKonfiguration("./resources/server/designs/" + name + "/");
    }

    @Override
    public boolean existiertDesign(String design) {
        File f = new File("./resources/server/designs/");
        String[] list = f.list();
        if (list == null) return false;
        for (String s : list) {
            if (s.equals(design)) return true;
        }
        return false;
    }
}
