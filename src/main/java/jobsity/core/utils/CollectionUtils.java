package jobsity.core.utils;

import java.util.List;
import java.util.Objects;

public class CollectionUtils {

    public static boolean isEmpty(final List list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    public static boolean isNotEmpty(final List list) {
        return Objects.nonNull(list) && !list.isEmpty();
    }
}
