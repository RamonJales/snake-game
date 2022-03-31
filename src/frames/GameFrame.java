package frames;

import javax.swing.JFrame;

import panels.GamePanel;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public GameFrame() {
		initUI();
	}
	
	private void initUI() {
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
