package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.OutLineOrdersStatList;
import java.util.List;

public interface AdminOrdersStatService {

    List<OutLineOrdersStatList> getOutlineOrdersStat();

    List<OutLineOrdersStatList> getAllOrdersStat();

}