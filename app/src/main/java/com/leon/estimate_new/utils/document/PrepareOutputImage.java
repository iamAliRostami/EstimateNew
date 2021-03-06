package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.utils.PDFUtility.PDF_ADDRESS;
import static com.leon.estimate_new.utils.PDFUtility.createPdfCrooki;
import static com.leon.estimate_new.utils.PDFUtility.createPdfOriginalForm;
import static com.leon.estimate_new.utils.PDFUtility.createPdfPrivilegeForm;
import static com.leon.estimate_new.utils.PDFUtility.getImagesFromPDF;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.leon.estimate_new.activities.FinalReportActivity;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.utils.CustomToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrepareOutputImage extends BaseAsync {
    private final ExaminerDuties examinerDuty;
    private final boolean licence;
    private Bitmap[] bitmaps;
    private Bitmap bitmapOutput;
    private Bitmap bitmapCrooki;
    private Bitmap licenceBitmap;
    private List<String[]> licenceRows;

    public PrepareOutputImage(Context context, ExaminerDuties examinerDuties, boolean licence, Object... view) {
        super(context, view);
        examinerDuty = examinerDuties;
        this.licence = licence;
        if (view.length == 3) {
            bitmaps = new Bitmap[2];
            bitmaps[0] = (Bitmap) view[1];
            bitmaps[1] = (Bitmap) view[2];
        }
    }

    public PrepareOutputImage(Context context, ExaminerDuties examinerDuties,
                              boolean licence, List<String[]> licenceRows, Object... view) {
        super(context, view);
        examinerDuty = examinerDuties;
        this.licence = licence;
        this.licenceRows = licenceRows;
        if (view.length == 3) {
            bitmaps = new Bitmap[2];
            bitmaps[0] = (Bitmap) view[1];
            bitmaps[1] = (Bitmap) view[2];
        }
    }


    @Override
    public void postTask(Object o) {
        if (licence)
            ((FinalReportActivity) o).setFormImageView(new Bitmap[]{bitmapOutput, bitmapCrooki},
                    licenceBitmap, licenceRows);
        else ((FinalReportActivity) o).setFormImageView(new Bitmap[]{bitmapOutput, bitmapCrooki});

    }

    @Override
    public void preTask(Object o) {

    }

    @Override
    public void backgroundTask(Activity activity) {
        try {
            if (bitmaps != null && bitmaps.length > 0)
                createPdfOriginalForm(activity, null, getFormData(), true, bitmaps);
            else
                createPdfOriginalForm(activity, null, getFormData(), true);
            bitmapOutput = getImagesFromPDF(new File(PDF_ADDRESS), activity);
        } catch (Exception e) {
            e.printStackTrace();
            new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
        }
        try {
            if (bitmaps != null && bitmaps.length > 0)
                createPdfCrooki(activity, null, getCrookiData(), true, bitmaps);
            else
                createPdfCrooki(activity, null, getCrookiData(), true);
            bitmapCrooki = getImagesFromPDF(new File(PDF_ADDRESS), activity);
        } catch (Exception e) {
            new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
        }
        if (licence)
            try {
                if (bitmaps != null && bitmaps.length > 0)
                    createPdfPrivilegeForm(activity, null, getLicenceData(), true, bitmaps);
                else
                    createPdfPrivilegeForm(activity, null, getLicenceData(), true);
                licenceBitmap = getImagesFromPDF(new File(PDF_ADDRESS), activity);
            } catch (Exception e) {
                new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
            }
    }

    private List<String[]> getFormData() {
        final List<String[]> temp = new ArrayList<>();

        String[] rowString = new String[]{examinerDuty.billId != null ? examinerDuty.billId :
                examinerDuty.neighbourBillId, "?????????? ??????", examinerDuty.eshterak, "????????????",
                examinerDuty.radif, "????????"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.sanadNumber), "?????????? ??????",
                examinerDuty.parNumber, "?????????? ????????????", examinerDuty.trackNumber, "?????????? ????????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.fatherName, "?????? ??????", examinerDuty.sureName,
                "?????? ????????????????", examinerDuty.firstName, "??????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.phoneNumber, "???????? ????????", examinerDuty.mobile != null ?
                examinerDuty.mobile : examinerDuty.moshtarakMobile, "???????? ??????????",
                examinerDuty.nationalId, "??????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.postalCode, "???? ????????", examinerDuty.address, "????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.noeVagozariS, "?????? ??????????????", "?????????????? ????????",
                "?????????????? ????????", "??????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.block, "????????", String.valueOf(examinerDuty.arseNew),
                String.valueOf(examinerDuty.arse), "????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.arz, "?????? ??????", String.valueOf(examinerDuty.aianKolNew),
                String.valueOf(examinerDuty.aianKol), "???????????? ????"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.arzeshMelk), "???????? ????????????????",
                String.valueOf(examinerDuty.aianMaskooniNew),
                String.valueOf(examinerDuty.aianMaskooni), "???????????? ????????????"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.taxfifS), "?????? ??????????",
                String.valueOf(examinerDuty.aianNonMaskooniNew),
                String.valueOf(examinerDuty.aianNonMaskooni), "???????????? ??????????"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.tedadTaxfif), "?????????? ???????? ??????????",
                String.valueOf(examinerDuty.tedadMaskooniNew),
                String.valueOf(examinerDuty.tedadMaskooni), "?????????? ???????? ????????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.adamLicence ? "????????" : "??????????", "???????? ?????? ????????",
                String.valueOf(examinerDuty.tedadTejariNew),
                String.valueOf(examinerDuty.tedadTejari), "?????????? ???????? ??????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.qaradad ? "????????" : "??????????", "?????????????? ?????????? ????????",
                String.valueOf(examinerDuty.tedadSaierNew), String.valueOf(examinerDuty.tedadSaier),
                "?????????? ???????? ????????"};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.qaradadNumber, "?????????? ??????????????",
                String.valueOf(examinerDuty.zarfiatQarardadiNew),
                String.valueOf(examinerDuty.zarfiatQarardadi), "?????????? ????????????????"};
        temp.add(rowString);

        rowString = new String[]{"??????????", "??????????", "???????? ????????????", "?????????? ????????", "?????? ??????", "????????????"};
        temp.add(rowString);

        final ArrayList<Tejariha> tejarihas = new ArrayList<>(getApplicationComponent().MyDatabase().tejarihaDao().getTejarihaByTrackNumber(examinerDuty.trackNumber));
        for (int i = 0; i < tejarihas.size(); i++) {
            final Tejariha tejariha = tejarihas.get(i);
            rowString = new String[]{String.valueOf(tejariha.capacity), tejariha.a, tejariha.vahedMohasebe,
                    String.valueOf(tejariha.tedadVahed), tejariha.noeShoql, tejariha.karbari};
            temp.add(rowString);
        }
        for (int i = 8; i > tejarihas.size(); i--) {
            rowString = new String[]{"-", "-", "-", "-", "-", "-"};
            temp.add(rowString);
        }

        rowString = new String[]{examinerDuty.qotrEnsheabFS, "?????? ???????????? ????????????",
                examinerDuty.qotrEnsheabS, "?????? ???????????? ????",
                examinerDuty.ezharNazarF ? "????????" : "??????????", "?????????? ?????? ????????????:",
                examinerDuty.ezharNazarA ? "????????" : "??????????", "?????????? ?????? ????:"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.faseleOtherA), "????????:",
                String.valueOf(examinerDuty.faseleSangA), "?????? ??????:",
                String.valueOf(examinerDuty.faseleAsphaltA), "????????????:",
                String.valueOf(examinerDuty.faseleKhakiA), "????????:", "?????????? ???? ???????? ????"};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.faseleOtherF), "????????:",
                String.valueOf(examinerDuty.faseleSangF), "?????? ??????:",
                String.valueOf(examinerDuty.faseleAsphaltF), "????????????:",
                String.valueOf(examinerDuty.faseleKhakiF), "????????:", "???? ???????? ????????????"};
        temp.add(rowString);

        final int charPerLine = 110;
        String description = examinerDuty.description.concat(" *** ").concat(examinerDuty.masrafDescription)
                .concat(" *** ").concat(examinerDuty.chahDescription).concat(" *** ").concat(examinerDuty.mapDescription);
        rowString = new String[]{description.substring(0, Math.min(description.length(), charPerLine)), "??????????????: "};
        temp.add(rowString);

        while (description.length() > charPerLine && temp.size() < 35) {
            description = description.substring(charPerLine);
            rowString = new String[]{description.substring(0, Math.min(description.length(), charPerLine)), " "};
            temp.add(rowString);
        }

        rowString = new String[]{examinerDuty.examinerName};
        temp.add(rowString);
        return temp;
    }

    private List<String[]> getCrookiData() {
        final List<String[]> temp = new ArrayList<>();
        String[] rowString = new String[]{examinerDuty.zoneTitle};
        temp.add(rowString);

        rowString = new String[]{String.valueOf(examinerDuty.x1), String.valueOf(examinerDuty.y1)};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.address};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.nameAndFamily, examinerDuty.radif, examinerDuty.eshterak,
                examinerDuty.phoneNumber, examinerDuty.mobile != null ? examinerDuty.mobile :
                examinerDuty.moshtarakMobile, examinerDuty.postalCode};
        temp.add(rowString);

        rowString = new String[]{examinerDuty.examinerName};
        temp.add(rowString);

        return temp;
    }

    private List<String[]> getLicenceData() {
        if (licenceRows == null) {
            licenceRows = new ArrayList<>();
            String[] rowString = new String[]{examinerDuty.examinationDay};
            licenceRows.add(rowString);

            rowString = new String[]{examinerDuty.zoneTitle};
            licenceRows.add(rowString);

            rowString = new String[]{examinerDuty.zoneTitle, examinerDuty.billId != null ?
                    examinerDuty.billId : examinerDuty.neighbourBillId, examinerDuty.trackNumber};
            licenceRows.add(rowString);

            rowString = new String[]{examinerDuty.nameAndFamily, examinerDuty.fatherName,
                    examinerDuty.nationalId, examinerDuty.mobile != null ? examinerDuty.mobile :
                    examinerDuty.moshtarakMobile, examinerDuty.operation, examinerDuty.parNumber,
                    examinerDuty.sodurDate};
            licenceRows.add(rowString);

            rowString = new String[]{examinerDuty.zoneTitle, examinerDuty.address, String.valueOf(examinerDuty.pelak)};
            licenceRows.add(rowString);

            rowString = new String[]{String.valueOf(examinerDuty.faseleAsphaltA +
                    examinerDuty.faseleKhakiA + examinerDuty.faseleSangA), String.valueOf(examinerDuty.faseleAsphaltA),
                    String.valueOf(examinerDuty.faseleKhakiA), String.valueOf(examinerDuty.faseleSangA)};
            licenceRows.add(rowString);

            rowString = new String[]{String.valueOf(examinerDuty.faseleAsphaltF +
                    examinerDuty.faseleKhakiF + examinerDuty.faseleSangF), String.valueOf(examinerDuty.faseleAsphaltF),
                    String.valueOf(examinerDuty.faseleKhakiF), String.valueOf(examinerDuty.faseleSangF)};
            licenceRows.add(rowString);

            rowString = new String[]{String.valueOf(examinerDuty.faseleAsphaltF +
                    examinerDuty.faseleKhakiF + examinerDuty.faseleSangF + examinerDuty.faseleAsphaltF +
                    examinerDuty.faseleKhakiF + examinerDuty.faseleSangF)};
            licenceRows.add(rowString);

            rowString = new String[]{examinerDuty.examinerName};

            licenceRows.add(rowString);
        }
        return licenceRows;
    }

    @Override
    public void backgroundTask(Context context) {
    }
}
