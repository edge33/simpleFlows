package it.Maida.Flows.GFX;

import it.Maida.Flows.Core.FlowStruct;
import it.Maida.Flows.DLVhandling.DLVCommand;
import it.Maida.Flows.DLVhandling.DLVSolver;
import it.Maida.Flows.DLVhandling.DlvInvoker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.sun.xml.internal.ws.api.model.MEP;

public class FlowsPanel extends JPanel {

	private final int TILE_SIZE = 100;
	
	private int gridSize;
	private int gridLenght;
	private int offset;
	
	private FlowStruct flowStruct;
	private int colorSelected = FlowStruct.GREEN;
	private Color ardesia = new Color(112, 128, 144);
	private Color amaranto = new Color(229, 43, 80);
	
	private BufferedImage backGround;
	private BufferedImage bridge;
	
	private final int circleWidth = 75;
	private int scaledCircleWidth;
	private double scale;
	private int scaledTile;

	private JButton loadLevel;
	private JButton reset;
	private JButton solveButton;
	private JButton backButton;
	
	private JComboBox<String> sampleLevels;

	public FlowsPanel(final int gridSize) {

		this.setLayout(null);

		this.gridSize = gridSize;
		flowStruct = new FlowStruct(gridSize);
		
		this.solveButton = new JButton("Solve");
		
		
		final File folder = initComboBox(gridSize);
		
		this.solveButton = new JButton("Solve");
		this.loadLevel = new JButton("LoadLevel");
		this.reset = new JButton("Reset");
		this.backButton = new JButton("Back to Main Menu");
		

		initImages();

		this.loadLevel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String selectedLevelName = (String) sampleLevels.getSelectedItem();
				
				File level = new File( folder + File.separator + selectedLevelName + ".txt");
				
				FileReader inputFile = null;
				try {
					inputFile = new FileReader(level);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader in = new BufferedReader(inputFile);

				String line = null;
				try {
					line = in.readLine();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				while (line != null) {
					int x = 0;
					int y = 0;
					int color = 0;

					try {

						String[] splitted = line.split(" ");

						x = Integer.parseInt(splitted[0]);
						y = Integer.parseInt(splitted[1]);
						color = Integer.parseInt(splitted[2]);
						flowStruct.setColorAt(x, y, color);

						line = in.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				repaint();

			}
		});

		this.reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FlowsPanel.this.flowStruct.resetAll();
				repaint();
			}
		});

		this.solveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DlvInvoker dlvInvoker = new DlvInvoker(new DLVCommand( new DLVSolver(flowStruct)));
				if (FlowsPanel.this.flowStruct.validateStruct())
					dlvInvoker.solve();
				else
					JOptionPane.showMessageDialog(null, "Errore nello schema!");
				repaint();
			}

		});

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				int x = e.getX();
				int y = e.getY();

				if ((x > offset && x < offset + gridLenght)	&& (y > offset && y < offset + gridLenght)) {
					int i = (y - offset) / scaledTile;
					int j = (x - offset) / scaledTile;
					if ( e.getButton() == MouseEvent.BUTTON1 ) {
						flowStruct.setColorAt(i, j, colorSelected);
					} else if ( e.getButton() == MouseEvent.BUTTON3 ) {
						flowStruct.setColorAt(i, j, FlowStruct.NONE);
					}
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset && y < offset + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.RED;
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 50 * scale && y < offset + 50 * scale
								+ scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.GREEN;
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 100 * scale && y < offset + 100
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.YELLOW;
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 150 * scale && y < offset + 150
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.BLUE;
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 200 * scale && y < offset + 200
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.ORANGE;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 250 * scale && y < offset + 250
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.CYAN;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 300 * scale && y < offset + 300
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.MAGENTA;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 350 * scale && y < offset + 350
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.PINK;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 400 * scale && y < offset + 400
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.WHITE;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 450 * scale && y < offset + 450
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.LIGHT_GRAY;
					repaint();
				}
				if ((x > scaledTile * FlowsPanel.this.gridSize + 100 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 100
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset + 500 * scale && y < offset + 500
								* scale + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.NONE;
					repaint();
				}

				if ((x > scaledTile * FlowsPanel.this.gridSize + 150 * scale && x < scaledTile
						* FlowsPanel.this.gridSize
						+ 150
						* scale
						+ scaledCircleWidth / 2)
						&& (y > offset && y < offset + scaledCircleWidth / 2)) {
					colorSelected = FlowStruct.BRIDGE;
					repaint();
				}
			}
		});
		
		
		this.backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.getInstance().switchToPanel( MainFrame.getInstance().getMainPanel() );
			}
		});
		
		
		add(loadLevel);
		add(solveButton);
		add(reset);
		add(sampleLevels);
		add(backButton);

	}

	private void initImages() {
		try {
			this.bridge = ImageIO.read(new File("resources/bridge.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			this.backGround = ImageIO.read(new File("resources/Background.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private File initComboBox(final int gridSize) {
		this.sampleLevels = new JComboBox<String>();

		final File folder = new File( "LevelsFlow" + File.separator + gridSize );
		
		File[] levels = folder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String fileName) {
				
				if ( fileName.toLowerCase().endsWith(".txt") ) {
					File file = new File(dir + File.separator + fileName);
					file.getName();
					FlowsPanel.this.sampleLevels.addItem( file.getName().substring(0, file.getName().length() - 4) );
				}
				return true;
			}
		
		});
		return folder;
	}

	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(this.backGround, 0, 0, this.getWidth(), this.getHeight(),
				null);
		double hScale = getWidth() / (double) (gridSize * TILE_SIZE);
		double vScale = getHeight() / (double) ((gridSize + 1) * TILE_SIZE);

		scale = Math.min(hScale, vScale);

		scaledTile = (int) (TILE_SIZE * scale);

		offset = (int) (25 * scale);

		solveButton.setBounds( (int) (gridSize * scaledTile + offset + 200 * scale), 20, 75, 50);
		sampleLevels.setBounds((int) (gridSize * scaledTile + offset + 200 * scale) , 100, 100, 20);
		loadLevel.setBounds( (int) (gridSize * scaledTile + offset + 200 * scale), 150, 120, 50);
		reset.setBounds((int) (gridSize * scaledTile + offset + 200 * scale), 250, 75, 50);
		backButton.setBounds((int) (gridSize * scaledTile + offset + 200 * scale), 310, 150, 50);

		gridLenght = (int) (scaledTile * gridSize);

		int k = 0;

		g.setColor(Color.GRAY);
		for (int i = 0; i < gridSize + 1; i++) {

			g.drawLine(offset + k, offset, offset + k, gridLenght + offset);
			g.drawLine(offset, offset + k, gridLenght + offset, offset + k);

			k += scaledTile;
		}

		scaledCircleWidth = (int) (circleWidth * scale);
		int selectedColorBox = (int) (scaledCircleWidth + 10 * scale);

		for (int i = 0; i < flowStruct.getSize(); i++) {

			for (int j = 0; j < flowStruct.getSize(); j++) {

				drawCircle(g, i, j);

			}

		}

		drawThumbNails(g, selectedColorBox);
		drawFlows(g);
	}

	private void drawThumbNails(Graphics g, int selectedColorBox) {

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.RED)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale), offset - 2,
					selectedColorBox / 2, selectedColorBox / 2);
		g.setColor(Color.RED);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale), offset,
				scaledCircleWidth / 2, scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.GREEN)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 50 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.GREEN);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 50 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.YELLOW)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 100 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.YELLOW);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 100 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.BLUE)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 150 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.BLUE);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 150 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.ORANGE)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 200 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);

		g.setColor(Color.ORANGE);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 200 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);

		if (colorSelected == FlowStruct.CYAN)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 250 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.CYAN);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 250 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.MAGENTA)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 300 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.magenta);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 300 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.PINK)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 350 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.pink);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 350 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);
		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.WHITE)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 400 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.white);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 400 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);
		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.LIGHT_GRAY)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 450 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.lightGray);
		g.fillOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 450 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		g.setColor(Color.GRAY);
		if (colorSelected == FlowStruct.NONE)
			g.drawRect((int) (scaledTile * gridSize + 98 * scale),
					(int) (offset + 500 * scale - 2), selectedColorBox / 2,
					selectedColorBox / 2);
		g.setColor(Color.WHITE);
		g.drawOval((int) (scaledTile * gridSize + 100 * scale),
				(int) (offset + 500 * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2);

		// disegno il bridge
		if (colorSelected == FlowStruct.BRIDGE)
			g.drawRect((int) (scaledTile * gridSize + 150 * scale), offset - 2,
					selectedColorBox / 2, selectedColorBox / 2);

		g.drawImage(bridge, (int) (scaledTile * gridSize + 150 * scale),
				(int) (offset * scale), scaledCircleWidth / 2,
				scaledCircleWidth / 2, null);
		g.setColor(Color.GRAY);

		g.setColor(Color.GRAY);

	}

	private void drawFlows(Graphics g) {

		for (Integer key : flowStruct.getArc().keySet()) {
			switch (key) {
			case FlowStruct.BLUE:
				g.setColor(Color.blue);
				break;
			case FlowStruct.GREEN:
				g.setColor(Color.green);
				break;
			case FlowStruct.RED:
				g.setColor(Color.red);
				break;
			case FlowStruct.ORANGE:
				g.setColor(Color.orange);
				break;
			case FlowStruct.YELLOW:
				g.setColor(Color.yellow);
				break;
			case FlowStruct.CYAN:
				g.setColor(Color.cyan);
				break;
			case FlowStruct.MAGENTA:
				g.setColor(Color.magenta);
				break;
			case FlowStruct.PINK:
				g.setColor(Color.pink);
				break;
			case FlowStruct.WHITE:
				g.setColor(Color.white);
				break;
			case FlowStruct.LIGHT_GRAY:
				g.setColor(Color.lightGray);
				break;
			case FlowStruct.ARDESIA:
				g.setColor(ardesia);
				break;
			case FlowStruct.AMARANTO:
				g.setColor(amaranto);
				break;
			default:
				break;
			}

			for (Point point : flowStruct.getArc().get(key)) {
				int i = point.x / flowStruct.getSize();
				int j;
				if (point.x % flowStruct.getSize() != 0) {
					j = (point.x % flowStruct.getSize()) - 1;
				} else {
					j = flowStruct.getSize() - 1;
					i--;
				}
				int i2 = point.y / flowStruct.getSize();
				int j2;

				if (point.y % flowStruct.getSize() != 0) {
					j2 = (point.y % flowStruct.getSize()) - 1;
				} else {
					j2 = flowStruct.getSize() - 1;
					i2--;
				}

				// System.out.println("<" + point.x + " , " + point.y + ">"
				// + " color " + key);
				// System.out.println("<" + i + "," + j + ">" + "<" + i2 + ","
				// + j2 + ">");

				if ((point.y - point.x == 1)) {// da
												// sinistra
												// a
												// destra
					g.fillRect(
							(int) (scaledTile * j + (scaledTile - offset)),
							(int) (scaledTile * i + (scaledTile / 2) + offset / 3),
							scaledTile, scaledTile / 3);
				}
				if (point.x - point.y == 1) {
					g.fillRect(
							(int) (scaledTile * j2 + (scaledTile - offset)),
							(int) (scaledTile * i2 + (scaledTile / 2) + offset / 3),
							scaledTile, scaledTile / 3);

				}
				if (point.y - point.x == flowStruct.getSize()) {
					g.fillRect(
							(int) (scaledTile * j + (scaledTile - offset - 2 * (offset / 3))),
							(int) (scaledTile * i + (scaledTile / 2) + offset / 3),
							scaledTile / 3, scaledTile + (scaledTile / 3));
				}
				if (point.x - point.y == flowStruct.getSize()) {
					g.fillRect(
							(int) (scaledTile * j2 + (scaledTile - offset - 2 * (offset / 3))),
							(int) (scaledTile * i2 + (scaledTile / 2) + offset / 3),
							scaledTile / 3, scaledTile + (scaledTile / 3));
				}

			}
		}
	}

	private void drawCircle(Graphics g, int i, int j) {

		switch (flowStruct.getColorAt(i, j)) {

		case FlowStruct.GREEN:
			g.setColor(Color.GREEN);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.YELLOW:
			g.setColor(Color.YELLOW);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.BLUE:
			g.setColor(Color.BLUE);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.ORANGE:
			g.setColor(Color.ORANGE);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.RED:
			g.setColor(Color.RED);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.CYAN:
			g.setColor(Color.CYAN);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.MAGENTA:
			g.setColor(Color.magenta);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.PINK:
			g.setColor(Color.pink);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.WHITE:
			g.setColor(Color.white);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.LIGHT_GRAY:
			g.setColor(Color.lightGray);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.ARDESIA:
			g.setColor(ardesia);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.AMARANTO:
			g.setColor(amaranto);
			g.fillOval((int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth);
			break;
		case FlowStruct.BRIDGE:
			g.drawImage(bridge, (int) (scaledTile * j + offset + 13 * scale),
					(int) (scaledTile * i + offset + 13 * scale),
					scaledCircleWidth, scaledCircleWidth, null);
			break;
		}
	}
}
