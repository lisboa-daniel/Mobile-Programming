package com.daniel.appcontatoonline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daniel.appcontatoonline.modelo.Contato;
import com.daniel.appcontatoonline.retrofit.InicializadorRetrofit;
import com.daniel.appcontatosonline3m.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FormContatoActivity extends AppCompatActivity {

    Contato contato;

    ImageView imgFoto;
    EditText txtNome;
    EditText txtTelefone;
    EditText txtEmail;
    EditText txtEndereco;
    Button btSalvar;
    Bitmap foto = null;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contato);

        imgFoto = findViewById(R.id.imgFoto);
        txtNome = findViewById(R.id.txtNome);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtEmail = findViewById(R.id.txtEmail);
        txtEndereco = findViewById(R.id.txtEndereco);
        btSalvar = findViewById(R.id.btSalvar);

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });

        if(this.getIntent().getSerializableExtra("contato")!=null)
        {
            contato =(Contato) this.getIntent().getSerializableExtra("contato");
            preencherFormContato();
        }



        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contato==null)
                {
                    cadastraContato();
                }
                else
                {
                    AtualizaContato();
                }
            }
        });
}
    /*fim do on create*/

    private void tirarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            Bundle extra = data.getExtras();
            foto = (Bitmap) extra.get("data");
            imgFoto.setImageBitmap(foto);
        }

    }

    private void AtualizaContato() {
        contato.setNome(txtNome.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        if(foto!=null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.JPEG,92,stream);
            String imageString = Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
            contato.setFoto(imageString);
        }

        InicializadorRetrofit retrofit =  new InicializadorRetrofit();
        retrofit.getServicoContatos().AtualizaContato(contato.getId(),contato).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(FormContatoActivity.this,"Contato Atualizado!",Toast.LENGTH_LONG).show();
                FormContatoActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FormContatoActivity.this,"Contato não foi atualizado",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherFormContato() {
        txtNome.setText(contato.getNome());
        txtTelefone.setText(contato.getTelefone());
        txtEndereco.setText(contato.getEndereco());
        txtEmail.setText(contato.getEmail());

        if(contato.getFoto()!=null && !contato.getFoto().equals("")) {

            byte[] ImagemBytes = Base64.decode(contato.getFoto(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(ImagemBytes,0,ImagemBytes.length);
            imgFoto.setImageBitmap(bitmap);
        }


    }

    private void cadastraContato() {
        contato = new Contato();
        contato.setId(UUID.randomUUID().toString());
        contato.setNome(txtNome.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());

        if(foto!=null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.JPEG,92,stream);
            String imageString = Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
            contato.setFoto(imageString);
        }

        InicializadorRetrofit retrofit = new InicializadorRetrofit();
        retrofit.getServicoContatos().CadastraContato(contato).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(FormContatoActivity.this,"Contato Cadastrado!",Toast.LENGTH_LONG).show();
                        FormContatoActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        contato = null;
                        Toast.makeText(FormContatoActivity.this,"Contato não Cadastrado.",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


}