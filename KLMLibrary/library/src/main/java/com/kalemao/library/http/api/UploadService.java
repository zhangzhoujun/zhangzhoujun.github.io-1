package com.kalemao.library.http.api;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dim on 2017/6/27 19:05 邮箱：271756926@qq.com
 */

public interface UploadService {

    @Headers("Connection: close")
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFileInfo(@Url() String url, @QueryMap Map<String, String> options, @PartMap Map<String, RequestBody> externalFileParameters);

}
