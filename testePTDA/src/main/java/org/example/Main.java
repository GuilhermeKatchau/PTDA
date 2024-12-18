package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;


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
            int nextId = getNextId(connection);
            int numTicket = getNextNumTicket(connection);

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
    private static int getNextId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM passenger";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            int nextId = 1; // Começa com 1, caso a tabela esteja vazia
            if (resultSet.next()) {
                nextId = resultSet.getInt("max_id") + 1;
            }
            return nextId;
        }
    }

    // Função para obter o próximo número de bilhete na tabela ticket
    //ISTO VAI PARA A CLASSE TICKET
    private static int getNextNumTicket(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id) AS max_num FROM ticket";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            int nextNum = 1; // Começa com 1, caso a tabela esteja vazia
            if (resultSet.next()) {
                nextNum = resultSet.getInt("max_num") + 1;
            }
            return nextNum;
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
    public static void salvarDadosFlight(String destination, int id_Airplane, String source, int id_Flight, int maxPassengers, String hTakeOff, String hLanding, String codename) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "INSERT INTO flight (destination, id_plane, source1, id, maxPassengers, timeTakeOff, timeLanding, codename) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, destination);
            stmt.setInt(2, id_Airplane);
            stmt.setString(3, source);
            stmt.setInt(4, id_Flight);
            stmt.setInt(5, maxPassengers);
            stmt.setString(6, String.valueOf(hTakeOff));
            stmt.setString(7, String.valueOf(hLanding));
            stmt.setString(8, codename);

            stmt.executeUpdate();
            System.out.println("Dados do voo inseridos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao guardar os dados do voo!");
        }
    }


}
