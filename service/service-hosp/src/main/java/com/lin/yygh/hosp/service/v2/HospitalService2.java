package com.lin.yygh.hosp.service.v2;

import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService2 {
    void save(Map<String, Object> paramMap);

    Hospital2 getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    boolean updateStatus(String id, Integer status);

    Hospital show(String id);

    String getHospName(String hoscode);

    List<Hospital> findByHosname(String hosname);

    Map<String, ?> item(String hoscode);
}
