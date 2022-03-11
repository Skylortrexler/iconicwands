package website.skylorbeck.minecraft.iconicwands.blocks;

import net.minecraft.block.LightBlock;

import java.util.Collection;
import java.util.Iterator;

public class TimedLightBlock extends LightBlock {
    public TimedLightBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL_15, 7).with(WATERLOGGED, false));
    }

    protected static <T> T getPrev(Collection<T> values, T value) {
        Iterator<T> iterator = values.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().equals(value)) continue;
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return values.iterator().next();
        }
        return iterator.next();
    }
}
