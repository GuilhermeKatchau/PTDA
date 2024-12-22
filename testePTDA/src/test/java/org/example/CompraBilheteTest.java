package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("Lisboa", testTicket.getSource(), "Source should match");
        assertEquals("Porto", testTicket.getDestination(), "Destination should match");
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
}