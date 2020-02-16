package ba.unsa.etf.rpr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EvidencijaTest {
    private static Evidencija evidencija;
    private static List<Rezervacija> testneRezervacije = new ArrayList<>();

    @BeforeAll
    static void setUpClass() throws NeispravanFormatRezervacije {
        Rezervacija rezervacija1 = new PeriodicnaRezervacija("VA", "Miki Maus", LocalTime.of(9,0),
                LocalTime.of(12,0),1);

        Rezervacija rezervacija2 = new VandrednaRezervacija("1-15", "Paja Patak", LocalTime.of(14,30),
                LocalTime.of(16,30), LocalDate.of(2019,12,5));

        Rezervacija rezervacija3 = new PeriodicnaRezervacija("A3", "Meho Mehić", LocalTime.of(11,30),
                LocalTime.of(14,30),3);

        Rezervacija rezervacija4 = new VandrednaRezervacija("0-01", "Niko Nikić", LocalTime.of(17,0),
                LocalTime.of(19,0), LocalDate.of(2019,12,17));

        Rezervacija rezervacija5 = new PeriodicnaRezervacija("0-01", "Milo Milić", LocalTime.of(10,15),
                LocalTime.of(11,45),2);

        Rezervacija rezervacija6 = new VandrednaRezervacija("VA", "Dora Istražitelj", LocalTime.of(16,0),
                LocalTime.of(18,30), LocalDate.of(2019,12,20));

        evidencija = new Evidencija();
        testneRezervacije.add(rezervacija1);
        testneRezervacije.add(rezervacija2);
        testneRezervacije.add(rezervacija3);
        testneRezervacije.add(rezervacija4);
        testneRezervacije.add(rezervacija5);
        testneRezervacije.add(rezervacija6);
    }

    @BeforeEach
    void setUpTest() {
        evidencija = new Evidencija();
    }

    @Test
    void toStringTest() {
        for(int i = 2; i < 4; i++) {
            evidencija.rezervisi(testneRezervacije.get(i));
        }

        String ispravno = "A3 - Meho Mehić (periodična) - početak: 11:30, kraj: 14:30, svakog četvrtka\n";
        ispravno += "0-01 - Niko Nikić (vandredna) - početak: 17:00, kraj: 19:00, na dan 17/12/2019";

        assertEquals(ispravno, evidencija.toString());
    }

    @Test
    void testZakazi1() {
        evidencija.rezervisi(testneRezervacije.get(0));
        assertAll(
                () -> assertEquals(1, evidencija.dajSveRezervacije().size()),
                () -> assertTrue(evidencija.dajSveRezervacije().contains(testneRezervacije.get(0)))
        );
    }

    @Test
    void testZakazi2() {
        List<Rezervacija> dogadjaji = new ArrayList<>();
        dogadjaji.add(testneRezervacije.get(0));
        dogadjaji.add(testneRezervacije.get(1));

        evidencija.rezervisi(dogadjaji);
        assertAll(
                () -> assertEquals(2, evidencija.dajSveRezervacije().size()),
                () -> assertTrue(evidencija.dajSveRezervacije().contains(testneRezervacije.get(0))),
                () -> assertTrue(evidencija.dajSveRezervacije().contains(testneRezervacije.get(1)))
        );
    }

    @Test
    void testOtkazi1() {
        evidencija.rezervisi(testneRezervacije.get(0));
        evidencija.rezervisi(testneRezervacije.get(1));

        evidencija.otkaziRezervaciju(testneRezervacije.get(0));

        assertAll(
                () -> assertEquals(1, evidencija.dajSveRezervacije().size()),
                () -> assertTrue(evidencija.dajSveRezervacije().contains(testneRezervacije.get(1))),
                () -> assertFalse(evidencija.dajSveRezervacije().contains(testneRezervacije.get(0)))
        );
    }

    @Test
    void testOtkazi2() {
        evidencija.rezervisi(testneRezervacije);

        List<Rezervacija> rezervacije = new ArrayList<>();
        rezervacije.add(testneRezervacije.get(0));
        rezervacije.add(testneRezervacije.get(1));

        evidencija.otkaziRezervacije(rezervacije);

        assertAll(
                () -> assertEquals(4, evidencija.dajSveRezervacije().size()),
                () -> assertFalse(evidencija.dajSveRezervacije().contains(testneRezervacije.get(1))),
                () -> assertFalse(evidencija.dajSveRezervacije().contains(testneRezervacije.get(0))),
                () -> assertTrue(evidencija.dajSveRezervacije().contains(testneRezervacije.get(2)))
        );
    }

    @Test
    void testOtkazi3() {
        evidencija.rezervisi(testneRezervacije);

        evidencija.otkaziRezervacije(dogadjaj -> dogadjaj.getNazivSale().equals("A3"));

        assertAll(
                () -> assertEquals(5, evidencija.dajSveRezervacije().size()),
                () -> assertFalse(evidencija.dajSveRezervacije().contains(testneRezervacije.get(2)))
        );
    }

    @Test
    void testDajEvidencijuZaSalu1() {
        evidencija.rezervisi(testneRezervacije);
        List<Rezervacija> rezervacije = evidencija.dajEvidencijuZaSalu("VA");

        assertAll(
                () -> assertTrue(rezervacije.contains(testneRezervacije.get(0))),
                () -> assertTrue(rezervacije.contains(testneRezervacije.get(5))),
                () -> assertEquals(2, rezervacije.size())
        );
    }

    @Test
    void testDajEvidencijuZaSalu2() {
        evidencija.rezervisi(testneRezervacije);

        assertThrows(IllegalArgumentException.class,
                () -> evidencija.dajEvidencijuZaSalu("2-02"),
                "Za salu 2-02 ne postoje evidentirane rezervacije"
                );
    }

    @Test
    void testDajEvidenciju() {
        evidencija.rezervisi(testneRezervacije);
        Map<String, List<Rezervacija>> mapa = evidencija.dajEvidenciju();

        List<Rezervacija> zaVa = mapa.get("VA");
        List<Rezervacija> za01 = mapa.get("0-01");
        List<Rezervacija> zaA3 = mapa.get("A3");

        assertAll(
                () -> assertTrue(zaVa.contains(testneRezervacije.get(0))),
                () -> assertTrue(zaVa.contains(testneRezervacije.get(5))),
                () -> assertTrue(za01.contains(testneRezervacije.get(3))),
                () -> assertTrue(za01.contains(testneRezervacije.get(4))),
                () -> assertTrue(zaA3.contains(testneRezervacije.get(2))),
                () -> assertEquals(4, mapa.size())
        );
    }

    @Test
    void testDajSveRezervacije(){
        evidencija.rezervisi(testneRezervacije);
        assertEquals(testneRezervacije, evidencija.dajSveRezervacije());
    }

    @Test
    void testFiltriraj() {
        evidencija.rezervisi(testneRezervacije);

        List<Rezervacija> filtrirani = evidencija.filtrirajPoKriteriju(d ->
                d.getPocetak().getHour() >= 12);

        assertAll(
                () -> assertEquals(3, filtrirani.size()),
                () -> assertTrue(filtrirani.contains(testneRezervacije.get(1))),
                () -> assertTrue(filtrirani.contains(testneRezervacije.get(3))),
                () -> assertTrue(filtrirani.contains(testneRezervacije.get(5)))
        );
    }

    @Test
    void testDajRezervacijeZaDan1() throws NeispravanFormatRezervacije {
        evidencija.rezervisi(testneRezervacije);
        Rezervacija rez = new PeriodicnaRezervacija("0-01", "Miki Maus", LocalTime.of(9,0),
                LocalTime.of(12,0),1);
        evidencija.rezervisi(rez);

        List<Rezervacija> zaDan = evidencija.dajRezervacijeZaDan("0-01",LocalDate.of(2019,12,17));

        assertAll(
                () -> assertEquals(2, zaDan.size()),
                () -> assertTrue(zaDan.contains(testneRezervacije.get(3))),
                () -> assertTrue(zaDan.contains(rez))
        );
    }

    @Test
    void testDajSortirane1() {
        evidencija.rezervisi(testneRezervacije);

        List<Rezervacija> sortirani = evidencija.dajSortiraneRezervacije("0-01",
                (r1, r2) -> r1.getKraj().compareTo(r2.getKraj()));

        assertAll(
                () -> assertEquals(testneRezervacije.get(3), sortirani.get(1)),
                () -> assertEquals(testneRezervacije.get(4), sortirani.get(0)),
                () -> assertEquals(2, sortirani.size())
        );
    }

    @Test
    void testDajSortirane2() {
        evidencija.rezervisi(testneRezervacije);

        List<Rezervacija> sortirani = evidencija.dajSortiraneRezervacije("0-01",
                (r1, r2) -> -r1.getPocetak().compareTo(r2.getPocetak()));

        assertAll(
                () -> assertEquals(testneRezervacije.get(3), sortirani.get(0)),
                () -> assertEquals(testneRezervacije.get(4), sortirani.get(1)),
                () -> assertEquals(2, sortirani.size())
        );
    }

    @Test
    void testDajSortiranePoTipu1() throws NeispravanFormatRezervacije {
        evidencija.rezervisi(testneRezervacije);

        Rezervacija rez = new PeriodicnaRezervacija("0-01", "Miki Maus", LocalTime.of(9,0),
                LocalTime.of(12,0),1);
        Rezervacija rez2 = new VandrednaRezervacija("0-01", "predavac", LocalTime.of(14,30),
                LocalTime.of(15,30), LocalDate.of(2019,12,20));
        Rezervacija rez3 =  new PeriodicnaRezervacija("0-01", "predavac2", LocalTime.of(9,0),
                LocalTime.of(12,0),0);
        evidencija.rezervisi(rez);
        evidencija.rezervisi(rez2);
        evidencija.rezervisi(rez3);

        Set<Rezervacija> sortirane = evidencija.dajSortiranePoTipu("0-01");
        List<Rezervacija> ispravno = new ArrayList<>();
        ispravno.add(rez3);
        ispravno.add(rez);
        ispravno.add(testneRezervacije.get(4));
        ispravno.add(testneRezervacije.get(3));
        ispravno.add(rez2);

        assertAll(
                () -> assertEquals(5, sortirane.size()),
                () -> assertArrayEquals(ispravno.toArray(), sortirane.toArray())
        );
    }


    @Test
    void testDajRezervacijeNakon1() throws NeispravanFormatRezervacije {
        evidencija.rezervisi(testneRezervacije);
        Rezervacija rez2 = new VandrednaRezervacija("0-01", "predavac", LocalTime.of(14,30),
                LocalTime.of(15,30), LocalDate.of(2019,12,20));
        evidencija.rezervisi(rez2);

        List<Rezervacija> nakon = evidencija.dajRezervacijeNakon("0-01", LocalDate.of(2019,12,18));

        assertAll(
                () -> assertEquals(1, nakon.size()),
                () -> assertTrue(nakon.contains(rez2))
        );
    }

    @Test
    void testDajRezervacijeNakon2() throws NeispravanFormatRezervacije {
        evidencija.rezervisi(testneRezervacije);
        Rezervacija rez2 = new VandrednaRezervacija("0-01", "predavac", LocalTime.of(14,30),
                LocalTime.of(15,30), LocalDate.of(2019,12,20));
        evidencija.rezervisi(rez2);

        List<Rezervacija> nakon = evidencija.dajRezervacijeNakon("0-01", LocalDate.of(2019,12,20));

        assertTrue(nakon.isEmpty());
    }

    @Test
    void testDaLiJeSlobodna1() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(17,0), LocalTime.of(18,0));

        assertFalse(slobodna);
    }

    @Test
    void testDaLiJeSlobodna2() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(17,0), LocalTime.of(19,30));

        assertFalse(slobodna);
    }

    @Test
    void testDaLiJeSlobodna3() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(16,0), LocalTime.of(19,30));

        assertFalse(slobodna);
    }

    @Test
    void testDaLiJeSlobodna4() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(16,0), LocalTime.of(18,0));

        assertFalse(slobodna);
    }

    @Test
    void testDaLiJeSlobodna5() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(18,0), LocalTime.of(18,30));

        assertFalse(slobodna);
    }

    @Test
    void testDaLiJeSlobodna6() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,17),
                LocalTime.of(14,0), LocalTime.of(17,0));

        assertTrue(slobodna);
    }

    @Test
    void testDaLiJeSlobodna7() {
        evidencija.rezervisi(testneRezervacije);

        boolean slobodna = evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,13),
                LocalTime.of(10,0), LocalTime.of(12,0));

        assertFalse(slobodna);
    }

    @Test
    void testIzuzeci() {
        //testira isti izuzetak koji se baca iz navedenih metoda
        evidencija.rezervisi(testneRezervacije);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evidencija.daLiJeSlobodna("0-01", LocalDate.of(2019,12,13),
                                LocalTime.of(10,0), LocalTime.of(9,0)),
                        "Neispravni početak i kraj rezervacije"),
                () ->  assertThrows(IllegalArgumentException.class,
                        () -> evidencija.daLiJeSlobodna("2-02", LocalDate.of(2019,12,13),
                                LocalTime.of(10,0), LocalTime.of(12,0)),
                        "Za salu 2-02 ne postoje evidentirane rezervacije"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evidencija.dajRezervacijeNakon("2-02", LocalDate.of(2019,12,20)),
                        "Za salu 2-02 ne postoje evidentirane rezervacije"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evidencija.dajSortiranePoTipu("2-02"),
                        "Za salu 2-02 ne postoje evidentirane rezervacije"),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> evidencija.dajRezervacijeZaDan("2-02", LocalDate.of(2019,12,17)),
                        "Za salu 2-02 ne postoje evidentirane rezervacije")
        );
    }
}