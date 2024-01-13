package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.Charge;
import com.metalancer.backend.admin.dto.AdminServiceDTO.UpdatePortoneCharge;
import com.metalancer.backend.admin.dto.AdminServiceDTO.UpdateServiceCharge;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.service.entity.PortOneChargeEntity;
import com.metalancer.backend.service.entity.ServiceChargeEntity;
import com.metalancer.backend.service.repository.PortOneChargeRepository;
import com.metalancer.backend.service.repository.ServiceChargeRepository;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminServiceServiceImpl implements AdminServiceService {

    private final ServiceChargeRepository serviceChargeRepository;
    private final PortOneChargeRepository portOneChargeRepository;

    @Override
    public List<Charge> getAdminServiceChargeList() {
        return serviceChargeRepository.findAllByStatus(DataStatus.ACTIVE).stream().map(
            ServiceChargeEntity::toCharge).toList();
    }

    @Override
    public List<Charge> getAdminPortoneChargeList() {
        return portOneChargeRepository.findAllByStatus(DataStatus.ACTIVE).stream().map(
            PortOneChargeEntity::toCharge).toList();
    }

    @Override
    public Boolean updateAdminServiceCharge(PrincipalDetails user, UpdateServiceCharge dto) {
        User adminUser = user.getUser();
        Optional<ServiceChargeEntity> optionalServiceChargeEntity = serviceChargeRepository.findById(
            dto.getChargeId());
        if (optionalServiceChargeEntity.isPresent()) {
            ServiceChargeEntity serviceChargeEntity = optionalServiceChargeEntity.get();
            serviceChargeEntity.updateCharge(dto.getChargeRate(), adminUser);
            return dto.getChargeRate().equals(serviceChargeEntity.getChargeRate());
        } else {
            return false;
        }
    }

    @Override
    public Boolean updateAdminPortoneCharge(PrincipalDetails user, UpdatePortoneCharge dto) {
        User adminUser = user.getUser();
        Optional<PortOneChargeEntity> optionalPortOneChargeEntity = portOneChargeRepository.findById(
            dto.getChargeId());
        if (optionalPortOneChargeEntity.isPresent()) {
            PortOneChargeEntity portOneChargeEntity = optionalPortOneChargeEntity.get();
            portOneChargeEntity.updateCharge(dto.getChargeRate(), adminUser);
            return dto.getChargeRate().equals(portOneChargeEntity.getChargeRate());
        } else {
            return false;
        }
    }
}