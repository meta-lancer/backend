package com.metalancer.backend.users.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PayedAssets {
    // 다운로드 entity 생성 - 접근할 수 있는 링크 + 접근가능한 횟수 제한 + 아니면... 새로운 버킷에? 그리고 접근가능한 횟수 넘어가면 삭제?

    private Long payedAssetsId;
    private String orderNo;
    private String orderProductNo;
    private Long productsId;
    private String title;
    private String thumbnail;
    private LocalDateTime purchasedAt;
    private Integer downloadedCnt;
    private String downloadLink;

    @Builder
    public PayedAssets(Long payedAssetsId, String orderNo, String orderProductNo, Long productsId,
        String title,
        String thumbnail, LocalDateTime purchasedAt, Integer downloadedCnt, String downloadLink) {
        this.payedAssetsId = payedAssetsId;
        this.orderNo = orderNo;
        this.orderProductNo = orderProductNo;
        this.productsId = productsId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.purchasedAt = purchasedAt;
        this.downloadedCnt = downloadedCnt;
        this.downloadLink = downloadLink;
    }
}