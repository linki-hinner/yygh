package com.lin.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response);

    boolean importDictData(MultipartFile file);

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
