package com.example.metasearch.manager;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.metasearch.utils.HttpHelper;
import com.example.metasearch.network.interfaces.WebServerDeleteEntityCallbacks;
import com.example.metasearch.network.interfaces.WebServerPersonDataUploadCallbacks;
import com.example.metasearch.network.interfaces.WebServerPersonFrequencyUploadCallbacks;
import com.example.metasearch.network.interfaces.WebServerQueryCallbacks;
import com.example.metasearch.network.interfaces.WebServerUploadCallbacks;
import com.example.metasearch.data.model.Person;
import com.example.metasearch.network.request.ChangeNameRequest;
import com.example.metasearch.network.request.DeleteEntityRequest;
import com.example.metasearch.network.request.NLQueryRequest;
import com.example.metasearch.network.request.PersonFrequencyRequest;
import com.example.metasearch.network.response.ChangeNameResponse;
import com.example.metasearch.network.response.DeleteEntityResponse;
import com.example.metasearch.network.response.PersonFrequencyResponse;
import com.example.metasearch.network.response.PhotoNameResponse;
import com.example.metasearch.network.response.PhotoResponse;
import com.example.metasearch.network.response.TripleResponse;
import com.example.metasearch.network.api.ApiService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebRequestManager {
    private static final String WEB_SERVER_BASE_URL = "http://10.0.2.2:8081";
    private static WebRequestManager webImageUploader;
    private ApiService webService;

    private WebRequestManager(){
        this.webService = HttpHelper.getInstance(WEB_SERVER_BASE_URL).getRetrofit().create(ApiService.class);
    }

    public ApiService getWebService(){
        return webService;
    }

    public static WebRequestManager getWebImageUploader(){
        Log.d(TAG,"AIImageUploaderController 함수 들어옴 객체 생성 or 반환");
        if(webImageUploader == null){
            webImageUploader = new WebRequestManager();

        }
        return webImageUploader;
    }

    //추가된 이미지 관해 서버에 전송
//    public void uploadAddGalleryImage(DatabaseHelper databaseHelper, ImageUploadService service, File imageFile, String source){
//        Log.d(TAG,"web의 uploadAddGalleryImage 안에 들어옴");
//        //List<CompletableFuture<Void>> futuresList = new ArrayList<>();
//        Log.d(TAG,"addImge 이름 : " + imageFile.getName());
//        RequestBody requestBody;
//        MultipartBody.Part imagePart;
//        //추가 이미지 분석이 필요한 이미지를 전달
//        requestBody = RequestBody.create(MediaType.parse("image"),imageFile);
//        //파일의 경로를 해당 이미지의 이름으로 설정함
//        imagePart = MultipartBody.Part.createFormData("addImage",imageFile.getName(),requestBody);
//
//        //이미지 출처 정보를 전송할 RequestBody 생성
//        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),source); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달
//
//        //API 호출
//        Call<UploadResponse> call = service.uploadWebAddImage(imagePart,sourceBody); //이미지 업로드 API 호출
//        call.enqueue(new Callback<UploadResponse>() { //비동기
//            @Override
//            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "Image upload 성공: " + response.message());
//                }
//            }
//            @Override
//            public void onFailure(Call<UploadResponse> call, Throwable t) {
//                Log.e(TAG, "추가 이미지 업로드 실패함" + t.getMessage());
//            }
//        });
//    }

    public void uploadAddGalleryImage(ArrayList<String>addImagePaths, String dbName){
        Log.d(TAG,"web의 uploadAddGalleryImage 안에 들어옴");

        for(String addImagePath : addImagePaths){
            //Log.d(TAG,"addImge 이름 : " + imageFile.getName());
            File imageFile = new File(addImagePath);

            // 파일 이름을 URL 인코딩
            String fileName = null;
            try {
                fileName = URLEncoder.encode(imageFile.getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "파일 이름 인코딩 실패: " + e.getMessage());
                continue;
            }


            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", fileName, requestBody);

            Call<Void> call = webService.uploadWebAddImage(imagePart, dbName); // sourceBody 대신 dbName을 직접 전달합니다.
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Image upload 성공: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "추가 이미지 업로드 실패함" + t.getMessage());
                }
            });
        }

    }





    // Web Server에 인물 빈도 수 요청
    public void getPersonFrequency(String dbName, List<Person> people, WebServerPersonFrequencyUploadCallbacks callbacks) {
        List<String> personNames = people.stream().map(Person::getInputName).collect(Collectors.toList());
        PersonFrequencyRequest request = new PersonFrequencyRequest(dbName, personNames);
        Call<PersonFrequencyResponse> call = webService.getPersonFrequency(request);
        call.enqueue(new Callback<PersonFrequencyResponse>() {
            @Override
            public void onResponse(Call<PersonFrequencyResponse> call, Response<PersonFrequencyResponse> response) {
                if (response.isSuccessful()) {
                    callbacks.onPersonFrequencyUploadSuccess(response.body());
                } else {
                    callbacks.onPersonFrequencyUploadFailure("Response from server was not successful.");
                }
            }

            @Override
            public void onFailure(Call<PersonFrequencyResponse> call, Throwable t) {
                callbacks.onPersonFrequencyUploadFailure("Failed to retrieve data from server: " + t.getMessage());
            }
        });
    }

    // Web Server로 인물 이름 or 사진 이름 전송
    public void sendPersonData(String personData, String dbName, WebServerPersonDataUploadCallbacks callbacks) {
        // Gson 인스턴스 생성
        Gson gson = new Gson();

        // dbName과 인물 정보(인물 이름 또는 사진 이름)을 포함하는 Map 객체 생성
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("dbName", dbName);
        jsonMap.put("personName", personData);

        // Map 객체를 JSON 문자열로 변환
        String jsonObject = gson.toJson(jsonMap);

        // JSON 문자열을 바디로 사용하여 RequestBody 객체 생성
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject);

        // POST 요청 보내기
        Call<List<String>> sendCall = webService.sendPersonData(requestBody);
        sendCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("Upload", "Person data sent successfully");
                    assert response.body() != null;
                    Log.d("Upload", "Common Photos: " + response.body());
                    callbacks.onPersonDataUploadSuccess(response.body());
                } else {
                    callbacks.onPersonDataUploadFailure("Server responded with error");
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("Upload", "Error sending person data", t);
                callbacks.onPersonDataUploadFailure(t.getMessage());
            }
        });
    }
    // Web Server로 Circle to Search 이미지 분석 결과 전송
    public void sendDetectedObjectsToWebServer(List<String> detectedObjects, String dbName, WebServerUploadCallbacks callbacks) {
        // Gson 인스턴스 생성
        Gson gson = new Gson();

        // detectedObjects와 dbName을 포함하는 Map 객체 생성
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("dbName", dbName);
        jsonMap.put("properties", detectedObjects);

        // Map 객체를 JSON 문자열로 변환
        String jsonObject = gson.toJson(jsonMap);

        // JSON 문자열을 바디로 사용하여 RequestBody 객체 생성
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject);

        Log.d("e",jsonObject);

        // POST 요청 보내기
        Call<PhotoResponse> sendCall = webService.sendDetectedObjects(requestBody);
        sendCall.enqueue(new Callback<PhotoResponse>() {
            @Override
            public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Upload", "Detected objects sent successfully");
                    assert response.body() != null;
                    Log.d("Upload", "Common Photos: " + response.body().getPhotos().getCommonPhotos());
                    Log.d("Upload", "Individual Photos: " + response.body().getPhotos().getIndividualPhotos());
                    callbacks.onWebServerUploadSuccess(response.body());
                } else {
                    callbacks.onWebServerUploadFailure("Server responded with error");
                }
            }
            @Override
            public void onFailure(Call<PhotoResponse> call, Throwable t) {
                Log.e("Upload", "Error sending detected objects", t);
            }
        });
    }

    public void uploadDeleteGalleryImage(ArrayList<String> deleteImagePaths, String dbName ){
        RequestBody requestBody;
        MultipartBody.Part imagePart;
        for(String deleteImagePath : deleteImagePaths){
            // 파일 이름을 URL 인코딩
            String deleteImageName = null;
            try {
                deleteImageName = URLEncoder.encode(deleteImagePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "파일 이름 인코딩 실패: " + e.getMessage());
                continue;
            }
            deleteImageName = deleteImageName.substring(deleteImageName.lastIndexOf(File.separator) + 1);
            requestBody = RequestBody.create(MediaType.parse("filename"),deleteImageName);
            //파일의 경로를 해당 이미지의 이름으로 설정함
            imagePart = MultipartBody.Part.createFormData("deleteImage",deleteImageName,requestBody);
            RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"),dbName); //DB이름과 어디에 저장되어야하는지에 관한 정보를 전달



            //API 호출
            Call<Void> call = webService.uploadWebDeleteImage(imagePart,sourceBody); //이미지 업로드 API 호출
            call.enqueue(new Callback<Void>() { //비동기
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "delete Image upload 성공: " + response.message());

                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "삭제 이미지 업로드 실패함" + t.getMessage());
                }
            });
        }
    }

    public void changePersonName(String dbName, String oldName, String newName) {
        ChangeNameRequest request = new ChangeNameRequest(dbName, oldName, newName);
        webService.changeName(request).enqueue(new Callback<ChangeNameResponse>() {
            @Override
            public void onResponse(Call<ChangeNameResponse> call, Response<ChangeNameResponse> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 이름 변경
                    Log.d("Name Change", "Success: " + response.body().getMessage());
                } else {
                    // 서버에서 정상적으로 처리하지 못했을 때
                    try {
                        Log.e("Name Change", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ChangeNameResponse> call, Throwable t) {
                // 네트워크 문제 등으로 요청 자체가 실패
                Log.e("Name Change", "Error: " + t.getMessage());
            }
        });
    }
    //
    public void sendQueryToWebServer(String dbName, String query, WebServerQueryCallbacks callbacks) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(new NLQueryRequest(dbName, query));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject);

        Call<PhotoNameResponse> call = webService.sendCypherQuery(requestBody);
        call.enqueue(new Callback<PhotoNameResponse>() {
            @Override
            public void onResponse(@NonNull Call<PhotoNameResponse> call, @NonNull Response<PhotoNameResponse> response) {
                if (response.isSuccessful()) {
                    PhotoNameResponse photoNameResponse = response.body();
                    if (photoNameResponse != null && photoNameResponse.getPhotoName() != null) {
                        Log.d("PhotoNames", photoNameResponse.getPhotoName().toString());
                        callbacks.onWebServerQuerySuccess(photoNameResponse);
                    } else {
                        Log.e("Response Error", "Received null response body or empty photos list");
                    }
                } else {
                    Log.e("Response Error", "Failed to receive successful response: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<PhotoNameResponse> call, @NonNull Throwable t) {
                callbacks.onWebServerQueryFailure();
                Log.e("Request Error", "Failed to send request to server", t);
            }
        });
    }
    // 이미지 설명을 위한 속성을 받아옴
    public void fetchTripleData(String dbName, String photoName, Callback<TripleResponse> callback) {
        Call<TripleResponse> call = webService.fetchTripleData(dbName, photoName);
        call.enqueue(new Callback<TripleResponse>() {
            @Override
            public void onResponse(Call<TripleResponse> call, Response<TripleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    // 서버로부터 성공적인 응답을 받았지만, 응답 내용에 문제가 있을 때
                    Log.e(TAG, "Error fetching triple data: " + response.message());
                    callback.onFailure(call, new IOException("Response unsuccessful: " + response.message()));
                }
            }
            @Override
            public void onFailure(Call<TripleResponse> call, Throwable t) {
                // 네트워크 문제 등 요청 자체에 실패한 경우
                Log.e(TAG, "Failure fetching triple data", t);
                callback.onFailure(call, t);
            }
        });
    }
    public void deleteEntity(String dbName, String entityName, WebServerDeleteEntityCallbacks callbacks) {
        DeleteEntityRequest request = new DeleteEntityRequest(dbName, entityName);
        Call<DeleteEntityResponse> call = webService.deleteEntity(request);
        call.enqueue(new Callback<DeleteEntityResponse>() {
            @Override
            public void onResponse(Call<DeleteEntityResponse> call, Response<DeleteEntityResponse> response) {
                if (response.isSuccessful()) {
                    DeleteEntityResponse deleteEntityResponse = response.body();
                    if (deleteEntityResponse != null) {
                        callbacks.onDeleteEntitySuccess(deleteEntityResponse.getMessage());
                    } else {
                        callbacks.onDeleteEntityFailure("Response body is null");
                    }
                } else {
                    callbacks.onDeleteEntityFailure("Response unsuccessful: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<DeleteEntityResponse> call, Throwable t) {
                callbacks.onDeleteEntityFailure("Request failed: " + t.getMessage());
            }
        });
    }
}
