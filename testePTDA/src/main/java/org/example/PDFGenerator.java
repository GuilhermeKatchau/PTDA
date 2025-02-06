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

    // metodo para gerar um pdf com os detalhes do bilhete
    public static void generateTicketPDF(String fileName, String source, String destination, String flightCode, String passengerName, String seatNumber, double price) {
        try {
            // cria o arquivo pdf
            PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // adiciona uma imagem ao pdf
            String imagePath = "C:\\Users\\HP\\Desktop\\University\\Projeto\\PTDA\\testePTDA\\src\\main\\resources\\skybound_logo.png"; // caminho da imagem
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.setWidth(100); // define a largura da imagem
            image.setAutoScaleHeight(true); // ajusta a altura automaticamente
            document.add(image);

            // adiciona conteudo ao pdf
            document.add(new Paragraph("Detalhes da compra do bilhete"));
            document.add(new Paragraph(" ")); // espaco em branco
            document.add(new Paragraph("Origem: " + source));
            document.add(new Paragraph("Destino: " + destination));
            document.add(new Paragraph("Codigo do Voo: " + flightCode));
            document.add(new Paragraph("Passageiro: " + passengerName));
            document.add(new Paragraph("Assento: " + seatNumber));
            document.add(new Paragraph("Preco: €" + price));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Obrigado por escolher a nossa companhia aerea!"));

            // fecha o documento
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}