package org.example;

import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//VAI PARA PASSENGER
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
        System.out.println("1 - Luxuosa (200€)");
        System.out.println("2 - Intermédia (150€)");
        System.out.println("3 - Normal (100€)");
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

            // Obtém o próximo ID
            int nextId = getNextIDTicket(connection);
            int numTicket = getNextIDTicket(connection);

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
//VAI PARA CLASSE TICKET
            // Inserção na tabela ticket
            String sqlTicket = "INSERT INTO ticket (id_passenger, destination, price, id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statementTicket = connection.prepareStatement(sqlTicket)) {
                statementTicket.setInt(1, nextId); // O ID do passageiro
                statementTicket.setString(2, destination);
                statementTicket.setInt(3, price);
                statementTicket.setInt(4, numTicket);

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
    //VAI PARA A CLASSE PASSENGER

    // Função para obter o próximo número de bilhete na tabela ticket
    //ISTO VAI PARA A CLASSE TICKET

        // Certifique-se de que o método é estátic

        // Outros métodos da classe Main...

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

    public static void SaveTicket(int id_passenger,String destination, double price, String source1,Boolean refundable, int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO ticket (id_passenger,destination,price,trip,refundable,id) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_passenger);
            stmt.setString(2, destination);
            stmt.setDouble(3, price);
            stmt.setString(4,source1);
            stmt.setBoolean(5,refundable);
            stmt.setInt(6,id);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados!");
        }
    }
    public static void SaveCrewData(String destination, int id, String source1) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO crew (id,nome, shift, experience, ranq) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, destination);
            stmt.setInt(2, id);
            stmt.setString(3,source1);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados!");
        }
    }


        public static void salvarDadosAirplane(String destination, int id, String source1) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO airplane (id,destination,source1) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, destination);
            stmt.setInt(2, id);
            stmt.setString(3,source1);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados!");
        }
    }

    public static void salvarDadosFlight(int id_Airplane, int id_Flight, int maxPassengers, Date hTakeOff, Date hLanding, String destination, String source, String codename) {

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            // Formatar as datas no padrão esperado pelo MySQL
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedTakeOff = dateFormat.format(hTakeOff);
            String formattedLanding = dateFormat.format(hLanding);

            String sql = "INSERT INTO flight (id_plane, id, maxPassengers, timeTakeOff, timeLanding, destination, source1, codename) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_Airplane);
            stmt.setInt(2, id_Flight);
            stmt.setInt(3, maxPassengers);
            stmt.setString(4, formattedTakeOff); // Passa a data formatada
            stmt.setString(5, formattedLanding); // Passa a data formatada
            stmt.setString(6, destination);
            stmt.setString(7, source);
            stmt.setString(8, codename);

            stmt.executeUpdate();
            System.out.println("Dados do voo inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados do voo!");
        }
    }
    public static int getNextIDTicket(Connection connection) throws SQLException {
        // Lógica para obter o próximo número de ticket
        String query = "SELECT MAX(ticket_id) FROM ticket"; // Exemplo de SQL
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) + 1; // Incrementa o próximo ID
        } else {
            return 1; // Se não houver nenhum ticket, retorna 1
        }
    }

    public static void linkServiceToClass(int idClass,String className, String service, int idService) {
        String sql = "INSERT INTO class (id, name,service, price) VALUES (?, ? ,?, ?)";

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

    public static void saveSeatInfo(int idTicket, int idSeat, double price, Class classe){
        try(Connection conn= DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")){
            String sql = "INSERT INTO seat (id_Ticket, id_Seat, price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTicket);
            stmt.setInt(2, idSeat);
            stmt.setDouble(3, price);
            stmt.setString(4, String.valueOf(classe));
            stmt.executeUpdate();
            System.out.println("Dados do assento inseridos com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
