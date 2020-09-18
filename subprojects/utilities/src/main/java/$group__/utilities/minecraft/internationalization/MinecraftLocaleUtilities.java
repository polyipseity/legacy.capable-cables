package $group__.utilities.minecraft.internationalization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraftforge.fml.DistExecutor;

import java.util.Locale;
import java.util.Optional;

public enum MinecraftLocaleUtilities {
	;

	public static Locale getCurrentLocale() { return DistExecutor.unsafeRunForDist(() -> DistLambdaHolder.Client::getCurrentLocale, () -> Locale::getDefault); }

	public enum DistLambdaHolder {
		;

		public enum Client {
			;

			public static Locale getCurrentLocale() {
				return Optional.ofNullable(Minecraft.getInstance())
						.map(Minecraft::getLanguageManager)
						.map(LanguageManager::getCurrentLanguage)
						.map(Language::getJavaLocale)
						.orElseGet(Locale::getDefault);
			}
		}
	}
}
