package com.zahiar.db;

import java.io.Serializable;

public class DbGetAccountMessage implements Serializable
{

    private final long id;

    public DbGetAccountMessage(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

}
