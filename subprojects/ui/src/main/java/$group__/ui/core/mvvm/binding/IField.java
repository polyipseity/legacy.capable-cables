package $group__.ui.core.mvvm.binding;

import $group__.utilities.ThrowableUtilities;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IField<T> {
	static <T> T getValueNonnull(IField<? extends T> field) {
		return field.getValue()
				.orElseThrow(() ->
						ThrowableUtilities.BecauseOf.illegalArgument("Unexpected 'null'",
								"field", field));
	}

	Optional<? extends T> getValue();

	void setValue(@Nullable T value);
}
