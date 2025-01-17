package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SkyBoundGestaoVoosTest {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        Flight.getFlights().clear();
    }

    @Test
    void testAdicionarVoo() {
        Flight.addFlight(1, 101, 150, new Date(), new Date(), new Date(), "Porto", "Lisboa","LisboaPorto");
        assertEquals(1, Flight.getFlights().size());
    }

    @Test
    void testRemoverVoo() {
        Flight.addFlight(1, 101, 150, new Date(), new Date(), new Date(), "Porto", "Lisboa","LisboaPorto");
        Flight.removeFlight(0);
        assertEquals(0, Flight.getFlights().size());
    }

    @Test
    void testVooSemDadosObrigatorios() {
        assertThrows(IllegalArgumentException.class, () -> {
            Flight.addFlight(0, 101, 150, new Date(), new Date(), new Date(), "Porto", "Lisboa","LisboaPorto");
        });
    }
    @Test
    void testLoadFlights() {
        // Adiciona voos à lista simulada de voos
        Flight.addFlight(1, 101, 150, new Date(), new Date(), new Date(), "Porto", "Lisboa", "LisboaPorto");
        Flight.addFlight(2, 102, 200, new Date(), new Date(), new Date(), "Madrid", "Lisboa", "LisboaMadrid");

        // Cria uma instância da interface gráfica para testar o método loadFlights
        SkyBoundGestaoVoos gestaoVoos = new SkyBoundGestaoVoos();

        // Verifica se os voos foram carregados corretamente no modelo flights
       // assertEquals(1, gestaoVoos.flights.size(), "O número de voos carregados está incorreto!");

        // Verifica o conteúdo do primeiro voo formatado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expectedFlight1 = "ID Avião: 1 | ID Voo: 101 | Code Name: LisboaPorto | Origem: Lisboa | Destino: Porto | Partida: "
                + dateFormat.format(Flight.getFlights().get(0).gethTakeoff())
                + " | Chegada: "
                + dateFormat.format(Flight.getFlights().get(0).gethLanding())
                + " | Limite: 150";

        assertEquals(expectedFlight1, gestaoVoos.flights.get(0), "Os detalhes do primeiro voo estão incorretos!");
    }

}