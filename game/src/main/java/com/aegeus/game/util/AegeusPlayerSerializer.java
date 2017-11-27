package com.aegeus.game.util;

import com.aegeus.game.Aegeus;
import com.aegeus.game.entity.AgPlayer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AegeusPlayerSerializer implements JsonSerializer<AgPlayer> {
	@Override
	public JsonElement serialize(AgPlayer player, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject object = new JsonObject();
		object.addProperty("uuid", player.getPlayer().getUniqueId().toString());
		object.addProperty("level", player.getLevel());
		object.addProperty("xp", player.getXp());
		object.addProperty("soulpoints", player.getSoulpoints());
		object.addProperty("logins", player.getLogins()); //todo testing porpoises, remove
		object.add("compendium", Aegeus.GSON.toJsonTree(player.getCraftingCompendium()));
		return object;
	}
}