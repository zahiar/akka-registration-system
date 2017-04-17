package com.zahiar.create;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorSelection;
import akka.actor.Props;

import com.zahiar.db.DbCreateAccountMessage;

public class AccountCreateActor extends AbstractLoggingActor
{

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(AccountCreateMessage.class, s -> handleAccountCreate(s))
            .build();
    }

    private void handleAccountCreate(AccountCreateMessage message)
    {
        log().info("Message received for AccountCreate");

        ActorSelection db = getContext().actorSelection("/user/db");
        db.tell(new DbCreateAccountMessage(message), getSender());
    }

    public static Props props()
    {
        return Props.create(AccountCreateActor.class);
    }

}
