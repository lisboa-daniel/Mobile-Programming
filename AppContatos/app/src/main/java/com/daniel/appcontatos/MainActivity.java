package com.daniel.appcontatos;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daniel.appcontatos.dao.ContatosDao;
import com.daniel.appcontatos.modelo.Contato;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int LIGACAO_CODE = 345;
    ListView lstContatos;
    List<Contato> MeusContatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MeusContatos = new ArrayList<Contato>();

        lstContatos = findViewById(R.id.lstContatos);

        AtualizarLista();
        lstContatos.setOnCreateContextMenuListener(this);
    }

    private void AtualizarLista() {
        ContatosDao dao = new ContatosDao(this);
        MeusContatos = dao.TodosContatos();

        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this,android.R.layout.simple_list_item_1,

                MeusContatos
                );

        lstContatos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contato,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Contato contato = MeusContatos.get(info.position);

        if(item.getItemId() == R.id.menu_item_mapa)
        {
            String endereco = contato.getEndereco();
            Uri uri = Uri.parse("geo:0,0?q="+endereco+"&z=23");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.menu_item_email)
        {
            Uri uri = Uri.parse("mailto:"+contato.getEmail());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.menu_item_excluir)
        {
            ContatosDao dao = new ContatosDao(this);
            dao.RemoverContato(contato);
            AtualizarLista();
            Toast.makeText(this,"Contato Removido",Toast.LENGTH_LONG).show();
        }

        if(item.getItemId() == R.id.menu_item_editar)
        {
            Intent intent = new Intent(this,FormContatoActivity.class);
            intent.putExtra("contato",contato);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.menu_item_telefonar)
        {
            Uri uri = Uri.parse("tel:"+contato.getTelefone());
            Intent intent = new Intent(Intent.ACTION_CALL,uri);

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, LIGACAO_CODE);
            }
            else
            {
                startActivity(intent);
            }

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal,menu);

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
            //nova janela
            Intent intent = new Intent(this,FormContatoActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AtualizarLista();
    }


}