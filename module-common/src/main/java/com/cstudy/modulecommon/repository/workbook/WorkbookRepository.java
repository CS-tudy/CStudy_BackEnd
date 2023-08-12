package com.cstudy.modulecommon.repository.workbook;


import com.cstudy.modulecommon.domain.workbook.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookRepositoryCustom {

}
