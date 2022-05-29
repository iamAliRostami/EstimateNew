package com.leon.estimate_new.infrastructure;

import com.leon.estimate_new.tables.AddDocument;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.tables.CalculationInfo;
import com.leon.estimate_new.tables.CalculationUserInput;
import com.leon.estimate_new.tables.CalculationUserInputSend;
import com.leon.estimate_new.tables.GISInfo;
import com.leon.estimate_new.tables.GISToken;
import com.leon.estimate_new.tables.ImageDataThumbnail;
import com.leon.estimate_new.tables.ImageDataTitle;
import com.leon.estimate_new.tables.Input;
import com.leon.estimate_new.tables.Login;
import com.leon.estimate_new.tables.LoginFeedBack;
import com.leon.estimate_new.tables.LoginInfo;
import com.leon.estimate_new.tables.Place;
import com.leon.estimate_new.tables.Request;
import com.leon.estimate_new.tables.UploadImage;
import com.leon.estimate_new.tables.Uri;
import com.leon.estimate_new.utils.SimpleMessage;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Leon on 12/9/2017.
 */
public interface IAbfaService {

    @POST("MoshtarakinApi/SepanoDMS/V1/Auth/Login/{userName}/{password}")
    Call<LoginFeedBack> login(@Path("userName") String username, @Path("password") String password);

    @POST("/MoshtarakinApi/SepanoDMS/V1/Login/{username}/{password}")
    Call<Login> login2(@Path("username") String username, @Path("password") String password);

    @POST("/MoshtarakinApi/Gis/V1/GetXy/jesuschrist/{billId}")
    Call<Place> getXY(@Path("billId") String billId);

    @POST("/MoshtarakinApi/SepanoDMS/V1/GetDoc/{token}")
    Call<ResponseBody> getDoc(@Path("token") String token, @Body Uri uri);

    @GET("/MoshtarakinApi/SepanoDms/V1/GetTitles/{token}")
    Call<ImageDataTitle> getTitle(@Path("token") String token);

    @GET("/MoshtarakinApi/SepanoDMS/V1/GetDocsListThumbnail/{billIdOrTrackNumber}/{token}")
    Call<ImageDataThumbnail> getDocsListThumbnail(@Path("token") String token,
                                                  @Path("billIdOrTrackNumber") String billIdOrTrackNumber
    );

    @GET("/MoshtarakinApi/SepanoDMS/V1/GetDocsListHighQuality/{billIdOrTrackNumber}/{token}")
    Call<ArrayList<String>> getDocsListHighQuality(@Path("token") String token,
                                                   @Path("billIdOrTrackNumber") String billIdOrTrackNumber
    );

    @Multipart
    @POST("/MoshtarakinApi/SepanoDMS/V1/Upload/{token}")
    Call<UploadImage> uploadDoc(@Path("token") String token, @Part MultipartBody.Part imageFile,
                                @Part("docId") int docId, @Part("billId") String billId);

    @Multipart
    @POST("/MoshtarakinApi/SepanoDMS/V1/Upload/{token}")
    Call<UploadImage> uploadDocNew(@Path("token") String token, @Part MultipartBody.Part imageFile,
                                   @Part("docId") int docId, @Part("trackingNumber") String trackingNumber);

    @POST("/MoshtarakinApi/SepanoDMS/V1/Add/{token}")
    Call<AddDocument> addDocument(@Path("token") String token, @Body AddDocument addDocument);


    @PATCH("/Auth/Account/UpdateDeviceId")
    Call<SimpleMessage> signSerial(@Query("deviceId") String deviceId);

    @PATCH("/Auth/Account/UpdateDeviceIdAnanymous")
    Call<SimpleMessage> signSerial(@Body LoginInfo logininfo);

    @GET("/MoshtarakinApi/ExaminationManager/GetMyWorks")
    Call<Input> getMyWorks();

    @GET("/MoshtarakinApi/ExaminationManager/GetExaminationDetails/")
    Call<CalculationInfo> getMyWorksDetails(@Query("trackNumber") String trackNumber);


    @POST("/MoshtarakinApi/ExaminationManager/SetExaminationInfo")
    Call<SimpleMessage> setExaminationInfo(@Body ArrayList<CalculationUserInputSend> calculationUserInputSend);

    @POST("/MoshtarakinApi/ExaminationManager/SetExaminationInfo")
    Call<SimpleMessage> SetExaminationInfo(@Body CalculationUserInput calculationUserInput);

    @GET("/MoshtarakinApi/Gis/V1/Token/jesuschrist")
    Call<GISToken> getGISToken();

    @Headers("Content-Type: application/json")
    @POST("/MoshtarakinApi/Gis/V1/Parcels")
    Call<String> getGisParcels(@Body GISInfo gisInfo);

    @POST("/MoshtarakinApi/Gis/V1/WaterPipe")
    Call<String> getGisWaterPipe(@Body GISInfo gisInfo);

    @POST("/MoshtarakinApi/Gis/V1/WaterTransfer")
    Call<String> getGisWaterTransfer(@Body GISInfo gisInfo);

    @POST("/MoshtarakinApi/Gis/V1/SanitationTransfer")
    Call<String> getGisSanitationTransfer(@Body GISInfo gisInfo);

    @GET("/moshtarakinApi/ExaminationManager/GetArzeshDaraii?")
    Call<Arzeshdaraei> getArzeshDaraii(@Query("zoneId") int zoneId);

    @POST("/moshtarakinApi/ExaminationManager/RegisterNew")
    Call<SimpleMessage> sendRequestNew(@Body Request request);

    @POST("/moshtarakinApi/ExaminationManager/RegisterAS")
    Call<SimpleMessage> sendRequestAfterSale(@Body Request request);
}

