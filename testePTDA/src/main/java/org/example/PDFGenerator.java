package org.example;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class PDFGenerator {

    public static void generateTicketPDF(String fileName, String source, String destination, String flightCode, String passengerName, String seatNumber, double price) {
        try {
            // Cria o arquivo PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona uma imagem ao PDF
            String imagePath = "src/main/resources/skybound_logo.png"; // Caminho da imagem
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.setWidth(100); // Define a largura da imagem
            image.setAutoScaleHeight(true); // Ajusta a altura automaticamente
            document.add(image);

            // Adiciona conteúdo ao PDF
            document.add(new Paragraph("Detalhes da Compra do Bilhete"));
            document.add(new Paragraph(" ")); // Espaço em branco
            document.add(new Paragraph("Origem: " + source));
            document.add(new Paragraph("Destino: " + destination));
            document.add(new Paragraph("Código do Voo: " + flightCode));
            document.add(new Paragraph("Passageiro: " + passengerName));
            document.add(new Paragraph("Assento: " + seatNumber));
            document.add(new Paragraph("Preço: €" + price));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Obrigado por escolher a nossa companhia aérea!"));

            // Fecha o documento
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}