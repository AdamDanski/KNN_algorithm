import java.util.Arrays;

public class TestSetClass {
    double[] wspolrzedne;
    String nazwa;

    public TestSetClass(double[] wspolrzedne, String nazwa) {
        this.wspolrzedne = wspolrzedne;
        this.nazwa = nazwa;
    }

    public double[] getWektor() {
        return wspolrzedne;
    }

    public String getGatunek() {
        return nazwa;
    }

    @Override
    public String toString() {
        return "TestSetClass{" +
                "wspolrzedne=" + Arrays.toString(wspolrzedne) +
                ", nazwa='" + nazwa + '\'' +
                '}';
    }
}
