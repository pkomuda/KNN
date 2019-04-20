import java.util.Comparator;

public class ComparatorPoOdleglosci implements Comparator<Punkt>
{
    @Override
    public int compare(Punkt o1, Punkt o2) {
        if(o1.getOdleglosc()-(o2.getOdleglosc())<0)
            return -1;
        else if(o1.getOdleglosc()-(o2.getOdleglosc())>0)
            return 1;
        else return 0;
    }
}