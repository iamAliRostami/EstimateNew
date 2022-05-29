package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.Images;

import java.util.List;

@Dao
public interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertImage(Images images);


    @Query("SELECT * FROM Images LIMIT 1")
    Images getImage();

    @Query("SELECT COUNT(*) FROM Images")
    int getUnsentImage();

    @Query("SELECT * FROM Images WHERE peygiri =:peygiri And docId=:imageCode")
    List<Images> getImagesByPeygiriAndImageCode(String peygiri, String imageCode);

    @Query("SELECT * FROM Images WHERE peygiri =:peygiri")
    List<Images> getImagesByPeygiri(String peygiri);

    @Query("SELECT * FROM Images WHERE docId =:imageCode")
    List<Images> getImagesByImageCode(String imageCode);

    @Query("SELECT * FROM Images WHERE billId =:billId")
    List<Images> getImagesByBillId(String billId);

    @Query("SELECT * FROM Images WHERE trackingNumber =:trackingNumber OR billId =:billId")
    List<Images> getImagesByTrackingNumberOrBillId(String trackingNumber, String billId);

    @Query("DELETE FROM Images WHERE imageId = :imageId")
    void deleteByID(int imageId);
}
