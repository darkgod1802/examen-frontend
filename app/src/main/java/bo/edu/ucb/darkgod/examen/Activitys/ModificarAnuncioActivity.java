package bo.edu.ucb.darkgod.examen.Activitys;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import bo.edu.ucb.darkgod.examen.Activitys.Componentes.EditTextClickeable;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;
import bo.edu.ucb.darkgod.examen.R;
import bo.edu.ucb.darkgod.examen.Servicios.AnuncioServicio;

public class ModificarAnuncioActivity extends AppCompatActivity {

    private EditText etFecha,etHora,etTitulo,etDescripcion;
    private TextInputLayout etFechaLayout, etHoraLayout,etTituloLayout,etDescripcionLayout;
    private Button btnModificar;
    private AnuncioServicio servicio;
    private final Calendar c = Calendar.getInstance();
    private int id;
    private EditText[] editTexts;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_anuncio);
        id=getIntent().getIntExtra("id",id);

        //Relacionando variable con UI
        etFecha =findViewById(R.id.etFecha);
        etHora =findViewById(R.id.etHora);
        etTitulo=findViewById(R.id.etTitulo);
        etDescripcion=findViewById(R.id.etDescripcion);
        etFechaLayout=findViewById(R.id.etFecha_layout);
        etHoraLayout=findViewById(R.id.etHora_layout);
        etTituloLayout=findViewById(R.id.etTitulo_layout);
        etDescripcionLayout=findViewById(R.id.etDescripcion_layout);
        btnModificar=findViewById(R.id.btnModificar);
        servicio=new AnuncioServicio(this,this);

        editTexts=new EditText[7];
        editTexts[0]=etTitulo;
        editTexts[1]=etDescripcion;
        editTexts[2]=etFecha;
        editTexts[3]=etHora;
        servicio.obtener(id,editTexts);

        Drawable img;
        img = this.getResources().getDrawable(R.drawable.calendar);
        img.setBounds(0, 0, 80, 80);
        etFecha.setCompoundDrawables(null, null, img, null);

        img = this.getResources().getDrawable(R.drawable.reloj);
        img.setBounds(0, 0, 80, 80);
        etHora.setCompoundDrawables(null, null, img, null);

        etFecha.setOnTouchListener(new EditTextClickeable(etFecha) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
                return onDate(etFecha,event);
            }
        });
        etHora.setOnTouchListener(new EditTextClickeable(etHora) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
                return onTime(etHora,event);
            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });

    }
    private void validarFormulario(){
        String titulo = etTitulo.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String fecha = etFecha.getText().toString();
        String hora = etHora.getText().toString();
        validarTitulo(titulo);
        validarDescripcion(descripcion);
        validarFecha(fecha);
        validarHora(hora);
        if(etDescripcionLayout.isErrorEnabled()||etTituloLayout.isErrorEnabled()||etFechaLayout.isErrorEnabled()||etHoraLayout.isErrorEnabled()){
            return;
        }
        String anio=fecha.substring(6);
        String mes=fecha.substring(3,5);
        String dia=fecha.substring(0,2);
        fecha=anio+"-"+mes+"-"+dia;
        Log.i("Crear Publicacion","Datos correctos"+fecha);
        Anuncio anuncio=new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setFecha(fecha);
        anuncio.setHora(hora);
        Log.i("ModificarTAG",anuncio.getTitulo());
        servicio.modificarAnuncio(id,anuncio,editTexts);
    }
    private boolean onDate(final View view, MotionEvent event) {
        // do something
        obtenerFecha();
        event.setAction(MotionEvent.ACTION_CANCEL);
        return false;
    }
    private boolean onTime(final View view, MotionEvent event) {
        // do something
        obtenerHora();
        event.setAction(MotionEvent.ACTION_CANCEL);
        return false;
    }
    private void obtenerFecha(){
        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);
        final String CERO = "0";
        final String BARRA = "/";
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                String fecha=diaFormateado + BARRA + mesFormateado + BARRA + year;
                etFecha.setText(fecha);
            }
        },anio, mes, dia);
        recogerFecha.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //Muestro el widget
        recogerFecha.show();
    }
    private void obtenerHora(){
        final String CERO = "0";
        final String DOS_PUNTOS = ":";
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                String hora=horaFormateada + DOS_PUNTOS + minutoFormateado;
                etHora.setText(hora);
            }
        }, hora, minuto, true);
        recogerHora.show();
    }
    private void validarTitulo(String titulo){
        if(titulo.trim().isEmpty()){
            etTituloLayout.setError(getString(R.string.error_campo_requerido));
        }else{
            etTituloLayout.setErrorEnabled(false);
        }
    }
    private void validarDescripcion(String descripcion){
        if(descripcion.trim().isEmpty()){
            etDescripcionLayout.setError(getString(R.string.error_campo_requerido));
        }else{
            etDescripcionLayout.setErrorEnabled(false);
        }
    }
    private void validarFecha(String fecha){
        if(fecha.trim().isEmpty()){
            etFechaLayout.setError(getString(R.string.error_campo_requerido));
        }else{
            if(!validarFormatoFecha(fecha)){
                etFechaLayout.setError(getString(R.string.error_fecha_formato_invalido));
            }else{
                etFechaLayout.setErrorEnabled(false);
            }
        }
    }
    private void validarHora(String hora){
        if(hora.trim().isEmpty()){
            etHoraLayout.setError(getString(R.string.error_campo_requerido));
        }else{
            if(!validarFormatoHora(hora)){
                etHoraLayout.setError(getString(R.string.error_hora_formato_invalido));
            }else{
                etHoraLayout.setErrorEnabled(false);
            }
        }
    }
    public boolean validarFormatoFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("ES"));
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    public boolean validarFormatoHora(String fecha) {
        try {
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", new Locale("ES"));
            formatoHora.setLenient(false);
            formatoHora.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
