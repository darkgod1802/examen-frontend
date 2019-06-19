package bo.edu.ucb.darkgod.examen.Servicios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bo.edu.ucb.darkgod.examen.Activitys.Adaptadores.ListaAdaptador;
import bo.edu.ucb.darkgod.examen.Activitys.InicioSesionActivity;
import bo.edu.ucb.darkgod.examen.Activitys.ListaAnunciosActivity;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;

public class AnuncioServicio {
    private final String IP="http://192.168.100.243/api/";
    private final String URL = IP+"anuncios/";
    private Context context;
    private List<Anuncio> lista;
    private int total_paginas;
    private int pagina_actual;
    private Activity activity;
    private Cifrado cifrado;
    public AnuncioServicio(Context context, Activity activity){
        this.context=context;
        lista= new ArrayList<>();
        total_paginas=1;
        pagina_actual=1;
        this.activity=activity;
        cifrado=new Cifrado();
    }
    public void iniciarSesison(final String email, final String contra){
        String url_inicio = IP+"inicio_sesion/";
        HashMap<String, String> params = new HashMap<>();
        params.put("correo", email);
        params.put("contra", contra);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        JsonObjectRequest login = new JsonObjectRequest(Request.Method.POST, url_inicio, new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Log","OK");
                        progressDialog.dismiss();
                        String token="";
                        try {
                            token=response.getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Cifrado cifrado=new Cifrado();
                        cifrado.guardarToken(token,context);
                        Intent intent = new Intent(context, ListaAnunciosActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncio("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);
                    }
                }
        );
        Servicio.getInstance().addToRequestQueue(login);
    }

    public void obtenerAnuncios(int pagina, final String clave, final String tipo, final String orden, final ListaAdaptador adaptador, final TextView btnAtras, final TextView btnAdelante, final TextView tvPagina){
        final String token="Bearer "+cifrado.obtenerToken(context);
        pagina_actual=pagina;
        String query="?page="+pagina+"&tipo="+tipo+"&orden="+orden+"&clave="+clave;
        Log.i("LISTADO",query);
        Log.i("TOKEN",token);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncioSalir("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Por favor a intentarlo mas tarde.");
                        else
                            manejarError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    public void obtenerDetalle(int id, final TextView[] textViews){
        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);
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
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncioSalir("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    public void obtener(int id, final EditText[] editTexts){
        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);
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
                editTexts[0].setText(anuncio.getTitulo());
                editTexts[1].setText(anuncio.getDescripcion());
                editTexts[2].setText(formato(anuncio.getFecha()));
                editTexts[3].setText(anuncio.getHora().substring(0,5));
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncioSalir("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    public void crearAnuncio(final Anuncio anuncio){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("titulo", anuncio.getTitulo());
        params.put("descripcion", anuncio.getDescripcion());
        params.put("fecha", anuncio.getFecha());
        params.put("hora",anuncio.getHora());
        params.put("usuario_id","1");

        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        JsonObjectRequest crear = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Crear","OK");
                        progressDialog.dismiss();
                        anuncioSalir("Éxito","El anuncio fue publicado");
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncio("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(crear);
    }
    public void modificarAnuncio(final int id, final Anuncio anuncio, final EditText[] editTexts){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("titulo", anuncio.getTitulo());
        params.put("descripcion", anuncio.getDescripcion());
        params.put("fecha", anuncio.getFecha());
        params.put("hora",anuncio.getHora());
        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        JsonObjectRequest crear = new JsonObjectRequest(Request.Method.PUT, URL+id,new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ModificarTAG","OK");
                        progressDialog.dismiss();
                        anuncioSalir("Modificación exitosa","El anuncio fue modificado");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncio("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);obtener(id,editTexts);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(crear);
    }
    public void elimnarAnuncio(int id){
        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);
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
                        anuncioSalir("Eliminado","El anuncio fue eliminado");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncio("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            manejarError(error);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(eliminar);
    }
    private void renovarToken(){
        String url = IP+"/anuncios/token/";
        final String token="Bearer "+cifrado.obtenerToken(context);
        Log.i("TOKENSEG",token);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Renovando Acceso...");
        progressDialog.show();
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String nToken="";
                JSONObject jsonObject= response;
                try {
                    String token=jsonObject.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Cifrado cifrado=new Cifrado();
                cifrado.borrarToken(context);
                cifrado.guardarToken(nToken,context);
                anuncio("Renovamos su acceso","Por favor vuelva a realizar la accion.");
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse == null)
                            anuncioSalir("Error al conectar","No se puede establecer conexión con " +
                                    "el servidor. Vuelva a intentarlo despues");
                        else
                            anuncioSesionExpirada("Sesión expirada","Por favor vuelva a iniciar sesión");
                    }
                }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        Servicio.getInstance().addToRequestQueue(jsObjectRequest);
    }
    private void anuncioSalir(String titulo, String mensaje){
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
    private void anuncio(String titulo, String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        builder.show();
    }
    private void anuncioSesionExpirada(String titulo, String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cifrado.borrarToken(context);
                                Intent intent = new Intent (context, InicioSesionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        });
        builder.show();
    }
    private String formato(String fecha){
        String anio=fecha.substring(8);
        String mes=fecha.substring(5,7);
        String dia=fecha.substring(0,4);
        fecha=anio+"/"+mes+"/"+dia;
        return fecha;
    }
    private void manejarError(VolleyError error){
        String mensaje="";
        final String TAG="E_CONEX_SERVE";
        NetworkResponse networkResponse = error.networkResponse;
        switch (networkResponse.statusCode){
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONObject errors = data.getJSONObject("error");
                    mensaje = errors.getString("mensaje");
                    Log.i("TOKEN",mensaje);
                } catch (JSONException e) {
                    Log.e(TAG,"Error al convertir"+e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG,"Error al convertir"+e.getMessage());
                }
                if(mensaje.equals("Token expirado")){
                    anuncioSesionExpirada("Sesión expirada","Por favor vuelva a iniciar sesión");
                }else{
                    anuncio("Acción denegada","Usted no esta autorizado para realizar esta acción");
                }
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                Log.e(TAG,"No encuentro el servicio");
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONObject errors = data.getJSONObject("error");
                    mensaje = errors.getString("mensaje");
                    Log.i("TOKEN",mensaje);
                } catch (JSONException e) {
                    Log.e(TAG,"Error al convertir "+e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Error al convertir"+e.getMessage());
                }
                anuncio("Datos incorrectos",mensaje);

                break;
            case 503:
                Log.e(TAG,"No encuentro el servicio");
                anuncio("Accion detenida","Problemas al conectar con la base de datos");
                break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONObject errors = data.getJSONObject("error");
                    mensaje = errors.getString("mensaje");
                    Log.i("TOKEN",mensaje);
                } catch (JSONException e) {
                    Log.i("TOKEN","no pude");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                anuncio("Datos incorrectos",mensaje);
                break;
        }
    }
}
