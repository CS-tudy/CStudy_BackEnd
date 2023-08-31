package com.cstudy.modulecommon.repository.workbook;


import com.cstudy.modulecommon.domain.workbook.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookRepositoryCustom {

    @Query("SELECT w FROM Workbook w JOIN FETCH w.files f")
    List<Workbook> findByIdWithWorkbook();
}
