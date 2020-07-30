package $group__.client.gui.structures;

import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

import static $group__.utilities.specific.ComparableUtilities.greaterThanOrEqualTo;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum EnumGuiState {
	NEW {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(NEW, READY, DESTROYED); }
	},
	READY {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(READY, CLOSED, DESTROYED); }
	},
	CLOSED {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(CLOSED, NEW, DESTROYED); }
	},
	DESTROYED {
		@Override
		public EnumSet<EnumGuiState> getValidNextStates() { return EnumSet.of(DESTROYED); }
	};

	public abstract EnumSet<EnumGuiState> getValidNextStates();

	public boolean isReachedBy(EnumGuiState state) { return greaterThanOrEqualTo(state, this); }
}
