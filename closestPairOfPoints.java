import java.io.*;
import java.util.*;

public class closestPairOfPoints {
    static class P { double x,y; int id; P(double x,double y,int id){this.x=x;this.y=y;this.id=id;} }
    static class Res { P a,b; double d2; Res(P a,P b,double d2){this.a=a;this.b=b;this.d2=d2;} }

    public static Res closest(List<P> pts){
        P[] ax = pts.toArray(new P[0]);
        Arrays.sort(ax, Comparator.comparingDouble(p->p.x));
        P[] ay = ax.clone();
        Arrays.sort(ay, Comparator.comparingDouble(p->p.y));
        return rec(ax, ay);
    }

    private static Res rec(P[] ax, P[] ay){
        int n = ax.length;
        if(n<=3){
            Res best = new Res(ax[0], ax[1], d2(ax[0],ax[1]));
            for(int i=0;i<n;i++) for(int j=i+1;j<n;j++) best = min(best, new Res(ax[i],ax[j], d2(ax[i],ax[j])));
            Arrays.sort(ay, Comparator.comparingDouble(p->p.y));
            return best;
        }
        int mid = n/2;
        double midX = ax[mid].x;

        P[] axL = Arrays.copyOfRange(ax,0,mid);
        P[] axR = Arrays.copyOfRange(ax,mid,n);

        ArrayList<P> L = new ArrayList<>(mid), R = new ArrayList<>(n-mid);
        for(P p: ay) { if(p.x < midX || (p.x==midX && index(axL,p)>=0)) L.add(p); else R.add(p); }
        P[] ayL = L.toArray(new P[0]);
        P[] ayR = R.toArray(new P[0]);

        Res left = rec(axL, ayL);
        Res right= rec(axR, ayR);
        Res best = min(left,right);
        double d = Math.sqrt(best.d2);

        ArrayList<P> strip = new ArrayList<>();
        for(P p: ay) if(Math.abs(p.x - midX) < d) strip.add(p);

        for(int i=0;i<strip.size();i++){
            for(int j=i+1;j<strip.size() && (strip.get(j).y - strip.get(i).y) < d && j-i<=7; j++){
                best = min(best, new Res(strip.get(i), strip.get(j), d2(strip.get(i), strip.get(j))));
                d = Math.sqrt(best.d2);
            }
        }
        return best;
    }

    private static int index(P[] arr, P t){
        for(int i=0;i<arr.length;i++) if(arr[i]==t) return i; return -1;
    }
    private static double d2(P a,P b){ double dx=a.x-b.x, dy=a.y-b.y; return dx*dx+dy*dy; }
    private static Res min(Res a, Res b){ return a.d2<=b.d2? a: b; }

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        ArrayList<P> pts = new ArrayList<>(n);
        for(int i=0;i<n;i++){ String[] t=br.readLine().trim().split("\\s+");
            pts.add(new P(Double.parseDouble(t[0]), Double.parseDouble(t[1]), i));
        }
        Res r = closest(pts);
        System.out.printf(Locale.US, "Pair: P%d(%.6f,%.6f) — P%d(%.6f,%.6f), dist=%.6f\n",
                r.a.id,r.a.x,r.a.y, r.b.id,r.b.x,r.b.y, Math.sqrt(r.d2));
    }
}
