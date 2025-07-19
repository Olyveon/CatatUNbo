package org.catatunbo.spynet.auditUtils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.File;

public class ReporteAuditoriaPDF {

    // public static void main(String[] args) {
    //     new ReporteAuditoriaPDF().generarReporte("a","b","c","d");
    // }
    public String ubicacion = null;
    public String filename = null;

    public void generarReporte(String clientInfo,
                           String findings,
                           String observations,
                           String openaiBitacore) {
        try {

            LocalDateTime ahora = LocalDateTime.now();
            String fechaHoraNumerica = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            this.filename="auditoria_spynet"+fechaHoraNumerica+".pdf";

            // Prepara carpeta de destino
            String path = System.getProperty("user.home") + "/SpynetReports/";
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();

            // Documento con márgenes suaves
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(path + this.filename));
            doc.open();

            // —– Fuentes —–
            Font normal     = new Font(Font.HELVETICA, 12);
            Font bold       = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font clientTitle= new Font(Font.HELVETICA, 12, Font.BOLD | Font.UNDERLINE, new Color(0,0,0));
            Font policiesFont = new Font(Font.TIMES_ROMAN, 11, Font.ITALIC);

            // —– HEADER (info cliente + logo) —–
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{2f, 1.5f});

            PdfPCell cellInfo = new PdfPCell();
            cellInfo.setBorder(Rectangle.BOX);
            cellInfo.setPadding(10);
            cellInfo.addElement(new Paragraph("Información del cliente", clientTitle));
            cellInfo.addElement(new Paragraph(clientInfo, bold));
            header.addCell(cellInfo);

            Image logo = Image.getInstance(
                ReporteAuditoriaPDF.class.getResource("/images/Spynet.jpg"));
            logo.scaleToFit(120, 120);           // icono más grande
            logo.setAlignment(Element.ALIGN_CENTER);

            PdfPCell cellLogo = new PdfPCell(logo, false);
            cellLogo.setBorder(Rectangle.BOX);
            cellLogo.setPadding(10);
            cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.addCell(cellLogo);

            doc.add(header);
            doc.add(Chunk.NEWLINE);

            // —– POLÍTICAS en recuadro —–
            PdfPTable tblPolicies = new PdfPTable(1);
            tblPolicies.setWidthPercentage(100);
            tblPolicies.setSpacingBefore(10f);
            PdfPCell cellPolicies = new PdfPCell();
            cellPolicies.setBorder(Rectangle.BOX);
            cellPolicies.setPadding(12);
            cellPolicies.addElement(new Paragraph("Políticas de la Empresa", bold));
            cellPolicies.addElement(new Paragraph(
                "En Spynet, nos comprometemos con la confidencialidad, integridad y disponibilidad de la información de nuestros clientes. "
            + "Todos los procesos de auditoría se realizan bajo estrictos estándares éticos y profesionales. "
            + "La información contenida en este informe es confidencial y está destinada exclusivamente al cliente. "
            + "Queda prohibida la reproducción, distribución o divulgación de este documento sin autorización expresa de Spynet.\n\n"
            + "Nuestro equipo sigue las mejores prácticas internacionales en seguridad de la información y auditoría, "
            + "garantizando la objetividad, imparcialidad y transparencia en cada uno de nuestros análisis. "
            + "Cualquier hallazgo o recomendación aquí expuesta debe ser gestionada de acuerdo con las políticas internas del cliente y la normativa vigente.\n\n"
            + "Para cualquier duda o aclaración, puede contactar a nuestro equipo de soporte a través de los canales oficiales de Spynet.",
                policiesFont
            ));
            tblPolicies.addCell(cellPolicies);
            doc.add(tblPolicies);

            // —– HALLAZGOS —–
            PdfPTable tblFind = new PdfPTable(1);
            tblFind.setWidthPercentage(100);
            tblFind.setSplitRows(true);
            tblFind.setSplitLate(false);
            PdfPCell cFind = new PdfPCell();
            cFind.setPadding(10);
            cFind.addElement(new Paragraph("Hallazgos", bold));
            cFind.addElement(new Paragraph(findings, normal));
            tblFind.addCell(cFind);
            doc.add(tblFind);

            // —– OBSERVACIONES —–
            PdfPTable tblObs = new PdfPTable(1);
            tblObs.setWidthPercentage(100);
            tblObs.setSplitRows(true);
            tblObs.setSplitLate(false);
            PdfPCell cObs = new PdfPCell();
            cObs.setPadding(10);
            cObs.addElement(new Paragraph("Observaciones", bold));
            cObs.addElement(new Paragraph(observations, normal));
            tblObs.addCell(cObs);
            doc.add(tblObs);

            // —– BITÁCORA IA —–
            PdfPTable tblIA = new PdfPTable(1);
            tblIA.setWidthPercentage(100);
            tblIA.setSplitRows(true);
            tblIA.setSplitLate(false);
            PdfPCell cIA = new PdfPCell();
            cIA.setPadding(10);
            cIA.addElement(new Paragraph("Bitácora por IA", bold));
            cIA.addElement(new Paragraph(openaiBitacore, normal));
            tblIA.addCell(cIA);
            doc.add(tblIA);

            doc.close();
            System.out.println("✅ PDF generado en: " + path);
            this.ubicacion="PDF generado en: " + path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUbicacion(){
        return this.ubicacion;

    }


    public String getFilename(){
        return this.filename;
    }


}
