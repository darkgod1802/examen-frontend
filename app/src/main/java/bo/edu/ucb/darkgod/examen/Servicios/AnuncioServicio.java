package bo.edu.ucb.darkgod.examen.Servicios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;


public class AnuncioServicio {
    private final String URL = "http://192.168.100.243/api/anuncios/";
    private Context context;
    public AnuncioServicio(Context context){
        this.context=context;
    }
    public void crearAnuncio(final String titulo, final String descripcion, final String fecha,
                              final String hora, final Activity activity){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        Log.e("Des",titulo+","+descripcion+","+fecha+","+hora+1);
        StringRequest crear = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Crear","OK");
                        progressDialog.dismiss();
                        anuncio("Éxito","El anuncio fue publicado",activity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Log.d("Error.Response", "s"+error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("titulo", titulo);
                params.put("descripcion", descripcion);
                params.put("fecha", fecha);
                params.put("hora",hora);
                params.put("usuario_id","1");
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(crear);
    }
    public void elimnar(int id, final Activity activity){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        StringRequest eliminar = new StringRequest(Request.Method.DELETE, URL+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Elimnarr","OK");
                        progressDialog.dismiss();
                        anuncio("Eliminado","El anuncio fue eliminado",activity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Log.d("Error.Response", "s"+error.toString());
                    }
                }
        );
        Servicio.getInstance().addToRequestQueue(eliminar);
    }
    private void anuncio(String titulo, String mensaje, final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        });
        builder.show();
    }
}
