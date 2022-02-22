package com.lin.yygh.hosp.repository;

import com.lin.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findSchedulesByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);
}
