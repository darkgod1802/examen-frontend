package bo.edu.ucb.darkgod.examen.Servicios;

import java.util.ArrayList;
import java.util.List;

import bo.edu.ucb.darkgod.examen.Modelos.Anuncio;

public class UsuarioServicios {

    public List<Anuncio> obtenerAnuncios(String clave, String tipo, Boolean orden){

        List<Anuncio> lista= new ArrayList<>();
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-13","19:03",1,"2019-06-13 19:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","19:03",1,"2019-06-13 19:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","19:03",1,"2019-06-13 19:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","19:03",1,"2019-06-13 19:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","19:03",1,"2019-06-13 19:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-13","09:03",1,"2019-06-13 09:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-12","09:03",1,"2019-06-12 09:03:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","14:27",1,"2019-06-14 14:27:22"));
        lista.add(new Anuncio(1,"Reunion","Reunion sobre decretos","2019-06-14","14:27",1,"2019-06-14 14:27:22"));

        return lista;
    }
}
