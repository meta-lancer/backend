package com.metalancer.backend.request.dto;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import com.metalancer.backend.common.constants.ProductsRequestStatus;
import lombok.Data;

@Data
public class ProductsRequestDTO {

    @Data
    public static class Create {

        private ProductsRequestTypeEntity productionRequestType;
        private String title;
        private String content;
        private ProductsRequestStatus productsRequestStatus;
    }

    @Data
    public static class Update {

        private ProductsRequestTypeEntity productionRequestType;
        private String title;
        private String content;
        private ProductsRequestStatus productsRequestStatus;
    }
}
