package me.sasukector.custom_enchantments;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CustomEnchantments extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CustomEnchants.register();

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new CustomEnchants(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("telepathy")) {
            if (!(sender instanceof Player))
                return true;
            Player player = (Player) sender;

            if (player.isOp()) {
                ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
                item.addUnsafeEnchantment(CustomEnchants.TELEPATHY, 1);
                item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD + "Telepathy");
                if (meta.hasLore())
                    for (String l : meta.getLore())
                        lore.add(l);
                meta.setLore(lore);
                item.setItemMeta(meta);

                player.getInventory().addItem(item);
            }

            return true;
        }

        if (label.equalsIgnoreCase("telepathy_book")) {
            if (!(sender instanceof Player))
                return true;
            Player player = (Player) sender;

            if (player.isOp()) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta = item.getItemMeta();
                EnchantmentStorageMeta emeta = (EnchantmentStorageMeta) item.getItemMeta();
                emeta.addStoredEnchant(CustomEnchants.TELEPATHY, 1, false);
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD + "Telepathy");
                meta.setLore(lore);
                item.setItemMeta(meta);

                player.getInventory().addItem(item);
            }

            return true;
        }

        if (label.equalsIgnoreCase("jumpback")) {
            if (!(sender instanceof Player))
                return true;
            Player player = (Player) sender;

            if (player.isOp()) {
                ItemStack item = new ItemStack(Material.BOW);
                item.addUnsafeEnchantment(CustomEnchants.JUMPBACK, 1);
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD + "JumpBack");
                if (meta.hasLore())
                    for (String l : meta.getLore())
                        lore.add(l);
                meta.setLore(lore);
                item.setItemMeta(meta);

                player.getInventory().addItem(item);

                item = new ItemStack(Material.ARROW);
                player.getInventory().addItem(item);
            }

            return true;
        }

        if (label.equalsIgnoreCase("jumpback_book")) {
            if (!(sender instanceof Player))
                return true;
            Player player = (Player) sender;

            if (player.isOp()) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta = item.getItemMeta();
                EnchantmentStorageMeta emeta = (EnchantmentStorageMeta) item.getItemMeta();
                emeta.addStoredEnchant(CustomEnchants.JUMPBACK, 1, false);
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD + "JumpBack");
                meta.setLore(lore);
                item.setItemMeta(meta);

                player.getInventory().addItem(item);
            }

            return true;
        }

        return true;
    }
}
