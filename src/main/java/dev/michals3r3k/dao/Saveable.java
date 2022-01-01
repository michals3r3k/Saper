package dev.michals3r3k.dao;

import dev.michals3r3k.model.save.Save;

public interface Saveable
{
    void save(Save save);
    void update(Save save);
    void saveOrUpdate(Save save);
}
