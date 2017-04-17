package com.zahiar;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.zahiar.create.AccountCreateMessage;
import com.zahiar.db.DbGetAccountMessage;
import com.zahiar.db.DbAccountResult;
import scala.util.Try;

import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.pattern.PatternsCS.ask;

public class WebServer extends AllDirectives
{

    private final ActorSystem actorSystem;

    private final Http http;
    private final ActorMaterializer materializer;

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow;
    final CompletionStage<ServerBinding> binding;

    public WebServer(ActorSystem actorSystem, String host, int port) throws Exception
    {
        this.actorSystem = actorSystem;

        http = Http.get(actorSystem);
        materializer = ActorMaterializer.create(actorSystem);

        routeFlow = this.createRoute().flow(actorSystem, materializer);
        binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost(host, port), materializer);

        actorSystem.log().debug("Server online at http://" + host + ":" + port + "/");
        actorSystem.log().debug("Press any key to TERMINATE...");
        System.in.read();

        // Unbind from the port and then shutdown
        binding
            .thenCompose(ServerBinding::unbind)
            .thenAccept(unbound -> actorSystem.terminate());
    }

    private CompletionStage<DbAccountResult> getAccountHandler(long accountId)
    {
        return ask(
            actorSystem.actorSelection("/user/db"),
            new DbGetAccountMessage(accountId),
            500
        ).thenApply(DbAccountResult.class::cast);
    }

    private CompletionStage<DbAccountResult> createAccountHandler(AccountDetailsUnmashallerJson jsonData)
    {
        return ask(
            actorSystem.actorSelection("/user/accountcreate"),
            new AccountCreateMessage(jsonData.getFirstName(), jsonData.getLastName(), jsonData.getEmail()),
            500
        ).thenApply(DbAccountResult.class::cast);
    }

    private Route createRoute() {
        return route(
            get(() ->
                pathPrefix("account", () ->
                    path(longSegment(), (Long accountId) ->
                        onComplete(getAccountHandler(accountId), (Try<DbAccountResult> result) -> {
                            if (result.isFailure()) {
                                return complete(StatusCodes.SERVICE_UNAVAILABLE);
                            }

                            final DbAccountResult accountResult = result.get();
                            if (accountResult.accountDetails.isPresent()) {
                                return completeOK(accountResult.accountDetails.get(), Jackson.marshaller());
                            }

                            return complete(StatusCodes.NOT_FOUND);
                        })
                    )
                )
            ),

            post(() ->
                path("create-account", () ->
                    entity(Jackson.unmarshaller(AccountDetailsUnmashallerJson.class), accountCreate -> {
                        return onComplete(createAccountHandler(accountCreate), (Try<DbAccountResult> result) -> {
                            if (result.isFailure()) {
                                return complete(StatusCodes.SERVICE_UNAVAILABLE);
                            }

                            final DbAccountResult accountResult = result.get();
                            if (accountResult.accountDetails.isPresent()) {
                                return completeOK(accountResult.accountDetails.get(), Jackson.marshaller());
                            }

                            return complete(StatusCodes.NOT_FOUND);
                        });
                    })
                )
            )
        );
    }

}
