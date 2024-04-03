package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityHeater;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;

public class BlockHeater extends BlockTileEntity {
    public BlockHeater(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityHeater();
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        super.onBlockRemoved(world, x, y, z, data);

        TileEntity te = world.getBlockTileEntity(x, y + 1, z);
        if (te instanceof TileEntityFurnace) {
            ((TileEntityFurnace)te).maxCookTime = 200;
        }
    }
}
