package com.hxm.eshop.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-07 18:10:28
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

