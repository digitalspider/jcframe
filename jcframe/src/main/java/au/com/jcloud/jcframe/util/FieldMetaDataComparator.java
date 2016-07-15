package au.com.jcloud.jcframe.util;

import java.util.Comparator;

/**
 * Created by david on 24/06/16.
 */
public class FieldMetaDataComparator implements Comparator<FieldMetaData> {
    @Override
    public int compare(FieldMetaData fmd1, FieldMetaData fmd2) {
        return fmd1.getField().getName().compareTo(fmd2.getField().getName());
    }
}
