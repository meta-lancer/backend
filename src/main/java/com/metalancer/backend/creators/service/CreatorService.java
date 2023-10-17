package com.metalancer.backend.creators.service;

import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetRequest;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetCreatedResponse;
import com.metalancer.backend.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface CreatorService {

    AssetCreatedResponse createAsset(User user, MultipartFile[] thumbnails,
        MultipartFile[] views,
        MultipartFile zipFile, AssetRequest dto);

    String getAssetFilePreSignedUrl(Long productsId);
}