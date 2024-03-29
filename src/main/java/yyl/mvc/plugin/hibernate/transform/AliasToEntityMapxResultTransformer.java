package yyl.mvc.plugin.hibernate.transform;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import com.github.relucent.base.common.collection.Mapx;

@SuppressWarnings("serial")
public class AliasToEntityMapxResultTransformer extends AliasedTupleSubsetResultTransformer {

    public static final AliasToEntityMapxResultTransformer INSTANCE = new AliasToEntityMapxResultTransformer();

    /**
     * 私有构造
     */
    private AliasToEntityMapxResultTransformer() {
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Mapx result = new Mapx();
        for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                result.put(alias, tuple[i]);
            }
        }
        return result;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    /**
     * Serialization hook for ensuring singleton uniqueing.
     * @return The singleton instance : {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
