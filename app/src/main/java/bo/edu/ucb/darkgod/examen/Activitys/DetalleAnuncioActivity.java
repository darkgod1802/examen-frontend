package bo.edu.ucb.darkgod.examen.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bo.edu.ucb.darkgod.examen.R;
import bo.edu.ucb.darkgod.examen.Servicios.AnuncioServicio;

public class DetalleAnuncioActivity extends AppCompatActivity {

    private TextView tvTitulo,tvDescripcion,tvFecha,tvHora,tvAutor,tvCorreo,tvCreacion;
    private Button btnEditar,btnEliminar;
    private AnuncioServicio servicio;
    private int id;
    private TextView[] textViews;
    @Override
    protected void onPostResume() {
        super.onPostResume();
        servicio.obtenerDetalle(id,textViews);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_anuncio);

        id=getIntent().getIntExtra("id",id);
        Log.i("DetalleAnuncio",""+id);
        tvTitulo=findViewById(R.id.tvTitulo);
        tvDescripcion=findViewById(R.id.tvDescripcion);
        tvFecha=findViewById(R.id.tvFecha);
        tvHora=findViewById(R.id.tvHora);
        tvAutor=findViewById(R.id.tvAutor);
        tvCorreo=findViewById(R.id.tvCorreo);
        tvCreacion=findViewById(R.id.tvCreacion);
        btnEditar=findViewById(R.id.btnEditar);
        btnEliminar=findViewById(R.id.btnEliminar);

        textViews=new TextView[7];
        textViews[0]=tvTitulo;
        textViews[1]=tvDescripcion;
        textViews[2]=tvFecha;
        textViews[3]=tvHora;
        textViews[4]=tvAutor;
        textViews[5]=tvCorreo;
        textViews[6]=tvCreacion;
        servicio=new AnuncioServicio(this,this);
        servicio.obtenerDetalle(id,textViews);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ModificarAnuncioActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
    }
    private void eliminar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Esta seguro de eliminar esta publicación?")
                .setMessage("Una vez eliminada no podra volver a acceder al contenido.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                servicio.elimnarAnuncio(id);
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        builder.show();
    }
}
