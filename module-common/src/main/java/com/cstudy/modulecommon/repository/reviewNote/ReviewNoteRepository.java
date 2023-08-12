package com.cstudy.modulecommon.repository.reviewNote;

import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("reviewNoteMongoRepository")
public interface ReviewNoteRepository extends MongoRepository<ReviewNote, String> {
}
