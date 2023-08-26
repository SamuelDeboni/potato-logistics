package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;

import java.util.*;

public class TileEntityAutoBascket extends TileEntity {

    private int numUnitsInside = 0;
    private final Map<TileEntityAutoBascket.BasketEntry, Integer> contents = new HashMap<>();

    public void dropAllItems() {
        Random rand = new Random();
        for (Map.Entry<TileEntityAutoBascket.BasketEntry, Integer> entry : this.contents.entrySet()) {
            int stackSize;
            TileEntityAutoBascket.BasketEntry be = entry.getKey();
            for (int numItems = entry.getValue(); numItems > 0; numItems -= stackSize) {
                int maxStackSize;
                stackSize = maxStackSize = be.getItem().getItemStackLimit();
                int remainingItems = numItems - maxStackSize;
                if (remainingItems < 0) {
                    stackSize = numItems;
                }
                this.dropItemStack(rand, new ItemStack(be.id, stackSize, be.metadata, be.tag));
            }
        }
        this.contents.clear();
        this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Block.basket.id);
        this.updateNumUnits();
    }

    private void updateNumUnits() {
        this.numUnitsInside = 0;
        for (Map.Entry<TileEntityAutoBascket.BasketEntry, Integer> entry : this.contents.entrySet()) {
            TileEntityAutoBascket.BasketEntry be = entry.getKey();
            int numItems = entry.getValue();
            int unitsPerItem = this.getItemSizeUnits(be.getItem());
            this.numUnitsInside += unitsPerItem * numItems;
        }
    }

    private int getItemSizeUnits(Item item) {
        return 64 / item.getItemStackLimit();
    }

    private void dropItemStack(Random rand, ItemStack itemstack) {
        float f = rand.nextFloat() * 0.8f + 0.1f;
        float f1 = rand.nextFloat() * 0.8f + 0.1f;
        float f2 = rand.nextFloat() * 0.8f + 0.1f;
        EntityItem entityitem = new EntityItem(this.worldObj, (float)this.xCoord + f, (float)this.yCoord + f1, (float)this.zCoord + f2, itemstack);
        float f3 = 0.05f;
        entityitem.xd = (float)rand.nextGaussian() * 0.05f;
        entityitem.yd = (float)rand.nextGaussian() * 0.05f + 0.25f;
        entityitem.zd = (float)rand.nextGaussian() * 0.05f;
        this.worldObj.entityJoinedWorld(entityitem);
    }

    public int getNumUnitsInside() {
        return this.numUnitsInside;
    }

    public int getMaxUnits() {
        return 1728;
    }

    public void givePlayerAllItems(World world, EntityPlayer player) {
        ArrayList<TileEntityAutoBascket.BasketEntry> toRemove = new ArrayList<>();
        for (Map.Entry<TileEntityAutoBascket.BasketEntry, Integer> entry : this.contents.entrySet()) {
            int numItems;
            int stackSize;
            TileEntityAutoBascket.BasketEntry be = entry.getKey();
            for (numItems = entry.getValue().intValue(); numItems > 0; numItems -= stackSize) {
                int maxStackSize;
                stackSize = maxStackSize = be.getItem().getItemStackLimit();
                int remainingItems = numItems - maxStackSize;
                if (remainingItems >= 0) continue;
                stackSize = numItems;
                if (player.inventory.addItemStackToInventory(new ItemStack(be.id, stackSize, be.metadata, be.tag))) continue;
                break;
            }
            this.contents.put(be, numItems);
            if (numItems > 0) continue;
            toRemove.add(be);
        }
        for (TileEntityAutoBascket.BasketEntry basketEntry : toRemove) {
            this.contents.remove(basketEntry);
        }
        this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Block.basket.id);
        this.updateNumUnits();
    }

    public ItemStack removeOneItem() {
        TileEntityAutoBascket.BasketEntry firstKey = null;
        int itemCount = 0;
        for (Map.Entry<TileEntityAutoBascket.BasketEntry, Integer> entry : this.contents.entrySet()) {
            firstKey = entry.getKey();
            itemCount = entry.getValue();
            break;
        }

        if (firstKey == null || itemCount == 0) return null;

        ItemStack itemStack = new ItemStack(firstKey.getItem(), 1, firstKey.metadata);

        itemCount--;

        if (itemCount == 0) {
            this.contents.remove(firstKey);
        } else {
            this.contents.put(firstKey, itemCount);
        }

        this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Block.basket.id);
        this.updateNumUnits();

        return itemStack;
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        ListTag itemsTag = tag.getList("Items");
        this.contents.clear();
        for (int i = 0; i < itemsTag.tagCount(); ++i) {
            CompoundTag itemTag = (CompoundTag)itemsTag.tagAt(i);
            TileEntityAutoBascket.BasketEntry entry = TileEntityAutoBascket.BasketEntry.read(itemTag);
            short count = itemTag.getShort("Count");
            this.contents.put(entry, Integer.valueOf(count));
        }
        this.updateNumUnits();
    }

    @Override
    public void updateEntity() {
        if (this.worldObj == null || this.worldObj.isClientSide) {
            return;
        }
        AABB aabb = AABB.getBoundingBoxFromPool(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1);
        List<Entity> entities = this.worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
        boolean shouldUpdate = false;
        if (!entities.isEmpty()) {
            for (Entity e : entities) {
                EntityItem entity = (EntityItem)e;
                if (entity.item == null || entity.item.stackSize <= 0 || entity.delayBeforeCanPickup != 0) continue;
                shouldUpdate = this.importItemStack(entity.item);
                if (entity.item.stackSize > 0) continue;
                entity.item.stackSize = 0;
                e.outOfWorld();
            }
        }
        if (shouldUpdate) {
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Block.basket.id);
            this.updateNumUnits();
        }
    }

    private boolean importItemStack(ItemStack stack) {
        TileEntityAutoBascket.BasketEntry entry = new TileEntityAutoBascket.BasketEntry(stack.itemID, stack.getMetadata(), stack.tag);
        int sizeUnits = this.getItemSizeUnits(stack.getItem());
        int freeUnits = this.getMaxUnits() - this.numUnitsInside;
        int itemsToTake = Math.min(freeUnits / sizeUnits, stack.stackSize);
        if (itemsToTake <= 0) {
            return false;
        }
        stack.stackSize -= itemsToTake;
        int currentItemsInBE = this.contents.getOrDefault(entry, 0);
        this.contents.put(entry, currentItemsInBE += itemsToTake);
        return true;
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        ListTag itemsTag = new ListTag();
        for (Map.Entry<TileEntityAutoBascket.BasketEntry, Integer> entry : this.contents.entrySet()) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putShort("Count", (short)entry.getValue().intValue());
            TileEntityAutoBascket.BasketEntry.write(itemTag, entry.getKey());
            itemsTag.addTag(itemTag);
        }
        tag.put("Items", itemsTag);
    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }



    private static final class BasketEntry {
        public final int id;
        public final int metadata;
        public final CompoundTag tag;

        public BasketEntry(int id, int metadata, CompoundTag tag) {
            this.id = id;
            this.metadata = metadata;
            this.tag = tag;
        }

        public static TileEntityAutoBascket.BasketEntry read(CompoundTag tag) {
            short id = tag.getShort("id");
            short damage = tag.getShort("Damage");
            CompoundTag data = tag.getCompound("Data");
            return new TileEntityAutoBascket.BasketEntry(id, damage, data);
        }

        public static void write(CompoundTag tag, TileEntityAutoBascket.BasketEntry entry) {
            tag.putShort("id", (short)entry.id);
            tag.putShort("Damage", (short)entry.metadata);
            tag.putCompound("Data", entry.tag);
        }

        public Item getItem() {
            return Item.itemsList[this.id];
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TileEntityAutoBascket.BasketEntry)) {
                return false;
            }
            TileEntityAutoBascket.BasketEntry other = (TileEntityAutoBascket.BasketEntry)obj;
            if (this.id != other.id || this.metadata != other.metadata) {
                return false;
            }
            return this.tag.getValues().size() <= 2 && other.tag.getValues().size() <= 2;
        }

        public int hashCode() {
            if (this.tag.getValues().size() <= 2) {
                return Objects.hash(this.id, this.metadata);
            }
            return Objects.hash(this.id, this.metadata, this.tag);
        }
    }
}
