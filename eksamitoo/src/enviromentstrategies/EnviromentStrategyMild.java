package enviromentstrategies;

import car.DieselEngine;
import car.Engine;

import java.util.ArrayList;
import java.util.List;

public class EnviromentStrategyMild implements EnviromentStrategy {
    public List<Class<? extends Engine>> getAllowedEngines() {
        List<Class<? extends Engine>> allowedEngines = new ArrayList<>();
        allowedEngines.add(DieselEngine.class);
        return allowedEngines;
    }
}
