package com.daniel.appcontatoonline.retrofit;

import com.daniel.appcontatoonline.servicos.ServicoContatos;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class InicializadorRetrofit {

    Retrofit retrofit;

    public InicializadorRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.cronny.somee.com/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ServicoContatos getServicoContatos()
    {
        return retrofit.create(ServicoContatos.class);
    }
}
