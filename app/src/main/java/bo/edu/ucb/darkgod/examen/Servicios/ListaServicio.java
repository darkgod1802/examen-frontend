package bo.edu.ucb.darkgod.examen.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bo.edu.ucb.darkgod.examen.Activitys.Adaptadores.ListaAdaptador;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;

public class ListaServicio {
    private final String URL = "http://192.168.100.243/api/anuncios/";
    private Context context;
    private List<Anuncio> lista;
    private int total_paginas;
    private int pagina_actual;
    public ListaServicio(Context context){
        this.context=context;
        lista= new ArrayList<>();
        total_paginas=1;
        pagina_actual=1;
    }

    public void obtenerAnuncios(int pagina, final String clave, final String tipo, final String orden, final ListaAdaptador adaptador, final TextView btnAtras, final TextView btnAdelante, final TextView tvPagina){
        pagina_actual=pagina;
        String query="?page="+pagina+"&tipo="+tipo+"&orden="+orden+"&clave="+clave;
        Log.i("Lista",query);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        final JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, URL+query,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pagina_actual=response.getInt("current_page");
                    total_paginas=response.getInt("last_page");
                    if(pagina_actual>total_paginas){
                        obtenerAnuncios(total_paginas,clave,tipo,orden,adaptador,btnAtras,btnAdelante,tvPagina);
                    }
                    lista.clear();
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        lista.add(new Anuncio(
                                object.getInt("id"),
                                object.getString("titulo"),
                                object.getString("descripcion"),
                                formato(object.getString("fecha")),
                                object.getString("created_at")));
                    }
                    Log.i("Lara","logrado");
                    adaptador.actualizar(lista);
                    if(pagina_actual==1)
                        btnAtras.setVisibility(View.GONE);
                    else
                        btnAtras.setVisibility(View.VISIBLE);

                    if(pagina_actual==total_paginas)
                        btnAdelante.setVisibility(View.GONE);
                    else
                        btnAdelante.setVisibility(View.VISIBLE);

                    String index="Página "+pagina_actual+" de "+total_paginas;
                    tvPagina.setText(index);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Lara",e.getMessage());
                    progressDialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Lara", "Error Respuesta en JSON: " + error.getMessage());
                        progressDialog.dismiss();
                    }
                });
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    public void get(int id, final TextView[] textViews){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, URL+id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject= response;
                Anuncio anuncio=new Anuncio();
                try {
                    anuncio=new Anuncio(jsonObject.getInt("id"),
                            jsonObject.getString("titulo"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("fecha"),
                            jsonObject.getString("hora"),
                            jsonObject.getString("created_at"),
                            jsonObject.getString("nombres"),
                            jsonObject.getString("apellidos"),
                            jsonObject.getString("correo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textViews[0].setText(anuncio.getTitulo());
                textViews[1].setText(anuncio.getDescripcion());
                String fecha="Fecha del evento: "+formato(anuncio.getFecha());
                textViews[2].setText(fecha);
                String hora=" a las "+anuncio.getHora().substring(0,5);
                textViews[3].setText(hora);
                String nombre="Publicado por: "+anuncio.getNombres()+" "+anuncio.getApellidos();
                textViews[4].setText(nombre);
                String correo="Correo: "+anuncio.getCorreo();
                textViews[5].setText(correo);
                String publicacion="Publicado el: "+anuncio.getCreated_at();
                textViews[6].setText(publicacion);
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Lara", "Error Respuesta en JSON: " + error.getMessage());
                        progressDialog.dismiss();
                    }
                });
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    private String formato(String fecha){
        String anio=fecha.substring(8);
        String mes=fecha.substring(5,7);
        String dia=fecha.substring(0,4);
        fecha=anio+"/"+mes+"/"+dia;
        return fecha;

    }
}
