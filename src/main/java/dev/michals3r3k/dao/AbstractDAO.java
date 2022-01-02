package dev.michals3r3k.dao;

import dev.michals3r3k.json.reader.JSONReader;
import dev.michals3r3k.model.Saveable;

import java.util.List;

public class AbstractDAO<T extends Saveable<E>, E>
{
    private final JSONReader<T> reader;

    protected AbstractDAO(final JSONReader<T> reader)
    {
        this.reader = reader;
    }

    public List<T> getList()
    {
        return reader.read();
    }

}
