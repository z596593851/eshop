package com.hxm.eshop.member.dao;

import com.hxm.eshop.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-07 18:10:28
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
