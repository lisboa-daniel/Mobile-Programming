package com.daniel.appcontatoonline.servicos;

import com.daniel.appcontatoonline.modelo.Contato;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServicoContatos {

    @GET("contatos")
    public Call<List<Contato>> TodosContatos();

    @POST("contatos")
    public Call<Void> CadastraContato(@Body Contato contato);

    @PUT("contatos/{id}")
    public Call<Void> AtualizaContato(@Path("id") String id, @Body Contato contato);

    @DELETE("contatos/{id}")
    public Call<Void> RemoveContato(@Path("id") String id);
}
