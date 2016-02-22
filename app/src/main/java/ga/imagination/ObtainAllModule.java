package ga.imagination;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by 44260 on 2016/1/21.
 */
public class ObtainAllModule {

    private static final String class_a = "ga.imagination.module.ProjectA";
    private static final String class_b = "ga.imagination.module.ProjectA";
    private LinkedHashSet<String> moduleSet = new LinkedHashSet<>();

    public ObtainAllModule() {
        moduleSet.add(class_a);
        moduleSet.add(class_b);
    }
}
