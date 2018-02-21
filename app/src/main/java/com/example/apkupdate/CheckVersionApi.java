package com.example.apkupdate;
;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Shadow on 2017/11/15.
 */

public interface CheckVersionApi {
    @FormUrlEncoded
    @POST("Version/checkVersion")
    Observable<VersionBean> checkVersion(@Field("version") int version);

}
