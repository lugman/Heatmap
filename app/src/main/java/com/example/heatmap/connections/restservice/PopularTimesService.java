package com.example.heatmap.connections.restservice;

import android.util.Log;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PopularTimesService implements PopularTimesApi {
    private final String URL="https://us-central1-alpine-furnace-307816.cloudfunctions.net/";
    private PopularTimesApi populartimesApi;
    private static PopularTimesService instance;


    public PopularTimesService() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        populartimesApi = retrofit.create(PopularTimesApi.class);

    }
    public static PopularTimesService getInstance(){
        if (instance == null){
            instance = new PopularTimesService();
        }
        return instance;
    }
    /**
     * <h3>Realizará una invocación a get() de populartimes</h3>
     * <p>Se Llamará al metodo get() de populartimes con estos parametros </p>
     * @param parametersPT: Objeto de tipo  ParametersPT, este espera recibir:
     *  <ul>
     *  <li><strong>types:</strong> Uno o más valores de tipo lugar. Ejemplos Place types: ["bar","airport","park"]</li>
     *  <li><strong>p1:</strong> Array de tipo double con dos componentes. hace referencía a una tupla de phython en la librería populartimes. (48.132986, 11.566126) ->[48.132986, 11.566126] </li>
     *  <li><strong>p2:</strong> Valor similar al p1 con su valor corresponditne.
     *  <li><strong>n_threads:</strong> Número de hilos, por defecto 20.                  (OPCIONAL)</li>
     *  <li><strong>radio:</strong> Radio, por defecto 180.                              (OPCIONAL)</li>
     *  <li><strong>all_places:</strong> include/exclude places without populartimes.    (OPCIONAL)</li>
     *  </ul>
     */
    @Override
    public Call<ResponseBody> get(ParametersPT parametersPT) {
        if (parametersPT.p1.length != 2 || parametersPT.p2.length !=2){
                Log.e("ERROR","Argumento p1 o p2 incorrecto.");
                return null;
            }

        return populartimesApi.get(parametersPT);
    }
    /**
     * <h3>>Realizará una invocación aal metodo get_id() de  populartimes</h3>
     * @param parametersPT Objeto de tipo  ParametersPT, este espera recibir:
     *  <ul>
     *    <li><strong>place_id</strong> Espera un valor id de google place.</li>
     *  </ul>
     */
    @Override
    public Call<ResponseBody> get_id(ParametersPT parametersPT) {
        return populartimesApi.get_id(parametersPT);
    }
}