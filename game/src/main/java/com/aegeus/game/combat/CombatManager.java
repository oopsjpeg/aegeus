package com.aegeus.game.combat;

import com.aegeus.game.Aegeus;
import com.aegeus.game.ability.Ability;
import com.aegeus.game.entity.AgEntity;
import com.aegeus.game.entity.AgMonster;
import com.aegeus.game.item.tool.Armor;
import com.aegeus.game.item.tool.Weapon;
import com.aegeus.game.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class CombatManager {
	private static final ThreadLocalRandom random = ThreadLocalRandom.current();

	public static CombatInfo get(LivingEntity victim, LivingEntity attacker, ItemStack tool) {
		CombatInfo cInfo = new CombatInfo(victim, attacker);
		AgEntity vInfo = Aegeus.getInstance().getEntity(victim);
		AgEntity aInfo = Aegeus.getInstance().getEntity(attacker);

		if (tool != null && !tool.getType().equals(Material.AIR) && new Weapon(tool).verify()) {
			Weapon weapon = new Weapon(tool);

			if (attacker instanceof Player) weaponDura((Player) attacker, weapon);
			if (victim instanceof Player) armorDura((Player) victim);

			cInfo.setPhysDmg(weapon.getDmg());

			if (weapon.getFireDmg() > 0) {
				cInfo.addMagDmg(weapon.getFireDmg());
				cInfo.addEffect(() -> victim.setFireTicks(38 + (weapon.getTier() * 7)));
			}

			if (weapon.getIceDmg() > 0) {
				cInfo.addMagDmg(weapon.getIceDmg());
				cInfo.addEffect(() -> victim.addPotionEffect(new PotionEffect(
						PotionEffectType.SLOW, 10 + (weapon.getTier() * 4), 2)));
				cInfo.addSound(cInfo.getTarget().getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
			}

			if (weapon.getPoisonDmg() > 0) {
				cInfo.addMagDmg(weapon.getPoisonDmg());
				cInfo.addEffect(() -> victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30 + (weapon.getTier() * 12), 1)));
				cInfo.addSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
			}
			if (weapon.getPureDmg() > 0) {
				int matches = (int) Arrays.stream(victim.getEquipment().getArmorContents())
						.filter(i -> i != null && !i.getType().equals(Material.AIR) && new Armor(i).verify())
						.map(Armor::new).count();
				cInfo.addPhysDmg(weapon.getPureDmg() * (matches / 4));
			}
			if (weapon.getTrueHearts() > 0 && random.nextFloat() <= weapon.getTrueHearts()) {
				cInfo.addPhysDmg(victim.getMaxHealth() * (0.01 * weapon.getTier()));
				if (victim instanceof Player)
					victim.sendMessage(Util.colorCodes("            &c&l*** OPPONENT TRUE HEARTS&c!&l ***"));
				if (attacker instanceof Player)
					attacker.sendMessage(Util.colorCodes("            &e&l*** TRUE HEARTS&e!&l ***"));
			}
			if (weapon.getBlind() > 0 && random.nextFloat() <= weapon.getBlind()) {
				cInfo.addEffect(() -> victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 12 + (weapon.getTier() * 6), 1)));
				if (victim instanceof Player)
					victim.sendMessage(Util.colorCodes("            &c&l*** OPPONENT BLINDNESS&c!&l ***"));
				if (attacker instanceof Player)
					attacker.sendMessage(Util.colorCodes("            &e&l*** BLINDNESS&e!&l ***"));
			}
			if (weapon.getLifeSteal() > 0) {
				cInfo.addHealing(cInfo.getPhysDmg() * weapon.getLifeSteal());
			}

			float critChance = aInfo.getCritChance() + (Util.isAxe(tool.getType()) ? 0.05f : 0);
			if (critChance > 0 && random.nextFloat() <= critChance) {
				cInfo.multPhysDmg(1.75);
				if (victim instanceof Player)
					victim.sendMessage(Util.colorCodes("            &c&l*** OPPONENT CRITICAL&c!&l ***"));
				if (attacker instanceof Player)
					attacker.sendMessage(Util.colorCodes("            &e&l*** CRITICAL&e!&l ***"));
			}

			cInfo.multPhysDmg(1 - Math.max(0, vInfo.getPhysRes() - weapon.getPen()));
			cInfo.multMagDmg(1 - Math.max(0, vInfo.getMagRes()));

			if (cInfo.getHealing() > 0) cInfo.getEffects().add(() -> Util.heal(attacker, cInfo.getHealing()));
		}

		if (vInfo.getReflect() > 0 && random.nextFloat() <= vInfo.getReflect()) {
			cInfo.setTarget(attacker);

			//cInfo.addParticle(Particle.BLOCK_DUST, victim.getLocation(),
			//		40, 0.25, 0.8, 0.25, 0.2, new MaterialData(Material.EMERALD_BLOCK));
			cInfo.addSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);

			if (attacker instanceof Player)
				attacker.sendMessage(Util.colorCodes("            &c&l*** OPPONENT REFLECTED&c!&l ***"));
			if (victim instanceof Player)
				victim.sendMessage(Util.colorCodes("            &e&l*** REFLECTED&e!&l ***"));
		} else if (vInfo.getDodge() > 0 && random.nextFloat() <= vInfo.getDodge()) {
			cInfo.setPhysDmg(0);
			cInfo.setMagDmg(0);

			//cInfo.addParticle(Particle.BLOCK_DUST, victim.getLocation(),
			//		40, 0.25, 0.8, 0.25, 0.15, new MaterialData(Material.STONE));
			cInfo.addSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);

			if (attacker instanceof Player)
				attacker.sendMessage(Util.colorCodes("            &c&l*** OPPONENT DODGED&c!&l ***"));
			if (victim instanceof Player)
				victim.sendMessage(Util.colorCodes("            &e&l*** DODGED&e!&l ***"));
		} else if (vInfo.getBlock() > 0 && random.nextFloat() <= vInfo.getBlock()) {
			cInfo.setPhysDmg(0);

			//cInfo.addParticle(Particle.BLOCK_DUST, victim.getLocation(),
			//		40, 0.25, 0.8, 0.25, 0.1, new MaterialData(Material.BEDROCK));
			cInfo.addSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);

			if (attacker instanceof Player)
				attacker.sendMessage(Util.colorCodes("            &c&l*** OPPONENT BLOCKED&c!&l ***"));
			if (victim instanceof Player)
				victim.sendMessage(Util.colorCodes("            &e&l*** BLOCKED&e!&l ***"));
		}

		return cInfo;
	}

	public static void weaponDura(Player player, Weapon weapon) {
		if (weapon.getMaxDura() > 0) {
			for (int i = 0; i < player.getInventory().getSize(); i++) {
				ItemStack item = player.getInventory().getItem(i);
				if (item != null && new Weapon(item).getTime().equals(weapon.getTime())) {
					weapon.subtractDura(1);
					if (weapon.getDura() <= 0)
						player.getInventory().setItem(i, new ItemStack(Material.AIR));
					else
						player.getInventory().setItem(i, weapon.build());
				}
			}
		}
	}

	public static void armorDura(Player player) {
		player.getInventory().setHelmet(armorDura(player.getInventory().getHelmet()));
		player.getInventory().setChestplate(armorDura(player.getInventory().getChestplate()));
		player.getInventory().setLeggings(armorDura(player.getInventory().getLeggings()));
		player.getInventory().setBoots(armorDura(player.getInventory().getBoots()));
	}

	public static ItemStack armorDura(ItemStack item) {
		if (item != null && new Armor(item).verify()) {
			Armor armor = new Armor(item);
			if (armor.getMaxDura() > 0) {
				armor.subtractDura(1);
				if (armor.getDura() <= 0)
					return new ItemStack(Material.AIR);
			}
			return armor.build();
		}

		return item;
	}

	public static void useAbility(AgMonster info) {
		if (!info.getAbils().isEmpty() && info.getActiveAbil() == null) {
			Ability ability = info.getAbils().get(random.nextInt(info.getAbils().size()));
			info.setActiveAbil(ability);
			info.getEntity().getWorld().playSound(info.getEntity().getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.5f + (info.getTier() * 0.05f), 2);
			info.getEntity().setCustomName(Util.colorCodes("&d&l" + ability.getName()));
			ability.activate(info);
		}
	}

	public static void updateName(LivingEntity entity) {
		AgEntity info = Aegeus.getInstance().getEntity(entity);

		ChatColor color = getHealthColor(entity);
		if (info instanceof AgMonster && ((AgMonster) info).getActiveAbil() != null)
			entity.setCustomName(Util.colorCodes(color + "&l** " + ((AgMonster) info).getActiveAbil().getName() + color + "&l **"));
		else
			entity.setCustomName(Util.colorCodes("&7- " + color + Math.round(entity.getHealth()) + " &lHP&7 -"));
	}

	public static ChatColor getHealthColor(LivingEntity entity) {
		if (entity.getHealth() <= entity.getMaxHealth() * 0.25)
			return ChatColor.RED;
		else if (entity.getHealth() <= entity.getMaxHealth() * 0.5)
			return ChatColor.GOLD;
		else if (entity.getHealth() <= entity.getMaxHealth() * 0.75)
			return ChatColor.YELLOW;
		else
			return ChatColor.GREEN;
	}
}