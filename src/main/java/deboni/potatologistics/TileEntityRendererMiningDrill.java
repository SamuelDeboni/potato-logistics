package deboni.potatologistics;

import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererMiningDrill extends TileEntityRenderer<TileEntityMiningDrill> {

    @Override
    public void onWorldChanged(World world) {
        super.onWorldChanged(world);
    }
    @Override
    public void doRender(TileEntityMiningDrill tileEntity, double x, double y, double z, float g) {
        if (tileEntity.blocksToBreak.isEmpty()) return;

        double x1 = 0.5;
        double y1 = 0;
        double z1 = 0.5;
        int[] b = tileEntity.blocksToBreak.get(tileEntity.blocksToBreak.size() - 1);
        double x2 = b[0] - tileEntity.xCoord + 0.5;
        double y2 = b[1] - tileEntity.yCoord + 1.0;
        double z2 = b[2] - tileEntity.zCoord + 0.5;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 0, 0, 1);
        GL11.glLineWidth(10.0F);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x2, y2, z2);
        tessellator.draw();
        GL11.glPopMatrix();

        GL11.glPopAttrib();
    }
}
