package xigmatic.me.dogfight;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;

import java.util.*;

public class NPCManager {
    public static final String DISCONNECTED_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTcxMjUzMDQwOTEzOCwKICAicHJvZmlsZUlkIiA6ICJmYTgxNTNmN2YwMTY0MDc3YmFkNThkNDgwODE3MDk4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJYaWdtYXRpYyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZjgxNGVjNWJkNzRmMjhhZDZiZjJmMDJlMzI0MWM2MWM0ZmQyYjFhNjBkYTY3YzIwNGRhNjQ2ZmVmYWE0ZGQzIgogICAgfQogIH0KfQ==";
    public static final String DISCONNECTED_SIGNATURE = "yVaITKs+gdq9w/57dBh3R55t5CZE9O3HLmrWRaQjbs8DDVzYePAS7J/y1dxPnYSp7RCI4YF7unt3yIhzNc5BAkhKtipIM6EZjoGQBafhhoIpsfECO8vMspvVveLMu3ub3L6DCv/fAMOF1dN0/NfG7lcMOqV1utHibegjsETPcmA4LfcR1Uqod9xMYZwB89W+uyKe+ziXk9NCNeA8zU6AOzA18TskZZwyIRYLouNAPHwduI+zsG7NarGiA2PJfeRr7tJGOMGADjJmbBRRjzyRsWSSVPiIydObxGXPFtvJbR1fQyiwtWrOoSKcp03sJCxCIcbu2EP0unAorlTeQ35m0WabSJ1DD1KLc/CLH3i4ty225TkBJJYVp8WDTsfPYPTyiiz63mKpH7ht4mCcTmv7r560TUMr8wNwqNaEO4oX6ZADOajzcx7HFxsdf547gi4WxhJPnrqBSsh4z9AxTuEzzYeXHLY7XhacyV4DVLrFeEn2LGdCx7WOX2jTAsGz+lLnTJSErr9Y89rV3bz25myxuoVCrvqeHVcw1JszJKc5pXQADeFd0tnYVgfwtoIlaW35MbQr9BATb91lm6J9r8KJRmWskMI6ywd+9NqoOrE+1iG4FtjOdVTklKwQYYS84cb04Eu9Jy5LXtgvePFg/iPSe5szHV6C7FZ36De8Yx75NBo=";
    public static final String TRANSPARENT_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTcxMjM0OTg0NjI1MSwKICAicHJvZmlsZUlkIiA6ICJmYTgxNTNmN2YwMTY0MDc3YmFkNThkNDgwODE3MDk4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJYaWdtYXRpYyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zOWExMTY3YTk3NGY5Y2RlNGFhZjhjMzk4ZDg4M2NjMGY4ZTFiMzBlOTU0NDg0ZmI2ZGUwMjI5OTAxZmRiMmEwIgogICAgfQogIH0KfQ==";
    public static final String TRANSPARENT_SIGNATURE = "ZgZ+qIgn3qHhVI+hoR4GVqwnYd1vf3u33/46R+KbBgrw+OzztNO161P3k0gwXYvByz5p6UXe79ONcTCU3iVu4TNMXaTY4X6RTnOXCcTvl+eBeCuFAQgFaKyLNbojUECW0rjlI6f+GdqEz0Ul358SQDNo8t48hpk0jDN5HuQaFezSJYjD+QI1/FZYPTEegK07Nj66ETIKlfa7vzlHs7JgHTLzNjsJ11/S7ZwcZsxa6b0fMoXchXN2PZeGDnejGu+qAiKk7R6stGgdcsKw+8GwDFlVorrMzbtK0zxKorJhqO3a9H6mVILSZEaSXK0N3rD5rneaTLlTbxjypoz4GQhkdToAJD7KK/6ni+Ulz/GdfiHbP4z1E1HJ9umx0w03r57Afv0xwJCN2q+72yGvU/E9A2q+lBxrUs58yqSMzns8UezQ6xvWLulVGQqtddnlfEDAGcL9YrSXIaENPXfBsC9IFLD/F5jvmZmpE6YiIUgQ7S2sq3veNoRammySHcvQFrmmUCy4oNdrLHeQF5/rlouukZVsd28x5pK2j+cV2pFk+jXaZ0kdmEpej0KXZfL9m95E5ZAgvkgYd1qgJ37JrNwTwr+rgI2yDnUhkRf8U0IQf+V7lmlmGHxY245+b2kUW5tOmgJWL9RoOXF7scaTZbCgZhijz4Kou4Dn/ry1yhZXw84=";
    public static final HashMap<String, ServerPlayer> npcMap = new HashMap<>();

    public NPCManager() {

    }


    /**
     * Spawns an NPC only seen by the passed player
     * @param player Player to see the NPC
     * @param npcName Name of NPC
     * @param texture Texture string for skin
     * @param signature Signature String for skin
     */
    public static void spawnNPC(Player player, String npcName, String texture, String signature) {
        // Converts Player into CraftPlayer for NMS
        CraftPlayer craftPlayer = (CraftPlayer) player;

        // Converts CraftPlayer into ServerPlayer for connection handling
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        // Player Connection
        ServerGamePacketListenerImpl ps = serverPlayer.connection;

        // Creates a list of actions that the packet will send
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.noneOf(ClientboundPlayerInfoUpdatePacket.Action.class);
        // Adds the npc to player's game
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER);
        // Updates display name
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
        // Updates tab-list
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED);

        // Sends player info packet to player
        //ps.send(new ClientboundPlayerInfoUpdatePacket(actions, Arrays.asList(npc)));

        ServerPlayer npc = addNPC(npcName, texture, signature);

        // Adds Player physically into the world (CHANGE THIS FOR TAB-LIST ONLY NPC)
        ps.send(new ClientboundAddPlayerPacket(npc));
        ps.send(new ClientboundPlayerInfoUpdatePacket(actions, Arrays.asList(npc)));
    }


    /**
     * Spawns the passed NPC for the player specified (does not add to HashMap)
     * @param player Player to see NPC
     * @param npc NPC (ServerPlayer) to be spawned
     */
    private static void spawnNPC(Player player, ServerPlayer npc) {
        // Converts Player into CraftPlayer for NMS
        CraftPlayer craftPlayer = (CraftPlayer) player;

        // Converts CraftPlayer into ServerPlayer for connection handling
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        // Player Connection
        ServerGamePacketListenerImpl ps = serverPlayer.connection;

        // Creates a list of actions that the packet will send
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.noneOf(ClientboundPlayerInfoUpdatePacket.Action.class);
        // Adds the npc to player's game
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER);
        // Updates display name
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
        // Updates tab-list
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED);

        // Sends player info packet to player
        //ps.send(new ClientboundPlayerInfoUpdatePacket(actions, Arrays.asList(npc)));

        // Adds Player physically into the world (CHANGE THIS FOR TAB-LIST ONLY NPC)
        ps.send(new ClientboundAddPlayerPacket(npc));
        ps.send(new ClientboundPlayerInfoUpdatePacket(actions, Arrays.asList(npc)));
    }


    /**
     * Adds NPC with specified parameters to the universal NPC HashMap
     * ALWAYS returns the NPC instance even if the NPC already exists in the HashMap
     * @param npcName NPC Name string
     * @param texture Skin texture string
     * @param signature Skin signature string
     * @return NPC ServerPlayer instance
     */
    public static ServerPlayer addNPC(String npcName, String texture, String signature) {
        // More connection handling
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = server.overworld();

        // Creates a new profile for the npc
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcName);

        // Sets skin texture
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        // Creates the npc as a ServerPlayer
        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);

        // Spawns NPC for all players
        for(Player player : Bukkit.getOnlinePlayers())
            spawnNPC(player, npc);

        // Adds NPC to npcMap if not already present
        if(!npcMap.containsKey(npcName))
            npcMap.put(npcName, npc);

        // Returns NPC instance
        return npc;
    }


    public static void removeNPC(String npcName) {
        ServerPlayer npc;

        // Checks if NPC exists in HashMap
        try {
            npc = npcMap.get(npcName);
        } catch (Exception ignored) {
            Bukkit.getConsoleSender().sendMessage("Could not remove " + npcName + " because that NPC does not exist");
            return;
        }

        // Removes NPC for all online players
        for(Player player : Bukkit.getOnlinePlayers()) {
            // Converts Player into CraftPlayer for NMS
            CraftPlayer craftPlayer = (CraftPlayer) player;

            // Converts CraftPlayer into ServerPlayer for connection handling
            ServerPlayer serverPlayer = craftPlayer.getHandle();

            // Player Connection
            ServerGamePacketListenerImpl ps = serverPlayer.connection;

            // Removes NPC from tab-list
            ps.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(npc.getUUID())));
        }

        // Removes NPC from HashMap
        npcMap.remove(npcName);
    }


    /**
     * ONLY TO BE USED WHEN A PLAYER JOINS
     * @param player Player to see NPCs
     */
    public static void spawnAllNPCs(Player player) {
        for(String npcName : npcMap.keySet())
            spawnNPC(player, npcMap.get(npcName));
    }


    /**
     * Automatically adds all offline players (in the tournament) to the NPC HashMap as well as team names
     */
    public static void addTablistNPCs() {
        // Sets the players in the list
        for (TourneyTeam team : TeamManager.getAllTeams()) {
            // Team Name and Logo in player list
            NPCManager.addNPC(team.getTeamName(), NPCManager.TRANSPARENT_TEXTURE, NPCManager.TRANSPARENT_SIGNATURE);
            NPCManager.addNPC("_" + team.getTeamName(), NPCManager.TRANSPARENT_TEXTURE, NPCManager.TRANSPARENT_SIGNATURE);
            // Checks if player 1 for the team is disconnected and applies disconnected skin if offline
            if (!team.isPlayer1Online())
                NPCManager.addNPC(team.getPlayer1(), NPCManager.DISCONNECTED_TEXTURE, NPCManager.DISCONNECTED_SIGNATURE);
            // Checks if player 2 for the team is disconnected and applies disconnected skin if offline
            if (!team.isPlayer2Online())
                NPCManager.addNPC(team.getPlayer2(), NPCManager.DISCONNECTED_TEXTURE, NPCManager.DISCONNECTED_SIGNATURE);
        }
    }
}
