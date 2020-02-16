package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class VandrednaRezervacija extends Rezervacija {
    private LocalDate datum;

    public VandrednaRezervacija(String nazivSale, String predavac,
                                LocalTime pocetak, LocalTime kraj, LocalDate datum) throws NeispravanFormatRezervacije {
        super(nazivSale, predavac, pocetak, kraj);
        this.datum = datum;
    }

    public VandrednaRezervacija(LocalDate datum) {
        this.datum = datum;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatterVrijeme = DateTimeFormatter.ofPattern("kk:mm");
        String formatiraniPocetak = getPocetak().format(formatterVrijeme);
        String formatiraniKraj = getKraj().format(formatterVrijeme);

        DateTimeFormatter formatterDatum = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        String formatiraniDatum = getDatum().format(formatterDatum);

        return getNazivSale() + " - " + getPredavac() + " (vandredna) - početak: " + formatiraniPocetak +
                ", kraj: " + formatiraniKraj + ", na dan " + formatiraniDatum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VandrednaRezervacija that = (VandrednaRezervacija) o;
        return Objects.equals(getNazivSale(), that.getNazivSale()) &&
                Objects.equals(getPredavac(), that.getPredavac()) &&
                Objects.equals(getPocetak(), that.getPocetak()) &&
                Objects.equals(getKraj(), that.getKraj()) &&
                Objects.equals(getDatum(), that.getDatum());
    }

    @Override
    public int compareTo(Rezervacija o) {
        if(o instanceof PeriodicnaRezervacija){
            return 1;
        }

        return getDatum().compareTo(((VandrednaRezervacija)o).getDatum());
    }
}
