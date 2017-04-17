package com.zahiar;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataStore
{

    private static DataStore instance = null;

    private Map<Long, AccountDetails> inMemoryDataStore;
    private Long counter;

    public DataStore()
    {
        inMemoryDataStore = new HashMap<Long, AccountDetails>();

        // Just some initial dummy records
        inMemoryDataStore.put(1L, new AccountDetails(1L, "Charles", "Godfrey", "charles.godfrey@example.com"));
        inMemoryDataStore.put(2L, new AccountDetails(2L,"Arthur", "Wilson", "arthur.wilson@example.com"));

        counter = 3L;
    }

    public static DataStore getInstance()
    {
        if (instance == null) {
            instance = new DataStore();
        }

        return instance;
    }

    public Optional<AccountDetails> createAccount(AccountDetails accountDetails)
    {
        AccountDetails newAccount = new AccountDetails(counter, accountDetails.getFirstName(), accountDetails.getLastName(), accountDetails.getEmail());
        inMemoryDataStore.put(counter, newAccount);

        counter++;

        return Optional.of(newAccount);
    }

    public Optional<AccountDetails> findAccount(long id)
    {
        return Optional.ofNullable(inMemoryDataStore.get(id));
    }

}
