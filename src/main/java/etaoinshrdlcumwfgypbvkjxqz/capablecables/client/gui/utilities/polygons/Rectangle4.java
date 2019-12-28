package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectArguments;

@SideOnly(Side.CLIENT)
public class Rectangle4<T> extends PolygonN<T> {
    @SafeVarargs
    public Rectangle4(T... e) {
        super(e);
        if (e.length != 4) //noinspection ConfusingArgumentToVarargsMethod
            throw rejectArguments(e);
    }

    public T a() { return get(0); }
    public T b() { return get(1); }
    public T c() { return get(2); }
    public T d() { return get(3); }

    public static class i extends Rectangle4<Integer> {
        public i(int... e) { super(Arrays.stream(e).boxed().toArray(Integer[]::new)); }
    }
}
