package com.leon.estimate_new.utils;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;
import com.leon.estimate_new.helpers.Constants;

import java.io.IOException;

public class PageNumeration extends PdfPageEventHelper {
    private static Font FONT_FOOTER;
    private static Font FONT_FOOTER_EN;
    private static Font FONT_FOOTER_FA;
    private int pageNumber;

    PageNumeration(int pageNumber) {
        this.pageNumber = pageNumber;
        try {
            BaseFont baseFont = BaseFont.createFont(Constants.PDF_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            FONT_FOOTER = new Font(baseFont, 8, Font.NORMAL, BaseColor.DARK_GRAY);

            baseFont = BaseFont.createFont(Constants.PDF_FONT_NAME_FA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            FONT_FOOTER_FA = new Font(baseFont, 8, Font.NORMAL, BaseColor.DARK_GRAY);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        FONT_FOOTER_EN = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfPCell cell;
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            LanguageProcessor pe = new ArabicLigaturizer();
            //1st Column
            cell = new PdfPCell(new Phrase(pe.process("صفحه - ".concat(String.valueOf(writer.getPageNumber())).concat(" از 2")), FONT_FOOTER_FA));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 5, writer.getDirectContent());

            //2nd Column
            Anchor anchor = new Anchor(new Phrase(" QF-0017-04 ".concat(pe.process("کد فرم: ")), FONT_FOOTER));
//            anchor.setReference("http://mywebsite.com/");
            cell = new PdfPCell(anchor);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 5, writer.getDirectContent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}