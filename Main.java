package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // Captura de dados do utilizador
        System.out.print("Introduza o nome da pessoa: ");
        String name = scanner.nextLine();

        System.out.print("Introduza a idade: ");
        int age = scanner.nextInt();
        scanner.nextLine();  // Limpa a nova linha do buffer

        System.out.print("Introduza o email: ");
        String email = scanner.nextLine();

        System.out.println("Introduza o destino desejado: ");
        String destination = scanner.nextLine();

        System.out.println("Qual tipo de classe gostaria de estar ? (varia o preço do bilhete)");
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
        try {
            // Configurações da conexão (URL, utilizador e senha)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exerciciosql", "root", "123456789");
            connection.setAutoCommit(true); // Garante que as alterações são commitadas automaticamente

            // Obtém o próximo ID
            int nextId = getNextId(connection);
            int numTicket = getNextNumTicket(connection);

            // Declaração SQL para inserção
            String sql= "INSERT INTO person (name, age, email, id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.setInt(4, nextId);

             sql = "INSERT INTO  ticket (id_person,destination,price,num ) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(sql);
             statement.setInt(1,nextId);
             statement.setString(2,destination);
             statement.setInt(3,price);
             statement.setInt(4,numTicket);

            // Executa a inserção
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nova pessoa adicionada com sucesso!");
            } else {
                System.out.println("Nenhuma linha foi inserida.");
            }

            // Fecha a conexão
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // Função para obter o próximo ID
    private static int getNextId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM person";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        int nextId = 1; // Começa com 1, caso a tabela esteja vazia

        if (resultSet.next()) {
            nextId = resultSet.getInt("max_id") + 1;
        }

        resultSet.close();
        statement.close();
        return nextId;
    }
    private static int getNextNumTicket(Connection connection) throws SQLException {
        String sql = "SELECT MAX(num) AS max_num FROM ticket";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        int nextNum = 1; // Começa com 1, caso a tabela esteja vazia

        if (resultSet.next()) {
            nextNum = resultSet.getInt("max_num") + 1;
        }

        resultSet.close();
        statement.close();
        return nextNum;
    }

}
