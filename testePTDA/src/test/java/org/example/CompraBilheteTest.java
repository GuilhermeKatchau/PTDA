package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CompraBilheteTest {
    private Ticket testTicket;
    private Passenger testPassenger;


    @BeforeEach
    void setUp() {
        testTicket = new Ticket("Lisboa", "Porto", 12345, 150.00);
        testPassenger = new Passenger("John Doe", 30, "john@example.com", 2);
    }

    @Test
    void testTicketCreation() {
        assertNotNull(testTicket, "Ticket should be created successfully");
        assertEquals("Porto", testTicket.getSource(), "Source should match");
        assertEquals("Lisboa", testTicket.getDestination(), "Destination should match");
    }

    @Test
    void testPassengerValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Passenger("", 30, "john@example.com", 2);
        }, "Should throw exception for empty name");

        assertThrows(IllegalArgumentException.class, () -> {
            new Passenger("John Doe", -1, "john@example.com", 2);
        }, "Should throw exception for invalid age");

        assertThrows(IllegalArgumentException.class, () -> {
            new Passenger("John Doe", 30, "invalid-email", 2);
        }, "Should throw exception for invalid email format");
    }

    @Test
    void testTicketPrice() {
        assertTrue(testTicket.getPrice() > 0, "Ticket price should be positive");
    }

    @Test
    void testDestinationValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ticket("Lisboa", "", 12345, 150.00);
        }, "Should throw exception for empty destination");
    }

    @Test
    void testSourceDestinationEquality() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ticket("Lisboa", "Lisboa", 12345, 150.00);
        }, "Should throw exception when source and destination are the same");
    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(ParseException.class, () -> {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.parse("2022-01-01");
        }, "Should throw exception for invalid date format");
    }

    @Test
    void testFilterFlights() {
        CompraBilhete compraBilhete = new CompraBilhete();

        // Create sample dates
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date1;
        Date hTakeOff;
        Date hLanding;
        try {
            date1 = format.parse("01/01/2023 12:00:00");
            hTakeOff = format.parse("01/01/2023 12:00:00");
            hLanding = format.parse("01/01/2023 14:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Add test flights with "TEST_" prefix
        Flight.addFlight(1, 9212, 64, date1, hTakeOff, hLanding, "Porto", "Lisboa", "TEST_LisboaPorto");
        Flight.addFlight(2, 5548, 64, date1, hTakeOff, hLanding, "Paris", "Madrid", "TEST_MadridParis");
        Flight.addFlight(3, 5678, 64, date1, hTakeOff, hLanding, "Munique", "Bolonha", "TEST_BolonhaMunique");

        // Perform the flight filtering
        ArrayList<Flight> result = compraBilhete.filterFlights("Lisboa", "Porto");

        // Verify the filtering results
        assertEquals(1, result.size());
        assertEquals("Lisboa", result.get(0).getSource());
        assertEquals("Porto", result.get(0).getDestination());
        Flight.removeTestFlights();
    }

}