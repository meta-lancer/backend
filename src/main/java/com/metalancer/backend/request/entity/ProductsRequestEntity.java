package com.metalancer.backend.request.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.ProductsRequestStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.request.domain.ProductsRequest;
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
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products_request")
@ToString
public class ProductsRequestEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7483095261999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_request_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String content;
    private int viewCnt = 0;
    @Enumerated(EnumType.STRING)
    private ProductsRequestStatus productsRequestStatus;

    private String fileUrl;

    private String fileName;

    @Builder
    public ProductsRequestEntity(User writer,
        String title, String content, ProductsRequestStatus productsRequestStatus) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.productsRequestStatus = productsRequestStatus;
    }

    public void setFile(String fileUrl, String fileName) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public ProductsRequest toDomain() {
        return ProductsRequest.builder().productsRequestId(id).writerId(writer.getId())
            .nickname(writer.getNickname())
            .profileImg(writer.getProfileImg())
            .productsRequestStatus(productsRequestStatus)
            .title(title).content(content).createdAt(getCreatedAt()).updatedAt(getUpdatedAt())
            .createdAtKor(Time.convertDateToKorForRequest(getCreatedAt()))
            .updatedAtKor(Time.convertDateToKorForRequest(getUpdatedAt()))
            .createdAtEng(Time.convertDateToEngForRequest(getCreatedAt()))
            .updatedAtEng(Time.convertDateToEngForRequest(getUpdatedAt()))
            .build();
    }

    public void update(
        String title, String content, ProductsRequestStatus productsRequestStatus) {
        this.title = title;
        this.content = content;
        this.productsRequestStatus = productsRequestStatus;
    }

    public void deleteRequest() {
        delete();
    }

    public void addViewCnt() {
        this.viewCnt += 1;
    }
}
