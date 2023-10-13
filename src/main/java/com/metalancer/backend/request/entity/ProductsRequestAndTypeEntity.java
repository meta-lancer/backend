package com.metalancer.backend.request.entity;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import com.metalancer.backend.common.BaseTimeEntity;
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
@Entity(name = "products_request_and_type")
@ToString
public class ProductsRequestAndTypeEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7483443261999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_request_and_type_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "products_request_id", nullable = false)
    private ProductsRequestEntity productsRequestEntity;
    @ManyToOne
    @JoinColumn(name = "products_request_type_id", nullable = false)
    private ProductsRequestTypeEntity productsRequestTypeEntity;

    @Builder
    public ProductsRequestAndTypeEntity(ProductsRequestEntity productsRequestEntity,
        ProductsRequestTypeEntity productsRequestTypeEntity) {
        this.productsRequestEntity = productsRequestEntity;
        this.productsRequestTypeEntity = productsRequestTypeEntity;
    }

    public RequestCategory toRequestCategory() {
        return RequestCategory.builder().name(productsRequestTypeEntity.getName()).nameKor(
            productsRequestTypeEntity.getNameKor()).build();
    }
}
