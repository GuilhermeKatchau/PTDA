package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;

import static org.junit.jupiter.api.Assertions.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        // Dados de exemplo para voos
        Flight flight1 = new Flight(9212, 9212,64, new Date(), new Date(), new Date(),"Porto","Lisboa","LisboaPorto");
        Flight flight2 = new Flight(9345, 5548,64, new Date(), new Date(), new Date(),"Madrid","Paris","ParisMadrid");
        Flight flight3 = new Flight(1234, 5678,64, new Date(), new Date(), new Date(),"Bolonha","Munique","MuniqueBolonha");

        // Adiciona voos simulados à lista
        compraBilhete.getAvailableFlights().add(flight1);
        compraBilhete.getAvailableFlights().add(flight2);
        compraBilhete.getAvailableFlights().add(flight3);

        // Chama o método de filtragem
        ArrayList<Flight> result = compraBilhete.filterFlights("Lisboa", "Porto");

        // Verifica se a filtragem foi feita corretamente
        assertEquals(1, result.size());
        assertEquals("Lisboa", result.get(0).getSource());
        assertEquals("Porto", result.get(0).getDestination());
    }

    @Test
    void testCalcularPreco() {
        CompraBilhete compraBilhete = new CompraBilhete();

        // Teste para a classe Luxuosa
        compraBilhete.selectedClass = compraBilhete.luxurious;
        double precoLuxuoso = compraBilhete.calcularPreco(0);
        assertEquals(200.00, precoLuxuoso, 0.01);

        // Teste para a classe Económica
        compraBilhete.selectedClass = compraBilhete.economical;
        double precoEconomico = compraBilhete.calcularPreco(0);
        assertEquals(100.00, precoEconomico, 0.01);

        // Teste para a classe Premium
        compraBilhete.selectedClass = compraBilhete.premium;
        double precoPremium = compraBilhete.calcularPreco(0);
        assertEquals(150.00, precoPremium, 0.01);
    }
}