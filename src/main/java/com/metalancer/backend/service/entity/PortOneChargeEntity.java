package com.metalancer.backend.service.entity;

import com.metalancer.backend.admin.domain.Charge;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "portone_charges", schema = "metaovis")
@ToString
public class PortOneChargeEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 74814143299515L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portone_charge_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType pgName;

    @Column(nullable = false)
    private BigDecimal chargeRate;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    public Charge toCharge() {
        return Charge.builder().chargeId(id).chargeName(pgName.toString()).chargeRate(chargeRate)
            .build();
    }

    public void updateCharge(BigDecimal chargeRate, User manager) {
        this.chargeRate = chargeRate;
        this.manager = manager;
    }
}