package com.lin.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.yygh.cmn.listener.DictListener;
import com.lin.yygh.cmn.mapper.DictMapper;
import com.lin.yygh.cmn.service.DictService;
import com.lin.yygh.model.cmn.Dict;
import com.lin.yygh.vo.cmn.DictEeVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> list = baseMapper.selectList(dictQueryWrapper);
        list.forEach(dict -> {
            dict.setHasChildren(hasChildren(dict.getId()));
        });
        return list;
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void exportDictData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictVo = DictEeVo.builder()
                        .dictCode(dict.getDictCode())
                        .id(dict.getId())
                        .name(dict.getName())
                        .value(dict.getValue())
                        .parentId(dict.getParentId())
                        .build();
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper))
                    .sheet()
                    .doRead();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        return baseMapper.selectCount(dictQueryWrapper) > 0;
    }
}
