package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import net.minecraft.core.util.helper.Direction;
import sunsetsatellite.catalyst.energy.api.IEnergySink;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergy;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

import java.util.ArrayList;

public class TileEntityEnergyConnector extends TileEntityEnergyConductor {

    public static class Connection {
        public int x;
        public int y;
        public int z;

        public Connection(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public void writeToNBT(CompoundTag nbttagcompound) {
            nbttagcompound.putInt("x", x);
            nbttagcompound.putInt("y", y);
            nbttagcompound.putInt("z", z);
        }

        public void readFromNBT(CompoundTag nbttagcompound) {
            this.x = nbttagcompound.getInteger("x");
            this.y = nbttagcompound.getInteger("y");
            this.z = nbttagcompound.getInteger("z");
        }
        public static Connection readConnectionFromNBT(CompoundTag nbt) {
            if (nbt == null) {
                return null;
            }
            Connection con = new Connection(0, 0,0 );
            con.readFromNBT(nbt);
            return con;
        }
    }

    public TileEntityEnergyConnector() {
        setCapacity(512);
        setEnergy(0);
        setTransfer(32);
    }

    public ArrayList<Connection> connections = new ArrayList<>();
    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("connections");
        this.connections = new ArrayList<>();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.tagAt(i);
            this.connections.add(Connection.readConnectionFromNBT(nbttagcompound1));
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (Connection connection : this.connections) {
            if (connection == null) continue;
            CompoundTag nbttagcompound1 = new CompoundTag();
            connection.writeToNBT(nbttagcompound1);
            nbttaglist.addTag(nbttagcompound1);
        }
        nbttagcompound.put("connections", nbttaglist);
    }

    public boolean addConnection(int xi, int yi, int zi)  {
        TileEntity te = worldObj.getBlockTileEntity(xi, yi, zi);

        if (!(te instanceof TileEntityEnergyConnector)) return false;

        boolean hasConnection = false;
        for (Connection c: connections) {
            if (c.x == xi && c.y == yi && c.z == zi) {
                hasConnection = true;
                break;
            }
        }
        if (hasConnection) return false;

        for (Connection c: ((TileEntityEnergyConnector) te).connections) {
            if (c.x == this.x && c.y == this.y && c.z == this.z) {
                hasConnection = true;
                break;
            }
        }

        if (hasConnection) return false;

        connections.add(new Connection(xi, yi, zi));
        ((TileEntityEnergyConnector) te).connections.add(new Connection(this.x, this.y, this.z));
        PotatoLogisticsMod.LOGGER.info("Added connection on: " + xi + " " + yi + " " + zi);

        return true;
    }

    public void removeConnection(int xi, int yi, int zi) {
        int i = 0;
        for (Connection c: connections) {
            if (c.x == xi && c.y == yi && c.z == zi) {
                break;
            }
            i++;
        }
        if (i < connections.size()) connections.remove(i);
    }

    public ItemStack getBreakDrops() {
        ItemStack result = new ItemStack(PotatoLogisticsMod.itemWireSpool, 0);

        ArrayList<Connection> connectionsCopy = (ArrayList<Connection>) connections.clone();
        for (Connection c: connectionsCopy) {
            TileEntity te = worldObj.getBlockTileEntity(c.x, c.y, c.z);
            if (te instanceof TileEntityEnergyConnector) {
                ((TileEntityEnergyConnector) te).removeConnection(x, y, z);
            }
            result.stackSize++;
        }
        if (result.stackSize < 1){
            return null;
        }

        return result;
    }

    public void updateMachineConnections(Direction dir) {
        setConnection(sunsetsatellite.catalyst.core.util.Direction.getDirectionFromSide(dir.getId()), sunsetsatellite.catalyst.core.util.Connection.BOTH);
    }

    @Override
    public void tick() {
        sunsetsatellite.catalyst.core.util.Direction[] directions = sunsetsatellite.catalyst.core.util.Direction.values();

        int side = worldObj.getBlockMetadata(x, y, z);
        updateMachineConnections(Direction.getDirectionById(side).getOpposite());

        {
            sunsetsatellite.catalyst.core.util.Direction dir = sunsetsatellite.catalyst.core.util.Direction.getDirectionFromSide(side).getOpposite();
            TileEntity facingTile = dir.getTileEntity(this.worldObj, this);
            if (facingTile instanceof IEnergySink && !facingTile.equals(this.lastReceived)) {
                int provided = this.provide(dir, this.getMaxProvide(), true);
                if (provided > 0) {
                    int received = ((IEnergySink) facingTile).receive(dir.getOpposite(), provided, true);
                    if (received > 0) {
                        ((IEnergySink) facingTile).receive(dir.getOpposite(), provided, false);
                        this.provide(dir, received, false);
                        this.lastProvided = (TileEntityEnergy) facingTile;
                        ((TileEntityEnergy) facingTile).lastReceived = this;
                    }
                }
            }
        }

        for (Connection connection: connections) {
            TileEntity te = worldObj.getBlockTileEntity(connection.x, connection.y, connection.z);
            if (te instanceof TileEntityEnergyConductor) {
                TileEntityEnergyConnector connector = (TileEntityEnergyConnector) te;
                if (this.energy > this.capacity / 2 && connector.energy < connector.capacity && this.energy > connector.energy) {
                    int valueToTransfer = (this.energy - connector.energy) / 2;
                    valueToTransfer = Math.min(valueToTransfer, this.maxProvide);
                    this.energy -= valueToTransfer;
                    connector.energy += valueToTransfer;
                }
            }
        }

    }
    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }
}
