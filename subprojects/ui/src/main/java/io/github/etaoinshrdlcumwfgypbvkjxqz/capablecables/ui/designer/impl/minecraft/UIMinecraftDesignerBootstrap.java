package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.minecraft;

import com.google.common.base.Suppliers;
import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIFacade;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.UIDefaultDesignerModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.UIDefaultDesignerViewHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.UIDefaultDesignerViewModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NonNls;

import java.util.function.Supplier;

public enum UIMinecraftDesignerBootstrap {
	;

	@NonNls
	public static final String PATH = "ui_designer";
	private static final TextComponent DISPLAY_NAME = MinecraftTextComponentUtilities.getEmpty();

	public static Block getBlockEntry() {
		return DesignerBlock.getInstance();
	}

	public static TextComponent getDisplayName() {
		return DISPLAY_NAME;
	}

	public static @NonNls String getPath() {
		return PATH;
	}

	public static class DesignerBlock
			extends Block {
		private static final Supplier<DesignerBlock> INSTANCE = Suppliers.memoize(DesignerBlock::new);

		public DesignerBlock() {
			super(Block.Properties.from(Blocks.STONE));
		}

		private static DesignerBlock getInstance() {
			return INSTANCE.get();
		}

		@SuppressWarnings({"deprecation", "NullableProblems"})
		@Override
		public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
			if (worldIn.isRemote()) {
				// COMMENT client
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DistLambdaHolder.Client::openUI);
			}
			return ActionResultType.SUCCESS;
		}

		protected enum DistLambdaHolder {
			;

			protected enum Client {
				;

				protected static void openUI() {
					MinecraftClientUtilities.getMinecraftNonnull().displayGuiScreen(
							UIFacade.Minecraft.createScreen(getDisplayName(),
									UIFacade.Minecraft.createInfrastructure(
											IUIView.withThemes(UIDefaultDesignerViewHolder.createView(),
													Iterators.singletonIterator(UIDefaultDesignerViewHolder.createTheme())),
											UIDefaultDesignerViewModel.of(UIDefaultDesignerModel.ofDefault()),
											new DefaultBinder()
									))
					);
				}
			}
		}
	}
}
