package ba.unsa.etf.rpr;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Dogadjaj implements Comparable<Dogadjaj>{
    private String naziv;
    private LocalDateTime pocetak, kraj;

    private static void validirajDatume(LocalDateTime pocetak, LocalDateTime kraj) throws NeispravanFormatDogadjaja {
        if(kraj.isBefore(pocetak)){
            throw new NeispravanFormatDogadjaja("Neispravan format početka i kraja dogadjaja");
        }
    }

    public Dogadjaj(String naziv, LocalDateTime pocetak, LocalDateTime kraj) throws NeispravanFormatDogadjaja {
        validirajDatume(pocetak, kraj);
        this.naziv = naziv;
        this.pocetak = pocetak;
        this.kraj = kraj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public LocalDateTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) throws NeispravanFormatDogadjaja {
        validirajDatume(pocetak, getKraj());
        this.pocetak = pocetak;
    }

    public LocalDateTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalDateTime kraj) throws NeispravanFormatDogadjaja {
        validirajDatume(getPocetak(), kraj);
        this.kraj = kraj;
    }

    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dogadjaj dogadjaj = (Dogadjaj) o;
        return Objects.equals(naziv, dogadjaj.naziv) &&
                Objects.equals(pocetak, dogadjaj.pocetak) &&
                Objects.equals(kraj, dogadjaj.kraj);
    }

    @Override
    public abstract int compareTo(Dogadjaj o);

    public boolean pocinjeIzmedju(LocalDateTime pocetak, LocalDateTime kraj) {
        return (getPocetak().equals(pocetak) || getPocetak().isAfter(pocetak)) &&
                (getPocetak().equals(kraj) || getPocetak().isBefore(kraj));
    }

    public boolean zavrsavaIzmedju(LocalDateTime pocetak, LocalDateTime kraj) {
        return (getKraj().isBefore(kraj) || getKraj().equals(kraj)) &&
                (getKraj().isAfter(pocetak) || getKraj().equals(pocetak));
    }

    public boolean trajeTokom(LocalDateTime pocetak, LocalDateTime kraj){
        return (getPocetak().isBefore(pocetak) || getPocetak().equals(pocetak)) &&
                (getKraj().isAfter(kraj) || getKraj().equals(kraj));
    }
}
