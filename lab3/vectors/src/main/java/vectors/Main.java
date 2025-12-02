package vectors;

// Importy dla podejścia Adapter/Dekorator
import vectors.adapter.Polar2DAdapter;
import vectors.decorator.Vector3DDecorator;
import vectors.adapter.IPolar2D;

// Importy dla podejścia Dziedziczenie
import vectors.inheritance.Polar2DInheritance;
import vectors.inheritance.Vector3DInheritance;

import java.util.Arrays;

//public class Main
//{
//    public static void main(String[] args)
//    {
//
//    }
//}

public class Main {

    public static void main(String[] args) {
        System.out.println("============== METODA 1: WZORCE ADAPTER/DEKORATOR ==============");
        runDecoratorDemo();

        System.out.println("\n\n============== METODA 2: DZIEDZICZENIE ==============");
        runInheritanceDemo();
    }

    /**
     * Demonstracja działania przy użyciu wzorców projektowych.
     */
    public static void runDecoratorDemo() {
        // 1. Tworzenie wektorów
        // Tworzymy bazowe obiekty 2D
        Vector2D baseV1 = new Vector2D(3, 4);
        Vector2D baseV2 = new Vector2D(5, 12);
        Vector2D baseV3 = new Vector2D(1, 1);

        // "Dekorujemy" je, aby dodać funkcjonalność 3D
        IVector v1 = new Vector3DDecorator(baseV1, 0); // Wektor (3, 4, 0)
        IVector v2 = new Vector3DDecorator(baseV2, 0); // Wektor (5, 12, 0)
        IVector v3 = new Vector3DDecorator(baseV3, 7); // Wektor (1, 1, 7)

        // Na tej jednej liście wektorów możemy wykonać WSZYSTKIE operacje
        IVector[] vectors = {v1, v2, v3};
        String[] names = {"v1_dec", "v2_dec", "v3_dec"};

        // 2. Wyświetlenie współrzędnych
        System.out.println("--- Współrzędne (Adapter/Dekorator) ---");
        for (int i = 0; i < vectors.length; i++) {
            System.out.println("\nWektor: " + names[i]);
            // Używamy metody pomocniczej, która potrafi "odwinąć" dekorator
            // i "zaadaptować" bazę 2D do widoku polarnego.
            printDecoratorVectorInfo(vectors[i]);
        }

        // 3. Wyświetlenie iloczynów
        System.out.println("\n--- Iloczyny Skalarne (Adapter/Dekorator) ---");
        printDotProducts(vectors, names);

        System.out.println("\n--- Iloczyny Wektorowe (Adapter/Dekorator) ---");
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors.length; j++) {
                // Musimy rzutować na Vector3DDecorator, aby wywołać cross()
                IVector crossResult = ((Vector3DDecorator) vectors[i]).cross(vectors[j]);
                printCrossProduct(names[i], names[j], crossResult);
            }
        }
    }

    /**
     * Demonstracja działania przy użyciu dziedziczenia.
     */
    public static void runInheritanceDemo() {
        // 1. Tworzenie wektorów
        // Zauważ, że musimy od razu wybrać, czy wektor ma być 3D, czy Polarny.
        // Nie możemy użyć jednego obiektu do obu celów.

        // Lista wektorów do obliczeń 3D (kartezjańskich i iloczynu wektorowego):
        IVector v1_inh = new Vector3DInheritance(3, 4, 0);
        IVector v2_inh = new Vector3DInheritance(5, 12, 0);
        IVector v3_inh = new Vector3DInheritance(1, 1, 7);
        IVector[] vectors_3d = {v1_inh, v2_inh, v3_inh};
        String[] names_3d = {"v1_inh", "v2_inh", "v3_inh"};

        // OSOBNA lista wektorów do odczytu danych biegunowych 2D:
        // Te obiekty nie mają nic wspólnego z wektorami 3D powyżej.
        Polar2DInheritance p1_inh = new Polar2DInheritance(3, 4);
        Polar2DInheritance p2_inh = new Polar2DInheritance(5, 12);
        Polar2DInheritance p3_inh = new Polar2DInheritance(1, 1);

        // ZGODNIE Z DIAGRAMEM W PODEJŚCIU Z DZIEDZICZENIEM WEKTORY 3D NIE MAJĄ
        // MOŻLIWOŚCI ODCZYTANIA WSPÓŁRZĘDNYCH BIEGUNOWYCH

        // Tablica musi być typu konkretnej klasy 'Polar2DInheritance'
        Polar2DInheritance[] vectors_polar = {p1_inh, p2_inh, p3_inh};
        String[] names_polar = {"p1_inh", "p2_inh", "p3_inh"};


        // 2. Wyświetlenie współrzędnych
        System.out.println("--- Współrzędne (Dziedziczenie) ---");
        System.out.println("(Dane kartezjańskie z obiektów Vector3DInheritance)");
        for (int i = 0; i < vectors_3d.length; i++) {
            double[] c = vectors_3d[i].getComponents();
            System.out.printf("  Wektor %s: Kartezjańskie: (%.2f, %.2f, %.2f)\n",
                    names_3d[i], c[0], c[1], c[2]);
        }

        System.out.println("\n(Dane biegunowe z OSOBNYCH obiektów Polar2DInheritance)");
        for (int i = 0; i < vectors_polar.length; i++) {
            // Wywołujemy metody bezpośrednio na obiekcie klasy Polar2DInheritance
            // abs() jest dziedziczone z Vector2D, getAngle() jest z Polar2DInheritance
            System.out.printf("  Wektor %s: Biegunowe (XY): (r = %.2f, \u03B8 = %.2f\u00B0)\n",
                    names_polar[i], vectors_polar[i].abs(), vectors_polar[i].getAngle());
        }

        // 3. Wyświetlenie iloczynów
        System.out.println("\n--- Iloczyny Skalarne (Dziedziczenie) ---");
        // Używamy wektorów 3D
        printDotProducts(vectors_3d, names_3d);

        System.out.println("\n--- Iloczyny Wektorowe (Dziedziczenie) ---");
        for (int i = 0; i < vectors_3d.length; i++) {
            for (int j = 0; j < vectors_3d.length; j++) {
                // Musimy rzutować na Vector3DInheritance, aby wywołać cross()
                IVector crossResult = ((Vector3DInheritance) vectors_3d[i]).cross(vectors_3d[j]);
                printCrossProduct(names_3d[i], names_3d[j], crossResult);
            }
        }
    }


    // --- METODY POMOCNICZE (WSPÓLNE LUB DLA KONKRETNEJ METODY) ---

    /**
     * Metoda pomocnicza specyficzna dla podejścia Adapter/Dekorator.
     * Potrafi "odwinąć" dekorator, aby dostać się do bazowego obiektu 2D
     * i zaadaptować go do widoku polarnego.
     */
    public static void printDecoratorVectorInfo(IVector v) {
        // 1. Współrzędne kartezjańskie (prosto z dekoratora 3D)
        double[] c = v.getComponents();
        System.out.printf("  Kartezjańskie : (%.2f, %.2f, %.2f)\n", c[0], c[1], c[2]);

        // 2. Współrzędne biegunowe (z płaszczyzny XY)
        // "Odwiń" dekorator, aby znaleźć bazowy Vector2D
        IVector baseVector = v;
        while (baseVector instanceof Vector3DDecorator) {
            baseVector = ((Vector3DDecorator) baseVector).getSrcV();
        }

        // Jeśli bazą jest Vector2D, możemy go zaadaptować do IPolar2D
        if (baseVector instanceof Vector2D) {
            IPolar2D polarView = new Polar2DAdapter((Vector2D) baseVector);
            System.out.printf("  Biegunowe (XY): (r = %.2f, \u03B8 = %.2f\u00B0)\n",
                    polarView.abs(), polarView.getAngle());
        }
    }

    /**
     * Wspólna metoda pomocnicza do wyświetlania iloczynów skalarnych.
     */
    public static void printDotProducts(IVector[] vectors, String[] names) {
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors.length; j++) {
                double dotProduct = vectors[i].cdot(vectors[j]);
                System.out.printf("  %s \u00B7 %s = %.2f\n", names[i], names[j], dotProduct);
            }
        }
    }

    /**
     * Wspólna metoda pomocnicza do wyświetlania iloczynów wektorowych.
     */
    public static void printCrossProduct(String name1, String name2, IVector result) {
        double[] c = result.getComponents();
        String resultVectorStr = String.format("(%.2f, %.2f, %.2f)", c[0], c[1], c[2]);
        System.out.printf("  %s x %s = %s\n", name1, name2, resultVectorStr);
    }

}