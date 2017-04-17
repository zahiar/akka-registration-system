package com.zahiar.db;

import akka.actor.*;

import static akka.actor.SupervisorStrategy.restart;

import akka.japi.pf.DeciderBuilder;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.zahiar.DataStore;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

public class DbSupervisor extends AbstractLoggingActor
{

    private Router router;

    public DbSupervisor()
    {
        // Run on separate dispatcher due to its blocking nature
        final Props connectionProps = DbActor.props(DataStore.getInstance())
            .withDispatcher("akkasample.blocking-dispatcher");

        // Create a router with 5 actors, each with a separate database connection
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(connectionProps);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .matchAny(request -> router.route(request, sender()))
            .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy()
    {
        return new OneForOneStrategy(
            10,
            Duration.create("1 minute"),
            DeciderBuilder
                .match(RuntimeException.class, e -> restart())
                .build()
        );
    }

    public static Props props()
    {
        return Props.create(DbSupervisor.class);
    }

}
