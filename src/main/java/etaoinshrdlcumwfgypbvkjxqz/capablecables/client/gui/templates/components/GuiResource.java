package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.templates.components;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.Color;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Frame;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.Rectangle;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.polygons.XY;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class GuiResource<N extends Number, T extends Number> extends GuiRectangle<N> {
    protected Frame<N> padding;
    protected ResourceLocation resource;
    protected Rectangle<T> texture;

    public GuiResource(Rectangle<N> rect, Frame<N> padding, ResourceLocation resource, Rectangle<T> texture) {
        super(rect, Color.TRANSPARENT_I);
        this.padding = padding;
        this.resource = resource;
        this.texture = texture;
    }

    public void setPadding(Frame<N> padding) { this.padding = padding; }
    public Frame<N> getPadding() { return padding; }
    public void setResource(ResourceLocation resource) { this.resource = resource; }
    public ResourceLocation getResource() { return resource; }
    public void setTexture(Rectangle<T> texture) { this.texture = texture; }
    public Rectangle<T> getTexture() { return texture; }

    /** {@inheritDoc} */
    @Override
    public void setColor(Color color) { throw rejectUnsupportedOperation(); }

    /** {@inheritDoc} */
    @Override
    public void draw(Minecraft minecraft) {
        minecraft.getTextureManager().bindTexture(resource);

        XY<N> rectOffset = rect.getOffset();
        XY<N> rectSize = rect.getSize();
        XY<T> textureOffset = texture.getOffset();
        XY<T> textureSize = texture.getSize();
        drawModalRectWithCustomSizedTexture(rectOffset.getX().intValue(), rectOffset.getY().intValue(), textureOffset.getX().floatValue(), textureOffset.getY().floatValue(), rectSize.getX().intValue(), rectSize.getY().intValue(), textureSize.getX().floatValue(), textureSize.getY().floatValue());
    }

      /** {@inheritDoc} */
    @Override
    public GuiResource<N, T> clone() {
        GuiResource<N, T> r;
        try { r = (GuiResource<N, T>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); }
        r.padding = padding.clone();
        r.resource = resource;
        r.texture = texture.clone();
        return r;
    }
      /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiResource)) return false;
        if (!super.equals(o)) return false;
        GuiResource<?, ?> that = (GuiResource<?, ?>) o;
        return padding.equals(that.padding) &&
                resource.equals(that.resource) &&
                texture.equals(that.texture);
    }
      /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), padding, resource, texture); }

      /** {@inheritDoc} */
    @Override
    public GuiResource<N, T> toImmutable() { return new Immutable<>(this); }
    @javax.annotation.concurrent.Immutable
    public static class Immutable<N extends Number, T extends Number> extends GuiResource<N, T> {
        public Immutable(Rectangle<N> rect, Frame<N> padding, ResourceLocation resource, Rectangle<T> texture) { super(rect.toImmutable(), padding.toImmutable(), resource, texture.toImmutable()); }
        public Immutable(GuiResource<N, T> c) { super(c.rect, c.padding, c.resource, c.texture); }

         /** {@inheritDoc} */
        @Override
        public GuiResource.Immutable<N, T> clone() { try { return (GuiResource.Immutable<N, T>) super.clone(); } catch (ClassCastException ex) { throw unexpectedThrowable(ex); } }

         /** {@inheritDoc} */
        @Override
        public GuiResource.Immutable<N, T> toImmutable() { return this; }
         /** {@inheritDoc} */
        @Override
        public boolean isImmutable() { return true; }
    }
}
