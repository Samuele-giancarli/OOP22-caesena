package it.unibo.caesena.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import it.unibo.caesena.controller.Controller;
import it.unibo.caesena.utils.ResourceUtil;
import it.unibo.caesena.view.scene.GameOverScene;
import it.unibo.caesena.view.scene.GameScene;
import it.unibo.caesena.view.scene.PauseScene;
import it.unibo.caesena.view.scene.StartScene;
import it.unibo.caesena.view.scene.Scene;

/**
 * Implementation of UserInterface using Java Swing.
 */
public class GUI extends JFrame implements UserInterface {
    private static final long serialVersionUID = 8950849192853252728L;
    public static final float MODAL_PREFERRED_RATIO = 0.4f;
    public static final float MODAL_MAXIMUM_RATIO = 0.5f;
    public static final float MODAL_MINIMUM_RATIO = 0.2f;
    public static final float SCREEN_WIDTH = (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final float SCREEN_HEIGHT = (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static final Font BIG_NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, (int) Math.round(SCREEN_WIDTH * 0.009f));
    public static final Font BIG_BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, (int) Math.round(SCREEN_WIDTH * 0.009f));
    public static final Font MEDIUM_NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, (int) Math.round(SCREEN_WIDTH * 0.008f));
    public static final Font MEDIUM_BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, (int) Math.round(SCREEN_WIDTH * 0.008f));
    public static final Font SMALL_NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, (int) Math.round(SCREEN_WIDTH * 0.007f));
    public static final Font SMALL_BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, (int) Math.round(SCREEN_WIDTH * 0.007f));
    public static final int DEFAULT_PADDING = (int) Math.round(SCREEN_HEIGHT > SCREEN_WIDTH ? SCREEN_WIDTH * 0.005f : SCREEN_HEIGHT * 0.005f);
    public static final float SMALL_MODAL_RATIO = 0.5f;
    private static final float MINIMUM_SIZE_RATIO = 0.3f;
    private static final int MINIMUM_WIDTH = 200;
    private static final int MINIMUM_HEIGHT = 200;
    private final Controller controller;
    private Scene<JPanel> startScene;
    private Scene<JPanel> gameScene;
    private Scene<JPanel> pauseScene;
    private Scene<JPanel> gameOverScene;

    /**
     * Public constructor used to set UIManager and JFrame properties.
     * 
     * @param controller the controller to be used by the GUI
     */
    public GUI(final Controller controller) {
        super();

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.exit(ABORT);
        }
        UIManager.put("OptionPane.messageFont", MEDIUM_NORMAL_FONT);
        UIManager.put("OptionPane.buttonFont", MEDIUM_BOLD_FONT);
        UIManager.put("OptionPane.questionIcon", new ImageIcon());

        this.controller = controller;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                showExitDialog();
            }
        });

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float width = screenSize.width * MINIMUM_SIZE_RATIO;
        width = width < MINIMUM_WIDTH ? MINIMUM_WIDTH : width;
        float height = screenSize.height * MINIMUM_SIZE_RATIO;
        height = height < MINIMUM_HEIGHT ? MINIMUM_HEIGHT : height;
        this.setMinimumSize(new Dimension(Math.round(width), Math.round(height)));

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setLocationByPlatform(true);

        this.setIconImage(ResourceUtil.getBufferedImage("logo.png", List.of()));
        this.setVisible(true);

        resetScenes();

        this.controller.addUserInterface(this);
    }

    /**
     * Shows only startScene.
     */
    public void showStartScene() {
        this.setTitle(LocaleHelper.getSceneTitle("StartScene", true));
        this.startScene.setVisible(true);
        this.gameScene.setVisible(false);
        this.pauseScene.setVisible(false);
        this.gameOverScene.setVisible(false);
        this.setContentPane(startScene.getComponent());
        this.validate();
        this.repaint();
    }

    /**
     * Shows only gameScene and pauseScene.
     */
    public void showGameScene() {
        this.setTitle(LocaleHelper.getSceneTitle("GameScene", true));
        final JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new OverlayLayout(gamePanel));

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "togglePauseScene");
        gamePanel.getActionMap().put("togglePauseScene", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                togglePauseScene();
            }
        });

        this.startScene.setVisible(false);
        this.gameScene.setVisible(true);
        this.pauseScene.setVisible(false);
        this.gameOverScene.setVisible(false);
        gamePanel.add(this.pauseScene.getComponent());
        gamePanel.add(this.gameScene.getComponent());

        this.setContentPane(gamePanel);
        this.validate();
        this.repaint();
    }

    /**
     * Toggles the pauseScene.
     */
    public void togglePauseScene() {
        this.pauseScene.setVisible(!this.pauseScene.isVisible());
        setEnabledAllComponents(gameScene.getComponent(), !this.pauseScene.isVisible());
        setEnabledAllComponents(pauseScene.getComponent(), this.pauseScene.isVisible());
    }

    /**
     * Shows only gameOverScene.
     */
    private void showGameOverScene() {
        this.setTitle(LocaleHelper.getSceneTitle("GameOverScene", true));

        this.startScene.setVisible(false);
        this.gameScene.setVisible(false);
        this.pauseScene.setVisible(false);
        this.gameOverScene.setVisible(true);
        this.setContentPane(gameOverScene.getComponent());
        this.validate();
        this.repaint();
    }

    /**
     * Shows the exit dialog used to the exit the game.
     */
    public void showExitDialog() {
        final int result = JOptionPane.showConfirmDialog(this, LocaleHelper.getConfirmExitText(),
                LocaleHelper.getExitDialogTitle(), JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            exit();
        }
    }

    /**
     * Shows the back to startScene dialog used to go back to the start menu.
     */
    public void showBackTostartSceneDialog() {
        final int result = JOptionPane.showConfirmDialog(this, LocaleHelper.getConfirmBackToStartMenuText(),
                LocaleHelper.getBackToStartMenuText(), JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            resetScenes();
            this.controller.resetGame();
        }
    }

    /**
     * Resets all the scenes.
     */
    private void resetScenes() {
        this.startScene = new StartScene(this);
        this.gameScene = new GameScene(this);
        this.pauseScene = new PauseScene(this);
        this.gameOverScene = new GameOverScene(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exit() {
        this.controller.exitGame();
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Controller getController() {
        return this.controller;
    }

    /**
     * Sets the property enabled to all components inside the given Container
     * 
     * @param container the container of which components setEnabled will be called.
     * @param enabled   value to be used when calling setEnabled for every component.
     */
    private void setEnabledAllComponents(final Container container, final boolean enabled) {
        for (final var component : container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container) {
                setEnabledAllComponents((Container) component, enabled);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (controller.isGameOver()) {
            if (!gameOverScene.isVisible()) {
                showGameOverScene();
            }

            gameOverScene.update();
        } else if (!controller.getPlacedTiles().isEmpty()) {
            if (!gameScene.isVisible()) {
                showGameScene();
            }

            gameScene.update();
        } else {
            if (!startScene.isVisible()) {
                showStartScene();
            }

            startScene.update();
        }
    }
}
