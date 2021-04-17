package net.astercrono.gameoflife.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import net.astercrono.gameoflife.SeedGenerator;
import net.astercrono.gameoflife.graphics.grid.GridPanel;
import net.astercrono.gameoflife.graphics.menu.MenuActionType;
import net.astercrono.gameoflife.graphics.menu.MenuBar;
import net.astercrono.gameoflife.graphics.menu.MenuHandler;
import net.astercrono.gameoflife.life.Life;
import net.astercrono.gameoflife.life.LifeCycleListener;
import net.astercrono.gameoflife.life.LifeRunner;
import net.astercrono.gameoflife.life.ThreadedLifeRunner;
import net.astercrono.gameoflife.renderer.AwtGridPanelLifeRenderer;
import net.astercrono.gameoflife.renderer.LifeRenderer;

public class AppWindow implements MenuHandler, LifeCycleListener {
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 800;
	private static final int DEFAULT_LIFE_RATE_MS = 200;

	private JFrame frame = new JFrame();
	private LifeRenderer renderer;
	private MenuBar menuBar;
	private GridPanel gridPanel;
	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;
	private Life life;
	private LifeRunner lifeRunner;
	private int lifeRateMs = DEFAULT_LIFE_RATE_MS;

	public AppWindow() {
		init();
	}

	@Override
	public void handleMenuAction(MenuActionType type) {
		switch (type) {
		case SEED_OPEN:
			openSeed();
			break;
		case SEED_RANDOM:
			generateSeed();
			break;
		case START:
			startLife();
			break;
		case STOP:
			stopLife();
			break;
		case TOGGLE_PAUSE:
			togglePauseLife();
			break;
		default:
			break;
		}
		frame.repaint();
	}

	@Override
	public void lifeCycleUpdated(Life life) {
		System.out.println("Life Updated");
		renderer.render(life);
		gridPanel.getComponent().repaint();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void show() {
		frame.setVisible(true);
	}

	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}

	private void init() {
		menuBar = new MenuBar(this);

		frame.setSize(width, height);
		frame.setJMenuBar(menuBar.getComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopLife();
				close();
			}
		});
	}

	private void openSeed() {
		// TODO
	}

	private void startLife() {
		lifeRunner.start(lifeRateMs);
	}

	private void stopLife() {
		life = null;

		if (lifeRunner != null) {
			lifeRunner.stop();
			lifeRunner.clearLifeCycleListeners();
		}

		lifeRunner = null;
		resetFrameContent();
	}

	private void togglePauseLife() {
		lifeRunner.togglePause();
	}

	private void generateSeed() {
		List<List<Boolean>> seed = SeedGenerator.generateSeed(90, 70);

		stopLife();

		life = new Life(seed);
		lifeRunner = new ThreadedLifeRunner(life);
		lifeRunner.addLifeCycleListener(this);

		gridPanel = new GridPanel();
		gridPanel.setDimensions(life.getRowCount(), life.getColCount());
		frame.add(gridPanel.getComponent(), BorderLayout.CENTER);

		renderer = new AwtGridPanelLifeRenderer(gridPanel);

		menuBar.setMenuStart();
		startLife();
		show();
	}

	private void resetFrameContent() {
		frame.getContentPane().removeAll();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.repaint();
	}
}
