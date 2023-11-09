package com.urbanthreads.inventoryservice.service;


import com.urbanthreads.inventoryservice.model.Item;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

import java.net.URL;
import java.time.Duration;
import java.util.*;

@Service
public class S3Service {

    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private final String bucketName = "urban-threads";

    @PostConstruct
    public void init() {
        // Define the ARN of the IAM role you want to assume
        String roleArn = System.getenv("MY_AWS_ARN");
        // Create a credentials provider that assumes the IAM role
        StsAssumeRoleCredentialsProvider roleCredentialsProvider = StsAssumeRoleCredentialsProvider.builder()
                .stsClient(StsClient.builder()
                        .region(Region.US_EAST_2) // Specify the AWS region for the STS client
                        .build())
                .refreshRequest(assumeRoleRequest -> assumeRoleRequest.roleArn(roleArn))
                .build();

        // Use the assumed role credentials provider to create the S3 client
        s3Client = S3Client.builder()
                .region(Region.US_EAST_2) // Change to your desired region
                .credentialsProvider(roleCredentialsProvider)
                .build();

        // Use the assumed role credentials provider to create the S3 presigner
        s3Presigner = S3Presigner.builder()
                .region(Region.US_EAST_2) // Replace with your region
                .credentialsProvider(roleCredentialsProvider)
                .build();
    }
    // Generates a list of presigned URLs for uploading images
    public Set<String> generatePresignedUrls(Set<String> objectKeys, Long id) {
        Set<String> presignedUrls = new HashSet<>();

            for (String objectKey : objectKeys) {
                PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                        .putObjectRequest(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(id + "/" + objectKey)
                                .contentType("image/png") // This will match any image type
                                .build())
                        .signatureDuration(Duration.ofMinutes(60)) // The URL will expire in 60 minutes
                        .build();

                PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
                URL url = presignedRequest.url();
                presignedUrls.add(url.toString());
            }

        return presignedUrls;
    }

    public void generatePresignedUrlsForItems(List<Item> items) {
        Map<Long, Set<String>> presignedUrlsMap = new HashMap<>();

        for (Item item : items) {
            // Retrieve the list of image keys for each itemId
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(item.getId() + "/") // Assuming the itemId is used as a prefix
                    .build();
            System.out.println("id: "+item.getId());
            ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

            Set<String> presignedUrls = new HashSet<>();
            for (S3Object s3Object : listObjectsResponse.contents()) {
                // Generate the pre-signed URL for each image object
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Object.key())
                        .build();
                GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(60)) // URL expiry time
                        .getObjectRequest(getObjectRequest)
                        .build();
                PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
                presignedUrls.add(presignedGetObjectRequest.url().toString());
            }
            item.setImages(presignedUrls);
        }
    }


}

