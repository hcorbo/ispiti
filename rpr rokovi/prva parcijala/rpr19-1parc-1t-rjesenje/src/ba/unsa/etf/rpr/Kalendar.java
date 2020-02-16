package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Kalendar implements Pretrazivanje{

    private List<Dogadjaj> kalendar = new ArrayList<>();

    @Override
    public String toString() {
        return kalendar.stream()
                .map(Dogadjaj::toString)
                .collect(Collectors.joining("\n"));
    }

    public void zakaziDogadjaj(Dogadjaj dogadjaj){
        kalendar.add(dogadjaj);
    }

    public void otkaziDogadjaj(Dogadjaj dogadjaj){
        kalendar.remove(dogadjaj);
    }

    public void zakaziDogadjaje(List<Dogadjaj> dogadjaji){
        kalendar.addAll(dogadjaji);
    }

    public void otkaziDogadjaje(List<Dogadjaj> dogadjaji){
        kalendar.removeAll(dogadjaji);
    }

    public void otkaziDogadjaje(Predicate<Dogadjaj> kriterij){
        List<Dogadjaj> zaOtkazati = kalendar.stream().filter(kriterij).collect(Collectors.toList());
        otkaziDogadjaje(zaOtkazati);
    }

    public List<Dogadjaj> dajKalendar(){
        return kalendar;
    }

    public Map<LocalDate, List<Dogadjaj>> dajKalendarPoDanima(){
        Map<LocalDate, List<Dogadjaj>> mapa = new HashMap<>();
        for (Dogadjaj dogadjaj : kalendar){
            if(!mapa.containsKey(dogadjaj.getPocetak().toLocalDate())){
                mapa.put(dogadjaj.getPocetak().toLocalDate(), new ArrayList<>());
            }

            mapa.get(dogadjaj.getPocetak().toLocalDate()).add(dogadjaj);
        }

        return mapa;
    }

    Dogadjaj dajSljedeciDogadjaj(LocalDateTime datum){
        List<Dogadjaj> nakonDatuma = kalendar.stream().
                filter(dogadjaj -> dogadjaj.getPocetak().isAfter(datum)).collect(Collectors.toList());

        if(nakonDatuma.isEmpty()){
            throw new IllegalArgumentException("Nemate događaja nakon navedenog datuma");
        }

        Dogadjaj prvi = nakonDatuma.get(0);
        return nakonDatuma.stream().reduce(prvi, (d1,d2) -> d1.getPocetak().isBefore(d2.getPocetak()) ? d1 : d2);
    }

    @Override
    public List<Dogadjaj> filtrirajPoKriteriju(Predicate<Dogadjaj> kriterij) {
        return kalendar.stream().filter(kriterij).collect(Collectors.toList());
    }

    @Override
    public List<Dogadjaj> dajDogadjajeZaDan(LocalDateTime dan) {
        return kalendar.stream().filter(dogadjaj -> dogadjaj.getPocetak().toLocalDate().equals(dan.toLocalDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dogadjaj> dajSortiraneDogadjaje(BiFunction<Dogadjaj, Dogadjaj, Integer> kriterijSortiranja) {
        return kalendar.stream().sorted(kriterijSortiranja::apply)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Dogadjaj> dajSortiranePoPrioritetu() {
        return new TreeSet<>(kalendar);
    }

    @Override
    public boolean daLiSamSlobodan(LocalDateTime dan) {
        return kalendar.stream().
                noneMatch(d -> d.getPocetak().equals(dan) ||
                       d.getKraj().equals(dan) ||
                       (d.getPocetak().isBefore(dan)
                       && d.getKraj().isAfter(dan)));
    }

    @Override
    public boolean daLiSamSlobodan(LocalDateTime pocetak, LocalDateTime kraj) {
        if(pocetak.isAfter(kraj)){
            throw new IllegalArgumentException("Neispravni podaci o početku i kraju");
        }

        return kalendar.stream()
                .noneMatch(d -> d.pocinjeIzmedju(pocetak, kraj)
                        || d.zavrsavaIzmedju(pocetak, kraj)
                        || d.trajeTokom(pocetak, kraj));
    }
}
