package com.hxm.eshop.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.hxm.common.exception.NoStockException;
import com.hxm.common.to.SkuHasStockVo;
import com.hxm.common.to.mq.OrderTo;
import com.hxm.common.to.mq.StockDetailTo;
import com.hxm.common.to.mq.StockLockedTo;
import com.hxm.common.utils.R;
import com.hxm.eshop.ware.entity.WareOrderTaskDetailEntity;
import com.hxm.eshop.ware.entity.WareOrderTaskEntity;
import com.hxm.eshop.ware.feign.OrderFeignService;
import com.hxm.eshop.ware.service.WareOrderTaskDetailService;
import com.hxm.eshop.ware.service.WareOrderTaskService;
import com.hxm.eshop.ware.vo.OrderItemVo;
import com.hxm.eshop.ware.vo.OrderVo;
import com.hxm.eshop.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;

import com.hxm.eshop.ware.dao.WareSkuDao;
import com.hxm.eshop.ware.entity.WareSkuEntity;
import com.hxm.eshop.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {

        return skuIds.stream().map(item -> {
            Long count = this.baseMapper.getSkuStock(item);
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            skuHasStockVo.setSkuId(item);
            skuHasStockVo.setHasStock(count != null && count > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
    }

    /**
     * ???????????????????????????
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean orderLockStock(WareSkuLockVo vo) {
        /*
         * ?????????????????????????????????
         * ??????
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(wareOrderTaskEntity);
        //?????????????????????????????????????????????
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map((item) -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //??????????????????????????????????????????
            List<Long> wareIdList = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIdList);

            return stock;
        }).collect(Collectors.toList());

        //????????????
        for (SkuWareHasStock hasStock : collect) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();

            if (wareIds==null || wareIds.isEmpty()) {
                //??????????????????????????????????????????
                throw new NoStockException(skuId);
            }

            //????????????????????????????????????,??????????????????????????????????????????????????????MQ
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id??????????????????
            for (Long wareId : wareIds) {
                //?????????????????????1??????????????????0
                Long count = wareSkuDao.lockSkuStock(skuId,wareId,hasStock.getNum());
                if (count == 1) {
                    skuStocked = true;
                    WareOrderTaskDetailEntity taskDetailEntity = WareOrderTaskDetailEntity.builder()
                            .skuId(skuId)
                            .skuName("")
                            .skuNum(hasStock.getNum())
                            .taskId(wareOrderTaskEntity.getId())
                            .wareId(wareId)
                            .lockStatus(1)
                            .build();
                    wareOrderTaskDetailService.save(taskDetailEntity);
                    //??????MQ??????????????????
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(taskDetailEntity,detailTo);
                    lockedTo.setDetailTo(detailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);
                    break;
                } else {
                    //?????????????????????????????????????????????
                }
            }

            if (!skuStocked) {
                //???????????????????????????????????????
                throw new NoStockException(skuId);
            }
        }
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {
        //??????????????????id
        StockDetailTo detail = to.getDetailTo();
        Long detailId = detail.getId();

        /**
         * ??????
         * 1??????????????????????????????????????????????????????
         *   ?????????????????????????????????
         *      ?????????????????????
         *          1??????????????????????????????????????????
         *          2??????????????????????????????????????????
         *              ???????????????????????????????????????
         *                      ??????????????????????????????
         */
        WareOrderTaskDetailEntity taskDetailInfo = wareOrderTaskDetailService.getById(detailId);
        if (taskDetailInfo != null) {
            //??????wms_ware_order_task??????????????????
            Long id = to.getId();
            WareOrderTaskEntity orderTaskInfo = wareOrderTaskService.getById(id);
            //?????????????????????????????????
            String orderSn = orderTaskInfo.getOrderSn();
            //????????????????????????
            R orderData = orderFeignService.getOrderStatus(orderSn);
            if (orderData.getCode() == 0) {
                //????????????????????????
                OrderVo orderInfo = orderData.getData("data", new TypeReference<OrderVo>() {});

                //??????????????????????????????????????????????????????????????????
                if (orderInfo == null || orderInfo.getStatus() == 4) {
                    //???????????????????????????????????????
                    if (taskDetailInfo.getLockStatus() == 1) {
                        //?????????????????????????????????1?????????????????????????????????????????????
                        unLockStock(detail.getSkuId(),detail.getWareId(),detail.getSkuNum(),detailId);
                    }
                }
            } else {
                //????????????????????????????????????????????????????????????????????????
                //????????????????????????
                throw new RuntimeException("????????????????????????");
            }
        } else {
            //????????????
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????
     * @param orderTo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockStock(OrderTo orderTo) {

        String orderSn = orderTo.getOrderSn();
        //???????????????????????????????????????????????????????????????
        WareOrderTaskEntity orderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);

        //??????????????????id???????????? ????????????????????????????????????
        Long id = orderTaskEntity.getId();
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", id).eq("lock_status", 1));

        for (WareOrderTaskDetailEntity taskDetailEntity : list) {
            unLockStock(taskDetailEntity.getSkuId(),
                    taskDetailEntity.getWareId(),
                    taskDetailEntity.getSkuNum(),
                    taskDetailEntity.getId());
        }
    }

    public void unLockStock(Long skuId,Long wareId,Integer num,Long taskDetailId) {

        //????????????
        wareSkuDao.unLockStock(skuId,wareId,num);

        //????????????????????????
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        //???????????????
        taskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(taskDetailEntity);

    }

    @Data
    static
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }

}