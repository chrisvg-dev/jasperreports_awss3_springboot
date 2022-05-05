package com.cvg.reports.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AWSS3UploadService {
    private static final Regions AWS_REGION = Regions.US_EAST_1;
    private static final String BUCKET_NAME = "spring-report-storage";
    private static final String ARN = "spring-report-storage";
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;

    private AmazonS3 clientConfiguration() {
        AWSCredentials credentials = new BasicAWSCredentials( accessKey, secretKey );
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials( provider )
                .withRegion( AWS_REGION )
                .build();
    }

    private boolean existsBucketByName() {
        AmazonS3 client = this.clientConfiguration();
        if ( client.doesBucketExist( BUCKET_NAME ) ) {
            return true;
        }
        return false;
    }
    private boolean existsFileByName(String filename) {
        AmazonS3 client = this.clientConfiguration();
        if ( client.doesBucketExist( filename ) ) {
            return true;
        }
        return false;
    }

    public boolean putObject(String name, File file) {
        try {
            String fullPath = "Reports/" + name;
            AmazonS3 client = clientConfiguration();
            PutObjectResult uploadResult = client.putObject(BUCKET_NAME, fullPath, file);

            if (this.existsFileByName( fullPath ))
                return true;
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
