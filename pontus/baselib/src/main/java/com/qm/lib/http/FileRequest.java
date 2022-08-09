package com.qm.lib.http;

import com.qm.lib.entity.MPostResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-04
 * 更改时间：2019-07-04
 * 版本号：1
 */
public interface FileRequest {

    /**
     * 上传文件请求
     *
     * @param url      URL路径
     * @param paramMap 请求参数
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> postFile(@Url String url, @PartMap Map<String, RequestBody> paramMap);

    /**
     * 上传一张图片
     *
     * @return
     */
    @Multipart
    @POST
    Call<MPostResponse> postFile(@Url String url,
                                 @Part("success_action_status") RequestBody success_action_status,
                                 @Part("OSSAccessKeyId") RequestBody OSSAccessKeyId,
                                 @Part("policy") RequestBody policy,
                                 @Part("Signature") RequestBody Signature,
                                 @Part("key") RequestBody key,
                                 @Part MultipartBody.Part imgs);
}
