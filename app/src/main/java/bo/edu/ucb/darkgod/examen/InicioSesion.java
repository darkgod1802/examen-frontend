package bo.edu.ucb.darkgod.examen;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InicioSesion extends AppCompatActivity {

    // Referencias UI
    private EditText inCorreo, inContra;
    private TextInputLayout inLayoutCorreo, inLayoutContra;
    private Button iniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        //Relacionando variable con UI
        inCorreo = findViewById(R.id.correo);
        inContra = findViewById(R.id.contraseña);
        inLayoutCorreo= findViewById(R.id.correo_layout);
        inLayoutContra = findViewById(R.id.contraseña_layout);
        iniciarSesion = findViewById(R.id.iniciar_sesion);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
                Intent intent = new Intent (v.getContext(), Lista.class);
                startActivityForResult(intent, 0);
            }
        });

    }
    private void validarFormulario(){
        String correo = inCorreo.getText().toString();
        String contraseña = inContra.getText().toString();
        validarCorreo(correo);
        validarContra(contraseña);
        if(inLayoutCorreo.isErrorEnabled()|| inLayoutContra.isErrorEnabled()){
            return;
        }
        Log.i("Form login","Datos correctos");
    }
    private void validarCorreo(String correo){
        if(correo.trim().isEmpty()){
            Log.i("Form login","correo: "+correo);
            inLayoutCorreo.setError(getString(R.string.error_campo_requerido));
        }else{
            if(!correo.contains("@"))
            {
                inLayoutCorreo.setError(getString(R.string.error_correo_invalido));
            }else{
                inLayoutCorreo.setErrorEnabled(false);
            }
        }
    }
    private void validarContra(String contra){
        if(contra.trim().isEmpty()){
            inLayoutContra.setError(getString(R.string.error_campo_requerido));
        }else{
            if(contra.length()<5)
            {
                inLayoutContra.setError(getString(R.string.error_contra_invalida));
            }else{
                inLayoutContra.setErrorEnabled(false);
            }
        }
    }
}
