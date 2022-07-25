package com.daniel.appcontatoonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daniel.appcontatoonline.adapters.ContatosAdapters;
import com.daniel.appcontatoonline.modelo.Contato;
import com.daniel.appcontatoonline.retrofit.InicializadorRetrofit;
import com.daniel.appcontatosonline3m.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LIGACAO_CODE = 1;
    ListView lstContatos;
    List<Contato> listaContatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstContatos = findViewById(R.id.lstContatos);
        lstContatos.setOnCreateContextMenuListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AtualizaLista();
    }

    public void AtualizaLista() {
        InicializadorRetrofit retrofit = new InicializadorRetrofit();
        retrofit.getServicoContatos().TodosContatos().
                enqueue(new Callback<List<Contato>>() {
                    @Override
                    public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                        listaContatos = response.body();
                        ContatosAdapters adapter = new ContatosAdapters(MainActivity.this,
                                listaContatos
                        );
                        lstContatos.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Contato>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Não foi possivel conectar",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_sair)
        {
            this.finish();
        }

        if (item.getItemId() == R.id.menu_item_novo)
        {
            Intent intent = new Intent(this,FormContatoActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contatos,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contato contato = listaContatos.get(info.position);


        if (item.getItemId() == R.id.menu_item_mapa)//Menu item mapa
        {
            String endereco = contato.getEndereco();
            Uri uri = Uri.parse("geo:0,0?q=" + endereco + "&z=19");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_item_email) {
            Uri uri = Uri.parse("mailto:" + contato.getEmail());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.menu_item_telefonar) {
            Uri uri = Uri.parse("tel:" + contato.getTelefone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},LIGACAO_CODE);

            }
            else
            {
                startActivity(intent);
            }
        }

        if(item.getItemId() == R.id.menu_item_editar)
        {
            Intent intent = new Intent(this,FormContatoActivity.class );
            intent.putExtra("contato",contato);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.menu_item_excluir)
        {
            InicializadorRetrofit retrofit = new InicializadorRetrofit();
            retrofit.getServicoContatos().RemoveContato(contato.getId()).enqueue(
                    new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(MainActivity.this,"Contato Removido.",Toast.LENGTH_LONG).show();
                            AtualizaLista();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this,"Contato não pode ser removido.",Toast.LENGTH_LONG).show();
                        }
                    }
            );

        }
        return true;
    }
}