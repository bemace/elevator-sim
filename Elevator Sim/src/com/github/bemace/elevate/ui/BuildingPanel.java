package com.github.bemace.elevate.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.ElevatorCar;
import com.github.bemace.elevate.Floor;

public class BuildingPanel extends JPanel {
	private Building building;
	private BigDecimal lowestFloorElevation;
	private ElevatorShaftPanel shaftPanel;

	public BuildingPanel(Building building) {
		super(new BorderLayout());
		this.building = building;
		setBackground(Color.GRAY);
		lowestFloorElevation = building.getFloor(building.getMinFloorIndex()).getElevation();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 0;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		shaftPanel = new ElevatorShaftPanel();
		add(shaftPanel, BorderLayout.EAST);

		JPanel floorsPanel = new JPanel(new BuildingLayoutManager(building));
		floorsPanel.setOpaque(false);
		add(floorsPanel, BorderLayout.CENTER);

		List<Floor> floors = building.getFloors();
		Collections.reverse(floors);
		for (Floor floor : floors) {
			floorsPanel.add(new FloorPanel(floor), floor.getId());
		}

	}

	protected int convertElevationToY(BigDecimal elevation) {
		return getHeight()
				- 1
				- elevation.subtract(building.getModel().getBaseElevation()).multiply(SimulatorInterface.SCALE)
						.intValue();
	}

	protected static int convertDistanceToPixels(BigDecimal distance) {
		return distance.multiply(SimulatorInterface.SCALE).intValue();
	}

	class FloorPanel extends JPanel {
		private Floor floor;
		private JLabel label;

		public FloorPanel(Floor floor) {
			setLayout(new BorderLayout());
			this.floor = floor;
			label = new JLabel(floor.getId());
			label.setFont(label.getFont().deriveFont(16.0f));
			add(label, BorderLayout.WEST);
			// setOpaque(false);

			JPanel callPanel = new JPanel(new GridLayout(2, 1));

			JButton up = new JButton();
			up.setToolTipText("Summon elevator to go up");
			URL url = ClassLoader.getSystemResource("Call-Up-Dark.png");
			if (url != null)
				up.setIcon(new ImageIcon(url));
			else
				up.setText("Up");

			JButton down = new JButton();
			down.setToolTipText("Summon elevator to go down");
			url = ClassLoader.getSystemResource("Call-Down-Dark.png");
			if (url != null)
				down.setIcon(new ImageIcon(url));
			else
				down.setText("Dn");

			callPanel.add(up);
			callPanel.add(down);
			add(callPanel, BorderLayout.EAST);
		}

		public Floor getFloor() {
			return floor;
		}

	}

	class ElevatorShaftPanel extends JComponent {
		private Dimension size;
		private int leftGap;
		private int carWidth;

		public ElevatorShaftPanel() {
			size = new Dimension(convertDistanceToPixels(new BigDecimal(8)), building.getHeight()
					.multiply(SimulatorInterface.SCALE).intValue());
			setMinimumSize(size);
			setPreferredSize(size);
			setMaximumSize(size);
			setOpaque(true);
			leftGap = convertDistanceToPixels(new BigDecimal(".5"));
			carWidth = getWidth() - leftGap - leftGap;
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, getSize().width, size.height);
			// g.setColor(Color.RED);
			// g.drawLine(0, 0, getWidth() - 1, 0);

			for (Floor floor : building.getFloors()) {
				g.setColor(Color.DARK_GRAY);
				int floorY = convertElevationToY(floor.getElevation());
				int height = convertDistanceToPixels(floor.getHeight());
				int ceilY = convertElevationToY(floor.getElevation().add(floor.getHeight()));
				// System.out.println("Floor " + floor.getId() + ": " + floorY +
				// " up to " + ceilY + " (" + height + ")");
				g.fillRect(0, ceilY, getWidth() - 1, height);
				// g.setColor(Color.GREEN);
				// g.drawLine(0, ceilY, getWidth() - 1, ceilY);
				// g.setColor(Color.RED);
				// g.drawLine(0, floorY, getWidth() - 1, floorY);

				// break;
			}

			ElevatorCar car = building.getElevatorCar();

			int roofY = convertElevationToY(car.getElevation().add(car.getHeight()));
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(getWidth() / 2 - 1, 0, 2, roofY);

			carWidth = getWidth() - leftGap - leftGap;

			g.setColor(Color.BLUE);
			g.fillRect(leftGap, roofY, carWidth, convertDistanceToPixels(car.getHeight()));
		}
	}

}
