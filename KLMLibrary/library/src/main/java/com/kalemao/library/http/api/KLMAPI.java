package com.kalemao.library.http.api;


import com.kalemao.library.base.MResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by dim on 2017/5/19 14:03 邮箱：271756926@qq.com
 */

public interface KLMAPI {

    @GET("api/v2/home")
    Observable<MResponse> getHomeData();
}
