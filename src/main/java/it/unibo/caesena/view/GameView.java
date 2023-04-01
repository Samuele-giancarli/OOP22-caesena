package it.unibo.caesena.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JPanel;

import it.unibo.caesena.model.Player;
import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.utils.Direction;
import it.unibo.caesena.utils.Pair;
import it.unibo.caesena.view.components.FooterComponent;
import it.unibo.caesena.view.components.FooterComponentImpl;
import it.unibo.caesena.view.components.MainComponent;
import it.unibo.caesena.view.components.MainComponentImpl;
import it.unibo.caesena.view.components.SideBarComponent;
import it.unibo.caesena.view.components.SideBarComponentImpl;
import it.unibo.caesena.view.components.TileImage;

public class GameView extends JPanel implements View<JPanel> {

    private static final float MAIN_COMPONENT_RATIO = 0.8f;

    private final GUI userInterface;
    private final MainComponent<JPanel> mainComponent;
    private final FooterComponent<JPanel> footer;
    private final SideBarComponent<JPanel> sidebar;
    private Optional<TileImage> currentTileImage;

    public GameView(final GUI userInterface) {
        super();
        this.userInterface = userInterface;
        this.currentTileImage = Optional.empty();
        this.mainComponent = new MainComponentImpl(this);
        this.footer = new FooterComponentImpl(this);
        this.sidebar = new SideBarComponentImpl(this);
        this.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = MAIN_COMPONENT_RATIO;
        gridBagConstraints.weighty = MAIN_COMPONENT_RATIO;
        mainComponent.getComponent().setPreferredSize(new Dimension((int) Math.round(10 * MAIN_COMPONENT_RATIO),
                (int) Math.round(10 * MAIN_COMPONENT_RATIO)));
        this.add(mainComponent.getComponent(), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1 - MAIN_COMPONENT_RATIO;
        gridBagConstraints.weighty = MAIN_COMPONENT_RATIO;
        sidebar.getComponent().setPreferredSize(new Dimension((int) Math.round(10 * (1 - MAIN_COMPONENT_RATIO)),
                (int) Math.round(10 * MAIN_COMPONENT_RATIO)));
        this.add(sidebar.getComponent(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1 - MAIN_COMPONENT_RATIO;
        footer.getComponent().setPreferredSize(
                new Dimension((int) Math.round(10 * 1.0), (int) Math.round(10 * (1 - MAIN_COMPONENT_RATIO))));
        this.add(footer.getComponent(), gridBagConstraints);
        this.setVisible(false);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            this.mainComponent.getBoard().getComponent().setVisible(true);
            update();
        }
        
        super.setVisible(visible);
    }

    private void generateCurrentTileImage() {
        final Tile currentTile = userInterface.getController().getCurrentTile();
        if (currentTileImage.isEmpty() || !currentTile.equals(currentTileImage.get().getTile())) {
            final Player currentPlayer = userInterface.getController().getCurrentPlayer();
            final Color currentPlayerColor = userInterface.getPlayerColor(currentPlayer);
            this.currentTileImage = Optional.of(new TileImage(currentTile, currentPlayerColor));
        }
    }

    public final void updateHUD() {
        this.footer.update();
        this.sidebar.update();
    }

    public TileImage getCurrentTileImage() {
        return this.currentTileImage.get();
    }

    public void placeMeeple() {
        mainComponent.toggleComponents();
    }

    public boolean placeTile() {
        Optional<Pair<Integer, Integer>> placedTilePosition = mainComponent.getBoard().getUnlockedTileButtonPosition();
        if (placedTilePosition.isPresent()) {
            if (this.userInterface.getController().placeCurrentTile(placedTilePosition.get())) {
                mainComponent.getBoard().placeTile();
                return true;
            }
        }
        return false;
    }

    public void endTurn() {
        this.mainComponent.endTurn();
    }

    public void zoomIn() {
        this.mainComponent.getBoard().zoomIn();
    }

    public void zoomOut() {
        this.mainComponent.getBoard().zoomOut();
    }

    public void move(final Direction direction) {
        this.mainComponent.getBoard().move(direction);
    }

    public boolean canZoomIn() {
        return this.mainComponent.getBoard().canZoomIn();
    }

    public boolean canZoomOut() {
        return this.mainComponent.getBoard().canZoomOut();
    }

    public boolean canMove(final Direction direction) {
        return this.mainComponent.getBoard().canMove(direction);
    }

    public void removePlacedTile() {
        this.mainComponent.getBoard().removePlacedTile();
    }

    public void updateComponents() {
        this.mainComponent.getBoard().draw();
    }

    @Override
    public final JPanel getComponent() {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final GUI getUserInterface() {
        return this.userInterface;
    }

    @Override
    public void update() {
        this.generateCurrentTileImage();
        this.updateComponents();
        this.updateHUD();
    }
}
