package com.daniel.appcontatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daniel.appcontatos.dao.ContatosDao;
import com.daniel.appcontatos.modelo.Contato;

public class FormContatoActivity extends AppCompatActivity {

    ImageView imgFoto;
    EditText txtNome;
    EditText txtEmail;
    EditText txtTelefone;
    EditText txtEndereco;
    Button btnSalvar;
    Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contato);


        imgFoto = findViewById(R.id.imgFoto);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnSalvar = findViewById(R.id.btnSalvar);

        if (this.getIntent().getSerializableExtra("contato") != null)
        {
            contato = (Contato) this.getIntent().getSerializableExtra("contato");
            preencherFormContato();
        }

       btnSalvar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(contato == null)
               {
                   cadastraContato();
               }
               if (contato != null)
               {
                   AtualizaContato();
               }
           }
       });

    }

    private void AtualizaContato() {
        contato.setNome(txtNome.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());

        ContatosDao dao = new ContatosDao(this);

        dao.AtualizarContato(contato);
        Toast.makeText(this,"Atualizado com sucesso!", Toast.LENGTH_LONG).show();
        this.finish();

    }

    private void preencherFormContato() {
        txtNome.setText(contato.getNome());
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
    }

    private void cadastraContato() {
        contato = new Contato();
        contato.setNome(txtNome.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());

        ContatosDao dao = new ContatosDao(this);
        dao.CadastrarContato(contato);

        Toast.makeText(this,"Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
        this.finish();
    }
}