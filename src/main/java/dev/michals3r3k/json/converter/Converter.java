package dev.michals3r3k.json.converter;

import dev.michals3r3k.model.Saveable;

import java.util.List;

public interface Converter<T extends Saveable<I>, I, E, L extends List>
{
    E convert(T toConvert);
    L convert(List<T> listToConvert);
    E getById(L list, I id);
}
