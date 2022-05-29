package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.ExaminerDuties;

import java.util.List;

@Dao
public interface ExaminerDutiesDao {

    @Query("SELECT * FROM ExaminerDuties")
    List<ExaminerDuties> getExaminerDuties();

    //    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExaminerDuties> values);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExaminerDuties values);

    @Query("DELETE FROM ExaminerDuties WHERE trackNumber = :trackNumber")
    void deleteByTrackNumber(String trackNumber);

    @Query("SELECT * FROM ExaminerDuties WHERE isPeymayesh != '1' ORDER BY trackNumber desc ")
    List<ExaminerDuties> unreadExaminerDuties();

    @Query("SELECT * FROM ExaminerDuties ORDER BY trackNumber desc ")
    List<ExaminerDuties> ExaminerDuties();

    @Query("SELECT * FROM ExaminerDuties WHERE isPeymayesh != '1' AND trackNumber=:trackNumber ORDER BY trackNumber desc ")
//    List<ExaminerDuties> unreadExaminerDutiesByTrackNumber(String trackNumber);
    ExaminerDuties unreadExaminerDutiesByTrackNumber(String trackNumber);

    @Query("SELECT * FROM ExaminerDuties WHERE trackNumber=:trackNumber ORDER BY trackNumber desc ")
    ExaminerDuties examinerDutiesByTrackNumber(String trackNumber);

    @Query("UPDATE ExaminerDuties SET isPeymayesh = :sent  WHERE trackNumber = :trackNumber")
    int updateExamination(boolean sent, String trackNumber);
}
