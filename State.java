import java.util.Objects;

public class State {
    private int playerTotal;
    private boolean activeAce;
    private int dealerFirstCard;

    public State(int playerTotal, boolean activeAce, int dealerFirstCard) {
        this.playerTotal = playerTotal;
        this.activeAce = activeAce;
        this.dealerFirstCard = dealerFirstCard;
    }

    public int getPlayerTotal() {
        return playerTotal;
    }

    public boolean isActiveAce() {
        return activeAce;
    }

    public int getDealerFirstCard() {
        return dealerFirstCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return playerTotal == state.playerTotal &&
                activeAce == state.activeAce &&
                dealerFirstCard == state.dealerFirstCard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerTotal, activeAce, dealerFirstCard);
    }
}
