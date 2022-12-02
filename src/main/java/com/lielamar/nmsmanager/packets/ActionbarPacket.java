package com.lielamar.nmsmanager.packets;

import com.lielamar.nmsmanager.modules.Version;
import com.lielamar.nmsmanager.reflection.NMSConstructor;
import com.lielamar.nmsmanager.reflection.NMSMethod;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ActionbarPacket extends Packet {

    private final String message;
    private final UUID playerUUID;

    private ActionbarPacket(String message, UUID playerUUID) {
        this.message = message;
        this.playerUUID = playerUUID;
    }

    public static class Builder {

        private String message;
        private UUID playerUUID;

        public Builder() {
            this.message = null;
            this.playerUUID = null;
        }

        public Builder(Builder builder) {
            this.message = builder.message;
            this.playerUUID = builder.playerUUID;
        }

        public Builder(String message, UUID playerUUID) {
            this.message = message;
            this.playerUUID = playerUUID;
        }


        public Builder setMessage(String message) {
            this.message = message;

            return this;
        }

        public Builder setPlayerUUID(UUID playerUUID) {
            this.playerUUID = playerUUID;

            return this;
        }


        public ActionbarPacket build() {
            return new ActionbarPacket(this.message, this.playerUUID);
        }
    }


    @Override
    public Object toPacket() {
        try {
            Object packet;
            Object chatSerializerObject = NMSMethod.invokeMethod(NMSMethod.I_CHAT_BASE_COMPONENT_CHAT_SERIALIZER, null, "{\"text\": \"" + message + "\"}");

            if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_16_R1)) {
                // 1.16 (or above)
                Object chatMessageTypeObject = NMSMethod.invokeMethod(NMSMethod.CHAT_MESSAGE_TYPE_CHAT_MESSAGE_VALUE_OF, "GAME_INFO");
                packet = NMSConstructor.PACKET_PLAY_OUT_CHAT_1_16.getConstructor().newInstance(chatSerializerObject, chatMessageTypeObject, playerUUID);
            } else if(Version.getInstance().getNMSVersion().above(Version.NMSVersion.v1_12_R1)) {
                // 1.12 (or above)
                Object chatMessageTypeObject = NMSMethod.invokeMethod(NMSMethod.CHAT_MESSAGE_TYPE_CHAT_MESSAGE_VALUE_OF, "GAME_INFO");
                packet = NMSConstructor.PACKET_PLAY_OUT_CHAT_1_12.getConstructor().newInstance(chatSerializerObject, chatMessageTypeObject);
            } else {
                // 1.8 (or above)
                packet = NMSConstructor.PACKET_PLAY_OUT_CHAT_1_8.getConstructor().newInstance(chatSerializerObject, (byte)2);
            }

            return packet;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void sendThroughAPI(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
