package it.Maida.Flows.GFX;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	private JComboBox<Integer> gridSizeSelector;
	private JButton okButton;
	private FlowsPanel flowsPanel;
	private final int MAXIMUM_SIZE = 10;
	private BufferedImage homeImage;

	public MainPanel() {

		this.gridSizeSelector = new JComboBox();
		this.okButton = new JButton("OK");

		try {
			this.homeImage = ImageIO.read(new File("resources/HomeFlow.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int k = 5;
		while (k <= MAXIMUM_SIZE) {
			this.gridSizeSelector.addItem(k++);
		}

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				flowsPanel = new FlowsPanel(
						(int) Integer.parseInt(gridSizeSelector
								.getSelectedItem().toString()));
				MainFrame.getInstance().switchToPanel(flowsPanel);
			}
		});

		this.add(gridSizeSelector);
		this.add(okButton);
		repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {

		okButton.setLocation(this.getWidth() / 2, this.getHeight() / 2);
		this.gridSizeSelector.setLocation(this.getWidth() / 2 - 50,
				this.getHeight() / 2);
		g.drawImage(this.homeImage, 0, 0, this.getWidth(), this.getHeight(),
				null);

	}
}
