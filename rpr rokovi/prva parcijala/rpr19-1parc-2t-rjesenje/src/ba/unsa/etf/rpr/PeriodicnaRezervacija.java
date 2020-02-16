package ba.unsa.etf.rpr;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PeriodicnaRezervacija extends Rezervacija {
    private int dan;

    public PeriodicnaRezervacija(String nazivSale, String predavac, LocalTime pocetak,
                                 LocalTime kraj, int dan) throws NeispravanFormatRezervacije {
        super(nazivSale, predavac, pocetak, kraj);
        this.dan = dan;
    }

    public PeriodicnaRezervacija(int dan) {
        this.dan = dan;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        String formatiraniPocetak = getPocetak().format(formatter);
        String formatiraniKraj = getKraj().format(formatter);

        String[] daniUSedmici = new String[]{"svakog ponedjeljka", "svakog utorka", "svake srijede",
                "svakog četvrtka", "svakog petka", "svake subote", "svake nedjelje"};

        return getNazivSale() + " - " + getPredavac() + " (periodična) - početak: " + formatiraniPocetak +
                ", kraj: " + formatiraniKraj + ", " + daniUSedmici[dan];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodicnaRezervacija that = (PeriodicnaRezervacija) o;
        return Objects.equals(getNazivSale(), that.getNazivSale()) &&
                Objects.equals(getPredavac(), that.getPredavac()) &&
                Objects.equals(getPocetak(), that.getPocetak()) &&
                Objects.equals(getKraj(), that.getKraj()) &&
                Objects.equals(getDan(), that.getDan());
    }

    @Override
    public int compareTo(Rezervacija o) {
        if(o instanceof VandrednaRezervacija){
            return -1;
        }

        return Integer.compare(getDan(), ((PeriodicnaRezervacija)o).getDan());
    }
}
