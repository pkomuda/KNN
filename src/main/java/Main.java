import java.lang.reflect.InvocationTargetException;

public class Main
{
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Klasyfikator k = new Klasyfikator();
        k.czytanieZPliku("data_train.csv",false);
        k.czytanieZPliku("data_test.csv",true);
        k.sprawdzPoprawnosc(k.getTestowa(),k.kNN(5,"dlugoscKielicha","szerokoscKielicha"));
    }
}