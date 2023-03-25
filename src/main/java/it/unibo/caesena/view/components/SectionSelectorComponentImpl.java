package it.unibo.caesena.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.model.tile.TileSection;
import it.unibo.caesena.utils.ImageIconUtil;

public class SectionSelectorComponentImpl extends JPanel implements SectionSelectorComponent {
    private final Tile currentTile;
    private Optional<TileSection> currentSectionSelected = Optional.empty();

    public SectionSelectorComponentImpl(Tile tile, Dimension dimension) {
        super();
        this.currentTile = tile;
        this.setSize(dimension);
        redraw();
        this.setVisible(true);
    }

    @Override
    public TileSection getSelectedSection() {
        return currentSectionSelected.orElseThrow(()->new IllegalStateException("Tried to get section but none was selected"));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = this.getParent().getSize();
        int newSize = d.width > d.height ? d.height : d.width;
        newSize = newSize == 0 ? 100 : newSize;
        return new Dimension(newSize, newSize);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Image image = ImageIconUtil.getTileImage(this.currentTile);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    private void redraw() {
        this.removeAll();
        this.drawSections();
        this.validate();
        this.repaint();
    }

    private void drawSections() {
        this.setLayout(new BorderLayout());
        SectionButton upLeftButton = new SectionButton(TileSection.UpLeft);
        SectionButton upCenterButton = new SectionButton(TileSection.UpCenter);
        SectionButton upRightButton = new SectionButton(TileSection.UpRight);

        SectionButton leftUpButton = new SectionButton(TileSection.LeftUp);
        SectionButton leftCenterButton = new SectionButton(TileSection.LeftCenter);
        SectionButton leftDownButton = new SectionButton(TileSection.LeftDown);

        SectionButton rightUpButton = new SectionButton(TileSection.RightUp);
        SectionButton rightCenterButton = new SectionButton(TileSection.RightCenter);
        SectionButton rightDownButton = new SectionButton(TileSection.RightDown);


        SectionButton downLeftButton = new SectionButton(TileSection.DownLeft);
        SectionButton downCenterButton = new SectionButton(TileSection.DownCenter);
        SectionButton downRightButton = new SectionButton(TileSection.DownRight);

        SectionButton centerButton = new SectionButton(TileSection.Center);

        //TODO controllare se li ho messi giusti, ho qualche dubbio
        HorizontalSectionButtonContainer upPanel = new HorizontalSectionButtonContainer(upLeftButton, upCenterButton, upRightButton);
        VerticalSectionButtonContainer leftPanel = new VerticalSectionButtonContainer(leftUpButton, leftCenterButton, leftDownButton);
        VerticalSectionButtonContainer rightPanel = new VerticalSectionButtonContainer(rightUpButton, rightCenterButton, rightDownButton);
        HorizontalSectionButtonContainer downPanel = new HorizontalSectionButtonContainer(downLeftButton, downCenterButton, downRightButton);
        CenterSectionButtonContainer centerPanel = new CenterSectionButtonContainer(centerButton);

        this.add(upPanel, BorderLayout.SOUTH);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(downPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private String getLabelFromSection(TileSection section) {
        //return currentTile.getGameSet(section).getType().toString() + "-" + section.name();
        return "x";
    }

    private class SectionButton extends JButton {
        private final TileSection section;

        public SectionButton(TileSection section) {
            super();
            this.section = section;
            // TODO cambiare sta roba ovviamente
            String buttonLabel = getLabelFromSection(section);
            this.setText(buttonLabel);
            this.addActionListener((e) ->
            {
                currentSectionSelected = Optional.of(this.section);
            });
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            int s = (int)(d.getWidth()<d.getHeight() ? d.getHeight() : d.getWidth());
            return new Dimension (s,s);
        }
    }

    private class VerticalSectionButtonContainer extends JPanel {
        final Component firstComponent;
        final Component secondComponent;
        final Component thirdComponent;

        public VerticalSectionButtonContainer(Component firstComponent, Component secondComponent,
                Component thirdComponent) {
            this.firstComponent = firstComponent;
            this.secondComponent = secondComponent;
            this.thirdComponent = thirdComponent;

            this.add(firstComponent);
            this.add(Box.createVerticalStrut(10));
            this.add(secondComponent);
            this.add(Box.createVerticalStrut(10));
            this.add(thirdComponent);
        }

        @Override
        public void setLayout(LayoutManager mgr) {
            mgr = new GridLayout(0,1);
            super.setLayout(mgr);
        }

        @Override
        public void setOpaque(boolean isOpaque) {
            super.setOpaque(false);
        }
    };

    private class HorizontalSectionButtonContainer extends JPanel {
        final Component firstComponent;
        final Component secondComponent;
        final Component thirdComponent;

        public HorizontalSectionButtonContainer(Component firstComponent, Component secondComponent,
                Component thirdComponent) {
            this.firstComponent = firstComponent;
            this.secondComponent = secondComponent;
            this.thirdComponent = thirdComponent;

            this.add(firstComponent);
            this.add(Box.createVerticalStrut(10));
            this.add(secondComponent);
            this.add(Box.createVerticalStrut(10));
            this.add(thirdComponent);
        }

        @Override
        public void setLayout(LayoutManager mgr) {
            mgr = new GridLayout(0,1);
            super.setLayout(mgr);
        }

        @Override
        public void setOpaque(boolean isOpaque) {
            super.setOpaque(false);
        }
    };

    private class CenterSectionButtonContainer extends JPanel {
        final Component component;

        public CenterSectionButtonContainer(Component component) {
            this.component = component;
            this.add(component);
        }

        @Override
        public void setLayout(LayoutManager mgr) {
            mgr = new GridLayout(0,1);
            super.setLayout(mgr);
        }

        @Override
        public void setOpaque(boolean isOpaque) {
            super.setOpaque(false);
        }
    };

}
