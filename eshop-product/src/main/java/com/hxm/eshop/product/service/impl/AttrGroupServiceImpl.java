package com.hxm.eshop.product.service.impl;

import com.hxm.eshop.product.entity.AttrEntity;
import com.hxm.eshop.product.service.AttrService;
import com.hxm.eshop.product.vo.AttrGroupWithAttrsVo;
import com.hxm.eshop.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;

import com.hxm.eshop.product.dao.AttrGroupDao;
import com.hxm.eshop.product.entity.AttrGroupEntity;
import com.hxm.eshop.product.service.AttrGroupService;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if(catelogId==0){
            IPage<AttrGroupEntity> page=this.page(new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<>());
            return new PageUtils(page);
        }else {
            String key=(String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper=new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
            if(!StringUtils.isEmpty(key)){
                wrapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    /**
     * ????????????id???????????????????????????????????????????????????
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1?????????????????????
        List<AttrGroupEntity> attrGroupEntities = this.list(
                new QueryWrapper<AttrGroupEntity>()
                        .eq("catelog_id", catelogId));

        //2?????????????????????
        return attrGroupEntities.stream()
                .map(group -> {
                    AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
                    BeanUtils.copyProperties(group,attrsVo);
                    List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
                    attrsVo.setAttrs(attrs);
                    return attrsVo;
                })
                .collect(Collectors.toList());


    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {

        //1???????????????spu????????????????????????????????????????????????????????????????????????????????????
        AttrGroupDao baseMapper = this.getBaseMapper();
        return baseMapper.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
    }
}