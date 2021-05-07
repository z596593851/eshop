package com.hxm.eshop.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-07 18:10:28
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

