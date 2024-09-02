package xigmatic.me.dogfight;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public final class GlowManager {
    public GlowManager() {

    }


    /**
     * Adds glowing effect to given entity for only 1 player
     * @param player PLayer that will be able to see the glow-state change
     * @param entity Entity that will change glow-state for the player
     * @param glowing Determines the glow-state of the entity (true = on)
     */
    public static void setGlow(Player player, Entity entity, boolean glowing) {
        // Sets entityID and gets protocol manager
        int entityID = entity.getEntityId();
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        // Creates packet and specifies which entity it is applying the glowing to (entityID)
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityID);

        // Creates watcher
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);

        // Sets the watcher to monitor packets between player and byte (ion know)
        watcher.setEntity(player);

        // Sets the object as being either no glow or glowing
        if(glowing)
            watcher.setObject(0, serializer, (byte) (0x40));
        else
            watcher.setObject(0, serializer, (byte) (0x00));

        // Again ion know
        final List<WrappedDataValue> wrappedDataValueList = Lists.newArrayList();
        watcher.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            final WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(),
                    dataWatcherObject.getSerializer(), entry.getRawValue()));
        });
        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        // Sends packet to server
        try {
            manager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
