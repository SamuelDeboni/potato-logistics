package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.Util;
import deboni.potatologistics.gui.ContainerAutoCrafter;
import net.minecraft.client.gui.guidebook.crafting.CraftingSection;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.crafting.legacy.CraftingManager;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import net.minecraft.core.player.inventory.*;

import java.util.List;

public class TileEntityAutoCrafter extends TileEntity implements IInventory {
    public InventoryCrafting craftMatrix;
    public IInventory pattern;
    public IInventory extraOutputs;
    public IInventory craftResult = new InventoryCraftResult();
    public ContainerAutoCrafter dummyContainer;

    private final List<RecipeEntryCrafting<?, ?>> craftingRecipeEntries =  Registries.RECIPES.getAllCraftingRecipes();

    public TileEntityAutoCrafter() {
        dummyContainer = new ContainerAutoCrafter(null, this);
        craftMatrix = new InventoryCrafting(dummyContainer, 3, 3);
        pattern = new InventoryBasic("pattern", 9);
        extraOutputs = new InventoryBasic("extra outputs", 1);
    }

    @Override
    public int getSizeInventory() {
        return 10;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if (i == 0) {
            return craftResult.getStackInSlot(0);
        } else if (craftMatrix != null) {
            return craftMatrix.getStackInSlot(i-1);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (i == 0) {
           return craftResult.decrStackSize(i, j);
        } else if (craftMatrix != null) {
            return craftMatrix.decrStackSize(i, j);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if (i == 0) {
            craftResult.setInventorySlotContents(0, itemStack);
        } else if (craftMatrix != null) {
            craftMatrix.setInventorySlotContents(i - 1, itemStack);
        }
    }

    @Override
    public String getInvName() {
        return "Auto Crafter";
    }


    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        {
            ListTag nbttaglist = nbttagcompound.getList("CraftGrid");
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.tagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 0xFF;
                if (j >= this.craftMatrix.getSizeInventory()) continue;
                this.craftMatrix.setInventorySlotContents(j, ItemStack.readItemStackFromNbt(nbttagcompound1));
            }
        }
        {
            ListTag nbttaglist = nbttagcompound.getList("Pattern");
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.tagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 0xFF;
                if (j >= this.pattern.getSizeInventory()) continue;
                this.pattern.setInventorySlotContents(j, ItemStack.readItemStackFromNbt(nbttagcompound1));
            }
        }
        {
            ListTag nbttaglist = nbttagcompound.getList("CraftResult");
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.tagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 0xFF;
                if (j >= this.craftResult.getSizeInventory()) continue;
                this.craftResult.setInventorySlotContents(j, ItemStack.readItemStackFromNbt(nbttagcompound1));
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
                ItemStack stack = this.craftMatrix.getStackInSlot(i);
                if (stack == null) continue;
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte) i);
                stack.writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
            nbttagcompound.put("CraftGrid", nbttaglist);
        }
        {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.pattern.getSizeInventory(); ++i) {
                ItemStack stack = this.pattern.getStackInSlot(i);
                if (stack == null) continue;
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte) i);
                stack.writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
            nbttagcompound.put("Pattern", nbttaglist);
        }
        {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.craftResult.getSizeInventory(); ++i) {
                ItemStack stack = this.craftResult.getStackInSlot(i);
                if (stack == null) continue;
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte) i);
                stack.writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
            nbttagcompound.put("CraftResult", nbttaglist);
        }
    }


    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        if (Util.getBlockTileEntity(worldObj, this.x, this.y, this.z) != this) {
            return false;
        }
        return entityPlayer.distanceToSqr((double) this.x + 0.5, (double) this.y + 0.5, (double) this.z + 0.5) <= 64.0;
    }

    @Override
    public void sortInventory() {

    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }

    public ItemStack removeOneResult() {
        ItemStack stack = craftResult.getStackInSlot(0);
        if (stack != null) {
            stack.stackSize--;
            if (stack.stackSize <= 0) {
                craftResult.setInventorySlotContents(0, null);
            } else {
                craftResult.setInventorySlotContents(0, stack.copy());
            }
            stack.stackSize = 1;
            this.onInventoryChanged();
            return stack;
        }
        return null;
    }


    public boolean insertItem(ItemStack stackToInsert) {
        boolean inserted = false;

        int slotToInsert = -1;
        int lastSlotCount = 99;

        if (craftMatrix != null) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = craftMatrix.getStackInSlot(i);
                ItemStack stackPattern = pattern.getStackInSlot(i);
                if (stackPattern == null) continue;

                if (stack == null) {
                    if (stackPattern.itemID == stackToInsert.itemID && stackPattern.getMetadata() == stackToInsert.getMetadata()) {
                        slotToInsert = i;
                        lastSlotCount = 0;
                    }
                } else if (stack.itemID == stackToInsert.itemID
                        && stack.getMetadata() == stackToInsert.getMetadata()
                        && stack.stackSize < stack.getMaxStackSize()
                        && stack.stackSize < lastSlotCount
                ) {
                    slotToInsert = i;
                    lastSlotCount = stack.stackSize;
                }
            }

            if (slotToInsert >= 0) {
                if (lastSlotCount == 0) {
                    craftMatrix.setInventorySlotContents(slotToInsert, stackToInsert.copy());
                    this.onInventoryChanged();
                    inserted = true;
                } else {
                    ItemStack stack = craftMatrix.getStackInSlot(slotToInsert);
                    if (stack != null) {
                        stack.stackSize++;
                        craftMatrix.setInventorySlotContents(slotToInsert, stack);
                        this.onInventoryChanged();
                        inserted = true;
                    }
                }
            }
        }

        return inserted;
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    int timer = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.craftMatrix != null && !worldObj.isClientSide) {
            boolean canCraft = true;

            for (int i = 0; i < 9; i++) {
                ItemStack i0 = craftMatrix.getStackInSlot(i);
                ItemStack i1 = pattern.getStackInSlot(i);
                if (i0 != null && i1 == null || i0 == null && i1 != null) {
                    canCraft = false;
                    break;
                }

                if (i0 != null && i1 != null && i0.itemID != i1.itemID) {
                    canCraft = false;
                    break;
                }
            }

            if (extraOutputs.getStackInSlot(0) != null) {
                canCraft = false;
            }

            RecipeEntryCrafting<?, ?> recipe = null;
            for (RecipeEntryCrafting<?, ?> entry: craftingRecipeEntries) {
                if (entry.matches(this.craftMatrix)) {
                    recipe = entry;
                    break;
                }
            }

            ItemStack craftingResult = null;
            if (recipe != null)  {
                craftingResult = recipe.getCraftingResult(craftMatrix);
            }

            if (craftResult.getStackInSlot(0) == null && craftingResult != null && canCraft) {
                if (timer > 10) {
                    recipe.onCraftResult(this.craftMatrix);
                    craftResult.setInventorySlotContents(0, craftingResult);
                    this.onInventoryChanged();

                    int bucketCount = 0;
                    for (int i = 0; i < 9; i++) {
                        ItemStack s = craftMatrix.getStackInSlot(i);
                        if (s != null && s.itemID == Item.bucket.id) {
                            craftMatrix.setInventorySlotContents(i, null);
                            bucketCount += 1;
                        }
                    }

                    if (bucketCount > 0) {
                        extraOutputs.setInventorySlotContents(0, new ItemStack(Item.bucket, bucketCount));
                    }

                    timer = 0;
                }
                timer += 1;

                //CraftingManager.getInstance().onCraftResult(this.craftMatrix);
                //for (int i = 0; i < 9; i++) this.craftMatrix.decrStackSize(i, 1);
            }
        }
    }
}
