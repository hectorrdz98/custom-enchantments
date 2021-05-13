package me.sasukector.custom_enchantments;

import me.sasukector.custom_enchantments.enchants.Telepathy;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomEnchants implements Listener {

    public static final Enchantment TELEPATHY = new Telepathy("telepathy");
    public List<Enchantment> customEnchantments = new ArrayList<>();

    public CustomEnchants() {
        this.customEnchantments.add(TELEPATHY);
    }

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(TELEPATHY);
        if (!registered)
            registerEnchantment(TELEPATHY);
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            // Send message to console
        }
    }

    // Get a list with the custom enchantments of an item
    public List<Enchantment> getCustomEnchantments(ItemStack item) {
        List<Enchantment> itemCustomEnc = new ArrayList<Enchantment>();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            for (String lore : meta.getLore()) {
                for (Enchantment actEnc : this.customEnchantments) {
                    if (lore.equals(ChatColor.GOLD + actEnc.getName())) {
                        itemCustomEnc.add(actEnc);
                    }
                }
            }
        }
        return itemCustomEnc;
    }

    // Get a list with all the extra lore of an item
    public List<String> getLoreWithNoCustomEnchantments(List<String> originalLore, List<Enchantment> originalCustomEnc) {
        List<String> newLore = new ArrayList<>();
        for (String lore : originalLore) {
            boolean hasCustomEnc = false;
            for (Enchantment actEnc : originalCustomEnc) {
                if (lore.equals(ChatColor.GOLD + actEnc.getName())) {
                    hasCustomEnc = true;
                    break;
                }
            }
            if (!hasCustomEnc)
                newLore.add(lore);
        }
        return newLore;
    }

    @EventHandler()
    public void onAnvilUse(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack firstItem = anvil.getFirstItem();
        ItemStack secondItem = anvil.getSecondItem();
        ItemStack resultItem = anvil.getResult() != null ?  anvil.getResult().clone() : null;

        if (firstItem == null || secondItem == null)
            return;

        // Get the custom enchantments of the items
        List<Enchantment> firstItemCustomEnc = getCustomEnchantments(firstItem);
        List<Enchantment> secondItemCustomEnc = getCustomEnchantments(secondItem);
        List<String> firstExtraLore = new ArrayList<>();

        // Get the extra lore just for the first item
        if (firstItem.hasItemMeta() && firstItem.getItemMeta().hasLore()) {
            firstExtraLore = getLoreWithNoCustomEnchantments(firstItem.getItemMeta().getLore(), firstItemCustomEnc);
        }

        // If it's a valid enchantment
        if (resultItem != null) {
            // Apply custom enchantments to result item
            if (resultItem.hasItemMeta()) {
                ItemMeta resultMeta = resultItem.getItemMeta();
                List<String> resultLore = new ArrayList<String>();

                // Add custom enchantments lore to item
                for (Enchantment firstEnc : firstItemCustomEnc) {
                    if (secondItemCustomEnc.contains(firstEnc)) {
                        // If mixing same custom enchantment
                    } else {
                        resultLore.add(ChatColor.GOLD + firstEnc.getName());
                    }
                }
                for (Enchantment secondEnc : secondItemCustomEnc) {
                    if (!firstItemCustomEnc.contains(secondEnc)) {
                        resultLore.add(ChatColor.GOLD + secondEnc.getName());
                    }
                }

                // Add extra lore to item
                for (String l : firstExtraLore)
                    resultLore.add(l);
                resultMeta.setLore(resultLore);
                resultItem.setItemMeta(resultMeta);

                // Add custom enchantments to item
                for (Enchantment firstEnc : firstItemCustomEnc) {
                    if (secondItemCustomEnc.contains(firstEnc)) {
                        // If mixing same custom enchantment
                    } else {
                        resultItem.addUnsafeEnchantment(firstEnc, firstEnc.getStartLevel());
                    }
                }
                for (Enchantment secondEnc : secondItemCustomEnc) {
                    if (!firstItemCustomEnc.contains(secondEnc)) {
                        resultItem.addUnsafeEnchantment(secondEnc, secondEnc.getStartLevel());
                    }
                }

                // Save new result item
                event.setResult(resultItem);
            }

        // If it's not a valid enchantment
        } else {
            // If it's a book
            if (secondItem.getType() == Material.ENCHANTED_BOOK) {
                // List of all the valid enchantments
                List<Enchantment> validEnchantments = new ArrayList<>();

                // Add enchantments just to a valid type item
                for (Enchantment enc : secondItemCustomEnc) {
                    if (enc.getItemTarget().includes(firstItem)) {
                        validEnchantments.add(enc);
                    }
                }

                // If there're valid enchantments
                if (validEnchantments.size() > 0) {
                    // Clone first item
                    resultItem = firstItem.clone();
                    // Has meta
                    if (resultItem.hasItemMeta()) {
                        ItemMeta resultMeta = resultItem.getItemMeta();
                        List<String> resultLore = new ArrayList<String>();

                        // Add custom enchantments lore to item
                        for (Enchantment firstEnc : firstItemCustomEnc) {
                            if (validEnchantments.contains(firstEnc)) {
                                // If mixing same custom enchantment
                            } else {
                                resultLore.add(ChatColor.GOLD + firstEnc.getName());
                            }
                        }
                        for (Enchantment secondEnc : validEnchantments) {
                            if (!firstItemCustomEnc.contains(secondEnc)) {
                                resultLore.add(ChatColor.GOLD + secondEnc.getName());
                            }
                        }

                        // Add extra lore to item
                        for (String l : firstExtraLore)
                            resultLore.add(l);
                        resultMeta.setLore(resultLore);
                        resultItem.setItemMeta(resultMeta);

                        // Add custom enchantments to item
                        for (Enchantment firstEnc : firstItemCustomEnc) {
                            if (validEnchantments.contains(firstEnc)) {
                                // If mixing same custom enchantment
                            } else {
                                resultItem.addUnsafeEnchantment(firstEnc, firstEnc.getStartLevel());
                            }
                        }
                        for (Enchantment secondEnc : validEnchantments) {
                            if (!firstItemCustomEnc.contains(secondEnc)) {
                                resultItem.addUnsafeEnchantment(secondEnc, secondEnc.getStartLevel());
                            }
                        }
                    // Doesn't have meta
                    } else {
                        ItemMeta resultMeta = resultItem.getItemMeta();
                        List<String> resultLore = new ArrayList<String>();

                        for (Enchantment secondEnc : validEnchantments) {
                            resultLore.add(ChatColor.GOLD + secondEnc.getName());
                        }

                        resultMeta.setLore(resultLore);
                        resultItem.setItemMeta(resultMeta);

                        // Add custom enchantments to item
                        for (Enchantment secondEnc : validEnchantments) {
                            resultItem.addUnsafeEnchantment(secondEnc, secondEnc.getStartLevel());
                        }
                    }

                    // Save new result item
                    event.setResult(resultItem);
                    anvil.setRepairCost(5);
                }
            }
        }


    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand() == null)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY)) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                if (event.getPlayer().getInventory().firstEmpty() != -1 && !(event.getBlock().getState() instanceof Container)) {
                    event.setDropItems(false);

                    Player player = event.getPlayer();
                    Block block = event.getBlock();

                    Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
                    if (drops.isEmpty())
                        return;
                    player.getInventory().addItem(drops.iterator().next());
                }
            }
        }
    }
}
