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

        // captura de dados do utilizador
        System.out.print("introduza o nome da pessoa: ");
        String name = scanner.nextLine();

        System.out.print("introduza a idade: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // limpa a nova linha do buffer

        System.out.print("introduza o email: ");
        String email = scanner.nextLine();

        System.out.println("introduza o destino desejado: ");
        String destination = scanner.nextLine();

        System.out.println("qual tipo de classe gostaria de estar? (varia o preco do bilhete)");
        System.out.println("1 - premium (200€)");
        System.out.println("2 - luxuosa (150€)");
        System.out.println("3 - economica (100€)");
        int classChoice = scanner.nextInt();
        int price = 0;

        // define o preco com base na escolha de classe
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
                System.out.println("escolha invalida! a classe sera definida como normal (100€).");
                price = 100;
        }

        // conexao com a base de dados e insercao de dados
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            connection.setAutoCommit(false); // transacoes manuais para consistencia

            // obtem o proximo id do passageiro e do ticket
            int nextId = getNextIDPassenger(connection);
            int numTicket = getNextIDTicket(connection);

            // obtem o proximo id do voo (exemplo, ajuste conforme necessario)
            int idFlight = getNextIDFlight(connection);

            // insercao na tabela passenger
            String sqlPassenger = "INSERT INTO passenger (name_passenger, age, email, id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statementPassenger = connection.prepareStatement(sqlPassenger)) {
                statementPassenger.setString(1, name);
                statementPassenger.setInt(2, age);
                statementPassenger.setString(3, email);
                statementPassenger.setInt(4, nextId);

                int rowsInsertedPassenger = statementPassenger.executeUpdate();
                if (rowsInsertedPassenger > 0) {
                    System.out.println("nova pessoa adicionada com sucesso!");
                }
            }

            // insercao na tabela ticket
            String sqlTicket = "INSERT INTO ticket (id_passenger, name_passenger, seat, destination, price, trip, refundable, id, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statementTicket = connection.prepareStatement(sqlTicket)) {
                statementTicket.setInt(1, nextId); // o id do passageiro
                statementTicket.setString(2, name); // nome do passageiro
                statementTicket.setInt(3, 1); // id do assento (exemplo, ajuste conforme necessario)
                statementTicket.setString(4, destination);
                statementTicket.setInt(5, price);
                statementTicket.setString(6, "lisboa"); // origem (exemplo, ajuste conforme necessario)
                statementTicket.setBoolean(7, true); // refundable (exemplo, ajuste conforme necessario)
                statementTicket.setInt(8, numTicket);
                statementTicket.setInt(9, idFlight); // id do voo

                int rowsInsertedTicket = statementTicket.executeUpdate();
                if (rowsInsertedTicket > 0) {
                    System.out.println("bilhete adicionado com sucesso!");
                }
            }

            // confirma a transacao
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // funcao para obter o proximo id na tabela passenger
    public static int getNextIDPassenger(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM passenger";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // incrementa o proximo id
        } else {
            return 1; // se nao houver nenhum passageiro, retorna 1
        }
    }

    // funcao para obter o proximo numero de bilhete na tabela ticket
    public static int getNextIDTicket(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM ticket";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // incrementa o proximo id
        } else {
            return 1; // se nao houver nenhum ticket, retorna 1
        }
    }

    // funcao para obter o proximo id na tabela flight
    public static int getNextIDFlight(Connection connection) throws SQLException {
        String query = "SELECT MAX(id) FROM flight";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // incrementa o proximo id
        } else {
            return 1; // se nao houver nenhum voo, retorna 1
        }
    }

    // metodo para salvar dados do passageiro
    public static void SavePassengerData(String name_passenger, int age, String email, int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO passenger (name_passenger, age, email, id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name_passenger);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            stmt.executeUpdate();
            System.out.println("dados do passageiro inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro ao guardar os dados do passageiro!");
        }
    }

    // metodo para salvar dados do ticket
    public static void SaveTicket(String idTicket, int idPassenger, String name, int seatId, String destination, double price, String source, boolean refundable, int idFlight) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO ticket (id, id_passenger, name_passenger, id_seat, destination, price, source1, refundable, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTicket);
            stmt.setInt(2, idPassenger);
            stmt.setString(3, name);
            stmt.setInt(4, seatId);
            stmt.setString(5, destination);
            stmt.setDouble(6, price);
            stmt.setString(7, source);
            stmt.setBoolean(8, refundable);
            stmt.setInt(9, idFlight);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "erro ao salvar bilhete: " + ex.getMessage(), "erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodo para salvar dados da tripulacao
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
            System.out.println("dados da tripulacao inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro ao guardar os dados da tripulacao!");
        }
    }

    // metodo para deletar dados da tripulacao
    public static void deleteCrewData(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "DELETE FROM crew WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("dados da tripulacao apagados com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro ao apagar os dados da tripulacao!");
        }
    }

    // metodo para salvar dados do aviao
    public static void salvarDadosAirplane(String destination, int id, String source1) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO airplane (id, destination, source1) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, destination);
            stmt.setString(3, source1);
            stmt.executeUpdate();
            System.out.println("dados do aviao inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro ao guardar os dados do aviao!");
        }
    }

    // metodo para salvar dados do voo
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
            System.out.println("dados do voo inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro ao guardar os dados do voo!");
        }
    }

    // metodo para associar servico a classe
    public static void linkServiceToClass(int idClass, String className, String service, int idService) {
        String sql = "INSERT INTO class (id, nome, services, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClass);
            stmt.setString(2, className);
            stmt.setString(3, service);
            stmt.setInt(4, idService);
            stmt.executeUpdate();
            System.out.println("servico associado à classe com sucesso!");
        } catch (SQLException e) {
            System.err.println("erro ao associar servico à classe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // metodo para salvar informacoes do assento
    public static void saveSeatInfo(String idTicket, String name, int seatId, double price, boolean occupied, Class seatClass, int idFlight) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO seat (id_ticket, name_passenger, id_seat, price, occupied, class, id_flight) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTicket);
            stmt.setString(2, name);
            stmt.setInt(3, seatId);
            stmt.setDouble(4, price);
            stmt.setBoolean(5, occupied);
            stmt.setString(6, seatClass.getClassName());
            stmt.setInt(7, idFlight);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "erro ao salvar assento: " + ex.getMessage(), "erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodo para mostrar voos registrados
    public static void showRegisteredFlights() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("id do aviao: " + rs.getInt("id_plane"));
                System.out.println("id do voo: " + rs.getInt("id"));
                System.out.println("numero maximo de passageiros: " + rs.getInt("maxPassengers"));
                System.out.println("data: " + rs.getString("date1"));
                System.out.println("hora de partida: " + rs.getString("timeTakeOff"));
                System.out.println("hora de chegada: " + rs.getString("timeLanding"));
                System.out.println("destino: " + rs.getString("destination"));
                System.out.println("origem: " + rs.getString("source1"));
                System.out.println("codigo do voo: " + rs.getString("codename"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erro ao mostrar os voos registrados!");
        }
    }
}