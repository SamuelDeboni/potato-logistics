package deboni.potatologistics;

import deboni.potatologistics.blocks.entities.TileEntityEnergyConnector;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TileEntityRendererEnergyConnector extends TileEntityRenderer<TileEntityEnergyConnector> {

    @Override
    public void onWorldChanged(World world) {
        super.onWorldChanged(world);
    }
    @Override
    public void doRender(TileEntityEnergyConnector tileEntity, double x, double y, double z, float g) {
        if (tileEntity.connections.isEmpty()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);

        Tessellator tessellator = Tessellator.instance;
        for (TileEntityEnergyConnector.Connection c : tileEntity.connections){
            double x2 = c.x - tileEntity.xCoord;
            double y2 = c.y - tileEntity.yCoord;
            double z2 = c.z - tileEntity.zCoord;

            if (x2 > 0 || x2 == 0 && y2 > 0 || x2 == 0 && y2 == 0 && z2 > 0) continue;

            double dist = Math.sqrt(x2*x2 + y2*y2 + z2*z2);
            double yoff = Math.log(dist + 0.15);

            boolean b = false;
            for (double t = 0.0; t < 0.95; t += 0.1) {
                double tx = t * x2;
                double ty = t * y2;
                double tz = t * z2;

                double yoff0 = -0.8*((t-0.5)*(t-0.5)) + 0.2f;
                double yoff1 = -0.8*((t-0.4)*(t-0.4)) + 0.2f;
                yoff0 *= yoff;
                yoff1 *= yoff;

                float col = b ? 1.0f : 1.5f;
                b = !b;

                Util.draw3dLine(0.05, tx, ty - yoff0 , tz, tx + x2 * 0.1, ty + y2 * 0.1 - yoff1, tz + z2 * 0.1, col, col * 0.5f, col * 0.5f);
            }
        }

        GL11.glPopMatrix();
    }
}
