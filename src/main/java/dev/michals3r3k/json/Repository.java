package dev.michals3r3k.json;

public interface Repository<T, E>
{
    void saveOrUpdate(T toSave);
    void save(T toSave);
    void update(T toUpdate);
    void remove(T toRemove);
    void removeById(E id);
}
