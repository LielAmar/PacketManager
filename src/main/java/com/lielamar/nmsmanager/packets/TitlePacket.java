package com.lielamar.nmsmanager.packets;

import com.lielamar.nmsmanager.reflection.NMSConstructor;
import com.lielamar.nmsmanager.reflection.NMSMethod;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TitlePacket extends Packet {

    private final String title, subtitle;
    private final int fadeIn, showTime, fadeOut;

    private TitlePacket(String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.showTime = showTime;
        this.fadeOut = fadeOut;
    }

    public static class Builder {

        private String title, subtitle;
        private int fadeIn, showTime, fadeOut;

        public Builder() {
            this.title = null;
            this.subtitle = null;
            this.fadeIn = 0;
            this.showTime = 0;
            this.fadeOut = 0;
        }

        public Builder(Builder builder) {
            this.title = builder.title;
            this.subtitle = builder.subtitle;
            this.fadeIn = builder.fadeIn;
            this.showTime = builder.showTime;
            this.fadeOut = builder.fadeOut;
        }

        public Builder(String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
            this.title = title;
            this.subtitle = subtitle;
            this.fadeIn = fadeIn;
            this.showTime = showTime;
            this.fadeOut = fadeOut;
        }


        public Builder setTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;

            return this;
        }

        public Builder setFadeIn(int fadeIn) {
            this.fadeIn = fadeIn;

            return this;
        }

        public Builder setShowTime(int showTime) {
            this.showTime = showTime;

            return this;
        }

        public Builder setFadeOut(int fadeOut) {
            this.fadeOut = fadeOut;

            return this;
        }


        public TitlePacket build() {
            return new TitlePacket(this.title, this.subtitle, this.fadeIn, this.showTime, this.fadeOut);
        }
    }


    @Override
    public Object toPacket() {
        try {
            Object packet;
            Object chatSerializerObject;

            if(title != null) {
                Object titleType = NMSMethod.invokeMethod(NMSMethod.ENUM_TITLE_ACTION_VALUE_OF, "TITLE");
                chatSerializerObject = NMSMethod.invokeMethod(NMSMethod.I_CHAT_BASE_COMPONENT_CHAT_SERIALIZER, null, "{\"text\": \"" + title + "\"}");

                packet = NMSConstructor.PACKET_PLAY_OUT_TITLE.getConstructor().newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);
                return packet;
            }

            if(subtitle != null) {
                Object titleType = NMSMethod.invokeMethod(NMSMethod.ENUM_TITLE_ACTION_VALUE_OF, "SUBTITLE");
                chatSerializerObject = NMSMethod.invokeMethod(NMSMethod.I_CHAT_BASE_COMPONENT_CHAT_SERIALIZER, null, "{\"text\": \"" + subtitle + "\"}");

                packet = NMSConstructor.PACKET_PLAY_OUT_TITLE.getConstructor().newInstance(titleType, chatSerializerObject, fadeIn, showTime, fadeOut);
                return packet;
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void sendThroughAPI(Player player) {
        player.sendTitle(title, subtitle, fadeIn, showTime, fadeOut);
    }
}
