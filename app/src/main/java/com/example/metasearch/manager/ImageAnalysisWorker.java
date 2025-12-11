package com.example.metasearch.manager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.metasearch.data.dao.DatabaseHelper;

public class ImageAnalysisWorker extends Worker {
    private ImageServiceRequestManager imageServiceRequestManager;
    private static final String TAG = "ImageAnalysisWorker";

    public ImageAnalysisWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        this.imageServiceRequestManager = ImageServiceRequestManager.getInstance(context,databaseHelper);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 이미지 분석 시작
        Log.d(TAG, "ImageAnalysisWorker doWork() called");
        try {
            imageServiceRequestManager.getImagePathsAndUpload();
            Log.d(TAG, "ImageAnalysisWorker doWork() completed");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "ImageAnalysisWorker doWork() failed", e);
            return Result.failure();
        }
    }
}

