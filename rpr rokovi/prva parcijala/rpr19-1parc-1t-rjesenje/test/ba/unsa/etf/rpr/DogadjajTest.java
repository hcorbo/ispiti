package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DogadjajTest {

    private static Dogadjaj mojRodjendan, ispit;


    @org.junit.jupiter.api.BeforeAll
    static void setUp() throws NeispravanFormatDogadjaja {
         mojRodjendan = new DogadjajNiskogPrioriteta("Moj rođendan",
                LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(11,20,0)),
                LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(23,59,0)));

        ispit = new DogadjajVisokogPrioriteta("Ispit iz RPR-a",
                LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(11,0,0)),
                LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(15,0,0)));
    }

    @Test
    void testPostavljanjaDatuma1() {
        assertThrows(NeispravanFormatDogadjaja.class,
                () -> {
                    Dogadjaj neispravan = new DogadjajNiskogPrioriteta("Neispravan format",
                            LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(11,20,0)),
                            LocalDateTime.of(LocalDate.of(2019,11,8), LocalTime.of(23,59,0)));
                    // Kraj prije početka
                },
                "Neispravan format početka i kraja dogadjaja"
        );
    }

    @Test
    void testPostavljanjaDatuma2() {
        assertThrows(NeispravanFormatDogadjaja.class,
                () -> {
                    mojRodjendan.setPocetak(LocalDateTime.of(LocalDate.of(2019,12,7), LocalTime.of(11,20,0)));
                }, "Neispravan format početka i kraja dogadjaja"); // Kraj prije početka
    }

    @Test
    void testPostavljanjaDatuma3() {
        assertThrows(NeispravanFormatDogadjaja.class,
                () -> {
                    mojRodjendan.setKraj(LocalDateTime.of(LocalDate.of(2019,11,3), LocalTime.of(11,20,0)));
                }, "Neispravan format početka i kraja dogadjaja"); // Kraj prije početka
    }

    @Test
    void testPostavljanjaDatuma4() {
        assertDoesNotThrow(
                () -> {
                    mojRodjendan.setKraj(LocalDateTime.of(LocalDate.of(2019,12,22), LocalTime.of(10,0,0)));
                });
        // Vraćamo izvorno vrijeme kraja kako testovi ne bi padali
        assertDoesNotThrow(
                () -> {
                    mojRodjendan.setKraj(LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(23,59,0)));
                });
    }

    @Test
    void testGetPocetak() {
        LocalDateTime pocetak =  LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(11,20,0));
        assertEquals(pocetak, mojRodjendan.getPocetak());
    }

    @Test
    void testGetKraj() {
        LocalDateTime kraj =  LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(23,59,0));
        assertEquals(kraj, mojRodjendan.getKraj());
    }

    @Test
    void testGetNaziv() {
        String naziv = "Moj rođendan";
        assertEquals(naziv, mojRodjendan.getNaziv());
    }

    @Test
    void testToString1() throws NeispravanFormatDogadjaja {
        mojRodjendan.setKraj(LocalDateTime.of(LocalDate.of(2019,11,15), LocalTime.of(23,59,0)));
        String ispravno = "Moj rođendan (nizak prioritet) - početak: 15/11/2019 (11:20), kraj: 15/11/2019 (23:59)";
        assertEquals(ispravno, mojRodjendan.toString());
    }

    @Test
    void testToString2(){
        String ispravno = "Ispit iz RPR-a (visok prioritet) - početak: 07/12/2019 (11:00), kraj: 07/12/2019 (15:00)";
        assertEquals(ispravno, ispit.toString());
    }

    @Test
    void testEquals() throws NeispravanFormatDogadjaja {
        Dogadjaj dogadjaj = new DogadjajNiskogPrioriteta(mojRodjendan.getNaziv(), mojRodjendan.getPocetak(), mojRodjendan.getKraj());
        assertEquals(mojRodjendan, dogadjaj);
    }

    @Test
    void testNotEquals() throws NeispravanFormatDogadjaja {
        Dogadjaj dogadjaj2 = new DogadjajSrednjegPrioriteta(mojRodjendan.getNaziv(), mojRodjendan.getPocetak(), mojRodjendan.getKraj());
        assertNotEquals(mojRodjendan, dogadjaj2);
    }
}