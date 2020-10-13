package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IRecordCandidate;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractNamed
		implements INamed, IRecordCandidate {
	@Nullable
	private final String name;

	public AbstractNamed(@Nullable String name) { this.name = name; }

	@Override
	public Optional<? extends String> getName() { return Optional.ofNullable(name); }
}
