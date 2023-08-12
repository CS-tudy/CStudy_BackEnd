package com.cstudy.modulecommon.repository.file;


import com.cstudy.modulecommon.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {
}
