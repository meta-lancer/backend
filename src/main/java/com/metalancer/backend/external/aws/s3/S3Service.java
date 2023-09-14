package com.metalancer.backend.external.aws.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.metalancer.backend.common.constants.AssetType;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
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
    @Value("${cloud.aws.s3.cloudfront}")
    private String readBucket;

    private AmazonS3 s3Client;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(this.region)
            .build();
    }

    public String uploadToAssetBucket(AssetType assetType, Long assetId, MultipartFile file,
        String randomString)
        throws IOException {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = randomString + "." + ext;
        String bucketName = constructBucketName(assetId, assetType);
        String readBucketName = constructReadBucketName(assetId, assetType);

        s3Client.putObject(
            new PutObjectRequest(bucketName, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return readBucketName + "/" + fileName;
    }

    private String constructBucketName(Long assetId, AssetType assetType) {
        return assetBucket + "/asset/" + assetId + "/" + assetType.getPath();
    }

    private String constructReadBucketName(Long assetId, AssetType assetType) {
        return readBucket + "/asset/" + assetId + "/" + assetType.getPath();
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
}
