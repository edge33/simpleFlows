package it.Maida.Flows.GFX;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	private JComboBox<Integer> gridSizeSelector;
	private JButton okButton;
	private FlowsPanel flowsPanel;
	private final int MAXIMUM_SIZE = 10;

	public MainPanel() {

		this.gridSizeSelector = new JComboBox();
		this.okButton = new JButton("OK");

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
	}

}
