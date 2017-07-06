package com.aegeus.game.stats;

import org.bukkit.Material;

import java.util.concurrent.ThreadLocalRandom;

public class StatsT1 extends Stats {
	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	@Override
	public void prepare() {
		setTier(1);
		setChance(0.1f);

		getDefaultArmor().hpRegen = new IntPossible(5, 10);
		getDefaultArmor().energyRegen = new FloatPossible(0.01f, 0.02f);
		getDefaultArmor().physRes = new FloatPossible(0.01f, 0.03f);
		getDefaultArmor().magRes = new FloatPossible(0.01f, 0.03f);
		getDefaultArmor().block = new FloatPossible(0.01f, 0.02f);
		getDefaultArmor().dodge = new FloatPossible(0.01f, 0.02f);

		ArmorPossible helmet = new ArmorPossible();
		helmet.material = Material.LEATHER_HELMET;
		helmet.hp = new IntPossible(9, 54);
		addHelmet(helmet);

		ArmorPossible chestplate = new ArmorPossible();
		chestplate.material = Material.LEATHER_CHESTPLATE;
		chestplate.hp = new IntPossible(17, 99);
		addChestplate(chestplate);

		ArmorPossible leggings = new ArmorPossible();
		leggings.material = Material.LEATHER_LEGGINGS;
		leggings.hp = new IntPossible(16, 96);
		addLeggings(leggings);

		ArmorPossible boots = new ArmorPossible();
		boots.material = Material.LEATHER_BOOTS;
		boots.hp = new IntPossible(9, 51);
		addBoots(boots);

		getDefaultWeapon().pen = new FloatPossible(0.01f, 0.05f);
		getDefaultWeapon().fireDmg = new IntPossible(1, 4);
		getDefaultWeapon().iceDmg = new IntPossible(1, 4);
		getDefaultWeapon().poisonDmg = new IntPossible(1, 4);
		getDefaultWeapon().pureDmg = new IntPossible(1, 4);
		getDefaultWeapon().lifeSteal = new FloatPossible(0.01f, 0.2f);
		getDefaultWeapon().trueHearts = new FloatPossible(0.01f, 0.03f);
		getDefaultWeapon().blind = new FloatPossible(0.01f, 0.25f);

		WeaponPossible sword = new WeaponPossible();
		sword.material = Material.WOOD_SWORD;
		sword.dmg = new IntPossible(1, 12);
		sword.range = new IntPossible(0, 5);
		addWeapon(sword);

		WeaponPossible axe = new WeaponPossible();
		axe.material = Material.WOOD_AXE;
		axe.dmg = new IntPossible(1, 13);
		axe.range = new IntPossible(0, 4);
		addWeapon(axe);
	}

	@Override
	public boolean hasChestplate() {
		return random.nextBoolean();
	}

	@Override
	public boolean hasLeggings() {
		return random.nextBoolean();
	}

	@Override
	public boolean hasBoots() {
		return random.nextBoolean();
	}
}
