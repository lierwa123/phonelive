/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.yunbao.common.upload;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Handles basic helper functions used throughout the app.
 */
public class AWSTransferUtil {
    private static final String TAG = AWSTransferUtil.class.getSimpleName();

    private AmazonS3Client sS3Client;
    private AWSCredentialsProvider sMobileClient;
    private TransferUtility sTransferUtility;
    private static CognitoCachingCredentialsProvider sCredProvider;

    /**
     * Gets an instance of AWSMobileClient which is
     * constructed using the given Context.
     *
     * @param context Android context
     * @return AWSMobileClient which is a credentials provider
     */
    private AWSCredentialsProvider getCredProvider(Context context) {
        if (sMobileClient == null) {
            final CountDownLatch latch = new CountDownLatch(1);
            AWSMobileClient.getInstance().initialize(context, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails result) {
                    latch.countDown();
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "onError: ", e);
                    latch.countDown();
                }
            });
            try {
                latch.await();
                sMobileClient = AWSMobileClient.getInstance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sMobileClient;
    }

    /**
     * Gets an instance of a S3 client which is constructed using the given
     * Context.
     *
     * @param context Android context
     * @return A default S3 client.
     */
    public AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            try {
                String regionString = new AWSConfiguration(context)
                        .optJsonObject("S3TransferUtility")
                        .getString("Region");
                Region region = Region.getRegion(regionString);
                sS3Client = new AmazonS3Client(getCredProvider(context), region);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sS3Client;
    }


    // TODO: 2020-12-24 动态设置poolID、region等信息
    public AmazonS3Client getS3Client(Context context,String regionName,String poolId) {
        if (sS3Client == null) {
            Region region = Region.getRegion(regionName);
            sS3Client = new AmazonS3Client(getNewCredProvider(context,poolId,regionName), region);
        }
        return sS3Client;
    }

    private static CognitoCachingCredentialsProvider getNewCredProvider(Context context,String poolId,String regionName) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    poolId,
                    Regions.fromName(regionName));
        }
        return sCredProvider;
    }

    /**
     * Gets an instance of the TransferUtility which is constructed using the
     * given Context
     *
     * @param context Android context
     * @return a TransferUtility instance
     */
    public TransferUtility getTransferUtility(Context context,String region,String poolId) {
        if (sTransferUtility == null) {
            sTransferUtility = TransferUtility.builder()
                    .context(context)
                    .s3Client(getS3Client(context,region,poolId))
                    .build();
        }

        return sTransferUtility;
    }

    /**
     * Converts number of bytes into proper scale.
     *
     * @param bytes number of bytes to be converted.
     * @return A string that represents the bytes in a proper scale.
     */
    public String getBytesString(long bytes) {
        String[] quantifiers = new String[]{
                "KB", "MB", "GB", "TB"
        };
        double speedNum = bytes;
        for (int i = 0; ; i++) {
            if (i >= quantifiers.length) {
                return "";
            }
            speedNum /= 1024;
            if (speedNum < 512) {
                return String.format(Locale.US, "%.2f", speedNum) + " " + quantifiers[i];
            }
        }
    }

    /**
     * Copies the data from the passed in Uri, to a new file for use with the
     * Transfer Service
     *
     * @param context Android context
     * @param uri Content URI
     * @return New file copied from given URI
     * @throws IOException if data read/write fails
     */
    public File copyContentUriToFile(Context context, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File copiedData = new File(context.getDir("SampleImagesDir", Context.MODE_PRIVATE),
                UUID.randomUUID().toString());
        copiedData.createNewFile();

        FileOutputStream fos = new FileOutputStream(copiedData);
        byte[] buf = new byte[2046];
        int read = -1;
        while ((read = is.read(buf)) != -1) {
            fos.write(buf, 0, read);
        }

        fos.flush();
        fos.close();

        return copiedData;
    }

    /**
     * Fills in the map with information in the observer so that it can be used
     * with a SimpleAdapter to populate the UI
     */
    public void fillMap(Map<String, Object> map, TransferObserver observer, boolean isChecked) {
        int progress = (int) ((double) observer.getBytesTransferred() * 100 / observer
                .getBytesTotal());
        map.put("id", observer.getId());
        map.put("checked", isChecked);
        map.put("fileName", observer.getAbsoluteFilePath());
        map.put("progress", progress);
        map.put("bytes",
                getBytesString(observer.getBytesTransferred()) + "/"
                        + getBytesString(observer.getBytesTotal()));
        map.put("state", observer.getState());
        map.put("percentage", progress + "%");
    }
}
