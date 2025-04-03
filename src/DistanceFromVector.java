public class DistanceFromVector {
    double distance;
    String gatunek;

    public DistanceFromVector(double odleglosc, String gatunek) {
        this.distance = odleglosc;
        this.gatunek = gatunek;
    }

    @Override
    public String toString() {
        return "odleglosc=" + distance +
                ", gatunek='" + gatunek +"\n";
    }
}
