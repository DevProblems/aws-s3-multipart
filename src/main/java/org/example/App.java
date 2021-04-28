package org.example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.Copy;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;

/*
 @author Dev Problems
 104857600 bytes - 100mb
 */

public class App {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    TransferManager tm = null;
    void uploadMultipartFile(String bucketName, String keyName, String filePath) {
        try {
            tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .withMultipartUploadThreshold((long) (104857600))
                    .withMinimumUploadPartSize((long) (104857600))
                    .build();
            Upload upload = tm.upload(bucketName, keyName, new File(filePath));
            System.out.println("Object upload started");
            upload.waitForCompletion();
            System.out.println("Object upload complete");
            tm.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void copyMultipartFile(String fromBucket, String fromKeyName, String toBucket, String toKeyName) {
        try {
            TransferManager  tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .withMultipartCopyThreshold((long) (104857600))
                    .withMultipartCopyPartSize((long) (104857600))
                    .build();
            Copy copy = tm.copy(fromBucket, fromKeyName, toBucket, toKeyName);
            System.out.println("Object copy started");
            copy.waitForCompletion();
            System.out.println("Object copy completed");
            tm.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new App().uploadMultipartFile("bucket-name", "key-name", "file-path");
        new App().copyMultipartFile("source-bucket-name", "source-key-name", "destination-bucket-name", "destination-key-name");
    }
}
