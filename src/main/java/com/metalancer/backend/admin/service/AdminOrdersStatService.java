package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.OutLineOrdersStatList;
import com.metalancer.backend.admin.domain.UserCompletedOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminOrdersStatService {

    List<OutLineOrdersStatList> getOutlineOrdersStat();

    List<OutLineOrdersStatList> getAllOrdersStat();

    Page<UserCompletedOrder> getOrderList(Pageable pageable);
}