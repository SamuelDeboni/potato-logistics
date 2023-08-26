package deboni.potatologistics;

import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import java.util.Arrays;
import java.util.List;

public class TileEntityRendererPipe extends TileEntityRenderer<TileEntityPipe> {
    private static final ItemEntityRenderer itemEntityRenderer = new ItemEntityRenderer();
    private EntityItem itemEntity = null;

    @Override
    public void onWorldChanged(World world) {
        itemEntityRenderer.setRenderDispatcher(EntityRenderDispatcher.instance);
        super.onWorldChanged(world);
    }

    @Override
    public void doRender(TileEntityPipe tileEntity, double x, double y, double z, float g) {

        if (itemEntity == null)  {
            itemEntity = new EntityItem(tileEntity.worldObj, 0, 0, 0, null);
            itemEntity.setRot(0, 0);
            itemEntity.entityBrightness = 1;
            itemEntity.age = 0;
            itemEntity.field_804_d = 0;
        }

        List<float[]> blockPos = tileEntity.getStacksInPipePosition();
        List<ItemStack> stacks = tileEntity.getStacksInPipe();

        for (int i = 0; i < blockPos.size(); i++) {
            float[] pos = blockPos.get(i);
            ItemStack stack = stacks.get(i);
            itemEntity.item = stack;

            double yOffset = stack.itemID < Block.blocksList.length ? 0.1 : 0.3;
            itemEntityRenderer.doRenderItem(itemEntity, x + pos[0], y + pos[1] - yOffset, z + pos[2], 0, 0);
        }
    }
}
