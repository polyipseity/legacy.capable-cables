package $group__.common.configurations.components;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Map;

public class ConfigurationI18n {
	protected String translationKey;
	@Nullable
	protected WeakReference<ConfigurationI18n> parent;
	protected Map<String, ConfigurationI18n> children;

	public ConfigurationI18n(String translationKey, Map<String, ConfigurationI18n> children) {
		this.translationKey = translationKey;
		this.children = children;
		children.forEach((s, c) -> c.parent = new WeakReference<>(this));
	}
}
