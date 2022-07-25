package com.daniel.appcontatos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.daniel.appcontatos.modelo.Contato;

import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContatosDao extends SQLiteOpenHelper {
    public ContatosDao(@Nullable Context context) {
        super(context, "BDContatos", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String cmd = "create table contatos(id varchar(50) primary key," +
                "nome varchar(50)," +
                "endereco varchar(50)," +
                "telefone varchar(50)," +
                "email varchar(50)," +
                "foto varchar(50)," +
                "lastupdate varchar(50)" +
                ");";
        sqLiteDatabase.execSQL(cmd);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String cmd = "drop table contatos;";
        sqLiteDatabase.execSQL(cmd);
        onCreate(sqLiteDatabase);

    }

    public List<Contato> TodosContatos()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] colunas = new String[]{
                "id",
                "nome",
                "endereco",
                "telefone",
                "email",
                "foto",
                "lastupdate",
        };
        List<Contato> listaContatos = new ArrayList<>();
        Cursor cursor = db.query("contatos",colunas,null,null,null,null,"nome asc",null);
        while(cursor.moveToNext())
        {
            Contato c = new Contato();
            c.setId(cursor.getString(cursor.getColumnIndex("id")));
            c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            c.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            c.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            c.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            c.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            c.setLastupdate(cursor.getString(cursor.getColumnIndex("lastupdate")));
            listaContatos.add(c);
        }
        return listaContatos;
    }

    public void CadastrarContato(Contato c)
    {
        c.setId(UUID.randomUUID().toString());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conteudo = new ContentValues();
        conteudo.put("id",c.getId());
        conteudo.put("nome",c.getNome());
        conteudo.put("endereco",c.getEndereco());
        conteudo.put("telefone",c.getTelefone());
        conteudo.put("email",c.getEmail());
        conteudo.put("foto",c.getFoto());
        conteudo.put("lastupdate",c.getLastupdate());

        db.insert("contatos",null,conteudo);

    }

    public void RemoverContato(Contato c)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{c.getId()};
        db.delete("contatos","id=?",arg);
    }

    public void AtualizarContato(Contato c)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conteudo = new ContentValues();
        conteudo.put("nome",c.getNome());
        conteudo.put("endereco",c.getEndereco());
        conteudo.put("telefone",c.getTelefone());
        conteudo.put("email",c.getEmail());
        conteudo.put("foto",c.getFoto());
        conteudo.put("lastupdate",c.getLastupdate());

        String[] arg = new String[]{c.getId()};

        db.update("contatos",conteudo,"id=?",arg);
    }

}
