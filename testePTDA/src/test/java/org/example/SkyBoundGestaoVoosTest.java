package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class SkyBoundGestaoVoosTest {
    @BeforeEach
    void setUp() {
        Flight.getFlights().clear();
    }

    @Test
    void testAdicionarVoo() {
        Flight.addFlight(1, 101, 150, new Date(), new Date(), "Porto", "Lisboa", "LP101");
        assertEquals(1, Flight.getFlights().size());
    }

    @Test
    void testRemoverVoo() {
        Flight.addFlight(1, 101, 150, new Date(), new Date(), "Porto", "Lisboa", "LP101");
        Flight.removeFlight(0);
        assertEquals(0, Flight.getFlights().size());
    }

    @Test
    void testVooSemDadosObrigatorios() {
        assertThrows(IllegalArgumentException.class, () -> {
            Flight.addFlight(0, 101, 150, new Date(), new Date(), "", "Lisboa", "");
        });
    }
}