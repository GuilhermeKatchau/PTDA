package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Captura de dados do utilizador
        System.out.print("Introduza o nome da pessoa: ");
        String name = scanner.nextLine();

        System.out.print("Introduza a idade: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Limpa a nova linha do buffer

        System.out.print("Introduza o email: ");
        String email = scanner.nextLine();

        System.out.println("Introduza o destino desejado: ");
        String destination = scanner.nextLine();

        System.out.println("Qual tipo de classe gostaria de estar? (varia o preço do bilhete)");
        System.out.println("1 - Premium (200€)");
        System.out.println("2 - Luxuosa (150€)");
        System.out.println("3 - Económica (100€)");
        int classChoice = scanner.nextInt();
        int price = 0;

        // Define o preço com base na escolha de classe
        switch (classChoice) {
            case 1:
                price = 200;
                break;
            case 2:
                price = 150;
                break;
            case 3:
                price = 100;
                break;
            default:
                System.out.println("Escolha inválida! A classe será definida como Normal (100€).");
                price = 100;
        }


        // Conexão com a base de dados e inserção de dados
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            connection.setAutoCommit(false); // Transações manuais para consistência

            // Obtém o próximo ID do passageiro e do ticket
            int nextId = getNextIDPassenger(connection);
            int numTicket = getNextIDTicket(connection);

            // Obtém o próximo ID do voo (exemplo, ajuste conforme necessário)
            int idFlight = getNextIDFlight(connection);

            // Inserção na tabela passenger
            String sqlPassenger = "INSERT INTO passenger (name_passenger, age, email, id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statementPassenger = connection.prepareStatement(sqlPassenger)) {
                statementPassenger.setString(1, name);
                statementPassenger.setInt(2, age);
                statementPassenger.setString(3, email);
                statementPassenger.setInt(4, nextId);

                int rowsInsertedPassenger = statementPassenger.executeUpdate();
                if (rowsInsertedPassenger > 0) {
                    System.out.println("Nova pessoa adicionada com sucesso!");
                }
            }

            // Inserção na tabela ticket
            String sqlTicket = "INSERT INTO ticket (id_passenger, name_passenger, seat, destination, price, trip, refundable, id, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statementTicket = connection.prepareStatement(sqlTicket)) {
                statementTicket.setInt(1, nextId); // O ID do passageiro
                statementTicket.setString(2, name); // Nome do passageiro
                statementTicket.setInt(3, 1); // ID do assento (exemplo, ajuste conforme necessário)
                statementTicket.setString(4, destination);
                statementTicket.setInt(5, price);
                statementTicket.setString(6, "Lisboa"); // Origem (exemplo, ajuste conforme necessário)
                statementTicket.setBoolean(7, true); // Refundable (exemplo, ajuste conforme necessário)
                statementTicket.setInt(8, numTicket);
                statementTicket.setInt(9, idFlight); // ID do voo

                int rowsInsertedTicket = statementTicket.executeUpdate();
                if (rowsInsertedTicket > 0) {
                    System.out.println("Bilhete adicionado com sucesso!");
                }
            }

            // Confirma a transação
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // Função para obter o próximo ID na tabela passenger
    public static int getNextIDPassenger(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM passenger";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // Incrementa o próximo ID
        } else {
            return 1; // Se não houver nenhum passageiro, retorna 1
        }
    }

    // Função para obter o próximo número de bilhete na tabela ticket
    public static int getNextIDTicket(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM ticket";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // Incrementa o próximo ID
        } else {
            return 1; // Se não houver nenhum ticket, retorna 1
        }
    }

    // Função para obter o próximo ID na tabela flight
    public static int getNextIDFlight(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM flight";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // Incrementa o próximo ID
        } else {
            return 1; // Se não houver nenhum voo, retorna 1
        }
    }

    // Método para salvar dados do passageiro
    public static void SavePassengerData(String name_passenger, int age, String email, int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO passenger (name_passenger, age, email, id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name_passenger);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            stmt.executeUpdate();
            System.out.println("Dados do passageiro inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados do passageiro!");
        }
    }
    String idTicket = UUID.randomUUID().toString();
    // Método para salvar dados do ticket
    public static void SaveTicket(String idTicket, int idPassenger, String namePassenger, int idSeat, String destination, double price, String source, boolean refundable, int idFlight) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO ticket (id, id_passenger, name_passenger, id_seat, destination, price, source1, refundable, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTicket);
            stmt.setInt(2, idPassenger);
            stmt.setString(3, namePassenger);
            stmt.setInt(4, idSeat);
            stmt.setString(5, destination);
            stmt.setDouble(6, price);
            stmt.setString(7, source);
            stmt.setBoolean(8, refundable);
            stmt.setInt(9, idFlight);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o ticket: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para salvar dados da tripulação
    public static void saveCrewData(int idCrewMember, int idFlight, String name, String shift, int experience, String ranq) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO crew (id, id_flight, nome, shift, experience, ranq) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCrewMember);
            stmt.setInt(2, idFlight);
            stmt.setString(3, name);
            stmt.setString(4, shift);
            stmt.setInt(5, experience);
            stmt.setString(6, ranq);
            stmt.executeUpdate();
            System.out.println("Dados da tripulação inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados da tripulação!");
        }
    }

    // Método para deletar dados da tripulação
    public static void deleteCrewData(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "DELETE FROM crew WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Dados da tripulação apagados com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao apagar os dados da tripulação!");
        }
    }

    // Método para salvar dados do avião
    public static void salvarDadosAirplane(String destination, int id, String source1) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO airplane (id, destination, source1) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, destination);
            stmt.setString(3, source1);
            stmt.executeUpdate();
            System.out.println("Dados do avião inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados do avião!");
        }
    }

    // Método para salvar dados do voo
    public static void salvarDadosFlight(int id_Airplane, int id_Flight, int maxPassengers, Date date1, Date hTakeOff, Date hLanding, String destination, String source, String codename) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTakeOff = hourFormat.format(hTakeOff);
            String formattedLanding = hourFormat.format(hLanding);

            String sql = "INSERT INTO flight (id_plane, id, maxPassengers, date1, timeTakeOff, timeLanding, destination, source1, codename) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_Airplane);
            stmt.setInt(2, id_Flight);
            stmt.setInt(3, maxPassengers);
            stmt.setString(4, dateFormat.format(date1));
            stmt.setString(5, formattedTakeOff);
            stmt.setString(6, formattedLanding);
            stmt.setString(7, destination);
            stmt.setString(8, source);
            stmt.setString(9, codename);

            stmt.executeUpdate();
            System.out.println("Dados do voo inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados do voo!");
        }
    }

    // Método para associar serviço à classe
    public static void linkServiceToClass(int idClass, String className, String service, int idService) {
        String sql = "INSERT INTO class (id, nome, services, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClass);
            stmt.setString(2, className);
            stmt.setString(3, service);
            stmt.setInt(4, idService);
            stmt.executeUpdate();
            System.out.println("Serviço associado à classe com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao associar serviço à classe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para salvar informações do assento
    public static void saveSeatInfo(String idTicket, String namePassenger, int idSeat, double price,boolean occupied, Class classe, int idFlight) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO seat (id_Ticket, name_passenger, id_Seat, price, occupied, class, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTicket);
            stmt.setString(2, namePassenger);
            stmt.setInt(3, idSeat);
            stmt.setDouble(4, price);
            stmt.setBoolean(5,occupied);
            stmt.setString(6, classe.getClassName());
            stmt.setInt(7, idFlight);

            stmt.executeUpdate();
            System.out.println("Assento salvo com sucesso no banco de dados!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar o assento no banco de dados: " + e.getMessage());
        }
    }

    // Método para mostrar voos registrados
    public static void showRegisteredFlights() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID do Avião: " + rs.getInt("id_plane"));
                System.out.println("ID do Voo: " + rs.getInt("id"));
                System.out.println("Número Máximo de Passageiros: " + rs.getInt("maxPassengers"));
                System.out.println("Data: " + rs.getString("date1"));
                System.out.println("Hora de Partida: " + rs.getString("timeTakeOff"));
                System.out.println("Hora de Chegada: " + rs.getString("timeLanding"));
                System.out.println("Destino: " + rs.getString("destination"));
                System.out.println("Origem: " + rs.getString("source1"));
                System.out.println("Código do Voo: " + rs.getString("codename"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao mostrar os voos registrados!");
        }
    }
}