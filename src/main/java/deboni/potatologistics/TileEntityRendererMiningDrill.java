package deboni.potatologistics;

import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
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

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);

        if (!tileEntity.blocksToBreak.isEmpty()) {
            double x1 = 0.5;
            double y1 = 0;
            double z1 = 0.5;
            int[] b = tileEntity.blocksToBreak.get(tileEntity.blocksToBreak.size() - 1);
            double x2 = b[0] - tileEntity.x + 0.5;
            double y2 = b[1] - tileEntity.y + 1.0;
            double z2 = b[2] - tileEntity.z + 0.5;

            Util.draw3dLine(0.5, x1, y1, z1, x2, y2, z2, 4.0f, 0.0f, 0.0f);
        }

        double radius = 10.5;

        double wHeight = -tileEntity.y;

        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        Util.draw3dLine(0.1, -radius - 0.05, 0, -radius,  radius + 0.05, 0, -radius, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1, -radius, 0, -radius - 0.05, -radius, 0,  radius + 0.05, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1,  radius + 0.05, 0,  radius, -radius - 0.05, 0,  radius, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1,  radius, 0,  radius + 0.05,  radius, 0, -radius - 0.05, 0.0f, 0.0f, 1.0f);

        Util.draw3dLine(0.1, -radius, 0.05, -radius, -radius, wHeight, -radius, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1, -radius, 0.05,  radius, -radius, wHeight,  radius, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1,  radius, 0.05, -radius,  radius, wHeight, -radius, 0.0f, 0.0f, 1.0f);
        Util.draw3dLine(0.1,  radius, 0.05,  radius,  radius, wHeight,  radius, 0.0f, 0.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
