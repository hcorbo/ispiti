package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RezervacijaTest {
    private static Rezervacija periodicnaRezervacija, vandrednaRezervacija;


    @org.junit.jupiter.api.BeforeAll
    static void setUp() throws NeispravanFormatRezervacije {
        periodicnaRezervacija = new PeriodicnaRezervacija("VA", "Miki Maus", LocalTime.of(9,0),
                LocalTime.of(12,0),1);

        vandrednaRezervacija = new VandrednaRezervacija("1-15", "Paja Patak", LocalTime.of(14,30),
                LocalTime.of(16,30), LocalDate.of(2019,12,5));
    }

    @Test
    void testPostavljanjaDatuma1() {
        assertThrows(NeispravanFormatRezervacije.class,
                () -> {
                    Rezervacija neispravna = new PeriodicnaRezervacija("VA", "Miki Maus", LocalTime.of(15,0),
                            LocalTime.of(12,0),1);
                },
                "Neispravan format početka i kraja rezervacije"
        );
    }

    @Test
    void testPostavljanjaDatuma2() {
        assertThrows(NeispravanFormatRezervacije.class,
                () -> {
                    periodicnaRezervacija.setPocetak(LocalTime.of(15,20));
                }, "Neispravan format početka i kraja rezervacije");
    }

    @Test
    void testPostavljanjaDatuma3() {
        assertThrows(NeispravanFormatRezervacije.class,
                () -> {
                    periodicnaRezervacija.setKraj(LocalTime.of(8,20));
                }, "Neispravan format početka i kraja rezervacije");
    }

    @Test
    void testPostavljanjaDatuma4() {
        assertDoesNotThrow(
                () -> {
                    periodicnaRezervacija.setKraj(LocalTime.of(12,15));
                });
    }

    @Test
    void testGetterPocetak() {
        LocalTime pocetak =  LocalTime.of(9,0);
        assertEquals(pocetak, periodicnaRezervacija.getPocetak());
    }

    @Test
    void testGetterKraj() {
        LocalTime kraj =  LocalTime.of(16,30);
        assertEquals(kraj, vandrednaRezervacija.getKraj());
    }

    @Test
    void testGetterNazivIPredavac() {
        String naziv = "VA";
        String predavac = "Miki Maus";
        assertAll(
                () -> assertEquals(naziv, periodicnaRezervacija.getNazivSale()),
                () -> assertEquals(predavac, periodicnaRezervacija.getPredavac())
        );

    }

    @Test
    void testToString1() throws NeispravanFormatRezervacije {
        //da li ovdje formirati ovo korsiteci DateTimeFormater da mogu skontati kako se radi?
        periodicnaRezervacija.setKraj( LocalTime.of(12,0));
        String ispravno = "VA - Miki Maus (periodična) - početak: 09:00, kraj: 12:00, svakog utorka";
        assertEquals(ispravno, periodicnaRezervacija.toString());
    }

    @Test
    void testToString2(){
        String ispravno = "1-15 - Paja Patak (vandredna) - početak: 14:30, kraj: 16:30, na dan 05/12/2019";
        assertEquals(ispravno, vandrednaRezervacija.toString());
    }



    @Test
    void testNotEquals() throws NeispravanFormatRezervacije {
        Rezervacija rezervacija = new VandrednaRezervacija(periodicnaRezervacija.getNazivSale(), periodicnaRezervacija.getPredavac(),
                periodicnaRezervacija.getPocetak(), periodicnaRezervacija.getKraj(), LocalDate.of(2019,12,5));
        assertNotEquals(periodicnaRezervacija, rezervacija);
    }

    @Test
    void testEquals() throws NeispravanFormatRezervacije {
        Rezervacija rezervacija = new PeriodicnaRezervacija(periodicnaRezervacija.getNazivSale(), periodicnaRezervacija.getPredavac(),
                periodicnaRezervacija.getPocetak(), periodicnaRezervacija.getKraj(), 1);
        assertEquals(periodicnaRezervacija, rezervacija);
    }

}