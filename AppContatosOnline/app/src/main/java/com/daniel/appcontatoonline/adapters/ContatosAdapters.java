package com.daniel.appcontatoonline.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.daniel.appcontatosonline3m.R;
import com.daniel.appcontatoonline.modelo.Contato;

public class ContatosAdapters extends BaseAdapter
{
    Context context;
    List<Contato> listaContatos;

    public ContatosAdapters(Context context, List<Contato> listaContatos) {
        this.context = context;
        this.listaContatos = listaContatos;
    }

    @Override
    public int getCount() {
        return listaContatos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaContatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(convertView!=null){
            view = convertView;
        }
        else {
            view = inflater.inflate(R.layout.linha_contato, parent, false);
        }
        Contato contato = listaContatos.get(position);

        TextView txtNome = view.findViewById(R.id.txtNome);
        TextView txtEmail = view.findViewById(R.id.txtEmail);
        ImageView imgFoto = view.findViewById(R.id.imgFoto);
        txtNome.setText(contato.getNome());
        txtEmail.setText(contato.getEmail());

        if(contato.getFoto()!=null && !contato.getFoto().equals("")) {

            byte[] ImagemBytes = Base64.decode(contato.getFoto(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(ImagemBytes,0,ImagemBytes.length);
            imgFoto.setImageBitmap(bitmap);
        }

        return view;
    }
}
