package com.metalancer.backend.request.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.domain.ProductsRequestComment;
import com.metalancer.backend.request.dto.ProductsRequestCommentsDTO;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.File;
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

    String getUploadRequestFilePreSignedUrl(PrincipalDetails user, Long requestId, String fileName);

    boolean updateRequestFile(PrincipalDetails user, Long requestId, File dto);

    Page<ProductsRequestComment> getProductsRequestCommentsList(Long requestId,
        PrincipalDetails user, Pageable pageable);

    Boolean createProductsRequestComments(Long requestId, ProductsRequestCommentsDTO.Create dto,
        PrincipalDetails user);

    Boolean deleteProductsRequestComments(Long requestId,
        Long commentId, PrincipalDetails user);

}