package com.zahiar.db;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.zahiar.AccountDetails;
import com.zahiar.DataStore;

import java.util.Optional;

public class DbActor extends AbstractLoggingActor
{

    private final DataStore dataStore;

    public DbActor(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(DbGetAccountMessage.class, query -> getAccount(query.getId()))
            .match(DbCreateAccountMessage.class, query -> createAccount(query.getAccountDetails()))
            .build();
    }

    private void getAccount(long id)
    {
        log().info("getAccount by id: " + id);

        final Optional<AccountDetails> accountDetails = dataStore.findAccount(id);
        sender().tell(new DbAccountResult(accountDetails), self());
    }

    private void createAccount(AccountDetails accountDetails)
    {
        log().info("createAccount for user: " + accountDetails.getFirstName() + ", " + accountDetails.getLastName());

        Optional<AccountDetails> result = dataStore.createAccount(accountDetails);
        sender().tell(new DbAccountResult(result), self());
    }

    public static Props props(DataStore dataStore)
    {
        return Props.create(DbActor.class, dataStore);
    }

}
