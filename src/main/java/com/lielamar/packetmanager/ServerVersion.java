package com.lielamar.packetmanager;

import org.bukkit.Bukkit;

public class ServerVersion {

    // Singleton
    private static final ServerVersion instance = new ServerVersion();
    public static ServerVersion getInstance() {
        return instance;
    }

    private ServerVersion() {
        try {
            this.serverVersion = Version.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
            System.out.println("[PacketManager] Your Server Version is " + serverVersion.getStrippedName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Version serverVersion;

    public String getVersion() {
        return this.serverVersion.getNMSName();
    }

    public String getStrippedVersion() {
        return this.serverVersion.getStrippedName();
    }

    public boolean above(Version version) {
        return serverVersion.getId() >= version.getId();
    }

    public enum Version {

        v1_8_R1("v1_8_R1", 1),
        v1_8_R2("v1_8_R2", 2),
        v1_8_R3("v1_8_R3", 3),
        v1_9_R1("v1_9_R1", 4),
        v1_9_R2("v1_9_R2", 5),
        v1_10_R1("v1_10_R1", 6),
        v1_11_R1("v1_11_R1", 7),
        v1_12_R1("v1_12_R1", 8),
        v1_13_R1("v1_13_R1", 9),
        v1_13_R2("v1_13_R2", 10),
        v1_14_R1("v1_14_R1", 11),
        v1_15_R1("v1_15_R1", 12),
        v1_16_R1("v1_16_R1", 13),
        v1_16_R2("v1_16_R2", 14),
        v1_16_R3("v1_16_R3", 15);

        private final String name;
        private final int id;

        Version(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getNMSName() { return this.name; }
        public String getStrippedName() { return this.name.substring(0, this.name.length()-1).replaceAll("v", "").replaceAll("R", "").replaceAll("_", "."); }

        int getId() { return id; }
    }
}
