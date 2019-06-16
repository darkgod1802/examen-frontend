package bo.edu.ucb.darkgod.examen.Servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bo.edu.ucb.darkgod.examen.Activitys.Adaptadores.ListaAdaptador;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;

public class ListaServicio {
    private static final String URL = "http://192.168.100.27/api/anuncios";
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

    public void obtenerAnuncios(int pagina, String clave, String tipo, String orden, final ListaAdaptador adaptador, final TextView btnAtras, final TextView btnAdelante, final TextView tvPagina){
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
                    lista.clear();
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        lista.add(new Anuncio(object.getInt("id"),object.getString("titulo"),
                                object.getString("descripcion"),object.getString("fecha"),
                                object.getString("hora"),1,object.getString("created_at")));
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
}
