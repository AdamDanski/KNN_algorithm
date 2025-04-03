import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int k;
        List<double[]> setosa = new ArrayList<>();
        List<double[]> versicolor = new ArrayList<>();
        List<double[]> virginica = new ArrayList<>();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String train_set="trainset.txt";
        String line;

        System.out.print("Enter k: ");
        k=scanner.nextInt();


        //loading z train-set
        try(BufferedReader br = new BufferedReader(new FileReader(train_set))){

            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                double[] vector = new double[4];

                for (int i = 0; i < 4; i++) {
                    vector[i] = Double.parseDouble(data[i].trim());
                }
                String type = data[data.length-1].trim();
                if(type.equals("Iris-setosa")){
                    setosa.add(vector);
                } else if (type.equals("Iris-virginica")) {
                    virginica.add(vector);
                } else{
                    versicolor.add(vector);
                }
            }
        }catch (IOException | NumberFormatException e){
            System.err.println("ERROR");
        }
        System.out.println("Iris-setosa:");
        printList(setosa);
        System.out.println("\nIris-versicolor:");
        printList(versicolor);
        System.out.println("\nIris-virginica:");
        printList(virginica);

        //loading test-set
        List<TestSetClass> testsetList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("testset.txt"))){
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                double[] vector = new double[4];
                for (int i = 0; i < 4; i++) {
                    vector[i] = Double.parseDouble(data[i].trim());
                }
                String type = data[data.length-1].trim();
                testsetList.add(new TestSetClass(vector,type));
            }
        }catch (IOException | NumberFormatException e){
            System.err.println("ERROR");
        }

        int correctPredictions = 0;
        for(TestSetClass testSetElement : testsetList){
            String predictedType = classifyTestSet(testSetElement, setosa, versicolor, virginica, k);

            String actualType = testSetElement.getGatunek().replace("Iris-", "").toLowerCase();
            String predictedTypeNormalized = predictedType.toLowerCase();

            if (predictedTypeNormalized.equals(actualType)) {
                correctPredictions++;
            }

            System.out.println("Tested vector: " + Arrays.toString(testSetElement.getWektor()) +
                   " => real type: " + testSetElement.getGatunek() +
                   " | classification: " + predictedType);
        }

        double accuracy = (double) correctPredictions / testsetList.size();
        System.out.println("Classification accuracy: " + (accuracy * 100) + "%");



        //providing vector by user

        while (true){
            System.out.println("Enter first coordinate: ");
            double wsp1 = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter second coordinate: ");
            double wsp2 = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter third coordinate:: ");
            double wsp3 = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter fourth coordinate: ");
            double wsp4 = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Please state your suspicions about the type, if you do not have them enter 'None': ");
            String name = scanner.nextLine();
            if(name.equals("None")){
                name="None suspicions about type ";
            }

            double[] x = new double[]{wsp1,wsp2,wsp3,wsp4};
            TestSetClass userTest = new TestSetClass(x,name);
            String predictedGatunekForUser = classifyTestSet(userTest, setosa, versicolor, virginica, k);

            System.out.println("User vector: " + Arrays.toString(userTest.getWektor()));
            System.out.println("User suspicions: " + userTest.getGatunek());
            System.out.println("Classification: " + predictedGatunekForUser+"\n");
        }
    }
    private static String classifyTestSet(TestSetClass testSetElement, List<double[]> setosa, List<double[]> versicolor, List<double[]> virginica, int k) {

        List<DistanceFromVector> distances = new ArrayList<>();

        for (double[] vector : setosa) {
            distances.add(new DistanceFromVector(distanceBetweenVectors(vector, testSetElement.getWektor()), "setosa"));
        }
        for (double[] vector : versicolor) {
            distances.add(new DistanceFromVector(distanceBetweenVectors(vector, testSetElement.getWektor()), "versicolor"));
        }
        for (double[] vector : virginica) {
            distances.add(new DistanceFromVector(distanceBetweenVectors(vector, testSetElement.getWektor()), "virginica"));
        }


        distances.sort(Comparator.comparingDouble(o -> o.distance));

        List<DistanceFromVector> kNearest = distances.subList(0, k);

        int setosaCount = 0;
        int versicolorCount = 0;
        int virginicaCount = 0;

        for (DistanceFromVector point : kNearest) {
            switch (point.gatunek) {
                case "setosa":
                    setosaCount++;
                    break;
                case "versicolor":
                    versicolorCount++;
                    break;
                case "virginica":
                    virginicaCount++;
                    break;
            }
        }

        String predictedType = "setosa";
        int maxCount = setosaCount;
        if (versicolorCount > maxCount) {
            predictedType = "versicolor";
            maxCount = versicolorCount;
        }
        if (virginicaCount > maxCount) {
            predictedType = "virginica";
        }

        switch (predictedType) {
            case "setosa":
                setosa.add(testSetElement.getWektor());
                break;
            case "versicolor":
                versicolor.add(testSetElement.getWektor());
                break;
            case "virginica":
                virginica.add(testSetElement.getWektor());
                break;
        }

        return predictedType;
    }

    private static void printList(List<double[]> list) {
        for (double[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
    }
    private static double distanceBetweenVectors(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }
        double distance = 0;
        double dif;
        for (int i = 0; i < arr1.length; i++) {
            dif=(arr2[i]-arr1[i])*(arr2[i]-arr1[i]);
            distance+=dif;
        }
        distance=Math.sqrt(distance);
        return distance;
    }
}
