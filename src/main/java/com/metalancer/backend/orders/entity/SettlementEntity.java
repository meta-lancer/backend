package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.orders.domain.SettlementRecordList;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
    @Schema(name = "현황")
    private String currentSituation;
    @Schema(name = "담당자")
    private String manager;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(name = "정산 상태")
    private SettlementStatus settlementStatus = SettlementStatus.REQUEST;
    @Column(nullable = false)
    @Schema(name = "요청일")
    private LocalDateTime requestDay;
    @Schema(name = "총 판매 금액")
    private Integer totalAmount;
    @Schema(name = "정산받은 금액")
    private Integer settlementAmount;
    @Schema(name = "수수료 금액")
    private Integer feeAmount;
    @Schema(name = "공제 금액(환불, 분쟁 시)")
    private Integer deductAmount;
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

    public SettlementRecordList toSettlementRecordList() {
        return SettlementRecordList.builder().settlementId(id)
            .requestDay(Time.convertDateToString(requestDay))
            .currentSituation(currentSituation)
            .manager(manager).settlementStatus(settlementStatus).build();
    }
}
