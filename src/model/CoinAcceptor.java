package model;

public class CoinAcceptor {
    private int amount;

    public CoinAcceptor(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "CoinAcceptor{" +
                "amount=" + amount +
                '}';
    }
}
