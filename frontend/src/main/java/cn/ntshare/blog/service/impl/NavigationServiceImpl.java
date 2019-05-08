package cn.ntshare.blog.service.impl;

import cn.ntshare.blog.dao.NavigationMapper;
import cn.ntshare.blog.dto.NavigationDTO;
import cn.ntshare.blog.enums.ResponseCodeEnum;
import cn.ntshare.blog.exception.SystemException;
import cn.ntshare.blog.service.NavigationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By Seven.wk
 * Description: 导航功能Service实现
 * Created At 2018/08/10
 */
@Service
public class NavigationServiceImpl implements NavigationService {

    @Autowired
    private NavigationMapper navigationMapper;

    @Override
    public PageInfo selectAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NavigationDTO> navigationDTOList = navigationMapper.selectAll();
        return new PageInfo<>(navigationDTOList);
    }

    @Override
    public NavigationDTO selectById(Integer id) {
        NavigationDTO navigationDTO = navigationMapper.selectById(id);
        if (navigationDTO == null) {
            throw new SystemException(ResponseCodeEnum.PAGE_NOT_FOUND);
        }
        return navigationDTO;
    }

    @Override
    public List<NavigationDTO> selectItem() {
        return navigationMapper.selectItem();
    }
}