package edu.pucmm.eitc.controlador;

import io.javalin.Javalin;
import edu.pucmm.eitc.encapsulaciones.Usuario;
import edu.pucmm.eitc.util.BaseControlador;
import edu.pucmm.eitc.services.FakeServices;

public class auth extends BaseControlador {

    public auth (Javalin app){
        super(app);
    }

    public void aplicarRutas(){

        app.post("/autenticar", ctx -> {
            //Obteniendo la informacion de la petion. Pendiente validar los parametros.
            String nombreUsuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");
            //Autenticando el usuario para nuestro ejemplo siempre da una respuesta correcta.
            Usuario usuario = FakeServices.getInstancia().autheticarUsuario(nombreUsuario, password);
            //agregando el usuario en la session... se puede validar si existe para solicitar el cambio.-
            ctx.sessionAttribute("usuario", usuario);
            //redireccionando la vista con autorizacion.
            ctx.redirect("/AsigAula1");
        });

    }



}
