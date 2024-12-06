package com.example.unimpdemo.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    private Context context;
    private final String TAG = "DownloadUtil";

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void download(Context context, final String url, final String saveDir, final String fileName, final OnDownloadListener listener) {
        this.context = context;

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Download failed: " + e.getMessage());
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    listener.onDownloadFailed();
                    return;
                }

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;

                String savePath = getDownloadDir(saveDir);
                Log.d(TAG, "Download directory: " + savePath);

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();

                    File file = new File(savePath, fileName);

                    // Kiểm tra và xóa file nếu đã tồn tại
                    if (file.exists()) {
                        Log.w(TAG, "File already exists, deleting: " + file.getAbsolutePath());
                        if (!file.delete()) {
                            Log.e(TAG, "Failed to delete existing file: " + file.getAbsolutePath());
                            listener.onDownloadFailed();
                            return;
                        }
                    }

                    fos = new FileOutputStream(file);
                    long sum = 0;

                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        listener.onDownloading(progress);
                    }

                    fos.flush();
                    listener.onDownloadSuccess(file);

                } catch (Exception e) {
                    Log.e(TAG, "Error during download: " + e.getMessage(), e);
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException ignored) {
                    }
                }
            }

        });
    }

    private String getDownloadDir(String saveDir) throws IOException {
        File downloadFile = new File(context.getExternalFilesDir(null), saveDir);
        if (!downloadFile.exists() && !downloadFile.mkdirs()) {
            Log.e(TAG, "Failed to create directory: " + downloadFile.getAbsolutePath());
            throw new IOException("Failed to create directory");
        }
        return downloadFile.getAbsolutePath();
    }

    @NonNull
    public String getNameFromUrl(String url) {
        return url;
    }

    public interface OnDownloadListener {
        void onDownloadSuccess(File file);

        void onDownloading(int progress);

        void onDownloadFailed();
    }
}
