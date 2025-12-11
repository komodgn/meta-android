package com.example.metasearch.manager;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.example.metasearch.data.dao.AnalyzedImageListDatabaseHelper;
import com.example.metasearch.data.dao.DatabaseHelper;
import com.example.metasearch.utils.HttpHelper;
import com.example.metasearch.network.interfaces.CircleDataUploadCallbacks;
import com.example.metasearch.data.model.Circle;
import com.example.metasearch.network.response.CircleDetectionResponse;
import com.example.metasearch.network.response.UploadResponse;
import com.example.metasearch.network.api.ApiService;
import com.example.metasearch.utils.UriToFileConverter;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIRequestManager {
    private static final String AI_SERVER_BASE_URL = "http://10.0.2.2:8080";
    private static AIRequestManager aiImageUploader;
    private ImageAnalyzeListManager imageAnalyzeListManager;
    private AnalyzedImageListDatabaseHelper analyzedImageListDatabaseHelper;

    private ApiService aiService;
    static final String TABLE_NAME = "Faces";
    private Context context;

    private AIRequestManager(Context context){
        //this.aiService = AIHttpService.getInstance(AIserver_BASE_URL);
        this.aiService = HttpHelper.getInstance(AI_SERVER_BASE_URL).getRetrofit().create(ApiService.class);
        this.analyzedImageListDatabaseHelper = AnalyzedImageListDatabaseHelper.getInstance(context);

        this.imageAnalyzeListManager = ImageAnalyzeListManager.getInstance(context);
        this.context = context;

    }

    public ApiService getAiService(){
        return aiService;
    }

    public static AIRequestManager getAiImageUploader(Context context){
        Log.d(TAG,"AIImageUploaderController 함수 들어옴 객체 생성 or 반환");
        if(aiImageUploader == null){
            aiImageUploader = new AIRequestManager(context);

        }
        return aiImageUploader;
    }

    // 이미지 분석을 시작한다는 첫 번째 요청
    // 서버에서 해당 db의 faces 폴더를 삭제해도록 함
    public CompletableFuture<Void> firstUploadImage(String DBName){
        Log.d(TAG,"첫 번째 업로드");
        CompletableFuture<Void> future = new CompletableFuture<>();

        RequestBody firstBody = RequestBody.create(MediaType.parse("text/plain"),"first"); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),DBName); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달

        Call<Void> call = aiService.upload_first(firstBody,sourceBody); //이미지 업로드 API 호출
        call.enqueue(new Callback<Void>() { //비동기
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "first upload 성공: " + response.message());
                    future.complete(null);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "first upload  실패함" + t.getMessage());
                future.complete(null);
            }
        });
        return future;
    }

    // 이미지 분석이 완료되었다는 마지막 요청
    public CompletableFuture<Void> completeUploadImage(DatabaseHelper databaseHelper,String DBName){
        CompletableFuture<Void> future = new CompletableFuture<>();

        int index = databaseHelper.getRowCount(TABLE_NAME);


        //이미지 출처 정보를 전송할 RequestBody 생성
        RequestBody finishBody = RequestBody.create(MediaType.parse("text/plain"),"finish"); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),DBName); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
        RequestBody indexBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(index)); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달


        Call<UploadResponse> call = aiService.upload_finish(finishBody,sourceBody,indexBody); //이미지 업로드 API 호출

        call.enqueue(new Callback<UploadResponse>() { //비동기
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "마지막 요청 성공: " + response.message());
                    if(response.body() != null) {
                        for (UploadResponse.ImageData imageData : response.body().getImages()) {
                            boolean isFaceExit = imageData.getIsExit(); //추출된 이미지가 있다는 것
                            if (isFaceExit) {
                                //Log.d(TAG,"추출한 얼굴 이미지 있음");
                                Log.d(TAG, "추가 이미지에 대한 응답 받음 / 추가할 얼굴 있음");
                                String imageName = imageData.getImageName();
                                String imageBytes = imageData.getImageBytes();
                                // 여기서 바이트 배열로 변환 후 이미지 처리를 할 수 있음
                                byte[] decodedString = Base64.decode(imageBytes, Base64.DEFAULT);
                                Log.d(TAG, "imageName : " + imageName);
                                databaseHelper.insertImage(imageName, decodedString);
                            } else {
                                Log.d(TAG, "추가 이미지에 대한 응답 받음 / 추가할 얼굴 없음");
                                //Log.e(TAG, "이미지 업로드 성공, 추출된 이미지 없음" + response.message());
                            }
                        }
                    }
                    future.complete(null);

                }
            }
            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.e(TAG, "마지막 요청 실패" + t.getMessage());
                future.complete(null);

            }
        });
        return future;
    }

    //추가된 이미지 관해 서버에 전송
    public CompletableFuture<Void> uploadAddGalleryImage( ArrayList<String> imagePaths,String source) throws IOException {
        ImageNotificationManager imageNotificationManager = ImageNotificationManager.getImageNotification(context);
        List<CompletableFuture<Void>> futuresList = new ArrayList<>();
        Log.d(TAG,"uploadAddGalleryImage 안에 들어옴");
        //List<CompletableFuture<Void>> futuresList = new ArrayList<>();
        //Log.d(TAG,"addImge 이름 : " + imageFile.getName());
        RequestBody requestBody;
        MultipartBody.Part imagePart;
        for(String addImagePath : imagePaths){
            CompletableFuture<Void> future = new CompletableFuture<>();
            futuresList.add(future);

            File imageFile = new File(addImagePath);

            //추가 이미지 분석이 필요한 이미지를 전달
            requestBody = RequestBody.create(MediaType.parse("image"),imageFile);
            //파일의 경로를 해당 이미지의 이름으로 설정함
            imagePart = MultipartBody.Part.createFormData("addImage",imageFile.getName(),requestBody);


            //이미지 출처 정보를 전송할 RequestBody 생성
            RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),source); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
            //API 호출
            Call<Void> call = aiService.uploadAddImage(imagePart,sourceBody); //이미지 업로드 API 호출
            call.enqueue(new Callback<Void>() { //비동기
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "AI 서버 Image upload 성공: " + response.message());
                       // updateProgress(imageNotificationManager,imagePaths.size() );
                        future.complete(null);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (t instanceof IOException) {
                        // 네트워크 관련 예외 처리 (예: timeout)
                        Log.e(TAG, "네트워크 문제 또는 서버에서 timeout 발생: " + t.getMessage());
                        Log.d(TAG, "오류로 인해 분석 취소되는 이미지 이름 : " + imageFile.getName());
                        //해당 이미지 처리 x로 설정
                        imageAnalyzeListManager.delete_fail_image_analyze(imageFile.getName());
                    } else {
                        // 그 외 예외 처리
                        Log.e(TAG, "알 수 없는 예외 발생: " + t.getMessage());
                        Log.d(TAG, "오류로 인해 분석 취소되는 이미지 이름 : " + imageFile.getName());
                        imageAnalyzeListManager.delete_fail_image_analyze(imageFile.getName());

                    }
                    future.complete(null); // 예외를 future에 설정하여 예외 상황을 알림
                }
//                    Log.e(TAG, "추가 이미지 업로드 실패함" + t.getMessage());
//                    //future.completeExceptionally(t); // 작업 실패 시 future에 예외를 설정
            });
        }
        return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
    }
//    public CompletableFuture<List<String>> uploadAddGalleryImage(ArrayList<String> imagePaths, String source) {
//        Log.d(TAG, "uploadAddGalleryImage 안에 들어옴");
//        //알림 매니저
//
//
//        // 첫 번째 Future를 null로 처리하여 체인을 시작합니다.
//        CompletableFuture<String> initialFuture = CompletableFuture.completedFuture(null);
//        List<String> results = new ArrayList<>();
//
//        // 이전 작업이 완료된 후 다음 작업을 시작하는 체인을 만듭니다.
//        CompletableFuture<String> chain = initialFuture;
//        int size = imagePaths.size();
//        for (String imagePath : imagePaths) {
//            chain = chain.thenCompose(aVoid -> uploadSingleImage(imagePath, source,size).thenApply(result -> {
//                results.add(result);
//                return result;
//            }));
//
//        }
//
//        // 모든 작업이 완료되면 결과 리스트를 반환합니다.
//        return chain.thenApply(v -> results);
//    }
//    private CompletableFuture<String> uploadSingleImage(String imagePath, String source, int imageSize) {
//        ImageNotificationManager imageNotificationManager = ImageNotificationManager.getImageNotification(context);
//        CompletableFuture<String> future = new CompletableFuture<>();
//        File imageFile = new File(imagePath);
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
//        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("addImage", imageFile.getName(), requestBody);
//        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"), source);
//
//        Call<Void> call = aiService.uploadAddImage(imagePart, sourceBody);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "AI 서버 이미지 업로드 완료 ");
//                    future.complete("Success: " + imagePath);
//
//                    // 업로드 성공 후에 진행률 업데이트
//                    updateProgress(imageNotificationManager, imageSize);
//                } else {
//                    future.complete("Failed: " + imagePath);
//                    //분석된 리스트에 삭제함 즉, DB에서 삭제함
//                    analyzedImageListDatabaseHelper.removeImagePath(imagePath);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                future.complete("Failed: " + imagePath);
//                analyzedImageListDatabaseHelper.removeImagePath(imagePath);
//
//            }
//        });
//
//        return future;
//    }

//    private void updateProgress(ImageNotificationManager manager, int imageSize) {
//        if (imageSize > 0) {  // imageSize가 0보다 클 때만 진행률 계산
//            int progressCurrent = manager.getProgressCurrent();
//            int increase = Math.round(100.0f / imageSize);
//            manager.setProgressCurrent(progressCurrent + increase);
//            Log.d(TAG, "현재 프로그래스 숫자 : " + progressCurrent);
//            Log.d(TAG, "increase : " + increase);
//        } else {
//            Log.e(TAG, "imageSize is zero, cannot update progress.");
//        }
//    }
    private void updateProgress(ImageNotificationManager manager, int imageSize) {
        if (imageSize > 0) {
            int progressCurrent = manager.getProgressCurrent(); //현재 진행량
            manager.setProgressCurrent(progressCurrent + +1);
            Log.d(TAG, "현재 프로그래스 숫자 : " + (progressCurrent + +1));
            Log.d(TAG, "increase : " + progressCurrent + +1);
        } else {
            Log.e(TAG, "imageSize is zero, cannot update progress.");
        }
    }




    //삭제된 이미지 관해 서버에 전송
    public CompletableFuture<Void> uploadDeleteGalleryImage(DatabaseHelper databaseHelper, ArrayList<String>deleteImagePaths,String source){
        List<CompletableFuture<Void>> futuresList = new ArrayList<>();

        RequestBody requestBody;
        MultipartBody.Part imagePart;

        for(String deleteImagePath : deleteImagePaths){
            CompletableFuture<Void> future = new CompletableFuture<>();
            futuresList.add(future);
            requestBody = RequestBody.create(MediaType.parse("filename"),deleteImagePath);
            //파일의 경로를 해당 이미지의 이름으로 설정함
            imagePart = MultipartBody.Part.createFormData("deleteImage",deleteImagePath,requestBody);

            //이미지 출처 정보를 전송할 RequestBody 생성
            RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),source); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
            // RequestBody endIndicatorBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isFinal)); //갤러리의 마지막 요청인지에 대한 정보 전달

            //API 호출
            Call<Void> call = aiService.UploadDeleteImage(imagePart,sourceBody); //이미지 업로드 API 호출
            call.enqueue(new Callback<Void>() { //비동기
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Image delete 성공: " + response.message());
                        future.complete(null);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "삭제 이미지 업로드 실패함" + t.getMessage());
                    future.complete(null);

                }
            });
        }

        return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));

    }

    //Database 이미지 서버에 전송
    public CompletableFuture<Void> uploadDBImage(Map<String, byte[]> imagesList, String dbName){
        //데이터베이스 서버 요청이 다 끝나면, 다 끝났다는 것을 반환함
        Log.d(TAG,"uploadDBImage 들어옴");
        // 모든 비동기 작업을 추적하기 위한 CompletableFuture 리스트 생성
        List<CompletableFuture<Void>> futuresList = new ArrayList<>();
        for (Map.Entry<String,byte[]> database_image_element : imagesList.entrySet()){
            CompletableFuture<Void> future = new CompletableFuture<>();
            futuresList.add(future);
            //byte[]에서 RequestBody 생성
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),database_image_element.getValue());
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("faceImage",database_image_element.getKey(),requestBody);

            //이미지 출처 정보를 전송할 RequestBody 생성
            RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),dbName);

            //API 호출
            Call<Void> call = aiService.uploadDatabaseImage(imagePart,sourceBody); //이미지 업로드 API 호출
            call.enqueue(new Callback<Void>() { //비동기
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "데이터베이스 업로드 성공함" + response.message());
                        future.complete(null); // 작업 성공 시 future를 완료 상태로 설정
                    }

                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "데이터베이스 업로드 실패함" + t.getMessage());
                    future.completeExceptionally(t); // 작업 실패 시 future에 예외를 설정
                }
            });
        }
        // 모든 futures가 완료될 때까지 기다림
        return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
    }

    public void deletePerson(String dbName, String imageName) {
//        DeleteEntityRequest request = new DeleteEntityRequest(dbName, imageName);
//        Call<Void> call = aiService.deletePerson(request);
        RequestBody db = RequestBody.create(MediaType.parse("text/plain"),dbName);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),imageName);
        Call<Void> call = aiService.deletePerson(db, name);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("DEL_PERSON", response.message());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DEL_PERSON", "DELETE PERSON FAILED");
            }
        });
    }


    // AI Server로 이미지와 원 리스트 전송
    public void uploadCircleData(Uri imageUri, List<Circle> circles, String source, Context context, CircleDataUploadCallbacks callbacks) throws IOException {
        File file = UriToFileConverter.getFileFromUri(context, imageUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("searchImage", file.getName(), requestFile);

        Gson gson = new Gson();
        String jsonCircles = gson.toJson(circles);
        RequestBody circleData = RequestBody.create(MediaType.parse("application/json"), jsonCircles);
        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"), source);

        Call<CircleDetectionResponse> call = aiService.uploadImageAndCircles(body, sourceBody, circleData);
        call.enqueue(new Callback<CircleDetectionResponse>() {
            @Override
            public void onResponse(Call<CircleDetectionResponse> call, Response<CircleDetectionResponse> response) {
                if (response.isSuccessful()) {
                    callbacks.onCircleUploadSuccess(response.body().getDetectedObjects());
                } else {
                    callbacks.onCircleUploadFailure("Server responded with error");
                }
            }
            @Override
            public void onFailure(Call<CircleDetectionResponse> call, Throwable t) {
                callbacks.onCircleUploadFailure("Failed to upload data and image: " + t.getMessage());
            }
        });
    }
}
