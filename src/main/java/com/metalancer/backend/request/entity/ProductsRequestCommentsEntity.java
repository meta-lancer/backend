package com.metalancer.backend.request.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.request.domain.ProductsRequestComment;
import com.metalancer.backend.users.entity.User;
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
@Entity(name = "products_request_comment")
@ToString
public class ProductsRequestCommentsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7483094242999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_request_comment_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;
    @ManyToOne
    @JoinColumn(name = "products_request_id", nullable = false)
    private ProductsRequestEntity productsRequestEntity;
    @Column(nullable = false)
    private String content;

    @Builder
    public ProductsRequestCommentsEntity(User writer, ProductsRequestEntity productsRequestEntity,
        String content) {
        this.writer = writer;
        this.productsRequestEntity = productsRequestEntity;
        this.content = content;
    }

    public ProductsRequestComment toDomain() {
        return ProductsRequestComment.builder()
            .productsRequestCommentId(id)
            .productsRequest(productsRequestEntity.toDomain())
            .nickname(writer.getNickname())
            .profileImg(writer.getProfileImg())
            .content(content)
            .createdAtKor(Time.convertDateToKorForRequest(getCreatedAt()))
            .updatedAtKor(Time.convertDateToKorForRequest(getUpdatedAt()))
            .createdAtEng(Time.convertDateToEngForRequest(getCreatedAt()))
            .updatedAtEng(Time.convertDateToEngForRequest(getUpdatedAt()))
            .createdAt(getCreatedAt())
            .updatedAt(getUpdatedAt())
            .build();
    }

    public void update(String content) {
        this.content = content;
    }

    public void deleteComment() {
        delete();
    }
}
