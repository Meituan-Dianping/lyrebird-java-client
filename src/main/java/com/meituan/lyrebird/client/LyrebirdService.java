package com.meituan.lyrebird.client;

import com.meituan.lyrebird.client.api.*;

import com.meituan.lyrebird.client.api.bandwidth.BandwidthTemplate;
import com.meituan.lyrebird.client.api.bandwidth.SpeedLimit;
import retrofit2.Call;
import retrofit2.http.*;

public interface LyrebirdService {
    @GET("api/status")
    Call<Status> status();

    @PUT("api/mock/{group}/activate")
    Call<BaseResponse> activate(@Path("group") String groupID);

    @PUT("api/mock/groups/deactivate")
    Call<BaseResponse> deactivate();

    @GET("api/flow")
    Call<Flow[]> getFlowList();

    @GET("api/flow/{flowId}")
    Call<FlowDetail> getFlowDetail(@Path("flowId") String flowId);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE",path = "api/flow",hasBody = true)
    Call<BaseResponse> clearFlowList(@Body Flow flow);

    @GET("api/event/{channel}")
    Call<Events> getEventList(@Path("channel") String channel);

    @GET("api/data/{dataId}")
    Call<LBMockData> getMockData(@Path("dataId") String dataId);

    @Headers("Content-Type: application/json")
    @PUT("api/bandwidth")
    Call<SpeedLimit> setSpeedLimit(@Body BandwidthTemplate template);

    @GET("api/bandwidth")
    Call<SpeedLimit> getSpeedLimit();
}
