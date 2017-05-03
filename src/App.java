import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.image4j.codec.ico.ICODecoder;

public class App implements ActionListener {

	private JFrame mainWindow;
	private JButton loadImage;
	private final File userDir = new File("/Users/admin/Desktop");
	private final FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "gif", "png", "bmp",
			"ico");
	private BufferedImage loadedImage;
	private JLayeredPane imagePane;
	private JScrollPane imageScrollPane;
	private JLabel imageLabel;
	private JButton applyFilter;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App clipper = new App();
					clipper.initialise();

				} catch (Exception e) {

				}
			}
		});

	}

	protected void initialise() {
		mainWindow = new JFrame("Image Engraver");
		mainWindow.setSize(1200, 700);
		mainWindow.setResizable(false);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.getContentPane().setBackground(Color.black);
		mainWindow.setLayout(null);
		loadImage = makeButton("load image");
		loadImage.setBounds(15, 15, 120, 40);
		applyFilter = makeButton("apply filter");
		applyFilter.setBounds(150, 15, 120, 40);
		imagePane = new JLayeredPane();

		imageScrollPane = new JScrollPane(imagePane);
		imageScrollPane.setBounds(5, 125, 1185, 540);
		imageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		imageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// imageScrollPane.getViewport().setBackground(Color.black);
		mainWindow.getContentPane().add(loadImage);
	}

	private JButton makeButton(String text) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.addActionListener(this);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setFont(new Font("Arial", Font.BOLD, 12));
		mainWindow.getContentPane().add(button);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		switch (action) {
		case "load image":

			JFileChooser chooser = new JFileChooser(userDir);
			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(mainWindow);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath();
				File file = new File(path);
				loadedImage = null;
				try {
					loadedImage = ImageIO.read(file);
					if (loadedImage != null) {
						if (path != null) {
							imageLabel = new JLabel(new ImageIcon(path));
						}
					} else {
						List<BufferedImage> images = ICODecoder.read(file);
						System.out.println(images);
						loadedImage = images.get(0);
						if (path != null) {
							imageLabel = new JLabel(new ImageIcon(loadedImage));
						}
					}
					mainWindow.getContentPane().remove(imageScrollPane);
					mainWindow.repaint();
					mainWindow.revalidate();
					int height = loadedImage.getHeight();
					int width = loadedImage.getWidth();
					imagePane = null;
					imagePane = new JLayeredPane();
					imagePane.setPreferredSize(new Dimension(width, height));
					imageLabel.setSize(new Dimension(width, height));
					imagePane.add(imageLabel);
					imageScrollPane = null;
					imageScrollPane = new JScrollPane(imagePane);
					imageScrollPane.setBounds(5, 125, 1185, 540);
					imageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
					imageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					mainWindow.getContentPane().add(imageScrollPane);
				} catch (IOException e) {

				}

			}
			break;
		case "apply filter":
			convertToGray(loadedImage);
			mainWindow.getContentPane().remove(imageScrollPane);
			mainWindow.repaint();
			mainWindow.revalidate();
			imageLabel = new JLabel(new ImageIcon(loadedImage));
			int height = loadedImage.getHeight();
			int width = loadedImage.getWidth();
			imagePane.setPreferredSize(new Dimension(width, height));
			imageLabel.setSize(new Dimension(width, height));
			imagePane.add(imageLabel);
			imageScrollPane = null;
			imageScrollPane = new JScrollPane(imagePane);
			imageScrollPane.setBounds(5, 125, 1185, 540);
			imageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			imageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			mainWindow.getContentPane().add(imageScrollPane);
			break;
		}

	}

	public void convertToGray(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(!isTransparent(x,y)) {
					Color c = new Color(image.getRGB(x, y));
					int red = (int) (c.getRed() * 0.21);
					int green = (int) (c.getGreen() * 0.72);
					int blue = (int) (c.getBlue() * 0.07);
					int sum = red + green + blue;
					Color newColor = new Color(sum, sum, sum);
					image.setRGB(x, y, newColor.getRGB());
				}
			}
		}
	}

	public boolean isTransparent(int x, int y) {
		int pixel = loadedImage.getRGB(x, y);
		if ((pixel >> 24) == 0x00) {
			return true;
		} else {
			return false;
		}
	}
}
