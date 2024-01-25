package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.item.ItemStack;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class BurnerTooltip extends TileTooltip<TileEntityBurner> {
    @Override
    public void initTooltip() {
        addClass(TileEntityBurner.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityBurner burner, AdvancedInfoComponent advancedInfoComponent) {
        ItemStack fuel = burner.getStackInSlot(0);
        advancedInfoComponent.drawStringWithShadow("Burn time: " + burner.currentBurnTime + "t", 0);
        ItemStack[] stacks = new ItemStack[]{fuel};
        advancedInfoComponent.drawItemList(stacks, 0);
    }
}
