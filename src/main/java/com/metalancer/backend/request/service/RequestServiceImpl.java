package com.metalancer.backend.request.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.external.aws.s3.S3Service;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.domain.ProductsRequestComment;
import com.metalancer.backend.request.dto.ProductsRequestCommentsDTO;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.File;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import com.metalancer.backend.request.entity.ProductsRequestCommentsEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.request.repository.ProductsRequestCommentsRepository;
import com.metalancer.backend.request.repository.ProductsRequestRepository;
import com.metalancer.backend.users.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class RequestServiceImpl implements RequestService {

    private final ProductsRequestRepository productsRequestRepository;
    private final ProductsRequestCommentsRepository productsRequestCommentsRepository;
    private final S3Service s3Service;

    @Override
    public Page<ProductsRequest> getProductsRequestList(List<String> requestTypeOptions,
        Pageable adjustedPageable) {
        return productsRequestRepository.findAll(requestTypeOptions, adjustedPageable);
    }

    @Override
    public ProductsRequest createRequest(PrincipalDetails user, Create dto) {
        return productsRequestRepository.save(user.getUser(), dto);
    }

    @Override
    public ProductsRequest updateRequest(PrincipalDetails user, Update dto, Long requestId) {
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        if (productsRequestEntity.getWriter() != user.getUser()) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }
        return productsRequestRepository.update(user.getUser(), dto, productsRequestEntity);
    }

    @Override
    public ProductsRequest getRequestDetail(PrincipalDetails user, Long requestId) {
        return productsRequestRepository.findDetail(requestId);
    }

    @Override
    public boolean deleteRequest(PrincipalDetails user, Long requestId) {
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        if (productsRequestEntity.getWriter() != user.getUser()) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }
        productsRequestEntity.deleteRequest();
        return productsRequestEntity.getStatus().equals(DataStatus.DELETED);
    }

    @Override
    public String getUploadRequestFilePreSignedUrl(PrincipalDetails user, Long requestId,
        String fileName) {
        return s3Service.getRequestFilePresignedUrl(requestId, fileName);
    }

    @Override
    public boolean updateRequestFile(PrincipalDetails user, Long requestId, File dto) {
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        productsRequestEntity.setFile(dto.getFileUrl(), dto.getFileName());
        return true;
    }

    @Override
    public Page<ProductsRequestComment> getProductsRequestCommentsList(Long requestId,
        PrincipalDetails user, Pageable pageable) {
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        Page<ProductsRequestCommentsEntity> productsRequestCommentsEntities = productsRequestCommentsRepository.findAllByPage(
            productsRequestEntity, user, pageable);
        List<ProductsRequestComment> productsRequestComments = new ArrayList<>();
        for (ProductsRequestCommentsEntity productsRequestCommentsEntity : productsRequestCommentsEntities) {
            ProductsRequestComment productsRequestComment = productsRequestCommentsEntity.toDomain();
            productsRequestComment.checkIsWriter(
                user != null && productsRequestCommentsEntity.getWriter().getId()
                    .equals(user.getUser().getId()));
            productsRequestComments.add(productsRequestComment);
        }
        return new PageImpl<>(productsRequestComments, pageable,
            productsRequestCommentsEntities.getTotalElements());
    }

    @Override
    public Boolean createProductsRequestComments(Long requestId,
        ProductsRequestCommentsDTO.Create dto, PrincipalDetails user) {
        User foundUser = user.getUser();
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        ProductsRequestCommentsEntity createdProductsRequestCommentsEntity = ProductsRequestCommentsEntity.builder()
            .productsRequestEntity(productsRequestEntity)
            .writer(foundUser)
            .content(dto.getContent())
            .build();
        productsRequestCommentsRepository.save(createdProductsRequestCommentsEntity);
        return productsRequestCommentsRepository.findOptionalById(
            createdProductsRequestCommentsEntity.getId()).isPresent();
    }

    @Override
    public Boolean deleteProductsRequestComments(Long requestId,
        Long commentId, PrincipalDetails user) {
        User foundUser = user.getUser();
        ProductsRequestEntity productsRequestEntity = productsRequestRepository.findEntity(
            requestId);
        ProductsRequestCommentsEntity productsRequestCommentsEntity = productsRequestCommentsRepository.findByProductsRequestEntityAndId(
            productsRequestEntity, commentId);
        // active인 경우에만
        if (!DataStatus.ACTIVE.equals(productsRequestCommentsEntity.getStatus())) {
            return false;
        }
        if (!(foundUser.getId().equals(productsRequestCommentsEntity.getWriter().getId()))) {
            throw new BaseException(ErrorCode.AUTHORITY_NOT_HAVE);
        }
        productsRequestCommentsEntity.deleteComment();
        return DataStatus.DELETED.equals(productsRequestCommentsEntity.getStatus());
    }
}