package com.zahiar;

import akka.actor.ActorSystem;

import com.zahiar.create.AccountCreateActor;
import com.zahiar.db.DbActor;

public class Application
{

    public static void main(String[] args) throws Exception
    {
        ActorSystem actorSystem = ActorSystem.create();

        // Register the Actors we have and give them names, so that we can reference them from other Actors
        actorSystem.actorOf(DbActor.props(DataStore.getInstance()), "db");
        actorSystem.actorOf(AccountCreateActor.props(), "accountcreate");

        WebServer webServer = new WebServer(actorSystem, "localhost", 8080);
    }

}
