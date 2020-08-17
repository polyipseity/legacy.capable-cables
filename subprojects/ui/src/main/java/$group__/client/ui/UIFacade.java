package $group__.client.ui;

import $group__.client.ui.mvvm.core.binding.IBinder;
import $group__.client.ui.mvvm.minecraft.UIInfrastructureMinecraft;
import $group__.client.ui.mvvm.minecraft.adapters.UIAdapterScreen;
import $group__.client.ui.mvvm.minecraft.core.IUIInfrastructureMinecraft;
import $group__.client.ui.mvvm.minecraft.core.IUIViewModelMinecraft;
import $group__.client.ui.mvvm.minecraft.core.views.IUIViewMinecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO more methods should be created
public enum UIFacade {
	;

	@OnlyIn(Dist.CLIENT)
	public enum UFMinecraft {
		;

		public static <C extends Container> UIAdapterScreen.WithContainer<? extends IUIInfrastructureMinecraft<?, ?, ?>, C> createScreen(ITextComponent title, IUIInfrastructureMinecraft<?, ?, ?> infrastructure, C container) {
			return new UIAdapterScreen.WithContainer<>(title, infrastructure, container); // TODO would prefer a more abstract return type
		}

		public static Screen createScreen(ITextComponent title, IUIInfrastructureMinecraft<?, ?, ?> infrastructure) {
			return new UIAdapterScreen<>(title, infrastructure);
		}

		public static IUIInfrastructureMinecraft<?, ?, ?> createInfrastructure(IUIViewMinecraft<?> view, IUIViewModelMinecraft<?> viewModel, IBinder binder) {
			return new UIInfrastructureMinecraft<>(view, viewModel, binder);
		}
	}

	public enum UFResources {

		/* TODO find the best way to find the most suitable parser to parse the resource
		private static final Set<String> PACKAGES = Collections.newSetFromMap(
				MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap()
		);


		public static <T, R> IUIResourceParser<T, R> parseUIResource(T target, R resource) {
			for (String pkg : PACKAGES) {
				DynamicUtilities.REFLECTIONS_CACHE.getUnchecked(pkg).getSubTypesOf(IUIResourceParser.class);
			}
		}

		@SuppressWarnings("UnusedReturnValue")
		public static boolean registerUIResourceParserPackage(String packageName) { return PACKAGES.add(packageName); }

		static {
			registerUIResourceParserPackage(IUIResourceParser.class.getPackage().getName());
		}
		 */
	}
}
