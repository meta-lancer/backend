package com.metalancer.backend.admin.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminProductDTO {

    @Data
    @NoArgsConstructor
    public static class DeleteList {

        private List<Long> productsIdList;
    }

}
