package it.unibo.caesena.model.tile;

public enum TileSection {
    UpLeft,
    UpCenter,
    UpRight,
    RightUp,
    RightCenter,
    RightDown,
    DownRight,
    DownCenter,
    DownLeft,
    LeftDown,
    LeftCenter,
    LeftUp,
    Center;

    // TODO cercare nome migliore
    // se combacia con un altro pezzo (match)
    private boolean closed = false;

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    private static int getSectionsInSide() {
        return 3;
    }

    private static TileSection shiftTileSection(final TileSection section, final int offset) {
        if(section == TileSection.Center) {
            return TileSection.Center;
        }
        int index = Math.floorMod(section.ordinal() + offset, values().length);
        if (values()[index].equals(TileSection.Center)) {
            index = (index + 1) % values().length;
        }
        
        return values()[index];
    }

    public static TileSection rotateClockwise(final TileSection section) {
        return shiftTileSection(section, getSectionsInSide());
    }

    public static TileSection next(final TileSection section) {
        return shiftTileSection(section, 1);
    }

    public static TileSection previous(final TileSection section) {
        return shiftTileSection(section, -1);
    }
}
