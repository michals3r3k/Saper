package dev.michals3r3k.factory;

import dev.michals3r3k.user.Save;

public interface Saveable
{
    void save(Save save);
    void update(Save save);
    void saveOrUpdate(Save save);
}
