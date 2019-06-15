package bo.edu.ucb.darkgod.examen;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ListaViewHolder> {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int MOUNTH_MILLIS = 30 * HOUR_MILLIS;

    Date inputDate;
    Date outputDate;
    String formattedDateString;
    String prettyTimeString;

    private List<Anuncio> anuncios;
    private static final String TXT_FECHA_EVENTO="Fecha del evento: ";
    public AdaptadorLista(List<Anuncio> anuncios){
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
        listaViewHolder.tvIcon.setText(anuncios.get(position).getTitulo().substring(0, 1));
        listaViewHolder.tvFechaPublicacion.setText(toDuration(anuncio.getCreated_at()));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256), rnd.nextInt(256));
        ((GradientDrawable) listaViewHolder.tvIcon.getBackground()).setColor(color);
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
        }
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
