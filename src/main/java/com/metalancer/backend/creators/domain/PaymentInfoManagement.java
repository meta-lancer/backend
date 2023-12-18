package com.metalancer.backend.creators.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class PaymentInfoManagement {

    private final Long PaymentInfoManagementId;
    private final String registerNo;
    private final String idCardCopy;
    private final String bank;
    private final String accountCopy;

    @Builder

    public PaymentInfoManagement(Long paymentInfoManagementId, String registerNo, String idCardCopy,
        String bank, String accountCopy) {
        PaymentInfoManagementId = paymentInfoManagementId;
        this.registerNo = registerNo;
        this.idCardCopy = idCardCopy;
        this.bank = bank;
        this.accountCopy = accountCopy;
    }
}
