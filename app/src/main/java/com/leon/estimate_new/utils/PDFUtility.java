package com.leon.estimate_new.utils;

import static com.leon.estimate_new.helpers.Constants.MAP_SELECTED;
import static com.leon.estimate_new.helpers.MyApplication.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import androidx.core.content.ContextCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;
import com.leon.estimate_new.R;
import com.leon.estimate_new.helpers.Constants;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PDFUtility {
    public static final String PDF_ADDRESS = getContext().getExternalFilesDir(null)
            + File.separator + getContext().getString(R.string.pdf_folder);
    private static final float PAGE_MARGIN = 10f;
    private static final float PADDING = 3f;
    private static final float BORDER = 1f;
    private static BaseFont BASE_FONT;
    private static Font FONT_TITLE;
    private static Font FONT_TITTER;
    private static Font FONT_TEXT_ITALIC;
    private static Font FONT_TEXT;
    private static Font FONT_EN;

    public static void createPdfOriginalForm(Context context, OnDocumentClose mCallback, List<String[]> items,
                                             boolean isPortrait, Bitmap... bitmaps) throws Exception {
        try {
            BASE_FONT = BaseFont.createFont(Constants.PDF_FONT_NAME_FA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        FONT_TITLE = new Font(BASE_FONT, 11, Font.BOLD);
        FONT_TITTER = new Font(BASE_FONT, 12, Font.BOLD);
        FONT_TEXT = new Font(BASE_FONT, 12, Font.NORMAL);
        FONT_TEXT_ITALIC = new Font(BASE_FONT, 13, Font.ITALIC);
        FONT_EN = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        File file = new File(PDF_ADDRESS);
        if (file.exists()) {
            if (!file.delete()) return;
        }

        Document document = new Document();
        document.setMargins(PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());
        new FileOutputStream(PDF_ADDRESS);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(PDF_ADDRESS));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration(1));
        pdfWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        document.open();
        document.add(createHeaderOriginal(context));
        document.add(createDataTable(items, bitmaps));
        document.close();

        try {
            pdfWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (mCallback != null) {
            mCallback.onPDFDocumentClose(file);
        }
    }

    public static void createPdfCrooki(Context context, OnDocumentClose mCallback, List<String[]> items,
                                       boolean isPortrait, Bitmap... bitmaps) throws Exception {
        try {
            BASE_FONT = BaseFont.createFont(Constants.PDF_FONT_NAME_FA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        FONT_TITLE = new Font(BASE_FONT, 13, Font.BOLD);
        FONT_TITTER = new Font(BASE_FONT, 14, Font.BOLD);
        FONT_TEXT = new Font(BASE_FONT, 13, Font.NORMAL);
        FONT_TEXT_ITALIC = new Font(BASE_FONT, 13, Font.ITALIC);
        FONT_EN = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        File file = new File(PDF_ADDRESS);
        if (file.exists()) if (!file.delete()) return;

        final Document document = new Document();
        document.setMargins(PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());
        new FileOutputStream(PDF_ADDRESS);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(PDF_ADDRESS));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration(1));
        pdfWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        document.open();
        document.add(createHeaderCrooki(context, items.get(0)[0]));
        document.add(createCrookiDataTable(items, bitmaps));
        document.close();
        try {
            pdfWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (mCallback != null) mCallback.onPDFDocumentClose(file);
    }

    public static void createPdfPrivilegeForm(Context context, OnDocumentClose mCallback, List<String[]> items,
                                              boolean isPortrait, Bitmap... bitmaps) throws Exception {
        try {
            BASE_FONT = BaseFont.createFont(Constants.PDF_FONT_NAME_FA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        FONT_TITLE = new Font(BASE_FONT, 13, Font.NORMAL);
        FONT_TITTER = new Font(BASE_FONT, 14, Font.BOLD);
        FONT_TEXT = new Font(BASE_FONT, 14, Font.NORMAL);
        FONT_TEXT_ITALIC = new Font(BASE_FONT, 13, Font.ITALIC);

        File file = new File(PDF_ADDRESS);
        if (file.exists()) {
            if (!file.delete()) return;
        }

        Document document = new Document();
        document.setMargins(PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());
        new FileOutputStream(PDF_ADDRESS);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(PDF_ADDRESS));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration(1));
        pdfWriter.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        document.open();

        document.add(createHeaderPrivilege(context, items.get(0)[0]));

        document.add(createPrivilegeDataTable(items, bitmaps));

//        document.add(createPrivilegeSignBox(bitmaps));

        document.close();

        try {
            pdfWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (mCallback != null) {
            mCallback.onPDFDocumentClose(file);
        }
    }

    private static PdfPTable createHeaderPrivilege(Context context, String date) throws Exception {
        final PdfPTable table = new PdfPTable(3);
        LanguageProcessor pe = new ArabicLigaturizer();
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4, 5, 4});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell();
        { /* LEFT TOP */
            cell.setBorder(PdfPCell.NO_BORDER);
            final PdfPTable pdfPTable = new PdfPTable(2);
            pdfPTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            Paragraph temp = new Paragraph(pe.process(".............................."), FONT_TITLE);
            pdfPTable.addCell(temp);

            temp = new Paragraph(pe.process("شماره: "), FONT_TITLE);
            pdfPTable.addCell(temp);

            temp = new Paragraph(pe.process(date), FONT_TITLE);
            pdfPTable.addCell(temp);

            temp = new Paragraph(pe.process("تاریخ درخواست: "), FONT_TITLE);
            pdfPTable.addCell(temp);

            cell.addElement(pdfPTable);

            table.addCell(cell);
        }
        { /* MIDDLE TEXT */
            final PdfPTable logoTable = new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            final Image logo = getImageFromDrawable(((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.form_icon)));
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setBorder(PdfPCell.NO_BORDER);
            logoTable.addCell(logoCell);

            final Paragraph temp = new Paragraph(pe.process("درخواست صدور مجوز واگذاری و حفاری انشعاب"), FONT_TITLE);
            logoCell = new PdfPCell(temp);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            logoCell.setBorder(PdfPCell.NO_BORDER);
            logoCell.setPadding(10f);
            logoTable.addCell(logoCell);

            cell = new PdfPCell(logoTable);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(4f);
            table.addCell(cell);
        }
        {/* RIGHT TOP LOGO*/
            cell = new PdfPCell(new PdfPCell());
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
        }
        return table;
    }

    private static PdfPTable createHeaderCrooki(Context context, String zoneTitle) throws Exception {
        final PdfPTable table = new PdfPTable(3);
        final LanguageProcessor pe = new ArabicLigaturizer();
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 8, 2});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell(new PdfPCell());
        {
            /* LEFT TOP LOGO */
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
        }

        {
            /* MIDDLE TEXT */
            Paragraph temp = new Paragraph(pe.process("شرکت آب و فاضلاب استان اصفهان"), FONT_TITTER);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            zoneTitle = "امور آب و فاضلاب ".concat(zoneTitle);
            temp = new Paragraph(pe.process(zoneTitle), FONT_TITTER);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            temp = new Paragraph(pe.process("کروکی محل و مشخصات مالک"), FONT_TITTER);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            table.addCell(cell);
        }
        /* RIGHT TOP LOGO*/
        {
            final PdfPTable logoTable = new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            final Image logo = getImageFromDrawable(((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.form_icon)));
            if (logo != null) logo.scaleToFit(100, 100);

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBorder(PdfPCell.NO_BORDER);

            logoTable.addCell(logoCell);

            cell = new PdfPCell(logoTable);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }
        return table;
    }

    private static PdfPTable createHeaderOriginal(Context context) throws Exception {
        final PdfPTable table = new PdfPTable(3);
        final LanguageProcessor pe = new ArabicLigaturizer();
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 8, 2});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell cell;
        {
            /* LEFT TOP LOGO */
            cell = new PdfPCell(new PdfPCell());
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
        }
        {
            /* MIDDLE TEXT */
            Paragraph temp = new Paragraph(pe.process("شرکت آب و فاضلاب استان اصفهان"), FONT_TITTER);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);
            temp = new Paragraph(pe.process("ارزیابی"), FONT_TITTER);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);
            table.addCell(cell);
        }
        /* RIGHT TOP LOGO*/
        {
            Image logo = getImageFromDrawable(((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.form_icon)));
            if (logo != null) logo.scaleToFit(80, 80);
            table.addCell(logo);
        }
        return table;
    }

    public static PdfPTable createPrivilegeDataTable(List<String[]> dataTable, Bitmap... bitmaps)
            throws DocumentException {
        final PdfPTable table = createTable(1, new float[]{1f});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        PdfPTable row = new PdfPTable(2);
        LanguageProcessor pe = new ArabicLigaturizer();
        row.setWidthPercentage(100);
        row.setWidths(new float[]{1, 2});
        row.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        row.setWidthPercentage(100);

        PdfPTable pdfPTableTemp = new PdfPTable(2);
        pdfPTableTemp.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        Paragraph temp = new Paragraph(pe.process(dataTable.get(2)[0]), FONT_TEXT_ITALIC);
        PdfPCell pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);


        temp = new Paragraph(pe.process("منطقه: "), FONT_TITLE);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);

        PdfPCell pdfPCell = new PdfPCell(pdfPTableTemp);
        pdfPCell.setPadding(10f);

        temp = new Paragraph(pe.process(dataTable.get(2)[1]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process("شناسه قبض: "), FONT_TITLE);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);
        pdfPCell = new PdfPCell(pdfPTableTemp);
        pdfPCell.setPadding(10f);

        temp = new Paragraph(pe.process(dataTable.get(2)[2].trim()), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process("شماره پیگیری: "), FONT_TITLE);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);
        pdfPCell = new PdfPCell(pdfPTableTemp);
        row.addCell(pdfPCell);

        String text = "مدیر محترم / شهردار محترم شهرداری ".concat(dataTable.get(1)[0]);
        temp = new Paragraph(pe.process(text), FONT_TITTER);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 1));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp = new PdfPTable(1);
        pdfPTableTemp.addCell(pdfPCellTemp);


        text = "آقای / خانم ".concat(dataTable.get(3)[0]).concat(" فرزند ").concat(dataTable.get(3)[1]);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);

        text = " کد ملی ".concat(dataTable.get(3)[2]).concat(" به شماره همراه ").concat(dataTable.get(3)[3]);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.addCell(pdfPCellTemp);

        row.addCell(pdfPTableTemp);

        table.addCell(row);

        row = new PdfPTable(1);
        text = "ٔدر خصوص انجام عملیات ".concat(dataTable.get(3)[4])
                .concat(" با مشخصات زیر معرفی می شوند.  ");
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);

        text = "خواهشمند است ضمن اعلام بلامانع بودن واگذاری انشعاب ملک به شماره پروانه "
                .concat(dataTable.get(3)[5]).concat(" تاریخ صدور ").concat(dataTable.get(3)[6]);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);

        text = "نسبت به صدور مجوز حفاری و اعلام بر و کف معبر در مسیر مورد نظر دستور اقدام را مبذول فرمایید.";
        temp = new Paragraph(pe.process(text), FONT_TEXT);
//        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);


        text = "آدرس محل: شهر".concat(dataTable.get(4)[0]).concat(" خیابان / کوچه / بن بست: ")
                .concat(dataTable.get(4)[1]).concat(" پلاک ").concat(dataTable.get(4)[2]);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
//        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);


        text = "کل حفاری انشعاب آب ".concat(dataTable.get(5)[0]).concat(" متر ");
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 1));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);

        text = "(طول حفاری در آسفالت ".concat(dataTable.get(5)[1]).concat(" متر، حفاری در مسیر خاکی ")
                .concat(dataTable.get(5)[2]).concat(" متر، حفاری در موزاییک با سنگ فرش  ")
                .concat(dataTable.get(5)[3]).concat(" متر )");
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);


        text = "کل حفاری انشعاب فاضلاب ".concat(dataTable.get(6)[0]).concat(" متر ");
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 1));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);

        text = "(طول حفاری در آسفالت ".concat(dataTable.get(6)[1]).concat(" متر، حفاری در مسیر خاکی ")
                .concat(dataTable.get(6)[2]).concat(" متر، حفاری در موزاییک با سنگ فرش  ")
                .concat(dataTable.get(6)[3]).concat(" متر )");
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        row.addCell(pdfPCellTemp);

        final PdfPTable mapTable = new PdfPTable(1);
        mapTable.setWidthPercentage(100);
        mapTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        text = "کروکی محل (در صورت نامشخص بودن موقعیت محل کروکی ترسیم گردد.)";
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        mapTable.addCell(pdfPCellTemp);

        final Image map = getImageFromBitmap(MAP_SELECTED);
        PdfPCell mapCell = new PdfPCell(map);
        mapCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        mapCell.setBorder(PdfPCell.NO_BORDER);
        mapTable.addCell(mapCell);

        PdfPCell cell = new PdfPCell(mapTable);
        cell.setUseAscender(true);
        cell.setPadding(4f);
        row.addCell(cell);

        row.addCell(createPrivilegeSignBox(dataTable.get(8)[0], bitmaps));


        table.addCell(row);
        return table;
    }

    public static PdfPTable createCrookiDataTable(List<String[]> dataTable, Bitmap... bitmaps)
            throws DocumentException {
        final PdfPTable table = createTable(1, new float[]{1f});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        LanguageProcessor pe = new ArabicLigaturizer();

        final PdfPTable row = new PdfPTable(1);
        row.setWidthPercentage(100);
        row.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        row.setWidthPercentage(100);

        final PdfPTable mapTable = new PdfPTable(1);
        mapTable.setWidthPercentage(100);
        mapTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        final Image map = getImageFromBitmap(MAP_SELECTED);
        if (map != null) {
            map.scaleToFit(400, 400);
        }
        final PdfPCell mapCell = new PdfPCell(map);

        mapCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        mapCell.setBorder(PdfPCell.NO_BORDER);
        mapTable.addCell(mapCell);

        final PdfPCell cell = new PdfPCell(mapTable);
        cell.setUseAscender(true);
        cell.setPadding(4f);
        row.addCell(cell);

        PdfPTable pdfPTableTemp = new PdfPTable(3);
        Paragraph temp = new Paragraph(dataTable.get(1)[1].concat(" :X"), FONT_EN);
        PdfPCell pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTableTemp.addCell(pdfPCellTemp);

        temp = new Paragraph(dataTable.get(1)[0].concat(" :Y"), FONT_EN);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTableTemp.addCell(pdfPCellTemp);

        temp = new Paragraph(" :UTM ", FONT_EN);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        PdfPTable pdfPTable = new PdfPTable(2);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" نقطه "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(temp);
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPTable.addCell(pdfPCellTemp);

        pdfPTableTemp.addCell(pdfPTable);
        pdfPCellTemp = new PdfPCell(pdfPTableTemp);

        row.addCell(pdfPCellTemp);

        pdfPTableTemp = new PdfPTable(1);
        String text = "آدرس: ".concat(dataTable.get(2)[0]);
        temp = new Paragraph(pe.process(text), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setPadding(4f);
        pdfPTableTemp.addCell(pdfPCellTemp);
        row.addCell(pdfPCellTemp);

        pdfPTable = new PdfPTable(6);
        pdfPTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        pdfPTableTemp.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        temp = new Paragraph(pe.process(" کدپستی "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" تلفن همراه "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" تلفن ثابت "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" اشتراک "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" ردیف "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(" نام مالک "), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[5]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[4]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[3]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[2]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[1]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        temp = new Paragraph(pe.process(dataTable.get(3)[0]), FONT_TEXT_ITALIC);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setPadding(4f);
        pdfPCellTemp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPTable.addCell(pdfPCellTemp);

        pdfPCellTemp = new PdfPCell(pdfPTable);
        row.addCell(pdfPCellTemp);

        pdfPCellTemp = new PdfPCell(createCrookiSignBox(dataTable.get(4)[0], bitmaps));
        pdfPCellTemp.setPadding(4f);
        pdfPTableTemp.addCell(pdfPCellTemp);
        row.addCell(pdfPCellTemp);

        table.addCell(row);
        return table;
    }

    public static PdfPTable createDataTable(List<String[]> dataTable, Bitmap... bitmaps) throws DocumentException {
        PdfPTable table = createTable(1, new float[]{1f});
        for (int i = 0; i < 4; i++)
            table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER, new float[]{3f, 2f, 3f, 2f, 3f, 2f},
                    new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                    new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                            BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE}, dataTable.get(i),
                    FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE));

        table.addCell(createTableRow(4, PdfPCell.ALIGN_CENTER, new float[]{3f, 2f, 9f, 1f},
                new float[]{BORDER, BORDER, BORDER, PdfPCell.NO_BORDER}, new BaseColor[]{
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(4), FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE));


        table.addCell(createTableRow(5, PdfPCell.ALIGN_CENTER, new float[]{1f, 1f, 1f, 1f, 1f},
                new float[]{BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE}, dataTable.get(5),
                FONT_TEXT, FONT_TITLE, FONT_TITLE, FONT_TITLE, FONT_TITLE));

        for (int i = 1; i < 9; i++)
            table.addCell(createTableRow(5, PdfPCell.ALIGN_CENTER, new float[]{1f, 1f, 1f, 1f, 1f},
                    new float[]{BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                    new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                            BaseColor.WHITE, BaseColor.WHITE}, dataTable.get(5 + i),
                    FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TEXT, FONT_TITLE));

        table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 1f, 2f, 1f, 2f, 2f},
                new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(14), FONT_TITLE, FONT_TITLE, FONT_TITLE, FONT_TITLE, FONT_TITLE, FONT_TITLE));
        for (int i = 1; i < 9; i++)
            table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                    new float[]{1f, 1f, 2f, 1f, 2f, 2f},
                    new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                    new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                            BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                    dataTable.get(14 + i)));

        table.addCell(createTableRow(8, PdfPCell.ALIGN_CENTER,
                new float[]{2f, 2f, 1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(23), FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE));

        table.addCell(createTableRow(9, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 3f},
                new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(24), FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TITLE));
        table.addCell(createTableRow(9, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 3f},
                new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(25), FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TEXT, FONT_TITLE, FONT_TITLE));

        table.addCell(createDataTableDescription(dataTable, 2, PdfPCell.ALIGN_RIGHT, new float[]{13f, 1f},
                new float[]{PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE}, FONT_TEXT, FONT_TITLE));

        final PdfPCell pdfPCellTemp = new PdfPCell(createOriginalSignBox(dataTable.get(dataTable.size() - 1)[0], bitmaps));
        pdfPCellTemp.setPadding(4f);
        PdfPTable pdfPTableTemp = new PdfPTable(1);
        pdfPTableTemp.addCell(pdfPCellTemp);
        table.addCell(pdfPCellTemp);

        return table;
    }

    public static PdfPTable createDataTableDescription(List<String[]> dataTable, int column, int align, float[] width, float[] border,
                                                       BaseColor[] baseColors, Font... fonts) throws DocumentException {
        final PdfPTable table = createTable(column, width);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        final PdfPCell cell = createPdfCell();
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        for (int i = 26; i < dataTable.size() - 1; i++) {
            for (int j = 0; j < column; j++) {
                cell.setPhrase((fonts != null && fonts.length > 0) ? addPhrase(dataTable.get(i)[j], fonts[j]) : addPhrase(dataTable.get(i)[j]));
                cell.setBorderWidthRight(border[j]);
                cell.setBackgroundColor(baseColors[j]);
                table.addCell(cell);
            }
        }
        return table;
    }

    public static PdfPTable createDataTableOld(List<String[]> dataTable, Bitmap... bitmaps) throws DocumentException {
        PdfPTable table = createTable(1, new float[]{1f});
        for (int i = 0; i < 4; i++)
            table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER, new float[]{3f, 2f, 3f, 2f, 3f, 2f},
                    new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                    new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                            BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE}, dataTable.get(i)));

        table.addCell(createTableRow(4, PdfPCell.ALIGN_CENTER, new float[]{3f, 2f, 9f, 1f},
                new float[]{BORDER, BORDER, BORDER, PdfPCell.NO_BORDER}, new BaseColor[]{
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(4)));

        table.addCell(createTableRow(4, PdfPCell.ALIGN_CENTER, new float[]{3f, 1f, 3f, 1f},
                new float[]{BORDER, BORDER, BORDER, PdfPCell.NO_BORDER}, new BaseColor[]{
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(5)));

        table.addCell(createTableRow(12, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER,
                        BORDER, PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(6)));

        table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(7)));

        table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(8)));

        table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(9)));


        for (int i = 0; i < 9; i++)
            table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER,
                    new float[]{1f, 1f, 2f, 1f, 2f, 2f},
                    new float[]{BORDER, BORDER, BORDER, BORDER, BORDER, PdfPCell.NO_BORDER},
                    new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                            BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                    dataTable.get(10 + i)));

        table.addCell(createTableRow(9, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE},
                dataTable.get(19)));

        table.addCell(createTableRow(9, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE},
                dataTable.get(20)));

        table.addCell(createTableRow(8, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(21)));

        table.addCell(createTableRow(6, PdfPCell.ALIGN_CENTER, new float[]{3f, 2f, 1f, 2f, 2f, 3f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER,
                        PdfPCell.NO_BORDER}, new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(22)));

        table.addCell(createTableRow(8, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 3f, 1f, 3f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(23)));

        table.addCell(createTableRow(8, PdfPCell.ALIGN_CENTER,
                new float[]{1f, 2f, 1f, 2f, 1f, 2f, 1f, 2f},
                new float[]{PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, BORDER,
                        PdfPCell.NO_BORDER, BORDER, PdfPCell.NO_BORDER, PdfPCell.NO_BORDER},
                new BaseColor[]{BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE,
                        BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE, BaseColor.WHITE},
                dataTable.get(24)));

        final PdfPCell pdfPCellTemp = new PdfPCell(createOriginalSignBox(/*"تست تست زاده"*/dataTable.get(25)[0], bitmaps));
        pdfPCellTemp.setPadding(4f);
        PdfPTable pdfPTableTemp = new PdfPTable(1);
        pdfPTableTemp.addCell(pdfPCellTemp);
        table.addCell(pdfPCellTemp);

        return table;
    }

    private static PdfPTable createTable(int column, float[] width) throws DocumentException {
        PdfPTable table = new PdfPTable(column);
        table.setWidths(width);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        return table;
    }

    private static PdfPTable createTableRow(int column, int align, float[] width, float[] border,
                                            BaseColor[] baseColors, String[] items, Font... fonts)
            throws DocumentException {
        PdfPTable table = createTable(column, width);
        PdfPCell cell = createPdfCell();
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < column; i++) {
            cell.setPhrase((fonts != null && fonts.length > 0) ? addPhrase(items[i], fonts[i]) : addPhrase(items[i]));
            cell.setBorderWidthRight(border[i]);
            cell.setBackgroundColor(baseColors[i]);
            table.addCell(cell);
        }
        return table;
    }

    private static PdfPTable createTableRow(int column, int align, float width, BaseColor baseColor,
                                            String[] items)
            throws DocumentException {
        float[] widths = new float[column];
        float[] borders = new float[column];
        BaseColor[] baseColors = new BaseColor[column];
        Arrays.fill(widths, width);
        Arrays.fill(borders, BORDER);
        Arrays.fill(baseColors, baseColor);
        borders[column - 1] = PdfPCell.NO_BORDER;
        return createTableRow(column, align, widths, borders, baseColors, items);
    }

    private static PdfPCell createPdfCell() {
        PdfPCell cell = new PdfPCell();
        cell.setPaddingTop(0);
        cell.setPaddingBottom(PADDING);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    @SuppressLint("SimpleDateFormat")
    private static PdfPTable createCrookiSignBox(String name, Bitmap... bitmaps) throws DocumentException {
        final LanguageProcessor pe = new ArabicLigaturizer();

        final PdfPTable signTable = new PdfPTable(1);
        signTable.setWidthPercentage(100);
        signTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        final PdfPTable tableTemp = new PdfPTable(3);
        tableTemp.setWidths(new float[]{1, 2, 2});

        String text = "امضا: ";
        Paragraph temp = new Paragraph(pe.process(text), FONT_TEXT);
        PdfPCell pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);
        tableTemp.addCell(pdfPCellTemp);

        final PersianCalendar persianCalendar = new PersianCalendar();
        text = persianCalendar.getPersianLongDate();
        text = text.concat(" - ").concat((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
        text = "تاریخ: ".concat(text);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        tableTemp.addCell(pdfPCellTemp);


        text = "تهیه کننده کروکی: ".concat(name);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);

        tableTemp.addCell(pdfPCellTemp);

        signTable.addCell(tableTemp);

        if (bitmaps != null && bitmaps.length > 0) {
            //
            final Image sign = getImageFromBitmap(bitmaps[1]);

            final PdfPCell signCell = new PdfPCell(sign);
            signCell.setBorder(PdfPCell.NO_BORDER);
            signTable.addCell(signCell);
        }

//        signTable.addCell(pdfPCellTemp);
        return signTable;
    }

    @SuppressLint("SimpleDateFormat")
    private static PdfPTable createPrivilegeSignBox(String name, Bitmap... bitmaps) {
        final LanguageProcessor pe = new ArabicLigaturizer();

        final PdfPTable signTable = new PdfPTable(1);
        signTable.setWidthPercentage(100);

        String text = "ارزیاب / رئیس مشترکین منطقه: ".concat(name);
        Paragraph temp = new Paragraph(pe.process(text), FONT_TEXT);
        PdfPCell pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);
        signTable.addCell(pdfPCellTemp);

        if (bitmaps != null && bitmaps.length > 0) {
            final Image sign = getImageFromBitmap(bitmaps[1]);
            if (sign != null) sign.setAlignment(Element.ALIGN_CENTER);
            final PdfPCell signCell = new PdfPCell(sign);
            signCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signCell.setBorder(PdfPCell.NO_BORDER);
            signTable.addCell(signCell);
        }


        final PersianCalendar persianCalendar = new PersianCalendar();
        text = persianCalendar.getPersianLongDate();
        text = text.concat(" - ").concat((new SimpleDateFormat("HH:mm:ss")).format(new Date()));

        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        signTable.addCell(pdfPCellTemp);


        text = "توجه: متقاضیانی که دارای پروانه ساختمانی یا پایان ساخت میباشند در هنگام مراجعه به شهرداری همراه داشته باشند.";
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);

        signTable.addCell(pdfPCellTemp);
        return signTable;
    }

    @SuppressLint("SimpleDateFormat")
    private static PdfPTable createOriginalSignBox(String name, Bitmap... bitmaps)
            throws DocumentException {
        final LanguageProcessor pe = new ArabicLigaturizer();

        final PdfPTable signTable = new PdfPTable(1);
        signTable.setWidthPercentage(100);
        signTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        final PdfPTable tableTemp = new PdfPTable(3);
        tableTemp.setWidths(new float[]{1, 2, 2});

        String text = "امضا متقاضی: ";
        Paragraph temp = new Paragraph(pe.process(text), FONT_TEXT);
        PdfPCell pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);
        tableTemp.addCell(pdfPCellTemp);

        final PersianCalendar persianCalendar = new PersianCalendar();
        text = persianCalendar.getPersianLongDate();
        text = text.concat(" - ").concat((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
        text = "تاریخ: ".concat(text);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        tableTemp.addCell(pdfPCellTemp);


        text = "تهیه کننده کروکی: ".concat(name);
        temp = new Paragraph(pe.process(text), FONT_TEXT);
        pdfPCellTemp = new PdfPCell(addEmptyLine(temp, 2));
        pdfPCellTemp.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellTemp.setBorder(PdfPCell.NO_BORDER);
        pdfPCellTemp.setPadding(4f);

        tableTemp.addCell(pdfPCellTemp);

        signTable.addCell(tableTemp);

        if (bitmaps != null && bitmaps.length > 0) {
            Image sign = getImageFromBitmap(bitmaps[0]);
            if (sign != null) sign.scaleToFit(50, 50);

            PdfPCell signCell = new PdfPCell(sign);
            final PdfPTable table = new PdfPTable(2);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            signCell.setBorder(PdfPCell.NO_BORDER);
            signCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(signCell);

            sign = getImageFromBitmap(bitmaps[1]);
            if (sign != null) sign.scaleToFit(80, 80);
            signCell = new PdfPCell(sign);
            signCell.setBorder(PdfPCell.NO_BORDER);
            signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            table.addCell(signCell);

            signTable.addCell(table);
        }

//        signTable.addCell(pdfPCellTemp);
        return signTable;
    }

    public static Image getImageFromDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bmp = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            try {
                Image image = Image.getInstance(stream.toByteArray());
                image.setAlignment(Element.ALIGN_RIGHT);
                image.scaleToFit(40, 40);
                return image;
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Image getImageFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_RIGHT);
            image.scaleToFit(200, 300);
            return image;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static Bitmap getImagesFromPDF(File pdfFilePath, Context context) throws IOException {
        final File destinationFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Environment.DIRECTORY_PICTURES);
        if (!destinationFolder.exists()) if (!destinationFolder.mkdirs()) return null;
        final ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFilePath, ParcelFileDescriptor.MODE_READ_ONLY);
        final PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            final PdfRenderer.Page page = renderer.openPage(i);
            final Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            final String timeStamp = (new SimpleDateFormat(context.getString(R.string.save_format_name))).format(new Date());
            final String fileNameToSave = "JPEG_" + new Random().nextInt() + "_" + timeStamp + ".jpg";
            final File file = new File(destinationFolder.getAbsolutePath(), fileNameToSave);
            if (file.exists()) if (!file.delete()) return null;
            try {
                final FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                return bitmap;
            } catch (Exception e) {
                new CustomToast().error(e.getMessage());
            }
        }
        return null;
    }

    public static Paragraph addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) paragraph.add(new Paragraph("\n"));
        return paragraph;
    }

    private static Phrase addPhrase(String s, Font... fonts) {
        LanguageProcessor pe = new ArabicLigaturizer();
        return new Phrase(pe.process(s), (fonts != null && fonts.length > 0) ? fonts[0] : FONT_TEXT);
    }

    private static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    private static void setMetaData(Document document) {
        document.addCreationDate();
        document.addAuthor("RAVEESH G S");
        document.addCreator("RAVEESH G S");
        document.addHeader("DEVELOPER", "RAVEESH G S");
    }

    private static Image getImage(byte[] imageByte, boolean isTintingRequired) throws Exception {
        Paint paint = new Paint();
        if (isTintingRequired) {
            paint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        }
        Bitmap input = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(input, 0, 0, paint);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        output.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setWidthPercentage(80);
        return image;
    }

    private static Image getBarcodeImage(PdfWriter pdfWriter, String barcodeText) {
        Barcode128 barcode = new Barcode128();
        //barcode.setBaseline(-1); //BARCODE TEXT ABOVE
        barcode.setFont(null);
        barcode.setCode(barcodeText);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setTextAlignment(Element.ALIGN_BASELINE);
        return barcode.createImageWithBarcode(pdfWriter.getDirectContent(), BaseColor.BLACK, null);
    }

    public interface OnDocumentClose {
        void onPDFDocumentClose(File file);
    }
}
