package com.doubleclick.x_course.API;



import com.doubleclick.x_course.Notifications.MyResponse;
import com.doubleclick.x_course.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAyQbKXvM:APA91bGo30QMA1TAvhRnr3VwPTOOCWXWLUupX2qHY-OMEYOhmk5vNVoylWHBQxrSdZs9yRbnFujOKIKJyr-G_XC-X184w9D73iTzE9xEm3HARtIGLFClez2mGz-6KOnX_LSzLRbmkc0u"
    }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
