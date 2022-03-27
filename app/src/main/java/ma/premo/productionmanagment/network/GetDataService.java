package ma.premo.productionmanagment.network;
import  ma.premo.productionmanagment.models.Notification_Hours;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface GetDataService {

    @GET("notification_heures/getAll")
    Call<List<Notification_Hours>> getAllNotification() ;
}
