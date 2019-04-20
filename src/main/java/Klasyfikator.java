import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Klasyfikator
{
    private List<Punkt> treningowa = new ArrayList<>();
    private List<Punkt> testowa = new ArrayList<>();

    List<Punkt> getTestowa()
    {
        return testowa;
    }

    void czytanieZPliku(String sciezka, boolean czyTestowa)
    {
        List<Punkt> punkty = new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(sciezka)))
        {
            String linia;
            String[] temp;


            while ((linia = br.readLine()) != null)
            {
                temp = linia.split(",");
                punkty.add(new Punkt(temp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!czyTestowa)
            treningowa = punkty;
        else
            testowa = punkty;
    }

    @SuppressWarnings("Duplicates")
    List<Integer> kNN(int k, String ...cecha) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        for (int i=0; i<cecha.length; i++)
            cecha[i] = "get"+cecha[i];
        double wynik;
        double p1c1,p1c2,p1c3=0,p1c4=0;
        double p2c1,p2c2,p2c3=0,p2c4=0;
        List<Integer> gatunki=new ArrayList<>();
        for (Punkt i : testowa)
        {
            p1c1 = (double)i.getClass().getMethod(cecha[0]).invoke(i);
            p1c2 = (double)i.getClass().getMethod(cecha[1]).invoke(i);
            if(cecha.length>=3)
            {
                p1c3 = (double)i.getClass().getMethod(cecha[2]).invoke(i);
                if(cecha.length==4)
                    p1c4 = (double)i.getClass().getMethod(cecha[3]).invoke(i);
            }

            for (Punkt j : treningowa)
            {
                p2c1 = (double)j.getClass().getMethod(cecha[0]).invoke(j);
                p2c2 = (double)j.getClass().getMethod(cecha[1]).invoke(j);
                if (cecha.length>=3)
                {
                    p2c3 = (double)j.getClass().getMethod(cecha[2]).invoke(j);
                    if(cecha.length==4)
                        p2c4=(double)j.getClass().getMethod(cecha[3]).invoke(j);
                }

                /***************wyliczenie odleglosci z Euklidesa*******************/
                wynik = (p2c1-p1c1)*(p2c1-p1c1);
                wynik += (p2c2-p1c2)*(p2c2-p1c2);
                wynik += (p2c3-p1c3)*(p2c3-p1c3);
                wynik += (p2c4-p1c4)*(p2c4-p1c4);
                j.setOdleglosc(wynik);
            }
            gatunki.add(glosowanie(k));
        }
        return gatunki;
    }

    private int glosowanie(int k) {
        int gatunek;

        Punkt [] test=treningowa.toArray(new Punkt[0]);
        Arrays.sort(test,new ComparatorPoOdleglosci());
        treningowa=new ArrayList<>(Arrays.asList(test));

        int setosa=0;
        int versicolor=0;
        int virginica=0;

        List<Punkt> temp=new ArrayList<>();


        for (int i = 0; i <k ; i++) {
            temp.add(treningowa.get(i));
            if(temp.get(i).getGatunek()==0)
                setosa++;
            else if (temp.get(i).getGatunek()==1)
                versicolor++;
            else virginica++;
        }

        gatunek=znajdzNajliczniejszy(temp,setosa,versicolor,virginica);
        System.out.println(setosa+" "+ versicolor+ " "+virginica);
        if (gatunek==0)
            System.out.println("X\n");
        else if (gatunek==1)
            System.out.println("  X\n");
        else
            System.out.println("    X\n");

        return gatunek;
    }

    void sprawdzPoprawnosc(List<Punkt> punkty, List<Integer> gatunkiTest)
    {
        double poprawne=0;
        for (int i = 0; i <punkty.size() ; i++) {
            if(punkty.get(i).getGatunek()==gatunkiTest.get(i))
                poprawne++;
        }
        int[] setosa = new int[3];
        int[] versicolor = new int[3];
        int[] virginica = new int[3];
        int[][] gatunki = {setosa,versicolor,virginica};

        for (int i = 0; i < punkty.size(); i++) {
            if(punkty.get(i).getGatunek()==0)
            {
                if (gatunkiTest.get(i)==punkty.get(i).getGatunek())
                    setosa[0]++;
                else if (gatunkiTest.get(i)==1)
                    setosa[1]++;
                else
                    setosa[2]++;
            }
            else if(punkty.get(i).getGatunek()==1)
            {
                if (gatunkiTest.get(i)==0)
                    versicolor[0]++;
                else if (gatunkiTest.get(i)==punkty.get(i).getGatunek())
                    versicolor[1]++;
                else
                    versicolor[2]++;
            }
            else
            {
                if (gatunkiTest.get(i)==0)
                    virginica[0]++;
                else if (gatunkiTest.get(i)==1)
                    virginica[1]++;
                else
                    virginica[2]++;
            }
        }

        DecimalFormat d = new DecimalFormat("0.00");
        System.out.println("Poprawnosc: "+d.format(100*poprawne/punkty.size())+"%\n");
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
                System.out.print(d.format(100*gatunki[i][j]/(double)getLiczebnosc(i))+"%\t\t");
            System.out.println();
        }
    }

    private int getLiczebnosc(int gatunek)
    {
        int ilosc=0;
        for (Punkt i:testowa)
        {
            if(gatunek==i.getGatunek())
                ilosc++;
        }
        return ilosc;
    }

    private int znajdzNajliczniejszy(List<Punkt> najblizsze, int... liczebnosc)
    {
        int gatunek=3;
        int max=Math.max(liczebnosc[0],Math.max(liczebnosc[1],liczebnosc[2]));
        int znalezione=0;
        for (int i=0; i<liczebnosc.length;i++) {
            if(max==liczebnosc[i])
            {
                gatunek=i;
                znalezione++;
            }
        }
        if (znalezione>1)
        {
            System.out.println("remis");
            if (gatunek==2 && liczebnosc[0]==liczebnosc[1] && liczebnosc[1]==liczebnosc[2])
            {
                return najblizsze.get(0).getGatunek();
            }
            else if (gatunek==1 && liczebnosc[0]==liczebnosc[1])
            {
                for (Punkt i : najblizsze)
                {
                    if (i.getGatunek()==0 || i.getGatunek()==1)
                        return i.getGatunek();
                }
            }
            else if (gatunek==2 && liczebnosc[0]==liczebnosc[2])
            {
                for (Punkt i : najblizsze)
                {
                    if (i.getGatunek()==0 || i.getGatunek()==2)
                        return i.getGatunek();
                }
            }
            else if (gatunek==2 && liczebnosc[1]==liczebnosc[2])
            {
                for (Punkt i : najblizsze)
                {
                    if (i.getGatunek()==1 || i.getGatunek()==2)
                        return i.getGatunek();
                }
            }
        }
        return gatunek;
    }
}