package com.example.civilizedtribes.interfaces;


import com.example.civilizedtribes.objects.MainResponse;
import com.example.civilizedtribes.utils.NetworkConstant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {

    @GET(NetworkConstant.IMAGE_LINKS)
    Call<MainResponse> getImageResponse();
//    @GET(NetworkConstant.MISC_DATA)
//    Call <MiscApi> getMiscDataResponse();
}

