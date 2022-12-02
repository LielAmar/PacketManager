package com.lielamar.nmsmanager.packets;

import com.lielamar.nmsmanager.modules.Version;
import com.lielamar.nmsmanager.reflection.NMSField;
import com.lielamar.nmsmanager.reflection.NMSMethod;
import org.bukkit.entity.Player;

public abstract class Packet {

    public abstract Object toPacket();
    protected abstract void sendThroughAPI(Player player);

    public void sendPacket(Player player) {
        if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_17_R1)) {
            sendThroughAPI(player);
        } else {
            sendThroughPacket(player);
        }
    }

    private void sendThroughPacket(Player player) {
        try {
            Object craftPlayer = NMSMethod.invokeMethod(NMSMethod.CRAFT_PLAYER_GET_HANDLE, player);
            Object playerConnection = NMSField.CRAFT_PLAYER_PLAYER_CONNECTION.getField().get(craftPlayer);

            NMSMethod.invokeMethod(NMSMethod.CRAFT_PLAYER_SEND_PACKET, playerConnection, toPacket());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
