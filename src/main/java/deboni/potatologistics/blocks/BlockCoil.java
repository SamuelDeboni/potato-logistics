package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityCoil;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;

public class BlockCoil extends BlockTileEntity {
    public BlockCoil(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCoil();
    }
}
