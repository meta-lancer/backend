package com.metalancer.backend.creators.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "settlement_products")
@ToString
public class SettlementProductsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716215412241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "settlement_products_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;
    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;
    @ManyToOne
    @JoinColumn(name = "settlement_id", nullable = false)
    private SettlementEntity settlementEntity;
    @Schema(name = "판매 횟수")
    private int salesQuantity;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(name = "정산 상태")
    private SettlementStatus settlementStatus = SettlementStatus.REQUEST;
    @Column(nullable = false)
    @Schema(name = "요청일")
    private LocalDateTime requestDay;
    @Schema(name = "총 판매 금액")
    private BigDecimal totalAmountKRW;
    @Schema(name = "정산받은 금액")
    private Integer settlementAmountKRW;
    @Schema(name = "카드사 수수료 금액")
    private BigDecimal portoneChargeAmountKRW;
    @Schema(name = "서비스 수수료 금액")
    private BigDecimal serviceChargeAmountKRW;
    @Schema(name = "프리랜서 수수료 금액")
    private BigDecimal freelancerChargeAmountKRW;
    @Schema(name = "공제 금액(환불, 분쟁 시)")
    private BigDecimal deductAmountKRW;
    @Schema(name = "총 판매 금액")
    private BigDecimal totalAmountUSD;
    @Schema(name = "정산받은 금액")
    private BigDecimal settlementAmountUSD;
    @Schema(name = "카드사 수수료 금액")
    private BigDecimal portoneChargeAmountUSD;
    @Schema(name = "서비스 수수료 금액")
    private BigDecimal serviceChargeAmountUSD;
    @Schema(name = "프리랜서 수수료 금액")
    private BigDecimal freelancerChargeAmountUSD;
    @Schema(name = "공제 금액(환불, 분쟁 시)")
    private BigDecimal deductAmountUSD;
    @Schema(name = "처리일")
    private LocalDateTime settlementDate;
    @Schema(name = "비고사항")
    private String referenceMemo;
    @Schema(name = "첨부파일")
    private String referenceFile;

    public int getSalesQuantity() {
        return this.salesQuantity;
    }

    @Builder
    public SettlementProductsEntity(CreatorEntity creatorEntity, ProductsEntity productsEntity,
        SettlementEntity settlementEntity, int salesQuantity,
        BigDecimal totalAmountKRW, Integer settlementAmountKRW, BigDecimal portoneChargeAmountKRW,
        BigDecimal serviceChargeAmountKRW, BigDecimal freelancerChargeAmountKRW,
        BigDecimal deductAmountKRW, BigDecimal totalAmountUSD, BigDecimal settlementAmountUSD,
        BigDecimal portoneChargeAmountUSD, BigDecimal serviceChargeAmountUSD,
        BigDecimal freelancerChargeAmountUSD, BigDecimal deductAmountUSD) {
        this.creatorEntity = creatorEntity;
        this.productsEntity = productsEntity;
        this.settlementEntity = settlementEntity;
        this.salesQuantity = salesQuantity;
        this.requestDay = LocalDateTime.now();
        this.totalAmountKRW = totalAmountKRW;
        this.settlementAmountKRW = settlementAmountKRW;
        this.portoneChargeAmountKRW = portoneChargeAmountKRW;
        this.serviceChargeAmountKRW = serviceChargeAmountKRW;
        this.freelancerChargeAmountKRW = freelancerChargeAmountKRW;
        this.deductAmountKRW = deductAmountKRW;
        this.totalAmountUSD = totalAmountUSD;
        this.settlementAmountUSD = settlementAmountUSD;
        this.portoneChargeAmountUSD = portoneChargeAmountUSD;
        this.serviceChargeAmountUSD = serviceChargeAmountUSD;
        this.freelancerChargeAmountUSD = freelancerChargeAmountUSD;
        this.deductAmountUSD = deductAmountUSD;
    }
}
