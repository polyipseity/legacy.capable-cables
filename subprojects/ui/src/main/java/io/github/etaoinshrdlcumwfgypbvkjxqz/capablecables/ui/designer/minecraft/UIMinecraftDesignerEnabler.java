package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.minecraft;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public enum UIMinecraftDesignerEnabler {
	;

	private static final TextComponent DISPLAY_NAME = MinecraftTextComponentUtilities.getEmpty();

	public static Block getBlockEntry() {
		return DesignerBlock.getInstance();
	}

	public static Item getItemEntry() {
		return DesignerBlock.getInstanceItem();
	}

	public static TextComponent getDisplayName() {
		return DISPLAY_NAME;
	}

	public static class DesignerBlock
			extends Block {
		private static final Supplier<DesignerBlock> INSTANCE = Suppliers.memoize(DesignerBlock::new);
		private static final Supplier<Item> INSTANCE_ITEM = Suppliers.memoize(() -> new BlockItem(getInstance(), new Item.Properties()));

		public DesignerBlock() {
			super(Block.Properties.from(Blocks.STONE));
		}

		private static DesignerBlock getInstance() {
			return INSTANCE.get();
		}

		private static Item getInstanceItem() {
			return INSTANCE_ITEM.get();
		}

		@SuppressWarnings({"deprecation", "NullableProblems"})
		@Override
		public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
			if (worldIn.isRemote()) {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DistLambdaHolder.Client::openUI);
			}
			return ActionResultType.SUCCESS;
		}

		protected enum DistLambdaHolder {
			;

			protected enum Client {
				;

				protected static void openUI() {
					/* TODO CODE
					MinecraftClientUtilities.getMinecraftNonnull().displayGuiScreen(
							UIFacade.Minecraft.createScreen(getDisplayName(),
									UIFacade.Minecraft.createInfrastructure(

											DesignerViewModel.of(DesignerModel.of()),
											new DefaultBinder()
									))
					);
					 */
				}
			}
		}
	}
}
