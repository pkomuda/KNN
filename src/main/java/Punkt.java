public class Punkt
{
    private double dlugoscKielicha;
    private double szerokoscKielicha;
    private double dlugoscPlatka;
    private double szerokoscPlatka;
    private int gatunek;
    private double odleglosc;

    Punkt(String[] rekord)
    {
        this.dlugoscKielicha = Double.parseDouble(rekord[0]);
        this.szerokoscKielicha = Double.parseDouble(rekord[1]);
        this.dlugoscPlatka = Double.parseDouble(rekord[2]);
        this.szerokoscPlatka = Double.parseDouble(rekord[3]);
        this.gatunek = Integer.parseInt(rekord[4]);
    }

    public double getdlugoscKielicha()
    {
        return dlugoscKielicha;
    }

    public double getszerokoscKielicha()
    {
        return szerokoscKielicha;
    }

    public double getdlugoscPlatka()
    {
        return dlugoscPlatka;
    }

    public double getszerokoscPlatka()
    {
        return szerokoscPlatka;
    }

    int getGatunek()
    {
        return gatunek;
    }

    double getOdleglosc()
    {
        return odleglosc;
    }

    void setOdleglosc(double odleglosc)
    {
        this.odleglosc = odleglosc;
    }

    @Override
    public String toString()
    {
        return "Punkt{" +
                "dlugoscKielicha=" + dlugoscKielicha +
                ", szerokoscKielicha=" + szerokoscKielicha +
                ", dlugoscPlatka=" + dlugoscPlatka +
                ", szerokoscPlatka=" + szerokoscPlatka +
                ", gatunek=" + gatunek +
                ", odleglosc=" + odleglosc +
                '}';
    }
}