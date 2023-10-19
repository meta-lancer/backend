package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.AssetType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Use_YN;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetRequest;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetCreatedResponse;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.external.aws.s3.S3Service;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsCategoryEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import com.metalancer.backend.products.entity.ProductsThumbnailEntity;
import com.metalancer.backend.products.entity.ProductsViewsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsCategoryRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import com.metalancer.backend.products.repository.ProductsThumbnailRepository;
import com.metalancer.backend.products.repository.ProductsViewsRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final ProductsRepository productsRepository;
    private final ProductsCategoryRepository productsCategoryRepository;
    private final ProductsTagRepository productsTagRepository;
    private final S3Service uploadService;
    private final ProductsThumbnailRepository productsThumbnailRepository;
    private final ProductsViewsRepository productsViewsRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;

    @Override
    public AssetCreatedResponse createAsset(User user, @NotNull MultipartFile[] thumbnails,
        @NotNull MultipartFile[] views, AssetRequest dto) {
        try {
            ProductsCategoryEntity categoryEntity = productsCategoryRepository.findByCategoryNameAndUseYN(
                dto.getProductsCategory(),
                Use_YN.YES);
            CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(user,
                DataStatus.ACTIVE);
            ProductsEntity savedProductsEntity = createProductsEntity(dto,
                categoryEntity, creatorEntity);
            Long savedProductsId = savedProductsEntity.getId();
            saveTagList(dto, savedProductsEntity);
            uploadThumbnailImages(savedProductsId, savedProductsEntity, thumbnails);
            upload3DViews(savedProductsId, savedProductsEntity, views);
            ProductsDetail productsDetail = savedProductsEntity.toProductsDetail();
            log.info("상세 데이터 조회");
            List<String> tagList = productsTagRepository.findTagListByProduct(savedProductsEntity);
            productsDetail.setTagList(tagList);
            List<String> thumbnailUrlList = productsThumbnailRepository.findAllUrlByProduct(
                savedProductsEntity);
            if (thumbnailUrlList != null && thumbnailUrlList.size() > 0) {
                savedProductsEntity.setThumbnail(thumbnailUrlList.get(0));
            }
            log.info("썸네일 목록 조회");
            List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
                savedProductsEntity);
            log.info("3D 뷰 데이터 조회");
            AssetFile assetFile = AssetFile.builder().thumbnailUrlList(thumbnailUrlList)
                .viewUrlList(viewUrlList).zipFileUrl("")
                .build();
            productsDetail.setAssetFile(assetFile);
            return AssetCreatedResponse.builder().productsDetail(productsDetail)
                .build();

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(ErrorCode.ASSETS_UPLOAD_FAILED);
        }
    }

    @Override
    public String getAssetFilePreSignedUrl(Long productsId) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        Optional<ProductsAssetFileEntity> productsAssetFileEntity = productsAssetFileRepository.findOptionalEntityByProducts(
            productsEntity);
        if (productsAssetFileEntity.isEmpty()) {
            String randomFileName = UUID.randomUUID().toString().substring(0, 10) + ".zip";
            String uploadedZipFileUrl = uploadService.getAssetFilePresignedUrl(productsId,
                randomFileName);
            String shortUrl = uploadService.extractBaseUrl(uploadedZipFileUrl);
            ProductsAssetFileEntity createdProductsAssetFileEntity = ProductsAssetFileEntity.builder()
                .productsEntity(productsEntity).url(shortUrl)
                .fileName(randomFileName)
                .build();
            productsAssetFileRepository.save(createdProductsAssetFileEntity);
            return uploadedZipFileUrl;
        }

        ProductsAssetFileEntity foundProductsAssetFileEntity = productsAssetFileEntity.get();
        String savedFileName = foundProductsAssetFileEntity.getFileName();
        return uploadService.getAssetFilePresignedUrl(productsId, savedFileName);
    }

    @Override
    public Boolean successAsset(Long productsId, PrincipalDetails user) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        ProductsAssetFileEntity productsAssetFileEntity = productsAssetFileRepository.findByProducts(
            productsEntity);
        productsAssetFileEntity.success();
        return productsAssetFileEntity.getSuccess();
    }

    @Override
    public Boolean failAsset(Long productsId, PrincipalDetails user) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        productsEntity.deleteProducts();
        // tag 삭제
        List<ProductsTagEntity> productsTagEntities = productsTagRepository.findTagEntityListByProduct(
            productsEntity);
        productsTagRepository.deleteAll(productsTagEntities);
        return true;
    }

    private void saveTagList(AssetRequest dto, ProductsEntity savedProductsEntity) {
        List<ProductsTagEntity> tagEntities = new ArrayList<>();
        for (String tag : dto.getTagList()) {
            ProductsTagEntity createdProductsTagEntity = ProductsTagEntity.builder()
                .productsEntity(savedProductsEntity).name(tag).build();
            tagEntities.add(createdProductsTagEntity);
        }
        productsTagRepository.saveAll(tagEntities);
    }

    private ProductsEntity createProductsEntity(AssetRequest dto,
        ProductsCategoryEntity categoryEntity, CreatorEntity creatorEntity) {
        ProductsEntity createdProductsEntity = ProductsEntity.builder()
            .productsCategoryEntity(categoryEntity)
            .creatorEntity(creatorEntity).title(dto.getTitle()).price(dto.getPrice())
            .productionProgram(dto.getProductionProgram()).compatibleProgram(
                dto.getCompatibleProgram()).assetDetail(dto.getAssetDetail()).assetNotice(
                dto.getAssetNotice()).assetCopyRight(dto.getAssetCopyRight())
            .website(dto.getWebsite()).build();
        productsRepository.save(createdProductsEntity);
        return productsRepository.findProductById(
            createdProductsEntity.getId());
    }

    private void uploadThumbnailImages(Long savedProductsId, ProductsEntity savedProductsEntity,
        MultipartFile[] thumbnails) throws IOException {
        try {
            int index = 1;
            List<ProductsThumbnailEntity> productsThumbnailEntities = new ArrayList<>();
            for (MultipartFile thumbnail : thumbnails) {
                String randomFileName = uploadService.getRandomStringForImageName(8);
                String uploadedThumbnailUrl = uploadService.uploadToAssetBucket(AssetType.THUMBNAIL,
                    savedProductsId, thumbnail, randomFileName);
                ProductsThumbnailEntity createdProductsThumbnailEntity = ProductsThumbnailEntity.builder()
                    .productsEntity(savedProductsEntity).thumbnailOrd(index++)
                    .thumbnailUrl(uploadedThumbnailUrl)
                    .build();
                productsThumbnailEntities.add(createdProductsThumbnailEntity);
            }
            productsThumbnailRepository.saveAll(productsThumbnailEntities);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage() + ": ", e);
            throw new BaseException(ErrorCode.THUMBNAILS_UPLOAD_FAILED);
        }
    }

    private void upload3DViews(Long savedProductsId, ProductsEntity savedProductsEntity,
        MultipartFile[] views)
        throws IOException {
        try {
            int index = 1;
            List<ProductsViewsEntity> productsViewsEntities = new ArrayList<>();
            for (MultipartFile view : views) {
                String randomFileName = uploadService.getRandomStringForImageName(8);
                String uploaded3DViewUrl = uploadService.uploadToAssetBucket(AssetType.VIEWS_3D,
                    savedProductsId, view, randomFileName);
                ProductsViewsEntity createdProductsViewsEntity = ProductsViewsEntity.builder()
                    .productsEntity(savedProductsEntity).viewOrd(index++).viewUrl(uploaded3DViewUrl)
                    .build();
                productsViewsEntities.add(createdProductsViewsEntity);
            }
            productsViewsRepository.saveAll(productsViewsEntities);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage() + ": ", e);
            throw new BaseException(ErrorCode.VIEWS_UPLOAD_FAILED);
        }
    }

    private void uploadAssetZipFile(Long savedProductsId, ProductsEntity savedProductsEntity,
        MultipartFile zipFile, AssetRequest dto)
        throws IOException {
        String randomFileName = uploadService.getRandomStringForImageName(8);
        String uploadedZipFileUrl = uploadService.uploadToAssetBucket(AssetType.ZIP,
            savedProductsId, zipFile, randomFileName);
        ProductsAssetFileEntity createdProductsAssetFileEntity = ProductsAssetFileEntity.builder()
            .productsEntity(savedProductsEntity).url(uploadedZipFileUrl)
            .fileName(randomFileName)
            .build();
        productsAssetFileRepository.save(createdProductsAssetFileEntity);
    }
}