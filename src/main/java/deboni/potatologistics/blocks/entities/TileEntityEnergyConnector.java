package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PipeStack;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;

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
        public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
            nbttagcompound.putInt("x", x);
            nbttagcompound.putInt("y", y);
            nbttagcompound.putInt("z", z);
            return nbttagcompound;
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
        for (int i = 0; i < this.connections.size(); i++) {
            if (this.connections.get(i) == null) continue;
            CompoundTag nbttagcompound1 = new CompoundTag();
            this.connections.get(i).writeToNBT(nbttagcompound1);
            nbttaglist.addTag(nbttagcompound1);
        }
        nbttagcompound.put("connections", nbttaglist);
    }

    public boolean addConnection(int x, int y, int z)  {
        TileEntity te = worldObj.getBlockTileEntity(x, y, z);

        if (!(te instanceof TileEntityEnergyConnector)) return false;

        boolean hasConnection = false;
        for (Connection c: connections) {
            if (c.x == x && c.y == y && c.z == z) {
                hasConnection = true;
                break;
            }
        }
        if (hasConnection) return false;

        for (Connection c: ((TileEntityEnergyConnector) te).connections) {
            if (c.x == xCoord && c.y == yCoord && c.z == zCoord) {
                hasConnection = true;
                break;
            }
        }

        if (hasConnection) return false;

        connections.add(new Connection(x, y, z));
        ((TileEntityEnergyConnector) te).connections.add(new Connection(xCoord, yCoord, zCoord));
        PotatoLogisticsMod.LOGGER.info("Added connection on: " + x + " " + y + " " + z);

        return true;
    }

    public void removeConnection(int x, int y, int z) {
        int i = 0;
        for (Connection c: connections) {
            if (c.x == x && c.y == y && c.z == z) {
                break;
            }
            i++;
        }
        if (i < connections.size()) connections.remove(i);
    }

    public ItemStack getBreakDrops() {
        ItemStack result = new ItemStack(PotatoLogisticsMod.itemWireSpool, 0);
        for (Connection c: connections) {
            TileEntity te = worldObj.getBlockTileEntity(c.x, c.y, c.z);
            if (te instanceof TileEntityEnergyConnector) {
                ((TileEntityEnergyConnector) te).removeConnection(xCoord, yCoord, zCoord);
            }
            result.stackSize++;
        }

        return result;
    }
}
