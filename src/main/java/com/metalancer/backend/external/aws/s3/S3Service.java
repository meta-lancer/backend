package com.metalancer.backend.external.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.metalancer.backend.common.constants.AssetType;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.assetBucket}")
    private String assetBucket;

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.cloudfront.asset}")
    private String readAssetBucket;
    @Value("${cloud.aws.s3.cloudfront.user}")
    private String readUserBucket;
    @Value("${cloud.aws.s3.cloudfront.service}")
    private String readServiceBucket;

    private AmazonS3 s3Client;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(this.region)
            .build();
    }

    public String getAssetFilePresignedUrl(Long productsId, String fileName) {
        String prefix = "asset/" + productsId + "/zip";
        fileName = prefix + "/" + fileName;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequestForAssetFile(
            fileName);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String getThumbnailPresignedUrl(Long productsId, String randomFileName,
        String extension) {
        String fileName = randomFileName + "." + extension;
        String prefix = "asset/" + productsId + "/thumbnail";
        fileName = prefix + "/" + fileName;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(
            fileName);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(assetBucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequestForAssetFile(
        String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(assetBucket, fileName)
                .withMethod(HttpMethod.PUT)
//                .withContentType("application/zip")
                .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5;
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }

    public String uploadToAssetBucket(AssetType assetType, Long assetId, MultipartFile file,
        String randomString)
        throws IOException {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = randomString + "." + ext;
        String bucketName = constructBucketName(assetId, assetType);
        String readBucketName = constructReadBucketName(assetId, assetType);

        ObjectMetadata metadata = getObjectMetadata(file, ext);

        s3Client.putObject(
            new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return readBucketName + "/" + fileName;
    }

    public String uploadToPortfolioReference(Long creatorId, Long portfolioId, MultipartFile file,
        String randomString)
        throws IOException {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = randomString + "." + ext;
        String bucketName = assetBucket + "/creator/" + creatorId + "/portfolio/" + portfolioId;
        String readBucketName =
            readAssetBucket + "/creator/" + creatorId + "/portfolio/" + portfolioId;

        ObjectMetadata metadata = getObjectMetadata(file, ext);

        s3Client.putObject(
            new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return readBucketName + "/" + fileName;
    }

    @NotNull
    private ObjectMetadata getObjectMetadata(MultipartFile file, String ext) {
        ObjectMetadata metadata = new ObjectMetadata();
        String mimeType = getMimeType(ext);
        metadata.setContentType(mimeType);
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String constructBucketName(Long assetId, AssetType assetType) {
        return assetBucket + "/asset/" + assetId + "/" + assetType.getPath();
    }

    private String constructReadBucketName(Long assetId, AssetType assetType) {
        return readAssetBucket + "/asset/" + assetId + "/" + assetType.getPath();
    }

    public String getRandomStringForImageName(int size) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = size;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public String getRequestFilePresignedUrl(Long requestId, String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        String prefix = "request/" + requestId + "/file";
        String newFileName = UUID.randomUUID().toString().substring(0, 8) + "." + ext;
        newFileName = prefix + "/" + newFileName;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(
            newFileName);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String uploadToPaymentInfoManagement(Long creatorId,
        MultipartFile file,
        String randomString)
        throws IOException {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = randomString + "." + ext;
        String bucketName =
            assetBucket + "/creator/" + creatorId + "/paymentInfo-management";
        String readBucketName =
            readAssetBucket + "/creator/" + creatorId + "/paymentInfo-management";

        ObjectMetadata metadata = getObjectMetadata(file, ext);

        s3Client.putObject(
            new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return readBucketName + "/" + fileName;
    }

    private String getMimeType(String ext) {
        // Implement logic to determine the MIME type based on the file extension
        // Example:
        switch (ext.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            // Add more cases for other file types
            default:
                return "application/octet-stream";
        }
    }

    public String extractBaseUrl(String signedUrl) {
        try {
            URL url = new URL(signedUrl);
            return url.getProtocol() + "://" + url.getHost() + url.getPath();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.SHORTEN_URL_FAILED);
        }
    }

}
