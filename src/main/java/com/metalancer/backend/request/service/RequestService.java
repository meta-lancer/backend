package com.metalancer.backend.request.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Page<ProductsRequest> getProductsRequestList(List<String> requestTypeOptions,
        Pageable adjustedPageable);

    ProductsRequest createRequest(PrincipalDetails user, Create dto);

    ProductsRequest updateRequest(PrincipalDetails user, Update dto, Long requestId);

    ProductsRequest getRequestDetail(PrincipalDetails user, Long requestId);

    boolean deleteRequest(PrincipalDetails user, Long requestId);
}