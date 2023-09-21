package deboni.potatologistics.gui;

import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBurner extends GuiContainer {

    public String name = "Burner";
    public TileEntityBurner tile;
    public GuiBurner(InventoryPlayer inventoryPlayer, TileEntityBurner tile) {
        super(new ContainerBurner(inventoryPlayer, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/potatologistics/gui/furnace_burner.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        GL11.glEnable(3553);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        j = (this.width - this.xSize) / 2;
        k = (this.height - this.ySize) / 2;
        if (this.tile.isBurning()) {
            int l = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 81, k + 36 + 12 - l, 176, 12 - l, 14, l + 2);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x, y, renderPartialTicks);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 70, 6, 0xFF404040);
    }


    public void initGui()
    {
        super.initGui();
    }
}