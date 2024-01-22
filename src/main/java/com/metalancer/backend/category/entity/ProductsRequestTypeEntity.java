package com.metalancer.backend.category.entity;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products_request_type")
@ToString
public class ProductsRequestTypeEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7410122177141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_request_type_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nameKor;
    private boolean useYn;

    @Builder
    public ProductsRequestTypeEntity(String name, String nameKor) {
        this.name = name;
        this.nameKor = nameKor;
    }

    public RequestCategory ToRequestCategory() {
        return RequestCategory.builder().name(name).nameKor(nameKor).build();
    }

    public void toggleUseYn(boolean useYn) {
        this.useYn = useYn;
    }
}
