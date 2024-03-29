package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetRequest;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdateWithOutThumbnail;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.MyPaymentInfoManagementCreate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.MyPaymentInfoManagementUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.PortfolioCreate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.PortfolioUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.RequestProductsCreate;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetCreatedResponse;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetUpdatedResponse;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CreatorService {

    AssetCreatedResponse createAsset(User user, MultipartFile[] thumbnails,
        MultipartFile[] views,
        AssetRequest dto);

    String getAssetFilePreSignedUrl(Long productsId);

    Boolean successAsset(Long productsId, PrincipalDetails user);

    Boolean failAsset(Long productsId, PrincipalDetails user);

    Boolean deleteAsset(Long productsId, PrincipalDetails user);

    List<Portfolio> deleteMyPortfolio(Long portfolioId, PrincipalDetails user);

    List<Portfolio> createMyPortfolio(MultipartFile[] files, PortfolioCreate dto,
        PrincipalDetails user);

    List<Portfolio> updateMyPortfolio(MultipartFile[] files, Long portfolioId, PortfolioUpdate dto,
        PrincipalDetails user);

    AssetUpdatedResponse updateAsset(Long productsId, User user, AssetUpdate dto);

    String getThumbnailPreSignedUrl(Long productsId, String extension);

    AssetUpdatedResponse updateAssetWithFile(MultipartFile[] thumbnails, Long productsId, User user,
        AssetUpdateWithOutThumbnail dto);

    boolean deleteMyPaymentInfoManagement(PrincipalDetails user);

    boolean createMyPaymentInfoManagement(MultipartFile idCardCopyFile,
        MultipartFile accountCopyFile, MyPaymentInfoManagementCreate dto,
        PrincipalDetails user) throws IOException;

    boolean updateMyPaymentInfoManagement(MultipartFile idCardCopyFile,
        MultipartFile accountCopyFile, MyPaymentInfoManagementUpdate dto,
        PrincipalDetails user) throws IOException;

    ProductsDetail createRequestProducts(User user, MultipartFile[] thumbnails,
        RequestProductsCreate dto) throws IOException;
}