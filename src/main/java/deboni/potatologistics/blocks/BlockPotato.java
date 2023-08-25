package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockPotato extends Block {
    public boolean isPowered;

    public BlockPotato(String key, int id, Material material) {
        super(key, id, material);
        isPowered = false;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.isPowered = !this.isPowered;

        if (player.getHeldItem().getItem().id == PotatoLogisticsMod.wrench.id) {
            world.setBlockMetadataWithNotify(x, y, z, this.isPowered ? 1 : 0);

            world.playSoundEffect(SoundType.WORLD_SOUNDS, (double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "random.click", 0.3f, isPowered ? 0.5f : 0.6f);
            world.notifyBlocksOfNeighborChange(x, y, z, this.id);
            return true;
        }
        return false;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (isPowered) {
            double d =  (double)((float)x + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d1 = (double)((float)y + 0.4f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d2 = (double)((float)z + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;

            world.spawnParticle("reddust", d , d1, d2 , 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        return (blockAccess.getBlockMetadata(x, y, z)) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        return this.isPoweringTo(world, x, y, z, side);
    }

    /*
    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(PotatoMod.potato)};
    }
    */
}
