package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Evidencija implements Pretrazivanje{
    private List<Rezervacija> rezervacije = new ArrayList<>();

    public void rezervisi(Rezervacija rezervacija){
        rezervacije.add(rezervacija);
    }

    public void rezervisi(List<Rezervacija> rezervacije){
        this.rezervacije.addAll(rezervacije);
    }

    public void otkaziRezervaciju(Rezervacija rezervacija){
        rezervacije.remove(rezervacija);
    }

    public void otkaziRezervacije(List<Rezervacija> rezervacije){
        this.rezervacije.removeAll(rezervacije);
    }

    public void otkaziRezervacije(Predicate<Rezervacija> kriterij){
        List<Rezervacija> zaOtkazati = rezervacije.stream().filter(kriterij).collect(Collectors.toList());
        otkaziRezervacije(zaOtkazati);
    }

    public Map<String, List<Rezervacija>> dajEvidenciju(){
        Map<String, List<Rezervacija>> evidencija = new HashMap<>();
        for (Rezervacija rezervacija : rezervacije){
            if(!evidencija.containsKey(rezervacija.getNazivSale())){
                evidencija.put(rezervacija.getNazivSale(), new ArrayList<>());
            }

            evidencija.get(rezervacija.getNazivSale()).add(rezervacija);
        }

        return evidencija;
    }

    public List<Rezervacija> dajEvidencijuZaSalu(String nazivSale){
        List<Rezervacija> evidencija = dajEvidenciju().get(nazivSale);
        if (evidencija == null){
            throw new IllegalArgumentException("Za salu " + nazivSale + " ne postoje evidentirane rezervacije");
        }

        return evidencija;
    }

    public List<Rezervacija> dajSveRezervacije(){
        return rezervacije;
    }

    @Override
    public String toString() {
        return rezervacije.stream()
                .map(Rezervacija::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<Rezervacija> filtrirajPoKriteriju(Predicate<Rezervacija> kriterij) {
        return rezervacije.stream().filter(kriterij).collect(Collectors.toList());
    }

    @Override
    public List<Rezervacija> dajRezervacijeZaDan(String nazivSale, LocalDate datum) {
        provjeriSalu(nazivSale);

       return  rezervacije.stream().
                filter(rezervacija -> {
                    if(rezervacija.getNazivSale() != nazivSale) return false;
                    if(rezervacija instanceof VandrednaRezervacija){
                        return ((VandrednaRezervacija) rezervacija).getDatum().equals(datum);
                    }else{
                        return ((PeriodicnaRezervacija) rezervacija).getDan() == datum.getDayOfWeek().getValue()-1;
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<Rezervacija> dajSortiraneRezervacije(String nazivSale, BiFunction<Rezervacija, Rezervacija, Integer> kriterijSortiranja) {
        provjeriSalu(nazivSale);
        return rezervacije.stream().filter(rezervacija -> rezervacija.getNazivSale().equals(nazivSale))
                .sorted(kriterijSortiranja::apply)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Rezervacija> dajSortiranePoTipu(String nazivSale) {
        provjeriSalu(nazivSale);
        return new TreeSet<>(rezervacije.stream().filter(r -> r.getNazivSale().equals(nazivSale)).collect(Collectors.toList()));
    }

    @Override
    public List<Rezervacija> dajRezervacijeNakon(String nazivSale, LocalDate datum) {
        provjeriSalu(nazivSale);
        return rezervacije.stream().filter(rezervacija -> {
            if (!rezervacija.getNazivSale().equals(nazivSale) || !(rezervacija instanceof VandrednaRezervacija)){
                return false;
            }

            return ((VandrednaRezervacija) rezervacija).getDatum().isAfter(datum);
        }).collect(Collectors.toList());
    }

    @Override
    public boolean daLiJeSlobodna(String nazivSale, LocalDate datum, LocalTime pocetak, LocalTime kraj) {
        provjeriSalu(nazivSale);

        if(kraj.isBefore(pocetak)){
            throw new IllegalArgumentException("Neispravni početak i kraj rezervacije");
        }

        return !rezervacije.stream().filter(rezervacija -> {
            if(!nazivSale.equals(rezervacija.getNazivSale())){
                return false;
            }

            if(rezervacija instanceof VandrednaRezervacija){
                return ((VandrednaRezervacija) rezervacija).getDatum().equals(datum) &&
                        !postojiPresjek(rezervacija.getPocetak(), rezervacija.getKraj(), pocetak,kraj);
            }else{
                return ((PeriodicnaRezervacija) rezervacija).getDan() == datum.getDayOfWeek().getValue() - 1 &&
                        !postojiPresjek(rezervacija.getPocetak(), rezervacija.getKraj(), pocetak,kraj);
            }
        }).collect(Collectors.toList()).isEmpty();
    }

    private void provjeriSalu(String sala){
        boolean postoji =  rezervacije.stream().anyMatch(rezervacija -> rezervacija.getNazivSale().equals(sala));
        if(!postoji){
            throw new IllegalArgumentException("Za salu " + sala + " ne postoje evidentirane rezervacije");
        }
    }

    public static boolean postojiPresjek(LocalTime pocetak1, LocalTime kraj1, LocalTime pocetak2, LocalTime kraj2){
        // return ! k2 <= p1 || p2 >= k1;
        return !((kraj2.isBefore(pocetak1) || kraj2.equals(pocetak1)) || (pocetak2.isAfter(kraj1) || pocetak2.equals(kraj1)));
    }
}
