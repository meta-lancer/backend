package com.metalancer.backend.request.dto;

import com.metalancer.backend.common.constants.ProductsRequestStatus;
import java.util.List;
import lombok.Data;

@Data
public class ProductsRequestDTO {

    @Data
    public static class Create {

        private List<String> productionRequestTypeList;
        private String title;
        private String content;
        private ProductsRequestStatus productsRequestStatus;
        private String relatedLink;
        private String fileUrl;
        private String fileName;
    }

    @Data
    public static class Update {

        private List<String> productionRequestTypeList;
        private String title;
        private String content;
        private ProductsRequestStatus productsRequestStatus;
        private String relatedLink;
        private String fileUrl;
        private String fileName;
    }

    @Data
    public static class File {

        private String fileUrl;
        private String fileName;
    }

}
