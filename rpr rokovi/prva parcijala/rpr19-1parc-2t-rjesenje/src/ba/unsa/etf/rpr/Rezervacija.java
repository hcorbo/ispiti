package ba.unsa.etf.rpr;

import java.time.LocalTime;
import java.util.Objects;

public abstract class Rezervacija implements Comparable<Rezervacija>{
    private String nazivSale;
    private String predavac;
    private LocalTime pocetak, kraj;

    private static void validirajVrijeme(LocalTime pocetak, LocalTime kraj) throws NeispravanFormatRezervacije {
        if(kraj.isBefore(pocetak)){
            throw new NeispravanFormatRezervacije("Neispravan format početka i kraja rezervacije");
        }
    }


    public Rezervacija(String nazivSale, String predavac, LocalTime pocetak, LocalTime kraj) throws NeispravanFormatRezervacije {
        validirajVrijeme(pocetak, kraj);
        this.nazivSale = nazivSale;
        this.predavac = predavac;
        this.pocetak = pocetak;
        this.kraj = kraj;
    }

    public Rezervacija() {
    }

    public String getNazivSale() {
        return nazivSale;
    }

    public void setNazivSale(String nazivSale) {
        this.nazivSale = nazivSale;
    }

    public String getPredavac() {
        return predavac;
    }

    public void setPredavac(String predavac) {
        this.predavac = predavac;
    }

    public LocalTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalTime pocetak) throws NeispravanFormatRezervacije {
        validirajVrijeme(pocetak, getKraj());
        this.pocetak = pocetak;
    }

    public LocalTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalTime kraj) throws NeispravanFormatRezervacije {
        validirajVrijeme(getPocetak(), kraj);
        this.kraj = kraj;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract  int compareTo(Rezervacija o);
}
