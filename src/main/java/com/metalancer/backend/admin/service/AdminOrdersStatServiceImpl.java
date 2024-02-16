package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.OutLineOrdersStatList;
import com.metalancer.backend.category.entity.TagsEntity;
import com.metalancer.backend.category.repository.TagsRepository;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminOrdersStatServiceImpl implements AdminOrdersStatService {

    private final TagsRepository tagsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public List<OutLineOrdersStatList> getOutlineOrdersStat() {
        int depth = 2;
        List<TagsEntity> parentTagList = tagsRepository.findAllParentsTagName(depth);
        List<OutLineOrdersStatList> response = new ArrayList<>();
        for (TagsEntity parentTag : parentTagList) {
            OutLineOrdersStatList outLineOrdersStatList = parentTag.toOutLineOrdersStatList();
            List<TagsEntity> parentTagAndChildTagList = tagsRepository.findAllParentsTagAndChildTagsByParentTag(
                parentTag);
            Integer cnt = 0;

            List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrderProductStatusIsNotAndStatus(
                OrderStatus.PAY_ING, DataStatus.ACTIVE);
            // 하나라도 만족하면 cnt ++;
            for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
                ProductsEntity productsEntity = orderProductsEntity.getProductsEntity();
                boolean productsHasAnyTagInTagList = tagsRepository.productsHasAnyTagInTagList(
                    productsEntity, parentTagAndChildTagList);
                if (productsHasAnyTagInTagList) {
                    cnt++;
                }
            }
            outLineOrdersStatList.setCnt(cnt);
            response.add(outLineOrdersStatList);
        }
        return response.stream()
            .sorted(Comparator.comparing(OutLineOrdersStatList::getCnt).reversed()).limit(3)
            .collect(Collectors.toList());
    }

    @Override
    public List<OutLineOrdersStatList> getAllOrdersStat() {
        int depth = 2;
        List<TagsEntity> parentTagList = tagsRepository.findAllParentsTagName(depth);
        List<OutLineOrdersStatList> response = new ArrayList<>();
        for (TagsEntity parentTag : parentTagList) {
            OutLineOrdersStatList outLineOrdersStatList = parentTag.toOutLineOrdersStatList();
            List<TagsEntity> parentTagAndChildTagList = tagsRepository.findAllParentsTagAndChildTagsByParentTag(
                parentTag);
            Integer cnt = 0;

            List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrderProductStatusIsNotAndStatus(
                OrderStatus.PAY_ING, DataStatus.ACTIVE);
            // 하나라도 만족하면 cnt ++;
            for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
                ProductsEntity productsEntity = orderProductsEntity.getProductsEntity();
                boolean productsHasAnyTagInTagList = tagsRepository.productsHasAnyTagInTagList(
                    productsEntity, parentTagAndChildTagList);
                if (productsHasAnyTagInTagList) {
                    cnt++;
                }
            }
            outLineOrdersStatList.setCnt(cnt);
            response.add(outLineOrdersStatList);
        }
        return response.stream()
            .sorted(Comparator.comparing(OutLineOrdersStatList::getCnt).reversed())
            .collect(Collectors.toList());
    }
}