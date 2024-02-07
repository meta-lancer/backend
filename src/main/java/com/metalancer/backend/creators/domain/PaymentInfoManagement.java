package com.metalancer.backend.creators.domain;

import com.metalancer.backend.common.constants.PaymentInfoType;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PaymentInfoManagement {

    private final PaymentInfoType paymentInfoType;
    private final Long paymentInfoManagementId;
    private final String registerNo;
    private final String idCardCopy;
    private final String bank;
    private final String accountNo;
    private final String accountCopy;
    private final String idCardCopyUploadedAt;
    private final String accountCopyUploadedAt;

    @Builder
    public PaymentInfoManagement(PaymentInfoType paymentInfoType, Long paymentInfoManagementId,
        String registerNo, String idCardCopy,
        String bank, String accountNo, String accountCopy, String idCardCopyUploadedAt,
        String accountCopyUploadedAt) {
        this.paymentInfoType = paymentInfoType;
        this.paymentInfoManagementId = paymentInfoManagementId;
        this.registerNo = registerNo;
        this.idCardCopy = idCardCopy;
        this.bank = bank;
        this.accountNo = accountNo;
        this.accountCopy = accountCopy;
        this.idCardCopyUploadedAt = idCardCopyUploadedAt;
        this.accountCopyUploadedAt = accountCopyUploadedAt;
    }
}
