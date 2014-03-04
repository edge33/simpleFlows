package it.Maida.Flows.GFX;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	private static MainFrame instance;
	private JPanel contentPanel;
	private MainPanel mainPanel;
	
	public static MainFrame getInstance() {
		if ( instance == null )
			instance = new MainFrame();
		return instance;
	}
	
	private MainFrame() {
		super("Flows");
	}
	
	private void init() {
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setSize(850, 610);
		this.setMinimumSize(new Dimension(850,610));
		
		this.mainPanel = new MainPanel();
		
		this.contentPanel = new JPanel(new BorderLayout());
		this.add(contentPanel);

		switchToPanel(mainPanel);
		this.setVisible(true);
	}
	
	public void switchToPanel(final JPanel newPanel) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				contentPanel.removeAll();
				contentPanel.add(newPanel,BorderLayout.CENTER);
				contentPanel.updateUI();
				newPanel.requestFocus();
				newPanel.setVisible(true);
			}
		});
	} 
	
	
	public static void main(String[] args) {
		MainFrame mainFrame = MainFrame.getInstance();
		mainFrame.init();
	}
	
}
