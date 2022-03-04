package com.lin.yygh.cmn.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lin.yygh.cmn.mapper.DictMapper;
import com.lin.yygh.model.cmn.Dict;
import com.lin.yygh.vo.cmn.DictEeVo;

public class DictListener extends AnalysisEventListener<DictEeVo> {
    private final DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo data, AnalysisContext context) {
        Dict dict = new Dict();
        dict.setParentId(data.getParentId());
        dict.setName(data.getName());
        dict.setValue(data.getValue());
        dict.setDictCode(data.getDictCode());
        dict.setId(data.getId());
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
