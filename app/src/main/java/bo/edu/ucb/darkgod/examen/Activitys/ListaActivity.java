package bo.edu.ucb.darkgod.examen.Activitys;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import bo.edu.ucb.darkgod.examen.Activitys.Adaptadores.ListaAdaptador;
import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;
import bo.edu.ucb.darkgod.examen.R;
import bo.edu.ucb.darkgod.examen.Servicios.ListaServicio;

public class ListaActivity extends AppCompatActivity{
    private String clave;
    private String tipo;
    private String orden;
    private int total_paginas;
    private int pagina_actual;

    // Referencias UI
    private RecyclerView rvLista;
    private Spinner spTipo;
    private FloatingActionButton btnAdd;
    private TextView btnAtras,btnAdelante, tvPagina;
    private ImageButton imgOrden;
    private List<Anuncio> lista;
    private ListaServicio servicios;
    private SearchView searchView;

    private ListaAdaptador adaptadorLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        //Inicializando valores
        clave="";
        tipo="id";
        orden="desc";
        pagina_actual=1;

        servicios=new ListaServicio(this);
        lista = new ArrayList<>();
        //Relacionando variable con UI
        rvLista =findViewById(R.id.recycler_view);
        btnAdd=findViewById(R.id.agregar);
        imgOrden=findViewById(R.id.imgOrdenar);
        btnAtras=findViewById(R.id.btnAtras);
        btnAdelante=findViewById(R.id.btnAdelante);
        tvPagina=findViewById(R.id.tvPagina);
        spTipo=findViewById(R.id.spTipo);

        adaptadorLista=new ListaAdaptador(this, lista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        rvLista.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLista.setAdapter(adaptadorLista);

        //Acciones
        spTipo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        tipo="id";
                        break;
                    case 1:
                        tipo="fecha";
                        break;
                    default:
                        tipo="titulo";
                        break;
                }
                pagina_actual=1;
                actualizar();
                Log.i("ListaActivity", position+","+tipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        imgOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orden.equals("asc")){
                    imgOrden.setBackgroundResource(R.drawable.orden_descendente);
                    orden="desc";
                }else{
                    imgOrden.setBackgroundResource(R.drawable.orden_ascendente);
                    orden="asc";
                }
                Log.i("ListaActivity", orden+"false es des");
                actualizar();
            }
        });

        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagina_actual=pagina_actual+1;
                actualizar();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagina_actual=pagina_actual-1;
                actualizar();
            }
        });
        actualizar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //Acciones del menu
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clave=query;
                actualizar();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                if(query.isEmpty()){
                    clave=query;
                    actualizar();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void actualizar(){
        servicios.obtenerAnuncios(pagina_actual,clave,tipo,orden,adaptadorLista,btnAtras,btnAdelante,tvPagina);
    }
}
