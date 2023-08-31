package deboni.potatologistics;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;

public class PipeStack {
    public ItemStack stack;
    public Direction direction;
    public int timer = 0;

    public PipeStack(ItemStack stack, Direction direction, int timer) {
        this.stack = stack;
        this.direction = direction;
        this.timer = timer;
    }

    public PipeStack() {
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        nbttagcompound.putShort("direction", (short) this.direction.getId());
        nbttagcompound.putShort("timer", (short) this.timer);
        stack.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        this.direction = Direction.getDirectionById(nbttagcompound.getShort("direction"));
        this.timer = nbttagcompound.getShort("timer");
        this.stack = ItemStack.readItemStackFromNbt(nbttagcompound);
    }

    public static PipeStack readPipeStackFromNbt(CompoundTag nbt) {
        if (nbt == null) {
            return null;
        }
        PipeStack stack = new PipeStack();
        stack.readFromNBT(nbt);
        return stack;
    }
}
