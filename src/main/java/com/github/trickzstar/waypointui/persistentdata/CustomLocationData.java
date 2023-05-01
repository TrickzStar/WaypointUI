package com.github.trickzstar.waypointui.persistentdata;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class CustomLocationData implements PersistentDataType<byte[], Location> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public byte[] toPrimitive(Location loc, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[256]);
        bb.putLong(Objects.requireNonNull(loc.getWorld()).getUID().getMostSignificantBits());
        bb.putLong(Objects.requireNonNull(loc.getWorld()).getUID().getLeastSignificantBits());
        bb.putDouble(loc.getX());
        bb.putDouble(loc.getY());
        bb.putDouble(loc.getZ());
        return bb.array();
    }

    @Override
    public Location fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);

        long worldUID = bb.getLong();
        long worldUID1 = bb.getLong();

        double locX = bb.getDouble();
        double locY = bb.getDouble();
        double locZ = bb.getDouble();

        UUID combinedUID = new UUID(worldUID, worldUID1);

        return new Location(Bukkit.getWorld(combinedUID), locX, locY, locZ);
    }
}
