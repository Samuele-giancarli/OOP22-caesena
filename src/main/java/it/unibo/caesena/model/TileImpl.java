package it.unibo.caesena.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.unibo.caesena.utils.Position;

public class TileImpl implements Tile {

    private final String imagePath;

    private Map<TileSection, GameSet> sections;
    private Optional<Position> currentPosition;
    private int rotationCount;

    public TileImpl(String imagePath) {
        this.imagePath = imagePath;
        
        this.currentPosition = Optional.empty();
        this.sections = new HashMap<>();
        this.rotationCount = 0;
    }

    @Override
    public void rotateClockwise() {
        final Map<TileSection, GameSet> rotateSections = new HashMap<>();
        //metto in up i left
        rotateSections.put(TileSection.UpRight, this.sections.get(TileSection.LeftUp));
        rotateSections.put(TileSection.UpCenter, this.sections.get(TileSection.LeftCenter));
        rotateSections.put(TileSection.UpLeft, this.sections.get(TileSection.LeftDown));
        
        //metto in left i down
        rotateSections.put(TileSection.LeftDown, this.sections.get(TileSection.DownRight));
        rotateSections.put(TileSection.LeftCenter, this.sections.get(TileSection.DownCenter));
        rotateSections.put(TileSection.LeftUp, this.sections.get(TileSection.DownLeft));
        
        //metto in down i right
        rotateSections.put(TileSection.DownRight, this.sections.get(TileSection.RightUp));
        rotateSections.put(TileSection.DownCenter, this.sections.get(TileSection.RightCenter));
        rotateSections.put(TileSection.DownLeft, this.sections.get(TileSection.RightDown));

        //metto in right gli up
        rotateSections.put(TileSection.RightDown, this.sections.get(TileSection.UpRight));
        rotateSections.put(TileSection.RightCenter, this.sections.get(TileSection.UpCenter));
        rotateSections.put(TileSection.RightUp, this.sections.get(TileSection.UpLeft));

        this.rotationCount = (this.rotationCount + 1) % 4;
        this.sections = rotateSections;
    }

    @Override
    public Optional<Position> getPosition() {
        return this.currentPosition;
    }

    @Override
    public void setPosition(Position pos) {
        this.currentPosition = Optional.of(pos);
    }

    @Override
    public boolean isPlaced() {
        return this.currentPosition.isPresent();
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void putSection(TileSection section, GameSet gameSet) {
        this.sections.put(section, gameSet);
    }

    @Override
    public int getRotationCount() {
        return this.rotationCount;
    }
}
