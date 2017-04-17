package com.zahiar.db;

import com.zahiar.AccountDetails;

import java.io.Serializable;

public class DbCreateAccountMessage implements Serializable
{

    private final AccountDetails accountDetails;

    public DbCreateAccountMessage(AccountDetails accountDetails)
    {
        this.accountDetails = accountDetails;
    }

    public AccountDetails getAccountDetails()
    {
        return accountDetails;
    }

}
