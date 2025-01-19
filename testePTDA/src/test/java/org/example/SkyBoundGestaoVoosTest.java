package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SkyBoundGestaoVoosTest {



    private Flight findFlightById(int id) {
        for (Flight flight : Flight.getFlights()) {
            if (flight.getId_Flight() == id) {
                return flight;
            }
        }
        return null;
    }


    @Test
    void testAdicionarVoo() {
        int testId = 1001;
        Date takeoffTime = new Date();
        Date landingTime = new Date(takeoffTime.getTime() + 60000);
        Flight.addFlight(1, testId, 150, new Date(), takeoffTime, landingTime, "Porto", "Lisboa", "TEST_LisboaPorto");
        Flight testFlight = findFlightById(testId);
        assertNotNull(testFlight, "O voo não foi adicionado corretamente.");
        Flight.removeTestFlights();
    }

    @Test
    void testRemoverVoo() {
        int flightIndex = Flight.getFlights().size();
        Date takeoffTime = new Date();
        Date landingTime = new Date(takeoffTime.getTime() + 60000);
        Flight.addFlight(1, flightIndex, 150, new Date(), takeoffTime, landingTime, "Porto", "Lisboa", "TEST_LisboaPorto");
        Flight.removeFlight(flightIndex);
        Flight testFlight = findFlightById(flightIndex);
        assertNull(testFlight, "O voo não foi removido corretamente.");
    }

    @Test
    void testVooSemDadosObrigatorios() {
        assertThrows(IllegalArgumentException.class, () -> {
            Flight.addFlight(0, 101, 150, new Date(), new Date(), new Date(), "Porto", "Lisboa","TEST_LisboaPorto");
        });
    }

}