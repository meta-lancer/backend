package com.metalancer.backend.request.dto;

import lombok.Data;

@Data
public class ProductsRequestCommentsDTO {

    @Data
    public static class Create {

        private String content;
    }

    @Data
    public static class Update {

        private String content;
    }
    
}
