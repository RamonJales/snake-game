package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final int boardWidht = 400;
	private final int boardHeight = 400;
	private final int bodyPartsize = 10;
	private final int allbodyParts = ((boardWidht * boardHeight) / (bodyPartsize * bodyPartsize));
	private final int randomPosition = 29;
	private final int delay = 150;
	
	private final int x[] = new int[allbodyParts];
	private final int y[] = new int[allbodyParts];
		
	private int bodyParts;
	private int appleX;
	private int appleY;
	private int appleEaten;
	
	private Timer timer;
	
	private Image imageDot;
	private Image imageHead;
	private Image imageApple;
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	public GamePanel() {
		initBoard();
	}
	
	private void initBoard() {
		addKeyListener(new MyKeyAdapter());
		setBackground(Color.BLACK);
		setFocusable(true);
		setPreferredSize(new Dimension(boardWidht, boardHeight));
		loadImages();
		initGame();
	}
	
	private void loadImages() {
				
		imageHead = new ImageIcon("resources/dot-head.png").getImage();
		imageApple = new ImageIcon("resources/apple.png").getImage();
		imageDot = new ImageIcon("resources/dot-shape.png").getImage();
	}
	
	private void initGame() {
		bodyParts = 3;
		
		for (int i = 0; i < bodyParts; i++) {
			x[i] = 50 - i * 10;
			y[i] = 50;
		}
		
		locateApple();
		
		timer = new Timer(delay, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	private void drawingScore(Graphics g, int opacity, int fontSize) {
		String msg = "Score: " + appleEaten;
		g.setColor(new Color(255, 0, 0, opacity));
		g.setFont(new Font("Ink Free", Font.BOLD, fontSize));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString(msg, (boardWidht - metrics.stringWidth(msg)) / 2, g.getFont().getSize());
	}
	
	private void doDrawing(Graphics g) {
		if (inGame) {
			
			g.drawImage(imageApple, appleX, appleY, this);
			
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.drawImage(imageHead, x[i], y[i], this);
				}
				else {
					g.drawImage(imageDot, x[i], y[i], this);
				}
			}
			
			drawingScore(g, 65, 18);
			
			Toolkit.getDefaultToolkit().sync();
		}
		else {
			gameOver(g);
		}
	}
	
	private void gameOver(Graphics g) {
		drawingScore(g, 200, 25);
		
		String msg = "Game Over!";
		Font font = new Font("Helvetica", Font.BOLD, 35);
		FontMetrics metrics = getFontMetrics(font);
		
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString(msg, ((boardWidht - metrics.stringWidth(msg)) / 2), (boardHeight / 2) );
	}
	
	private void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			appleEaten++;
			locateApple();
		}
	}
	
	private void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[(i - 1)];
			y[i] = y[(i - 1)];
		}
		if (leftDirection) {
			x[0] -= bodyPartsize;
		}
		if (rightDirection) {
			x[0] += bodyPartsize;
		}
		if (upDirection) {
			y[0] -= bodyPartsize;
		}
		if (downDirection) {
			y[0] += bodyPartsize;
		}
	}
	
	private void checkCollision() {
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				inGame = false;
			}
		}
		if (x[0] >= boardWidht) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if (y[0] >= boardHeight) {
			inGame = false;
		}
		if (y[0] < 0) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}
	
	private void locateApple() {
		int r = (int) (Math.random() * randomPosition);
		appleX = (r * bodyPartsize);
		
		r = (int) (Math.random() * randomPosition);
		appleY = (r * bodyPartsize);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCollision();
			move();
		}
		repaint();
	}
	
	private class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}
			
			if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}
			
			if ((key == KeyEvent.VK_UP) && (!downDirection)) {
				upDirection = true;
				rightDirection = false;
				leftDirection = false;
			}
			
			if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
				downDirection = true;
				rightDirection = false;
				leftDirection = false;
			}			
		}
	}
}