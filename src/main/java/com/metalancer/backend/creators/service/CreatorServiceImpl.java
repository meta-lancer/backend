package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.constants.AssetType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Use_YN;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetRequest;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetCreatedResponse;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.external.aws.s3.S3Service;
import com.metalancer.backend.products.entity.ProductsCategoryEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsCategoryRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final ProductsRepository productsRepository;
    private final ProductsCategoryRepository productsCategoryRepository;
    private final S3Service uploadService;

    @Override
    public AssetCreatedResponse createAsset(User user, MultipartFile[] thumbnails,
        MultipartFile[] views, MultipartFile zipFile, AssetRequest dto) {
        try {

            // DB
            // Products가 메인
            // 3총사
            // AssetFile
            // ProductsTag

            ProductsCategoryEntity categoryEntity = productsCategoryRepository.findByCategoryNameAndUseYN(
                dto.getProductsCategory(),
                Use_YN.YES);
            CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(user,
                DataStatus.ACTIVE);
            ProductsEntity savedProductsEntity = createProductsEntity(dto,
                categoryEntity, creatorEntity);
            Long savedProductsId = savedProductsEntity.getId();
            uploadThumbnailImages(savedProductsId, thumbnails);

//            // 에셋 3d json 등록
//            upload3DViews(dto, savedProductsId);
//            // 에셋 파일 등록
//            uploadAssetZipFile(dto, savedProductsId);

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(ErrorCode.IMAGES_UPLOAD_FAILED);
        }
        return null;
    }

    private ProductsEntity createProductsEntity(AssetRequest dto,
        ProductsCategoryEntity categoryEntity, CreatorEntity creatorEntity) {
        ProductsEntity createdProductsEntity = ProductsEntity.builder()
            .productsCategoryEntity(categoryEntity)
            .creatorEntity(creatorEntity).title(dto.getTitle()).price(dto.getPrice()).build();
        productsRepository.save(createdProductsEntity);
        return productsRepository.findProductById(
            createdProductsEntity.getId());
    }

    private void uploadThumbnailImages(Long savedProductsId,
        MultipartFile[] thumbnails) throws IOException {
        for (MultipartFile thumbnail : thumbnails) {
            String randomFileName = uploadService.getRandomStringForImageName(8);
            String uploadedThumbnailUrl = uploadService.uploadToAssetBucket(AssetType.THUMBNAIL,
                savedProductsId, thumbnail, randomFileName);
            // 썸네일 db 저장
        }
    }

    private void upload3DViews(Long savedProductsId, MultipartFile[] views)
        throws IOException {
        for (MultipartFile view : views) {
            String randomFileName = uploadService.getRandomStringForImageName(8);
            String uploaded3DViewUrl = uploadService.uploadToAssetBucket(AssetType.VIEWS_3D,
                savedProductsId, view, randomFileName);
            // 3d뷰 db 저장
        }
    }

    private void uploadAssetZipFile(Long savedProductsId, MultipartFile zipFile)
        throws IOException {
        String randomFileName = uploadService.getRandomStringForImageName(8);
        String uploadedZipFileUrl = uploadService.uploadToAssetBucket(AssetType.ZIP,
            savedProductsId, zipFile, randomFileName);
        // zip db 저장
    }
}