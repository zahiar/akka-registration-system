package com.zahiar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDetailsUnmashallerJson extends AccountDetails
{

    public AccountDetailsUnmashallerJson(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email)
    {
        super(firstName, lastName, email);
    }

}
