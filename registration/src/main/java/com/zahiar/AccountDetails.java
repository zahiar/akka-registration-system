package com.zahiar;

public class AccountDetails
{

    private long id;
    private String firstName, lastName, email;

    public AccountDetails(String firstName, String lastName, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public AccountDetails(long id, String firstName, String lastName, String email)
    {
        this(firstName, lastName, email);

        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

}
