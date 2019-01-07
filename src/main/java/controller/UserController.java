package controller;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class UserController {

    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>this is the / end point</h1>");
        });


    }
}
