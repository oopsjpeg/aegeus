package com.aegeus.game.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aegeus.game.util.Helper;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagFloat;
import net.minecraft.server.v1_10_R1.NBTTagInt;

public class Armor extends Item {
	
	private int tier = 0;
	
	private int hp = 0;
	private int hpRegen = 0;
	private float energyRegen = 0;
	
	private int level = 0;
	private int xp = 0;
	
	private Rarity rarity = Rarity.NONE;
	//private List<ArmorRune> runes = new ArrayList<>();
	
	public Armor(Material material) {
		super(material);
	}
	
	public Armor(ItemStack item){
		super(item);
		NBTTagCompound aegeus = Item.getAegeusInfo(item);
		if(aegeus != null){
			hp = aegeus.getInt("hp");
			hpRegen = (aegeus.hasKey("hpRegen")) ? aegeus.getInt("hpRegen") : 0;
			tier = aegeus.getInt("tier");
			level = (aegeus.hasKey("level")) ? aegeus.getInt("level") : 0;
			xp = (aegeus.hasKey("xp")) ? aegeus.getInt("xp") : 0;
			rarity = (aegeus.hasKey("rarity")) ? Rarity.getById(aegeus.getInt("rarity")) : Rarity.NONE;
		}
	}
	
	public void setHp(int hp){
		this.hp = hp;
	}
	
	public int getHp(){
		return hp;
	}
	
	public void setHpRegen(int hpRegen){
		this.hpRegen = hpRegen;
	}
	
	public int getHpRegen(){
		return hpRegen;
	}
	
	public void setTier(int tier){
		this.tier = tier;
	}
	
	public void setRarity(Rarity rarity){
		this.rarity = rarity;
	}
	
	//public List<ArmorRune> getRunes() {
	//	return runes;
	//}
	
	public List<String> getLore(){
		List<String> lore = new ArrayList<>();
		lore.add(Helper.colorCodes("&cHP: +" + hp));
		if(hpRegen > 0) lore.add(Helper.colorCodes("&cHP REGEN: +" + hpRegen + "/s"));
		if(energyRegen > 0) lore.add(Helper.colorCodes(("&cENERGY REGEN: +" + Math.round(energyRegen * 100) + "%")));
		if(level >= 1) {
			int maxXp = Math.round(Helper.calcMaxXP(level));
			lore.add(Helper.colorCodes("&6&oLevel " + level + " &7&o(" + Math.round((xp / maxXp) * 100) + "%)"));
		}
		if(rarity != null && !rarity.getLore().equalsIgnoreCase(""))
			lore.add(Helper.colorCodes(rarity.getLore()));
		return lore;
	}
	
	@Override
	public ItemStack build(){
		ItemStack item = new ItemStack(material);
		net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagCompound aegeus = new NBTTagCompound();
		aegeus.set("tier", new NBTTagInt(tier));
		aegeus.set("hp", new NBTTagInt(hp));
		aegeus.set("hpRegen", new NBTTagInt(hpRegen));
		aegeus.set("energyRegen", new NBTTagFloat(energyRegen));
		aegeus.set("level", new NBTTagInt(level));
		aegeus.set("xp", new NBTTagInt(xp));
		aegeus.set("rarity", new NBTTagInt(rarity.getId()));
		compound.set("AegeusInfo", aegeus);
		nmsStack.setTag(compound);
		item = CraftItemStack.asBukkitCopy(nmsStack);
		lore = getLore();
		ItemMeta meta = item.getItemMeta();
		if(name!=null) meta.setDisplayName(name);
		if(lore!=null) meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
}