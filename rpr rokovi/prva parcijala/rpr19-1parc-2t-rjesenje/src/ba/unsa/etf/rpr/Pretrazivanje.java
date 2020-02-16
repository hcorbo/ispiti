package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface Pretrazivanje {
    List<Rezervacija> filtrirajPoKriteriju(Predicate<Rezervacija> kriterij);
    List<Rezervacija> dajRezervacijeZaDan(String nazivSale, LocalDate datum);
    List<Rezervacija> dajSortiraneRezervacije(String nazivSale, BiFunction<Rezervacija, Rezervacija, Integer> kriterijSortiranja);
    Set<Rezervacija> dajSortiranePoTipu(String nazivSale);
    List<Rezervacija> dajRezervacijeNakon(String nazivSale, LocalDate datum);
    boolean daLiJeSlobodna(String nazivSale, LocalDate datum, LocalTime pocetak, LocalTime kraj);
}
