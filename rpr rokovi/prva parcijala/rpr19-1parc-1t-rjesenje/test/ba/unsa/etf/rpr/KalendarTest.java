package ba.unsa.etf.rpr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KalendarTest {
    private static Kalendar kalendar;
    private static List<Dogadjaj> testniDogadjaji = new ArrayList<>();


    @BeforeAll
    static void setUpClass() throws NeispravanFormatDogadjaja {

        Dogadjaj mojRodjendan = new DogadjajNiskogPrioriteta("Moj rođendan",
                LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(11,20,0)),
                LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(23,59,0)));

        Dogadjaj praznik = new DogadjajSrednjegPrioriteta("Dan državnosti",
                LocalDateTime.of(LocalDate.of(2019,11,25), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2019,11,25), LocalTime.of(23,59,0)));

        Dogadjaj ispit = new DogadjajVisokogPrioriteta("Ispit iz RPR-a",
                LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(11,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(15,0,0)));

        Dogadjaj putovanje = new DogadjajVisokogPrioriteta("Put na Maldive",
                LocalDateTime.of(LocalDate.of(2019,12,17), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,23), LocalTime.of(20,0,0)));

        Dogadjaj test = new DogadjajNiskogPrioriteta("testni dogadjaj",
                LocalDateTime.of(LocalDate.of(2019,12,18), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,23), LocalTime.of(20,0,0)));

        testniDogadjaji.add(mojRodjendan);
        testniDogadjaji.add(praznik);
        testniDogadjaji.add(ispit);
        testniDogadjaji.add(putovanje);
        testniDogadjaji.add(test);
    }

    // Vraćamo kalendar u polazno stanje prije svakog testa
    @BeforeEach
    void setUpTest() {
        kalendar = new Kalendar();
    }

    @Test
    void toStringTest() {
        for(int i = 2; i < 4; i++) {
            kalendar.zakaziDogadjaj(testniDogadjaji.get(i));
        }

        String ispravno = "Ispit iz RPR-a (visok prioritet) - početak: 07/12/2019 (11:00), kraj: 07/12/2019 (15:00)\n";
        ispravno += "Put na Maldive (visok prioritet) - početak: 17/12/2019 (10:00), kraj: 23/12/2019 (20:00)";

        assertEquals(ispravno, kalendar.toString());
    }

    @Test
    void testZakazi1() {
        kalendar.zakaziDogadjaj(testniDogadjaji.get(0));
        assertAll(
                () -> assertEquals(1, kalendar.dajKalendar().size()),
                () -> assertTrue(kalendar.dajKalendar().contains(testniDogadjaji.get(0)))
        );
    }

    @Test
    void testZakazi2() {
        // Metoda koja prima listu
        List<Dogadjaj> dogadjaji = new ArrayList<>();
        dogadjaji.add(testniDogadjaji.get(0));
        dogadjaji.add(testniDogadjaji.get(1));

        kalendar.zakaziDogadjaje(dogadjaji);
        assertAll(
                () -> assertEquals(2, kalendar.dajKalendar().size()),
                () -> assertTrue(kalendar.dajKalendar().contains(testniDogadjaji.get(0))),
                () -> assertTrue(kalendar.dajKalendar().contains(testniDogadjaji.get(1)))
        );
    }

    @Test
    void testOtkazi1() {
        kalendar.zakaziDogadjaj(testniDogadjaji.get(0));
        kalendar.zakaziDogadjaj(testniDogadjaji.get(1));

        kalendar.otkaziDogadjaj(testniDogadjaji.get(0));

        assertAll(
                () -> assertEquals(1, kalendar.dajKalendar().size()),
                () -> assertTrue(kalendar.dajKalendar().contains(testniDogadjaji.get(1))),
                () -> assertFalse(kalendar.dajKalendar().contains(testniDogadjaji.get(0)))
        );
    }


    @Test
    void testOtkazi2() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        List<Dogadjaj> dogadjaji = new ArrayList<>();
        dogadjaji.add(testniDogadjaji.get(0));
        dogadjaji.add(testniDogadjaji.get(1));

        // Metoda koja prima listu
        kalendar.otkaziDogadjaje(dogadjaji);

        assertAll(
                () -> assertEquals(3, kalendar.dajKalendar().size()),
                () -> assertFalse(kalendar.dajKalendar().contains(testniDogadjaji.get(1))),
                () -> assertFalse(kalendar.dajKalendar().contains(testniDogadjaji.get(0))),
                () -> assertTrue(kalendar.dajKalendar().contains(testniDogadjaji.get(2)))
        );
    }

    @Test
    void testOtkazi3() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        // Metoda koja prima lambda funkciju
        kalendar.otkaziDogadjaje(dogadjaj -> dogadjaj.getNaziv().equals("Put na Maldive"));

        assertAll(
                () -> assertEquals(4, kalendar.dajKalendar().size()),
                () -> assertFalse(kalendar.dajKalendar().contains(testniDogadjaji.get(3)))
        );
    }

    @Test
    void testDajKalendar() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        List<Dogadjaj> sviDogadjaji = kalendar.dajKalendar();
        assertArrayEquals(sviDogadjaji.toArray(), testniDogadjaji.toArray());
    }

    @Test
    void testDajKalendarPoDanima() throws NeispravanFormatDogadjaja {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        Dogadjaj dog = new DogadjajNiskogPrioriteta("dogadjaj",
                LocalDateTime.of(LocalDate.of(2019,12,18), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,30), LocalTime.of(20,0,0)));
        kalendar.zakaziDogadjaj(dog);

        Map<LocalDate, List<Dogadjaj>> mapa = kalendar.dajKalendarPoDanima();

        List<Dogadjaj> za18 = mapa.get(LocalDate.of(2019,12,18));
        List<Dogadjaj> za25 = mapa.get(LocalDate.of(2019,11,25));

        assertAll(
                () -> assertTrue(za18.contains(testniDogadjaji.get(4))),
                () -> assertTrue(za18.contains(dog)),
                () -> assertTrue(za25.contains(testniDogadjaji.get(1))),
                () -> assertEquals(1, za25.size()),
                () -> assertEquals(2, za18.size()),
                () -> assertEquals(5, mapa.size())
        );
    }

    @Test
    void testDajSljedeciDogadjaj1() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        Dogadjaj sljedeci = kalendar.dajSljedeciDogadjaj(LocalDateTime.of(LocalDate.of(2019,11,27),
                LocalTime.of(13,0,0)));

        assertEquals(sljedeci, testniDogadjaji.get(2));
    }

    @Test
    void testDajSljedeciDogadjaj2() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        assertThrows(IllegalArgumentException.class,
                () -> kalendar.dajSljedeciDogadjaj(LocalDateTime.of(LocalDate.of(2019,12,20),
                        LocalTime.of(13,0,0))),
                "Nemate događaja nakon navedenog datuma"
        );
    }

    @Test
    void testFiltriraj() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        List<Dogadjaj> filtrirani = kalendar.filtrirajPoKriteriju(d ->
                d.getPocetak().toLocalDate().getMonth().getValue() == 12);

        assertAll(
                () -> assertEquals(3, filtrirani.size()),
                () -> assertTrue(filtrirani.contains(testniDogadjaji.get(3))),
                () -> assertTrue(filtrirani.contains(testniDogadjaji.get(4)))
        );
    }

    @Test
    void testDajDogadjajeZaDan1() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        List<Dogadjaj> zaDan = kalendar.dajDogadjajeZaDan(LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(0,0,0)));
        assertAll(
                () -> assertEquals(1, zaDan.size()),
                () -> assertEquals(testniDogadjaji.get(2), zaDan.get(0))
        );
    }

    @Test
    void testDajDogadjajeZaDan2() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        List<Dogadjaj> zaDan = kalendar.dajDogadjajeZaDan(LocalDateTime.of(LocalDate.of(2019,11,1), LocalTime.of(0,0,0)));
        assertEquals(0, zaDan.size());
    }


    @Test
    void testDajSortiraneDogadjaje1() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        String[] sortiraniNazivi = new String[]{
                "Dan državnosti",
                "Ispit iz RPR-a",
                "Moj rođendan",
                "Put na Maldive",
                "testni dogadjaj"
        };

        List<Dogadjaj> sortirani = kalendar.
                dajSortiraneDogadjaje((d1,d2) -> d1.getNaziv().compareTo(d2.getNaziv()));

        assertArrayEquals(sortiraniNazivi, sortirani.stream().map(Dogadjaj::getNaziv).toArray());
    }

    @Test
    void testDajSortiraneDogadjaje2() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        String[] sortiraniNazivi = new String[]{
                "testni dogadjaj",
                "Put na Maldive",
                "Moj rođendan",
                "Ispit iz RPR-a",
                "Dan državnosti"
        };

        List<Dogadjaj> sortirani = kalendar.
                dajSortiraneDogadjaje((d1,d2) -> d1.getNaziv().compareTo(d2.getNaziv()) * -1);

        assertArrayEquals(sortiraniNazivi, sortirani.stream().map(Dogadjaj::getNaziv).toArray());
    }

    @Test
    void testdajSortiranePoPrioritetu() {
        kalendar.zakaziDogadjaje(testniDogadjaji);

        String[] nazivi = {
                "Moj rođendan",
                "testni dogadjaj",
                "Dan državnosti",
                "Ispit iz RPR-a",
                "Put na Maldive"
        };

        Set<Dogadjaj> sortirani = kalendar.dajSortiranePoPrioritetu();
        assertArrayEquals(nazivi, sortirani.stream().
                map(Dogadjaj::getNaziv).toArray());
    }

    @Test
    void testDaLiSamSlobodan1() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(testniDogadjaji.get(0).getPocetak()));
    }

    @Test
    void testDaLiSamSlobodan2() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(testniDogadjaji.get(1).getPocetak()));
    }

    @Test
    void testDaLiSamSlobodan3() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertTrue(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,11,10), LocalTime.of(18,20,0))));
    }

    @Test
    void testDaLiSamSlobodan4() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,12,20), LocalTime.of(10,0,0))));
    }

    @Test
    void testDaLiSamSlobodan5() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,12,20), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,22), LocalTime.of(10,0,0))));
    }

    @Test
    void testDaLiSamSlobodan6() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertTrue(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,11,26), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,11,28), LocalTime.of(10,0,0))));
    }

    @Test
    void testDaLiSamSlobodan7() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,12,16), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,28), LocalTime.of(10,0,0))));
    }

    @Test
    void testDaLiSamSlobodan8() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertFalse(kalendar.daLiSamSlobodan(
                LocalDateTime.of(LocalDate.of(2019,12,16), LocalTime.of(10,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,20), LocalTime.of(10,0,0))));
    }

    @Test
    void testDaLiSamSlobodanIzuzetak() {
        kalendar.zakaziDogadjaje(testniDogadjaji);
        assertThrows(IllegalArgumentException.class,
                () -> kalendar.daLiSamSlobodan(
                        LocalDateTime.of(LocalDate.of(2019,12,16), LocalTime.of(10,0,0)),
                        LocalDateTime.of(LocalDate.of(2019,11,20), LocalTime.of(10,0,0))),
                "Neispravni podaci o početku i kraju"
        );
    }
}