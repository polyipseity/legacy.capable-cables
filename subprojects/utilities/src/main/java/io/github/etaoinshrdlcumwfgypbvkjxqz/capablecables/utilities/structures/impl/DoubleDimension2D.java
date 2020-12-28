package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import java.beans.Transient;

public class DoubleDimension2D
		extends AbstractDimension2D {
	private static final long serialVersionUID = 4432299344969417136L;
	private double width, height;

	public DoubleDimension2D() { this(0, 0); }

	public DoubleDimension2D(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public double getWidth() { return width; }

	@Override
	public double getHeight() { return height; }

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Transient
	public DoubleDimension2D getSize() { return new DoubleDimension2D(width, height); }

	@Override
	public DoubleDimension2D clone() { return (DoubleDimension2D) super.clone(); }
}
