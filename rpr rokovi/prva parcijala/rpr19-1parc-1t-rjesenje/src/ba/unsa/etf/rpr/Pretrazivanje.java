package ba.unsa.etf.rpr;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface Pretrazivanje {
    List<Dogadjaj> filtrirajPoKriteriju(Predicate<Dogadjaj> kriterij);
    List<Dogadjaj> dajDogadjajeZaDan(LocalDateTime dan);
    List<Dogadjaj> dajSortiraneDogadjaje(BiFunction<Dogadjaj, Dogadjaj, Integer> kriterijSortiranja);
    Set<Dogadjaj> dajSortiranePoPrioritetu();
    boolean daLiSamSlobodan(LocalDateTime dan);
    boolean daLiSamSlobodan(LocalDateTime pocetak, LocalDateTime kraj);
}
