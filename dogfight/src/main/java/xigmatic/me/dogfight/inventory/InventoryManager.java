package xigmatic.me.dogfight.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import xigmatic.me.dogfight.RoleManager;

public class InventoryManager implements CommandExecutor, Listener {
    private Plugin plugin;
    private RoleManager roleManager;
    /**
     * Creates a new InventoryManager
     * @param plugin Pass through main Plugin class
     */
    public InventoryManager(Plugin plugin, RoleManager roleManager) {
        this.plugin = plugin;
        this.roleManager = roleManager;
    }


    /**
     * Initializes and opens the selection screen for "Glider" or "Sniper"
     */
    private void openInventory(Player player) {
        // Initializes hopper inventory (selection screen)
        Inventory selectionScreen = Bukkit.createInventory(player, InventoryType.HOPPER," ");

        // "Sniper" item selection button
        ItemStack sniper = new ItemStack(Material.CROSSBOW);
        sniper.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);
        ItemMeta sniperMeta = sniper.getItemMeta();


        // "Sniper" item selection button
        ItemStack glider = new ItemStack(Material.ELYTRA);

        // Places the items into the correct slots
        selectionScreen.setItem(0, sniper);
        selectionScreen.setItem(4, glider);

        // Opens inventory for "player"
        player.openInventory(selectionScreen);
    }


    /**
     * Click listener for selection screen
     * @param event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        if(event.getCurrentItem().getType() == Material.AIR || event.getInventory().getType() != InventoryType.HOPPER)
            return;

        // Cancels event
        event.setCancelled(true);

        // Refreshes inventory after 2 ticks
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.updateInventory();
            p.closeInventory();
        }, 2);

        // Sets role depending on what item is clicked
        if(event.getCurrentItem().getType() == Material.CROSSBOW) {
            roleManager.setSniper(p);
        } else if (event.getCurrentItem().getType() == Material.ELYTRA) {
            roleManager.setGlider(p);
        }
    }


    /**
     * Listens for certain commands
     * @param sender Entity/console where the command was issued
     * @param command Command object (not really important)
     * @param label Command header ( /_____ )
     * @param args Command arguments (follows command header)
     * @return Success of command execution
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // openTestInv
        if(label.equalsIgnoreCase("openTestInv")) {
            Player p = (Player) sender;
            openInventory(p);

            return true;
        }

        // giveEquipment
        if(label.equalsIgnoreCase("giveEquipment")) {
            roleManager.distributeEquipment();
        }

        return false;
    }
}
