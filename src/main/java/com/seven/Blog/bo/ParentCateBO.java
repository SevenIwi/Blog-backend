package com.seven.Blog.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created By Seven.wk
 * Description: 父分类
 * Created At 2018/11/27
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParentCateBO {

    private Integer value;

    private String label;

    private List<ChildrenCateBO> children;
}
