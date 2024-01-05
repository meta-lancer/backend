package com.metalancer.backend.users.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.products.domain.RequestOption;
import java.math.BigDecimal;
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
    private BigDecimal price;
    private String priceUnit;
    private String sellerNickname;
    private String sellerName;
    private String sellerPhone;
    private String thumbnail;
    private String purchasedAt;
    private Integer downloadedCnt;
    private String downloadLink;
    private OrderStatus orderStatus;
    private RequestOption requestOption;

    @Builder
    public PayedAssets(Long payedAssetsId, String orderNo, String orderProductNo, Long productsId,
        String title,
        BigDecimal price, String sellerNickname, String sellerName, String sellerPhone,
        String thumbnail, LocalDateTime purchasedAt, Integer downloadedCnt, String downloadLink,
        OrderStatus orderStatus, String priceUnit) {
        this.payedAssetsId = payedAssetsId;
        this.orderNo = orderNo;
        this.orderProductNo = orderProductNo;
        this.productsId = productsId;
        this.title = title;
        this.price = price;
        this.priceUnit = priceUnit;
        this.sellerNickname = sellerNickname;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.thumbnail = thumbnail;
        this.purchasedAt = Time.convertDateToStringWithDot(purchasedAt);
        this.downloadedCnt = downloadedCnt;
        this.downloadLink = downloadLink;
        this.orderStatus = orderStatus;
    }

    public void setRequestOption(RequestOption requestOption) {
        this.requestOption = requestOption;
    }
}
