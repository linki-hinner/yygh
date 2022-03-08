package com.lin.yygh.hosp.service.v2;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.HospitalQueryVo;

import java.util.List;
import java.util.Map;

public interface HospitalService2 extends IService<Hospital2> {
    Map<String, Object> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    boolean updateStatus(Long id, Integer status);

    public Hospital2 show(Long id) ;

    public String getHospName(Long id);

    List<Hospital> findByHosname(String hosname);

    Map<String, ?> item(Long id);
}
