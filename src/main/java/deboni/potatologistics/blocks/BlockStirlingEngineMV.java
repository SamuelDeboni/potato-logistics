package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityStirlingEngine;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;

public class BlockStirlingEngineMV extends BlockTileEntity {
    public BlockStirlingEngineMV(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        TileEntityStirlingEngine tile = new TileEntityStirlingEngine();
        tile.maxEnergy = 32;
        tile.maxTemperature = 1600;
        return tile;
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
