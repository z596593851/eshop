package com.hxm.eshop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.product.entity.AttrEntity;
import com.hxm.eshop.product.vo.AttrGroupRelationVo;
import com.hxm.eshop.product.vo.AttrRespVo;
import com.hxm.eshop.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author xiaoming
 * @date 2021-04-26 21:30:08
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    void deleteRelation(List<AttrGroupRelationVo> vos);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

