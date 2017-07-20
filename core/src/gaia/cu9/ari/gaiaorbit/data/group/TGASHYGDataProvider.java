package gaia.cu9.ari.gaiaorbit.data.group;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.utils.Array;

import gaia.cu9.ari.gaiaorbit.scenegraph.StarGroup.StarBean;
import gaia.cu9.ari.gaiaorbit.util.GlobalResources;
import gaia.cu9.ari.gaiaorbit.util.I18n;
import gaia.cu9.ari.gaiaorbit.util.Logger;

public class TGASHYGDataProvider extends AbstractStarGroupDataProvider {
    private static final boolean dumpToDisk = true;

    HYGDataProvider hyg;
    TGASDataProvider tgas;

    public TGASHYGDataProvider() {
        super();
        hyg = new HYGDataProvider();
        tgas = new TGASDataProvider();
    }

    @Override
    public Array<StarBean> loadData(String file) {
        return loadData(file, 1d);
    }

    @Override
    public Array<StarBean> loadData(String file, double factor) {
        Array<StarBean> tgasdata = tgas.loadData("data/tgas_final/tgas.csv");
        Array<StarBean> hygdata = hyg.loadData("data/hyg/hygxyz.bin");

        Map<String, Integer> tgasindex = tgas.getIndex();
        Map<String, Integer> hygindex = hyg.getIndex();

        // Merge everything, discarding hyg stars already in tgas
        // Contains removed HIP numbers
        Set<Integer> removed = new HashSet<Integer>();

        for (int i = 0; i < tgasdata.size; i++) {
            StarBean curr = tgasdata.get(i);
            int hip = (int) curr.data[StarBean.I_HIP];
            if (hip > 0 && hygindex.containsKey("HIP " + hip)) {
                removed.add((int) curr.data[StarBean.I_HIP]);
            }
        }

        // Add from hip to tgas
        Map<Integer, List<String>> hygindexinv = GlobalResources.invertMap(hygindex);

        for (int i = 0; i < hygdata.size; i++) {
            StarBean curr = hygdata.get(i);
            Integer hip = (int) curr.data[StarBean.I_HIP];
            if (!removed.contains(hip)) {
                int newidx = tgasdata.size;
                // Add to tgasdata
                tgasdata.add(curr);

                // Find out index and also add names
                List<String> names = hygindexinv.get(i);
                for (String n : names)
                    tgasindex.put(n, newidx);
            } else {
                // Use proper name
                if (tgasindex.containsKey("HIP " + i)) {
                    int oldidx = tgasindex.get("HIP " + i);
                    tgasdata.get(oldidx).name = curr.name;
                }
            }
        }

        list = tgasdata;
        index = tgasindex;

        if (dumpToDisk) {
            dumpToDisk(list, "/tmp/tgashyg.bin");
        }

        Logger.info(this.getClass().getSimpleName(), I18n.bundle.format("notif.nodeloader", list.size, file));

        return list;
    }

}