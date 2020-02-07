package $group__.$modId__.client.gui.components;

import $group__.$modId__.client.gui.themes.ITheme;
import $group__.$modId__.client.gui.traits.IColored;
import $group__.$modId__.client.gui.traits.IDrawable;
import $group__.$modId__.client.gui.traits.IThemed;
import $group__.$modId__.traits.IStructure;
import $group__.$modId__.traits.basic.ILogging;
import $group__.$modId__.traits.extensions.ICloneable;
import net.minecraft.client.gui.Gui;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;

public class GuiComposed<T extends GuiComposed<T, TH>, TH extends ITheme<TH>> extends Gui implements IStructure<T>, ICloneable<T>, IDrawable, IColored<Color>, IThemed<TH>, ILogging<Logger> {
	/* SECTION variables */

	@Nullable protected IDrawable drawable;
	@Nullable protected IColored<Color> colored;
	@Nullable protected IThemed<TH> themed;
	@Nullable protected ILogging<Logger> logging;


	/* SECTION constructors */

	public GuiComposed(IDrawable drawable, IColored<Color> colored, IThemed<TH> themed, ILogging<Logger> logging) {
		this.drawable = drawable;
		this.colored = colored;
		this.themed = themed;
		this.logging = logging;
	}


	/* SECTION getters & setters */

	@Override
	public Optional<Color> getColor() { return colored == null ? Optional.empty() : colored.getColor(); }

	@Override
	public boolean setColor(@Nullable Color theme) { return colored != null && colored.setColor(theme); }

	@Override
	public Optional<TH> getTheme() { return themed == null ? Optional.empty() : themed.getTheme(); }

	@Override
	public boolean setTheme(@Nullable TH theme) { return themed != null && themed.setTheme(theme); }

	@Override
	public Optional<Logger> getLogger() { return logging == null ? Optional.empty() : logging.getLogger(); }

	@Override
	public boolean setLogger(@Nullable Logger logger) { return logging != null && logging.setLogger(logger); }


	/* SECTION methods */

	@Override
	public boolean draw() { return drawable != null && drawable.draw(); }


	@Override
	public T toImmutable() {
		return null;
	}

	@Override
	public boolean isImmutable() { return false; }


	@Override
	public T clone() { return ICloneable.clone(super::clone, unboxOptional(getLogger())); }
}
