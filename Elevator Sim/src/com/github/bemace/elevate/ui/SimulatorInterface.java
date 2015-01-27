package com.github.bemace.elevate.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.ElevatorController;

public class SimulatorInterface extends JFrame {
	private Building building;
	private ElevatorController controller;
	/** Pixels per foot. */
	private static final BigDecimal SCALE = new BigDecimal(6);

	public SimulatorInterface(Building building, ElevatorController controller) {
		super("Elevator Simulator");
		this.building = building;
		this.controller = controller;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		create(getContentPane());

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setMaximumSize(new Dimension(width, height - 100));
	}

	protected void create(Container parent) {
		parent.setLayout(new BorderLayout());

		add(new JScrollPane(new BuildingPanel(building, SCALE)), BorderLayout.CENTER);
	}

	public void launch() {
		pack();
		if (getSize().height > 800)
			setSize(new Dimension(getSize().width, 800));

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
