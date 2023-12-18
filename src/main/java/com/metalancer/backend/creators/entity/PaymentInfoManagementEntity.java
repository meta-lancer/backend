package com.metalancer.backend.creators.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.users.entity.CreatorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "payment_info_management")
@ToString
public class PaymentInfoManagementEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 756241188241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "payment_info_management_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;
    @Column(nullable = false)
    private String registerNo;
    @Column(nullable = false)
    private String idCardCopy;
    @Column(nullable = false)
    private String bank;
    @Column(nullable = false)
    private String accountCopy;
    @Column(nullable = false)
    private boolean incomeAgree;

    @Builder
    public PaymentInfoManagementEntity(CreatorEntity creatorEntity, String registerNo,
        String idCardCopy, String bank,
        String accountCopy, boolean incomeAgree) {
        this.creatorEntity = creatorEntity;
        this.registerNo = registerNo;
        this.idCardCopy = idCardCopy;
        this.bank = bank;
        this.accountCopy = accountCopy;
        this.incomeAgree = incomeAgree;
    }

    public PaymentInfoManagement toPaymentInfoManagement() {
        return PaymentInfoManagement.builder().paymentInfoManagementId(id).registerNo(registerNo)
            .idCardCopy(idCardCopy)
            .bank(bank).accountCopy(accountCopy).build();
    }
}
