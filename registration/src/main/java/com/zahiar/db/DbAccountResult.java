package com.zahiar.db;

import com.zahiar.AccountDetails;

import java.util.Optional;

public class DbAccountResult
{

    public final Optional<AccountDetails> accountDetails;

    public DbAccountResult(Optional<AccountDetails> accountDetails)
    {
        this.accountDetails = accountDetails;
    }

}
