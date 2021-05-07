package com.hxm.eshop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.product.entity.AttrAttrgroupRelationEntity;
import com.hxm.eshop.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public void saveBatch(List<AttrGroupRelationVo> vos);
}

