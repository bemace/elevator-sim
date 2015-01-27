package com.github.bemace.elevate.ui;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.github.bemace.elevate.Direction;
import com.github.bemace.elevate.Floor;

class CallButton extends JButton {
	private static boolean iconsInitialized = false;
	private static ImageIcon upDark;
	private static ImageIcon downDark;
	private static ImageIcon upLit;
	private static ImageIcon downLit;

	private Floor floor;
	private Direction direction;
	private boolean isLit;

	public CallButton(Floor floor, Direction dir) {
		this.floor = floor;
		this.direction = dir;
		setToolTipText("Summon elevator to go " + dir);
		setIcon(getIcon(dir, isLit));
	}

	public boolean isLit() {
		return isLit;
	}

	public void setLit(boolean b) {
		if (b == isLit)
			return;

		isLit = b;
		setIcon(getIcon(direction, isLit));
	}

	public ImageIcon getIcon(Direction dir, boolean lit) {
		if (!iconsInitialized) {
			URL url = ClassLoader.getSystemResource("Call-Up-Dark.png");
			if (url != null)
				upDark = new ImageIcon(url);

			url = ClassLoader.getSystemResource("Call-Up-Lit.png");
			if (url != null)
				upLit = new ImageIcon(url);

			url = ClassLoader.getSystemResource("Call-Down-Dark.png");
			if (url != null)
				downDark = new ImageIcon(url);

			url = ClassLoader.getSystemResource("Call-Down-Lit.png");
			if (url != null)
				downLit = new ImageIcon(url);

			iconsInitialized = true;
		}

		if (dir == Direction.UP)
			return lit ? upLit : upDark;
		else
			return lit ? downLit : downDark;
	}
}