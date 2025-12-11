package com.example.metasearch.ui.activity;

import static com.example.metasearch.utils.GalleryImageManager.findMatchedUri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.metasearch.R;
import com.example.metasearch.databinding.ActivityGraphDisplayBinding;
import com.example.metasearch.utils.DatabaseUtils;
import com.example.metasearch.utils.GalleryImageManager;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class GraphDisplayActivity extends AppCompatActivity {
    private ActivityGraphDisplayBinding binding;
    private String imageName;
    private Uri imageUri = null;
    private List<ImageView> imageViews = new ArrayList<>(); // ImageView 객체 저장
    private static final int MAX_IMAGES = 10; // 최대 이미지 수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setWebView();
//        setRecyclerView(null);
    }
//    public void setRecyclerView(List<Uri> imageUris) {
//        // 갤러리의 모든 사진을 출력하는 세로 방향 RecyclerView 세팅
//        ImageAdapter adapter = new ImageAdapter(imageUris, this, null);
//        binding.recyclerView.setAdapter(adapter) ;
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//    }
    private void init() {
        binding = ActivityGraphDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Intent에서 이미지 URI 추출
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            imageName = GalleryImageManager.getFileNameFromUri(this, imageUri);
        } else {
            StyleableToast.makeText(this, "이미지를 불러올 수 없습니다.", R.style.customToast).show();
        }
    }
    public void setWebView() {
        binding.webView2.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // SSL 에러가 발생해도 계속 진행
                handler.proceed();
            }
        });
        binding.webView2.getSettings().setJavaScriptEnabled(true); //자바스크립트 실행을 허용. 이거 꼭 해줘야 화면 나타남
        binding.webView2.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        binding.webView2.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        Log.d("IMAGENAME", "http://10.0.2.2:8081/entityTripleGraph/" +
                DatabaseUtils.getPersistentDeviceDatabaseName(this) + "/" + imageName);
        // JavascriptInterface 추가
        binding.webView2.addJavascriptInterface(new WebAppInterface(this), "Android");

        binding.webView2.loadUrl("http://10.0.2.2:8081/entityTripleGraph/" +
                DatabaseUtils.getPersistentDeviceDatabaseName(this) + "/" + imageName);

    }
    private Handler handler = new Handler(Looper.getMainLooper());
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        // 자바스크립트에서 호출할 수 있는 메서드
        @JavascriptInterface
        public void receivePhotoName(final String photoName) {
            // 새 스레드에서 작업 실행
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 백그라운드 스레드에서 갤러리 이미지 이름들을 가져오기
                    final Uri matchedUri = findMatchedUri(photoName, mContext);
                    // 메인 스레드에서 UI 업데이트
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (matchedUri != null) {
                                Log.d("FUNCTION", "CALL func");
                                addImageToGallery(matchedUri);
                            } else {
                                @SuppressLint("ShowToast") Toast toast = Toast.makeText(mContext, "사진을 찾지 못했습니다", Toast.LENGTH_SHORT);
                                toast.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 500); // 1000ms = 1초 후에 실행
                            }
                        }
                    });
                }
            }).start();
        }
    }
    private List<Uri> existingUris = new ArrayList<>(); // 이미 추가된 이미지 URI 목록

    private void addImageToGallery(Uri uri) {
        if (existingUris.contains(uri)) {
            Toast.makeText(this, "이미 추가된 사진입니다.", Toast.LENGTH_SHORT).show();
            return; // 이미 목록에 있는 URI이면 추가하지 않고 종료
        }

        ImageView imageView = new ImageView(this);
        int sizeInPixels = dpToPx(300); // 이미지 크기를 픽셀 단위로 설정
        imageView.setLayoutParams(new LinearLayout.LayoutParams(sizeInPixels, sizeInPixels));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(uri);

        // 최대 이미지 수를 초과하면 가장 오래된 이미지 제거
        if (imageViews.size() >= MAX_IMAGES) {
            ImageView oldestView = imageViews.remove(imageViews.size() - 1);
            binding.imageContainer.removeView(oldestView);
            existingUris.remove(existingUris.size() - 1); // 가장 오래된 URI도 제거
        }

        // 새 이미지 및 URI 추가
        imageViews.add(0, imageView);
        binding.imageContainer.addView(imageView, 0);
        existingUris.add(0, uri); // 새 URI를 리스트의 맨 앞에 추가

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(requireContext(), GraphDisplayActivity.class);
//                intent.putExtra("imageUri", uri.toString());
//                startActivity(intent);
//            }
//        });
    }

//    private void setImageToPhotoView(Uri imageUri) {
//        // Glide를 사용하여 PhotoView에 이미지 설정
//        Glide.with(this)
//                .load(imageUri)
//                .into(binding.imageView2);
//    }
    // dp를 픽셀로 변환하는 유틸리티 메소드
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
