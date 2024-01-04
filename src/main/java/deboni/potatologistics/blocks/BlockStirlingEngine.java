package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityStirlingEngine;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;

public class BlockStirlingEngine extends BlockTileEntity {
    public BlockStirlingEngine(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityStirlingEngine();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }
}
