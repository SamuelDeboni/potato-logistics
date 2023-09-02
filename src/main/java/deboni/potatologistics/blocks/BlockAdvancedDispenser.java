package deboni.potatologistics.blocks;

import java.util.Random;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.entity.projectile.EntityArrowGolden;
import net.minecraft.core.entity.projectile.EntityCannonball;
import net.minecraft.core.entity.projectile.EntityEgg;
import net.minecraft.core.entity.projectile.EntityPebble;
import net.minecraft.core.entity.projectile.EntitySnowball;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldTypes;

public class BlockAdvancedDispenser
        extends BlockTileEntityRotatable {
    private Random random = new Random();

    public BlockAdvancedDispenser(String key, int id) {
        super(key, id, Material.metal);
    }

    @Override
    public int tickRate() {
        return 4;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        this.setDispenserDefaultDirection(world, i, j, k);
    }

    private void setDispenserDefaultDirection(World world, int i, int j, int k) {
        if (world.isClientSide) {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        int byte0 = 3;
        if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
            byte0 = 3;
        }
        if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
            byte0 = 2;
        }
        if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
            byte0 = 5;
        }
        if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.isClientSide) {
            return true;
        }
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
        player.displayGUIDispenser(tileentitydispenser);
        return true;
    }

    private void dispenseItem(World world, int x, int y, int z, Random random) {
        int l = world.getBlockMetadata(x, y, z);
        int x1 = 0;
        int z1 = 0;
        if (l == 3) {
            z1 = 1;
        } else if (l == 2) {
            z1 = -1;
        } else {
            x1 = l == 5 ? 1 : -1;
        }
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
        ItemStack itemstack = tileentitydispenser.getRandomStackFromInventory();
        double d = (double)x + (double)x1 * 0.6 + 0.5;
        double d1 = (double)y + 0.5;
        double d2 = (double)z + (double)z1 * 0.6 + 0.5;
        if (itemstack == null) {
            world.playSoundEffect(1001, x, y, z, 0);
        } else {
            if (itemstack.itemID == Item.ammoArrow.id || itemstack.itemID == Item.ammoArrowGold.id) {
                EntityArrow entityarrow = itemstack.itemID == Item.ammoArrow.id ? new EntityArrow(world, d, d1, d2, 0) : new EntityArrowGolden(world, d, d1, d2);
                entityarrow.setArrowHeading(x1, 0.1f, z1, 1.1f, 6.0f);
                entityarrow.doesArrowBelongToPlayer = true;
                world.entityJoinedWorld(entityarrow);
                world.playSoundEffect(1002, x, y, z, 0);
            } else if (itemstack.itemID == Item.ammoChargeExplosive.id) {
                EntityCannonball entitycannonball = new EntityCannonball(world, d, d1, d2);
                entitycannonball.setCannonballHeading(x1, 0.1, z1, 1.1f, 6.0f);
                world.entityJoinedWorld(entitycannonball);
                world.playSoundEffect(1002, x, y, z, 0);
            } else if (itemstack.itemID == Item.eggChicken.id) {
                EntityEgg entityegg = new EntityEgg(world, d, d1, d2);
                entityegg.setEggHeading(x1, 0.1, z1, 1.1f, 6.0f);
                world.entityJoinedWorld(entityegg);
                world.playSoundEffect(1002, x, y, z, 0);
            } else if (itemstack.itemID == Item.ammoSnowball.id) {
                EntitySnowball entitysnowball = new EntitySnowball(world, d, d1, d2);
                entitysnowball.setSnowballHeading(x1, 0.1, z1, 1.1f, 6.0f);
                world.entityJoinedWorld(entitysnowball);
                world.playSoundEffect(1002, x, y, z, 0);
            } else if (itemstack.itemID == Item.ammoPebble.id) {
                EntityPebble pebble = new EntityPebble(world, d, d1, d2);
                pebble.setSnowballHeading(x1, 0.1, z1, 1.1f, 6.0f);
                world.entityJoinedWorld(pebble);
                world.playSoundEffect(1002, x, y, z, 0);
            } else if (itemstack.itemID == Item.dye.id && itemstack.getMetadata() == 15) {
                int blockX = x + x1;
                int blockY = y;
                int blockZ = z + z1;
                int j1;
                int id = world.getBlockId(blockX, blockY, blockZ);
                int meta = world.getBlockMetadata(blockX, blockY, blockZ);
                if (Block.blocksList[id] instanceof BlockSaplingBase) {
                    if (!world.isClientSide) {
                        ((BlockSaplingBase)Block.blocksList[id]).growTree(world, blockX, blockY, blockZ, world.rand);
                    }
                }
                if (Block.blocksList[id] instanceof BlockSugarcane) {
                    if (!world.isClientSide) {
                        ((BlockSugarcane)Block.blocksList[id]).growReedOnTop(world, blockX, blockY, blockZ);
                    }
                }
                if (Block.blocksList[id] instanceof BlockCactus) {
                    if (!world.isClientSide) {
                        ((BlockCactus)Block.blocksList[id]).growCactusOnTop(world, blockX, blockY, blockZ);
                    }
                }
                if (id == Block.dirt.id) {
                    if (!world.isClientSide && Block.lightOpacity[world.getBlockId(blockX, blockY + 1, blockZ)] <= 2) {
                        int grass = Block.grass.id;
                        if (world.dimensionData.getWorldType() == WorldTypes.OVERWORLD_RETRO) {
                            grass = Block.grassRetro.id;
                        }
                        world.setBlockWithNotify(blockX, blockY, blockZ, grass);
                    }
                }
                if (id == Block.dirtScorched.id && !world.isClientSide && Block.lightOpacity[world.getBlockId(blockX, blockY + 1, blockZ)] <= 2) {
                    int grass = Block.grassScorched.id;
                    world.setBlockWithNotify(blockX, blockY, blockZ, grass);
                }
                if (id == Block.cropsWheat.id && meta < 7) {
                    if (!world.isClientSide) {
                        ((BlockCrops)Block.cropsWheat).fertilize(world, blockX, blockY, blockZ);
                    }
                }
                if (id == Block.mossStone.id || id == Block.mossLimestone.id || id == Block.mossGranite.id || id == Block.mossBasalt.id) {
                    if (!world.isClientSide) {
                        for (j1 = 0; j1 < 32; ++j1) {
                            int k1 = blockX;
                            int l1 = blockY;
                            int i2 = blockZ;
                            for (int j2 = 0; j2 < j1 / 16; ++j2) {
                                k1 += random.nextInt(3) - 1;
                                l1 += (random.nextInt(3) - 1) * random.nextInt(3) / 2;
                                i2 += random.nextInt(3) - 1;
                            }
                            if (Block.isBuried(world, k1, l1, i2) || world.getBlockLightValue(k1, l1 + 1, i2) > 5 || world.getBlockLightValue(k1, l1 - 1, i2) > 5 || world.getBlockLightValue(k1 + 1, l1, i2) > 5 || world.getBlockLightValue(k1 - 1, l1, i2) > 5 || world.getBlockLightValue(k1, l1, i2 - 1) > 5 || world.getBlockLightValue(k1, l1, i2 + 1) > 5) continue;
                            if (world.getBlockId(k1, l1, i2) == Block.stone.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.mossStone.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) == Block.limestone.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.mossLimestone.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) == Block.granite.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.mossGranite.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) == Block.basalt.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.mossBasalt.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) == Block.cobbleStone.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.cobbleStoneMossy.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) == Block.brickStonePolished.id) {
                                world.setBlockWithNotify(k1, l1, i2, Block.brickStonePolishedMossy.id);
                                continue;
                            }
                            if (world.getBlockId(k1, l1, i2) != Block.logOak.id) continue;
                            world.setBlockWithNotify(k1, l1, i2, Block.logOakMossy.id);
                        }
                    }
                }
                if (Block.blocksList[id] != null && Block.blocksList[id].hasTag(BlockTags.GROWS_FLOWERS)) {
                    if (!world.isClientSide) {

                        block2: for (j1 = 0; j1 < 128; ++j1) {
                            int k1 = blockX;
                            int l1 = blockY + 1;
                            int i2 = blockZ;
                            for (int j2 = 0; j2 < j1 / 16; ++j2) {
                                int id1 = world.getBlockId(k1 += random.nextInt(3) - 1, (l1 += (random.nextInt(3) - 1) * random.nextInt(3) / 2) - 1, i2 += random.nextInt(3) - 1);
                                if (Block.blocksList[id1] == null || !Block.blocksList[id1].hasTag(BlockTags.GROWS_FLOWERS)) continue block2;
                            }
                            if (world.getBlockId(k1, l1, i2) != 0) continue;
                            if ( random.nextInt(10) != 0) {
                                if (world.getBlockId(k1, l1 - 1, i2) == Block.dirtScorched.id || world.getBlockId(k1, l1 - 1, i2) == Block.dirtScorchedRich.id) {
                                    world.setBlockWithNotify(k1, l1, i2, Block.spinifex.id);
                                    continue;
                                }
                                world.setBlockWithNotify(k1, l1, i2, Block.tallgrass.id);
                                continue;
                            }
                            if ( random.nextInt(3) != 0) {
                                world.setBlockWithNotify(k1, l1, i2, Block.flowerYellow.id);
                                continue;
                            }
                            world.setBlockWithNotify(k1, l1, i2, Block.flowerRed.id);
                        }
                    }
                }
            } else {
                EntityItem entityitem = new EntityItem(world, d, d1 - 0.3, d2, itemstack);
                double d3 = random.nextDouble() * 0.1 + 0.2;
                entityitem.xd = (double)x1 * d3;
                entityitem.yd = 0.2f;
                entityitem.zd = (double)z1 * d3;
                entityitem.xd += random.nextGaussian() * (double)0.0075f * 6.0;
                entityitem.yd += random.nextGaussian() * (double)0.0075f * 6.0;
                entityitem.zd += random.nextGaussian() * (double)0.0075f * 6.0;
                world.entityJoinedWorld(entityitem);
                world.playSoundEffect(1000, x, y, z, 0);
            }
            world.playSoundEffect(2000, x, y, z, x1 + 1 + (z1 + 1) * 3);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower()) {
            boolean flag;
            boolean bl = flag = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
            if (flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            this.dispenseItem(world, x, y, z, rand);
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDispenser();
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        if (world.getBlockTileEntity(x, y, z) != null) {
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getBlockTileEntity(x, y, z);
            for (int l = 0; l < tileentitydispenser.getSizeInventory(); ++l) {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
                if (itemstack == null) continue;
                float f = this.random.nextFloat() * 0.8f + 0.1f;
                float f1 = this.random.nextFloat() * 0.8f + 0.1f;
                float f2 = this.random.nextFloat() * 0.8f + 0.1f;
                while (itemstack.stackSize > 0) {
                    int i1 = this.random.nextInt(21) + 10;
                    if (i1 > itemstack.stackSize) {
                        i1 = itemstack.stackSize;
                    }
                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world, (float)x + f, (float)y + f1, (float)z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                    float f3 = 0.05f;
                    entityitem.xd = (float)this.random.nextGaussian() * f3;
                    entityitem.yd = (float)this.random.nextGaussian() * f3 + 0.2f;
                    entityitem.zd = (float)this.random.nextGaussian() * f3;
                    world.entityJoinedWorld(entityitem);
                }
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }
}

