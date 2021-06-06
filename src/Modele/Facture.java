package Modele;

import be.quodlibet.boxable.*;
import be.quodlibet.boxable.datatable.DataTable;
import be.quodlibet.boxable.image.Image;
import be.quodlibet.boxable.line.LineStyle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Facture {

    // Create a new font object selecting one of the PDF base fonts
    private final PDFont fontPlain = PDType1Font.HELVETICA;
    private final PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private final PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    private final PDFont fontMono = PDType1Font.COURIER;


    // Create a document and add a page to it

    private final PDDocument document = new PDDocument();
    private final PDPage page = new PDPage(PDRectangle.A4);
    // PDRectangle.LETTER and others are also possible
    // rect can be used to get the page width and height

    //Dummy Table
    private final float margin = 50;
    private final int spaceBetweenTables = 50;
    // starting y position is whole page height subtracted by top and bottom margin
    private final float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin) - spaceBetweenTables;
    // we want table across whole page width (subtracted by left and right margin ofcourse)
    private final float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
    private final String outputFileName = "Facture.pdf";
    PDPageContentStream cos = new PDPageContentStream(document, page);
    private String nom;
    private String prenom;
    private int nofact;
    private int nocom;
    private Date datej = new Date();
    private boolean drawContent = true;
    private float yStart = yStartNewPage;
    private float bottomMargin = 70;
    // y position is your coordinate of top left corner of the table
    private float yPosition = 770;

    public Facture(String nom, String prenom, int nofact, int nocom, ArrayList<ProduitPanier> listePanier, double sommePrixTot, boolean clientFidele, String mTot) throws IOException
    {
        this.nom = nom;
        this.prenom = prenom;
        this.nofact = nofact;
        this.nocom = nocom;
        document.addPage(page);

        Image image = new Image(ImageIO.read(new File("src/Images/logoBlack.png")));
        // imagine we have pretty big logo and we want scale that a little bit
        float imageWidth = 75;
        image = image.scaleByWidth(imageWidth);


        // Start a new content stream which will "hold" the to be created content


        // close the content stream
        // Save the results and ensure that the document is properly closed:
        genTableTitre(image);
        genTableInfo(nom, prenom, nofact, nocom);
        genTableCommande(listePanier, sommePrixTot, clientFidele, mTot);
        cos.close();
        document.save(outputFileName);
        document.close();
    }

    public void setyPosition(float yPosition)
    {
        this.yPosition = yPosition;
    }

    public void genTableTitre(Image image) throws IOException
    {
        BaseTable tableTitre = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, true, drawContent);

        // the parameter is the row height
        Row<PDPage> headerRow = tableTitre.createRow(50);
        // the first parameter is the cell width
        Cell<PDPage> cell = headerRow.createImageCell(100, image);
        cell.setFont(fontBold);
        cell.setFontSize(20);
        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        // border style
        cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        tableTitre.addHeaderRow(headerRow);

        Row<PDPage> row = tableTitre.createRow(20);
        cell = row.createCell(50, "FACTURE");
        cell.setFontSize(24);
        cell.setFont(fontBold);
        cell.setValign(VerticalAlignment.MIDDLE);
        cell.setAlign(HorizontalAlignment.CENTER);
        cell = row.createCell(50, "VStore<br> IUT de Metz<br> Ile du Saulcy<br> BP 10628<br> 57045 Metz cedex 01");
        cell.setFontSize(12);
        cell.setAlign(HorizontalAlignment.RIGHT);

        tableTitre.draw();
        setyPosition(yPosition - tableTitre.getHeaderAndDataHeight());

    }

    private void genTableInfo(String nom, String prenom, int nofact, int nocom) throws IOException
    {
        BaseTable tableInfo = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, false, drawContent);
        Row<PDPage> headerRow = tableInfo.createRow(50);
        // the first parameter is the cell width
        Cell<PDPage> cell = headerRow.createCell(100, "Informations du Client");
        cell.setFont(fontBold);
        cell.setFontSize(20);
        // vertical alignment
        cell.setValign(VerticalAlignment.MIDDLE);
        // border style
        cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        tableInfo.addHeaderRow(headerRow);

        Row<PDPage> row = tableInfo.createRow(20);
        cell = row.createCell(20, "Nom :");
        cell.setFontSize(12);
        cell = row.createCell(40, nom + " " + prenom);
        cell.setFontSize(12);
        cell.setFont(fontBold);
        cell = row.createCell(20, "Facture n° :");
        cell.setFontSize(12);
        cell = row.createCell(20, Integer.toString(nofact));
        cell.setFontSize(12);
        cell.setFont(fontBold);

        row = tableInfo.createRow(20);
        cell = row.createCell(20, "Commande n° :");
        cell.setFontSize(12);
        cell = row.createCell(40, Integer.toString(nocom));
        cell.setFontSize(12);
        cell.setFont(fontBold);
        cell = row.createCell(20, "Date :");
        cell.setFontSize(12);
        cell = row.createCell(20, datej.toString());
        cell.setFontSize(12);
        cell.setFont(fontBold);

        tableInfo.draw();
        setyPosition(yPosition - tableInfo.getHeaderAndDataHeight());

    }

    private void genTableCommande(ArrayList<ProduitPanier> listePanier, double sommePrixTot, boolean clientFidele, String mTot) throws IOException
    {
        List<List> commandes = new ArrayList();
        commandes.add(new ArrayList<>(Arrays.asList("Produit", "Quantité", "Durée", "Prix Total")));
        for (int i = 0; i < listePanier.size(); i++) {
            commandes.add(new ArrayList<>(Arrays.asList(listePanier.get(i).getTitreProduit(), listePanier.get(i).getQuantite(), listePanier.get(i).getDuree() + " jour(s)", listePanier.get(i).getPrixTotal() + "€")));
        }
        String str = "-0%";
        if (clientFidele)
            str = "-10%";
        BaseTable tableCommande = new BaseTable(yPosition, yStart,
                bottomMargin, tableWidth, margin, document, page, true, drawContent);
        DataTable t = new DataTable(tableCommande, page);
        t.addListToTable(commandes, DataTable.HASHEADER);

        Row<PDPage> row = tableCommande.createRow(20);
        Cell cell = row.createCell(80, "Total");
        cell.setFontSize(8);
        cell.setFillColor(new Color(242, 242, 242));
        cell = row.createCell(20, Double.toString(sommePrixTot) + "€");
        cell.setFontSize(8);
        cell.setAlign(HorizontalAlignment.RIGHT);
        cell.setFillColor(new Color(242, 242, 242));
        row = tableCommande.createRow(20);
        cell = row.createCell(80, "Réduction");
        cell.setFontSize(8);
        cell = row.createCell(20, str);
        cell.setFontSize(8);
        cell.setAlign(HorizontalAlignment.RIGHT);
        row = tableCommande.createRow(20);
        cell = row.createCell(80, "Montant Final");
        cell.setFontSize(8);
        cell = row.createCell(20, mTot);
        cell.setFontSize(8);
        cell.setAlign(HorizontalAlignment.RIGHT);


        tableCommande.draw();
        setyPosition(yPosition - tableCommande.getHeaderAndDataHeight());
    }

}




