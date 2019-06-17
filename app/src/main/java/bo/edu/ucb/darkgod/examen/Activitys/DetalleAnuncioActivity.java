package bo.edu.ucb.darkgod.examen.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;
import bo.edu.ucb.darkgod.examen.R;
import bo.edu.ucb.darkgod.examen.Servicios.AnuncioServicio;
import bo.edu.ucb.darkgod.examen.Servicios.ListaServicio;

public class DetalleAnuncioActivity extends AppCompatActivity {

    private TextView tvTitulo,tvDescripcion,tvFecha,tvHora,tvAutor,tvCorreo,tvCreacion;
    private Button btnEditar,btnEliminar;
    private ListaServicio servicio;
    private AnuncioServicio anuncioServicio;
    private int id;
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

        TextView[] textViews=new TextView[7];
        textViews[0]=tvTitulo;
        textViews[1]=tvDescripcion;
        textViews[2]=tvFecha;
        textViews[3]=tvHora;
        textViews[4]=tvAutor;
        textViews[5]=tvCorreo;
        textViews[6]=tvCreacion;
        servicio=new ListaServicio(this);
        servicio.get(id,textViews);

        anuncioServicio=new AnuncioServicio(this);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
    }
    private void eliminar(){
        anuncioServicio.elimnar(id,this);
    }
}
