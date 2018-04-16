package enviromentstrategies;

import car.Engine;

import java.util.ArrayList;
import java.util.List;

public class EnviromentStrategyNone implements EnviromentStrategy {
    public List<Class<? extends Engine>> getAllowedEngines() {
        List<Class<? extends Engine>> allowedEngines = new ArrayList<>();
        return allowedEngines;
    }
}
