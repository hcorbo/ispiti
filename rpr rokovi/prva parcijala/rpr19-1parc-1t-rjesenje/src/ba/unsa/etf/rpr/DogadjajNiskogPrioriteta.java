package ba.unsa.etf.rpr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DogadjajNiskogPrioriteta extends Dogadjaj {
    public DogadjajNiskogPrioriteta(String naziv, LocalDateTime pocetak, LocalDateTime kraj) throws NeispravanFormatDogadjaja {
        super(naziv, pocetak, kraj);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu (kk:mm)");
        String formatiraniPocetak = getPocetak().format(formatter);
        String formatiraniKraj = getKraj().format(formatter);

        return getNaziv() + " (nizak prioritet) - početak: " +
                formatiraniPocetak + ", kraj: " + formatiraniKraj;
    }

    @Override
    public int compareTo(Dogadjaj o) {
        if((o instanceof DogadjajSrednjegPrioriteta) || (o instanceof DogadjajVisokogPrioriteta)){
            return -1;
        }

        return getNaziv().compareTo(o.getNaziv());
    }
}
