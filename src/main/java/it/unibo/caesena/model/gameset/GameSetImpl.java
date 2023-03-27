package it.unibo.caesena.model.gameset;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import it.unibo.caesena.model.meeple.Meeple;
import it.unibo.caesena.utils.Pair;
import it.unibo.caesena.utils.StringUtil;

public class GameSetImpl implements GameSet{

    private final GameSetType type;
    private final Set<Meeple> meeples;
    
    private int points;
    private boolean closed;

    public GameSetImpl (final GameSetType type) {
        this.type = type;
        this.points = type.getStartingPoints();
        this.meeples = new HashSet<>();
        this.closed = false;
    }

    @Override
    public boolean addMeeple(final Meeple meeple) {
        if (isMeepleFree() && meeple.placeOnTile()) {
            this.meeples.add(meeple);
            return true;
        }

        return false;
    }

    @Override
    public boolean isMeepleFree() {
        return this.meeples.isEmpty();
    }

    @Override
    public Optional<Pair<Set<Meeple>, Integer>> close() {
        if (this.isClosed()) {
            return Optional.empty();
        }

        this.closed = true;
        meeples.forEach(Meeple::removeFromTile);
        return Optional.of(new Pair<>(this.meeples, this.points));
    }

	@Override
	public GameSetType getType() {
		return this.type;
	}

    @Override
    public String toString() {
        return new StringUtil.ToStringBuilder().addFromObjectGetters(this).build();
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public void addPoints(final int points) {
        this.points += points;
    }

    @Override
    public boolean equals (final Object obj) {
        return this == obj;
    }

}
