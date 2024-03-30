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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import xigmatic.me.dogfight.GameState;
import xigmatic.me.dogfight.scoreboard.RoleManager;
import xigmatic.me.dogfight.scoreboard.TeamManager;

public class InventoryManager implements CommandExecutor, Listener {
    private final Plugin plugin;
    private final RoleManager roleManager;
    private GameState gameState;
    /**
     * Creates a new InventoryManager
     * @param plugin Pass through main Plugin class
     */
    public InventoryManager(Plugin plugin, RoleManager roleManager) {
        this.plugin = plugin;
        this.roleManager = roleManager;
    }


    /**
     * Sets the GameState associated with InventoryManager (Meant to create parity between GameManager and InventoryManager)
     * @param newGameState New GameState that InventoryManager reads
     */
    public void setGameState(GameState newGameState) {
        this.gameState = newGameState;
    }


    /**
     * Initializes and opens the selection screen for "Glider" or "Sniper"
     */
    public void openSelectionScreen(Player player) {
        // Initializes hopper inventory (selection screen)
        Inventory selectionScreen = Bukkit.createInventory(player, InventoryType.HOPPER," ");

        // "Sniper" item selection button
        ItemStack sniper = new ItemStack(Material.CROSSBOW);
        sniper.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);
        ItemMeta sniperMeta = sniper.getItemMeta();
        sniperMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);


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
     * @param event Event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        // Checks if an item was clicked or the inventory type is "hopper"
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        // Checks if the inventory type is "hopper" and will cancel the inventory move event but not execute any selection code
        if (event.getInventory().getType() != InventoryType.HOPPER) {
            // Cancels event
            event.setCancelled(true);

            // Refreshes inventory after 2 ticks
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                p.updateInventory();
                p.closeInventory();
            }, 2);
            return;
        }

        // Beginning of selection logic


        // Cancels event
        event.setCancelled(true);

        // Refreshes inventory after 2 ticks
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.updateInventory();
            p.closeInventory();
        }, 2);

        // Sets role depending on what item is clicked
        if(event.getCurrentItem().getType() == Material.CROSSBOW) {
            // Sets the player's role and the opposite player's role to the remaining role
            roleManager.setSniper(p.getName());
            roleManager.setGlider(TeamManager.getOtherPlayerOfTeam(p.getName()));
        } else if (event.getCurrentItem().getType() == Material.ELYTRA) {
            // Sets the player's role and the opposite player's role to the remaining role
            roleManager.setGlider(p.getName());
            roleManager.setSniper(TeamManager.getOtherPlayerOfTeam(p.getName()));
        }
    }


    /**
     * Item Drop listener to cancel any attempts to drop items
     * @param event Event
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getPlayer().getOpenInventory().getType() == InventoryType.PLAYER)
            // Cancels event
            event.setCancelled(true);
    }


    /**
     * Checks if an inventory is closed and cancels if the inventory is the selection screen
     * @param event Event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Prevents Inventory from opening again if the following conditions are met
        // The current gameState is NOT SELECTING1 or SELECTING2
        if(!(this.gameState == GameState.SELECTING1 || this.gameState == GameState.SELECTING2)
                // Inventory type is NOT hopper
                || event.getInventory().getType() != InventoryType.HOPPER
                // Player has a role (this identifies if something has already been clicked)
                || this.roleManager.getPlayerRole(event.getPlayer().getName()) != null)
            return;

        // Necessary Variables
        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();

        // Reopens inventory
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            player.openInventory(closedInventory);
        }, 1);
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
            openSelectionScreen(p);

            return true;
        }

        // giveEquipment
        if(label.equalsIgnoreCase("giveEquipment")) {
            roleManager.distributeEquipment();

            return true;
        }

        return false;
    }
}
