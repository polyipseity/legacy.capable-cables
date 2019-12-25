package io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.blocks;

import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.Registrables;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public final class BlocksOwn extends Registrables<Block> {
    public BlocksOwn() { super(Block.class); }
    public static BlocksOwn getInstance() { return getInstance(BlocksOwn.class); }

    @Override
    @SubscribeEvent
    public void register(RegistryEvent.Register<Block> e) {
        super.register(e);
    }
}
