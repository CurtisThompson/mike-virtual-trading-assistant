public class Sector {
    private String name;
    private double currentValue;
    private double percentChange;
    private double priceChange;


    public Sector(String name, double currentValue, double percentChange){
        this.name = name;
        this.currentValue = currentValue;
        this.percentChange = percentChange;
        this.priceChange = currentValue * (percentChange/100);
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentValue;
    }
    public double getCurrentValue() {
        return currentValue;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void printStr(){
        System.out.println(name + " " + currentValue + " " + percentChange);
    }
}
