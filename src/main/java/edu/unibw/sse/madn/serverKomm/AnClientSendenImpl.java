package edu.unibw.sse.madn.serverKomm;

import edu.unibw.sse.madn.spielLogik.FeldBesetztStatus;
import edu.unibw.sse.madn.spielLogik.SpielStatistik;
import edu.unibw.sse.madn.warteraumverwaltung.Warteraeume;

import java.rmi.RemoteException;

public class AnClientSendenImpl implements AnClientSendenSpiel, AnClientSendenRaumauswahl {
    @Override
    public boolean raeumeUpdaten(Sitzung sitzung, Warteraeume warteraeume) {
        try {
            sitzung.clientCallback().raeumeUpdaten(warteraeume);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean warteraumNamenUpdaten(Sitzung sitzung, String[] namen) {
        try {
            sitzung.clientCallback().warteraumNamenUpdaten(namen);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean spielStartet(Sitzung sitzung, String design) {
        try {
            sitzung.clientCallback().spielStartet(design);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean spielfeldUpdaten(Sitzung sitzung, FeldBesetztStatus[] feld, int[] geandert) {
        try {
            sitzung.clientCallback().spielfeldUpdaten(feld, geandert);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean spielNamenUpdaten(Sitzung sitzung, String[] namen) {
        try {
            sitzung.clientCallback().spielNamenUpdaten(namen);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean aktuellenSpielerSetzen(Sitzung sitzung, int spieler) {
        try {
            sitzung.clientCallback().aktuellenSpielerSetzen(spieler);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean wuerfelUpdaten(Sitzung sitzung, int wert) {
        try {
            sitzung.clientCallback().wuerfelUpdaten(wert);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean wuerfelnVorbei(Sitzung sitzung) {
        try {
            sitzung.clientCallback().wuerfelnVorbei();
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean ziehenVorbei(Sitzung sitzung) {
        try {
            sitzung.clientCallback().ziehenVorbei();
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean gifAnzeigen(Sitzung sitzung) {
        try {
            sitzung.clientCallback().gifAnzeigen();
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean spielVorbei(Sitzung sitzung, SpielStatistik statistik) {
        try {
            sitzung.clientCallback().spielVorbei(statistik);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
