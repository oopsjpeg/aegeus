package com.aegeus.game.util;

import com.aegeus.game.entity.Spawner;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Created by Silvre on 7/4/2017.
 * Project: aegeus
 * If you are reading this - you can read this
 */
public class SpawnerSerializer implements JsonSerializer<Spawner> {
	@Override
	public JsonElement serialize(Spawner spawner, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject o = new JsonObject();
		o.addProperty("world", spawner.getLocation().getWorld().getName());
		o.addProperty("x", spawner.getLocation().getX());
		o.addProperty("y", spawner.getLocation().getY());
		o.addProperty("z", spawner.getLocation().getZ());
		o.addProperty("max", spawner.getMaxCount());
		JsonArray stats = new JsonArray();
		spawner.getList().stream().filter(Objects::nonNull).map(x -> {
			if (x.getParent() != null)
				return new JsonPrimitive(x.getClass().getCanonicalName()
						+ ":" + x.getParent().getClass().getCanonicalName());
			else
				return new JsonPrimitive(x.getClass().getCanonicalName());
		}).forEach(stats::add);
		o.add("list", stats);
		return o;
	}
}
