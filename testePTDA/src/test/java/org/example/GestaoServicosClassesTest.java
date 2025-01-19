package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GestaoServicosClassesTest {
    private Class testClass;
    private ArrayList<String> testServices;

    @BeforeEach
    void setUp() {
        testServices = new ArrayList<>();
        testServices.add("Bagagem Extra");
        testServices.add("Refeição Gourmet");
        testClass = new Class("Luxuosa", 200.00, 10, testServices);
    }

    @Test
    void testClassCreation() {
        assertNotNull(testClass, "Class should be created successfully");
        assertEquals("Luxuosa", testClass.getClassName(), "Class name should match");
        assertEquals(200.00, testClass.getPrice(), "Price should match");
        assertEquals(10, testClass.getSeatCapacity(), "Seat capacity should match");
    }

    @Test
    void testServicesList() {
        List<String> services = testClass.getServices();
        assertNotNull(services, "Services list should not be null");
        assertEquals(2, services.size(), "Should have correct number of services");
        assertTrue(services.contains("Bagagem Extra"), "Should contain added service");
    }

    @Test
    void testInvalidClassCreation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Class("", 200.00, 10, testServices);
        }, "Should throw exception for empty class name");

        assertThrows(IllegalArgumentException.class, () -> {
            new Class("Luxuosa", -200.00, 10, testServices);
        }, "Should throw exception for negative price");

        assertThrows(IllegalArgumentException.class, () -> {
            new Class("Luxuosa", 200.00, -10, testServices);
        }, "Should throw exception for negative seat capacity");
    }

    @Test
    void testNullServices() {
        assertThrows(NullPointerException.class, () -> {
            new Class("Luxuosa", 200.00, 10, null);
        }, "Should throw exception for null services list");
    }

    @Test
    void testEmptyServices() {
        ArrayList<String> emptyServices = new ArrayList<>();
        Class classWithNoServices = new Class("Básica", 100.00, 50, emptyServices);
        assertTrue(classWithNoServices.getServices().isEmpty(), "Services list should be empty");
    }
}