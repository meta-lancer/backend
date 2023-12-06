package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.AssetType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetRequest;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.PortfolioCreate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.PortfolioUpdate;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetCreatedResponse;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetUpdatedResponse;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.external.aws.s3.S3Service;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
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
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.PortfolioRepository;
import com.metalancer.backend.users.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
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
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Override
    public AssetCreatedResponse createAsset(User user, @NotNull MultipartFile[] thumbnails,
        @NotNull MultipartFile[] views, AssetRequest dto) {
        try {
            user = userRepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
            );
            CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(user,
                DataStatus.ACTIVE);
            ProductsEntity savedProductsEntity = createProductsEntity(dto, creatorEntity);
            log.info("에셋 등록 - products 생성");
            createProductsAssetFileEntity(dto, savedProductsEntity);
            log.info("에셋 등록 - ProductsAssetFile 생성");
            Long savedProductsId = savedProductsEntity.getId();
            saveTagList(dto, savedProductsEntity);
            log.info("에셋 등록 - tagList 생성");
            uploadThumbnailImages(savedProductsId, savedProductsEntity, thumbnails);
            log.info("에셋 등록 - 썸네일 업로드");
            upload3DViews(savedProductsId, savedProductsEntity, views);
            log.info("에셋 등록 - 3D 뷰 업로드");
            long taskCnt = productsRepository.countAllByCreatorEntity(creatorEntity);
            double satisficationRate = 0.0;
            ProductsDetail productsDetail = savedProductsEntity.toProductsDetail(taskCnt,
                satisficationRate);
            getProductsDetailTagList(savedProductsEntity, productsDetail);
            AssetFile assetFile = getProductsDetailAssetFile(savedProductsEntity,
                productsDetail);
            productsDetail.setAssetFile(assetFile);
            return AssetCreatedResponse.builder().productsDetail(productsDetail)
                .build();

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(ErrorCode.ASSETS_UPLOAD_FAILED);
        }
    }

    private AssetFile getProductsDetailAssetFile(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> thumbnailUrlList = getProductsThumbnailList(savedProductsEntity,
            productsDetail);
        log.info("썸네일 목록 조회");
        List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
            savedProductsEntity);
        log.info("3D 뷰 데이터 조회");
        ProductsAssetFileEntity productsAssetFileEntity = productsAssetFileRepository.findByProducts(
            savedProductsEntity);
        AssetFile assetFile = productsAssetFileEntity.toAssetFile();
        assetFile.setThumbnailUrlList(thumbnailUrlList);
        assetFile.setViewUrlList(viewUrlList);
        assetFile.setZipFileUrl(productsAssetFileEntity.getUrl());
        return assetFile;
    }

    @Nullable
    private List<String> getProductsThumbnailList(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> thumbnailUrlList = productsThumbnailRepository.findAllUrlByProduct(
            savedProductsEntity);
        if (thumbnailUrlList != null && thumbnailUrlList.size() > 0) {
            productsDetail.setThumbnail(thumbnailUrlList.get(0));
            productsDetail.setThumbnailList(thumbnailUrlList);
        }
        return thumbnailUrlList;
    }

    private void getProductsDetailTagList(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> tagList = productsTagRepository.findTagListByProduct(savedProductsEntity);
        productsDetail.setTagList(tagList);
    }

    private void createProductsAssetFileEntity(AssetRequest dto,
        ProductsEntity savedProductsEntity) {
        ProductsAssetFileEntity createdProductsAssetFileEntity = ProductsAssetFileEntity.builder().
            productsEntity(savedProductsEntity)
            .productionProgram(String.join(", ", dto.getProductionProgram()))
            .fileName("")
            .url("")
            .compatibleProgram(
                String.join(", ", dto.getCompatibleProgram())).support(dto.getSupport())
            .animation(dto.getAnimation()).rigging(dto.getRigging()).copyRight(dto.getCopyRight())
            .extList(String.join(", ", dto.getExtList())).fileSize(dto.getFileSize())
            .recentVersion(dto.getRecentVersion()).build();
        productsAssetFileRepository.save(createdProductsAssetFileEntity);
    }

    @Override
    public String getAssetFilePreSignedUrl(Long productsId) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        Optional<ProductsAssetFileEntity> productsAssetFileEntity = productsAssetFileRepository.findOptionalEntityByProducts(
            productsEntity);
        if (productsAssetFileEntity.isEmpty()) {
            throw new NotFoundException("등록된 상품이 없습니다.", ErrorCode.NOT_FOUND);
        } else {
            ProductsAssetFileEntity foundProductsAssetFileEntity = productsAssetFileEntity.get();
            String url = foundProductsAssetFileEntity.getUrl();
            if (url == null || url.equals("")) {
                String randomFileName = UUID.randomUUID().toString().substring(0, 10) + ".zip";
                String uploadedZipFileUrl = uploadService.getAssetFilePresignedUrl(productsId,
                    randomFileName);
                String shortUrl = uploadService.extractBaseUrl(uploadedZipFileUrl);
                foundProductsAssetFileEntity.setUrl(shortUrl);
                foundProductsAssetFileEntity.setFileName(randomFileName);
                productsAssetFileRepository.save(foundProductsAssetFileEntity);
                return uploadedZipFileUrl;
            } else {
                String savedFileName = foundProductsAssetFileEntity.getFileName();
                return uploadService.getAssetFilePresignedUrl(productsId, savedFileName);
            }
        }
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
        if (!productsEntity.getCreatorEntity().getUser().getId().equals(user.getUser().getId())) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }
        productsEntity.deleteProducts();
        // tag 삭제
        List<ProductsTagEntity> productsTagEntities = productsTagRepository.findTagEntityListByProduct(
            productsEntity);
        productsTagRepository.deleteAll(productsTagEntities);
        return true;
    }

    @Override
    public Boolean deleteAsset(Long productsId, PrincipalDetails user) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        productsEntity.deleteProducts();
        if (!productsEntity.getCreatorEntity().getUser().getId().equals(user.getUser().getId())) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }
        List<ProductsTagEntity> productsTagEntities = productsTagRepository.findTagEntityListByProduct(
            productsEntity);
        productsTagRepository.deleteAll(productsTagEntities);
        return true;
    }

    @Override
    public AssetUpdatedResponse updateAsset(Long productsId, User user, AssetUpdate dto) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        // 본인만 접근 가능
        CreatorEntity creatorEntity = productsEntity.getCreatorEntity();
        if (!creatorEntity.getUser().getId().equals(user.getId())) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }

        productsEntity.update(dto);
        productsEntity = productsRepository.findProductById(productsId);
        // 혹시 몰라서 save까지
        productsRepository.save(productsEntity);
        productsEntity = productsRepository.findProductById(productsId);
        // 에셋 파일 조회
        ProductsAssetFileEntity foundProductsAssetFileEntity = productsAssetFileRepository.findByProducts(
            productsEntity);
        // 에셋 파일 업데이트
        foundProductsAssetFileEntity.update(productsEntity, dto);
        productsAssetFileRepository.save(foundProductsAssetFileEntity);

        // 썸네일 수정. 만약 보낸다면 기존에 갖고있던 거 싹 다 없애버리고..!
        List<String> thumbnailList = dto.getThumbnailList();
        if (thumbnailList != null && !thumbnailList.isEmpty()) {
            productsThumbnailRepository.deleteAllUrlByProduct(productsEntity);
            setUpdatedThumbnailList(thumbnailList, productsEntity);
        }

        // 태그 수정
        List<ProductsTagEntity> productsTagEntities = productsTagRepository.findTagEntityListByProduct(
            productsEntity);
        productsTagRepository.deleteAll(productsTagEntities);
        updateTagList(dto, productsEntity);

        // 작업 수, 에셋 평점
        long taskCnt = productsRepository.countAllByCreatorEntity(creatorEntity);
        double satisficationRate = 0.0;
        ProductsDetail productsDetail = productsEntity.toProductsDetail(taskCnt, satisficationRate);
        // tag 목록
        getProductsDetailTagList(productsEntity, productsDetail);
        // 에셋 파일
        AssetFile assetFile = getProductsDetailAssetFile(productsEntity,
            productsDetail);
        productsDetail.setAssetFile(assetFile);
        return AssetUpdatedResponse.builder().productsDetail(productsDetail).build();
    }

    @Override
    public String getThumbnailPreSignedUrl(Long productsId, String extension) {
        ProductsEntity productsEntity = productsRepository.findProductById(productsId);
        String randomFileName = uploadService.getRandomStringForImageName(8);
        return uploadService.getThumbnailPresignedUrl(productsId, randomFileName, extension);
    }

    private void setUpdatedThumbnailList(List<String> thumbnailList,
        ProductsEntity productsEntity) {
        int index = 1;
        List<ProductsThumbnailEntity> productsThumbnailEntities = new ArrayList<>();
        for (String thumbnailUrl : thumbnailList) {
            if (index == 1) {
                productsEntity.setThumbnail(thumbnailUrl);
            }
            ProductsThumbnailEntity createdProductsThumbnailEntity = ProductsThumbnailEntity.builder()
                .productsEntity(productsEntity).thumbnailOrd(index++)
                .thumbnailUrl(thumbnailUrl)
                .build();
            productsThumbnailEntities.add(createdProductsThumbnailEntity);
        }
        productsThumbnailRepository.saveAll(productsThumbnailEntities);
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


    private void updateTagList(AssetUpdate dto, ProductsEntity productsEntity) {
        List<ProductsTagEntity> tagEntities = new ArrayList<>();
        for (String tag : dto.getTagList()) {
            ProductsTagEntity createdProductsTagEntity = ProductsTagEntity.builder()
                .productsEntity(productsEntity).name(tag).build();
            tagEntities.add(createdProductsTagEntity);
        }
        productsTagRepository.saveAll(tagEntities);
    }

    private ProductsEntity createProductsEntity(AssetRequest dto, CreatorEntity creatorEntity) {
        ProductsEntity createdProductsEntity = ProductsEntity.builder()
            .creatorEntity(creatorEntity).title(dto.getTitle()).price(dto.getPrice())
            .assetDetail(dto.getAssetDetail())
            .assetNotice(
                dto.getAssetNotice()).assetCopyRight(dto.getAssetCopyRight())
            .build();
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
                if (index == 1) {
                    savedProductsEntity.setThumbnail(uploadedThumbnailUrl);
                }
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

    @Override
    public List<Portfolio> deleteMyPortfolio(Long portfolioId, PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        PortfolioEntity portfolioEntity = portfolioRepository.findEntityByIdAndCreator(portfolioId,
            creatorEntity);
        portfolioRepository.delete(portfolioEntity);
        return portfolioRepository.findAllByCreator(creatorEntity);
    }

    @Override
    public List<Portfolio> createMyPortfolio(PortfolioCreate dto, PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        PortfolioEntity portfolioEntity = PortfolioEntity.builder().creatorEntity(creatorEntity)
            .title(dto.getTitle()).beginAt(dto.getBeginAt()).endAt(dto.getEndAt())
            .workerCnt(dto.getWorkerCnt()).tool(dto.getTool()).referenceFile(dto.getReferenceFile())
            .build();
        portfolioRepository.save(portfolioEntity);
        return portfolioRepository.findAllByCreator(creatorEntity);
    }

    @Override
    public List<Portfolio> updateMyPortfolio(Long portfolioId, PortfolioUpdate dto,
        PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        PortfolioEntity portfolioEntity = portfolioRepository.findEntityByIdAndCreator(portfolioId,
            creatorEntity);
        portfolioEntity.update(dto.getTitle(), dto.getBeginAt(), dto.getEndAt(), dto.getWorkerCnt(),
            dto.getTool(), dto.getReferenceFile());
        portfolioRepository.save(portfolioEntity);
        return portfolioRepository.findAllByCreator(creatorEntity);
    }


}