package dev.michals3r3k.json.converter;

import dev.michals3r3k.model.Saveable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class JSONConverter<T extends Saveable<E>, E> implements Converter<T, E, JSONObject, JSONArray>
{

}
