package com.metalancer.backend.creators.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.domain.SettlementRecordList;
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
@Entity(name = "settlement")
@ToString
public class SettlementEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716215177241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "settlement_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(name = "정산 상태")
    private SettlementStatus settlementStatus = SettlementStatus.REQUEST;
    @Column(nullable = false)
    @Schema(name = "요청일")
    private LocalDateTime requestDay = LocalDateTime.now();
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
    @Schema(name = "현황")
    private String currentSituation;
    @Schema(name = "담당자")
    private String manager;
    @Schema(name = "처리일")
    private LocalDateTime settlementDate;
    @Schema(name = "은행명")
    private String bank;
    @Schema(name = "수령인")
    private String receiverNm;
    @Schema(name = "계좌번호")
    private String accountNo;
    @Schema(name = "비고사항")
    private String referenceMemo;
    @Schema(name = "첨부파일")
    private String referenceFile;

    @Builder
    public SettlementEntity(CreatorEntity creatorEntity,
        BigDecimal totalAmountKRW, Integer settlementAmountKRW,
        BigDecimal portoneChargeAmountKRW, BigDecimal serviceChargeAmountKRW,
        BigDecimal freelancerChargeAmountKRW,
        BigDecimal deductAmountKRW, BigDecimal totalAmountUSD, BigDecimal settlementAmountUSD,
        BigDecimal portoneChargeAmountUSD, BigDecimal serviceChargeAmountUSD,
        BigDecimal freelancerChargeAmountUSD,
        BigDecimal deductAmountUSD) {
        this.creatorEntity = creatorEntity;
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

    public SettlementRecordList toSettlementRecordList() {
        return SettlementRecordList.builder().settlementId(id)
            .requestDay(Time.convertDateToString(requestDay))
            .currentSituation(currentSituation)
            .manager(manager).settlementStatus(settlementStatus).build();
    }

    public void adminSettle(
        String currentSituation,
        String manager,
        String bank,
        String receiverNm,
        String accountNo,
        String referenceMemo,
        String referenceFile) {
        this.currentSituation = currentSituation;
        this.manager = manager;
        this.settlementDate = LocalDateTime.now();
        this.bank = bank;
        this.receiverNm = receiverNm;
        this.accountNo = accountNo;
        this.referenceMemo = referenceMemo;
        this.referenceFile = referenceFile;
    }
}
