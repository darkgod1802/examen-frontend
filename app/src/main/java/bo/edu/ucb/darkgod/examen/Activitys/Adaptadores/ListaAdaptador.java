package bo.edu.ucb.darkgod.examen.Activitys.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bo.edu.ucb.darkgod.examen.Activitys.DetalleAnuncioActivity;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;
import bo.edu.ucb.darkgod.examen.R;

public class ListaAdaptador extends RecyclerView.Adapter<ListaAdaptador.ListaViewHolder> {

    private Date inputDate;
    private Date outputDate;
    private String formattedDateString;
    private String prettyTimeString;
    private Context context;
    private List<Anuncio> anuncios;

    private static final String TXT_FECHA_EVENTO="Fecha del evento: ";
    public ListaAdaptador(Context context, List<Anuncio> anuncios){
        this.context = context;
        this.anuncios = anuncios;
    }
    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.elemento_lista, viewGroup, false);
        return new ListaViewHolder(listItem);
    }
    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder listaViewHolder, int position) {
        Anuncio anuncio = anuncios.get(position);
        String fechaEvento=TXT_FECHA_EVENTO+anuncio.getFecha();
        listaViewHolder.tvTitulo.setText(anuncio.getTitulo());
        listaViewHolder.tvDescripcion.setText(anuncio.getDescripcion());
        listaViewHolder.tvFechaEvento.setText(fechaEvento);
        listaViewHolder.tvIcon.setText(anuncios.get(position).getTitulo().substring(0, 1).toUpperCase());
        listaViewHolder.tvFechaPublicacion.setText(toDuration(anuncio.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitulo;
        private TextView tvDescripcion;
        private TextView tvFechaEvento;
        private TextView tvIcon;
        private TextView tvFechaPublicacion;

        public ListaViewHolder(View itemView){
            super(itemView);
            tvTitulo=itemView.findViewById(R.id.titulo);
            tvDescripcion=itemView.findViewById(R.id.descripcion);
            tvFechaEvento=itemView.findViewById(R.id.fecha_evento);
            tvIcon=itemView.findViewById(R.id.tvIcon);
            tvFechaPublicacion=itemView.findViewById(R.id.fecha);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ListaAdaptador","entre");
                    Context context = v.getContext();
                    int position = getAdapterPosition();
                    Anuncio anuncio = anuncios.get(position);
                    Intent intent = new Intent(v.getContext(), DetalleAnuncioActivity.class);
                    intent.putExtra("id",anuncio.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
    public void actualizar(List<Anuncio> anunciosNuevos){
        anuncios=anunciosNuevos;
        notifyDataSetChanged();
    }
    private String toDuration(String fecha) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",new Locale("ES"));
        try {
            inputDate=newDateFormat.parse(fecha);
            formattedDateString=newDateFormat.format(inputDate);
            outputDate=newDateFormat.parse(formattedDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime=new PrettyTime();
        prettyTimeString=prettyTime.format(outputDate);
        return prettyTimeString;
    }

}
