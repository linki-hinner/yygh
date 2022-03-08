package com.lin.yygh.vo.base;

import lombok.Data;

import java.util.Collection;

@Data
public class PageVo<T> {
    private Number total;
    private Collection<T> data;
}
