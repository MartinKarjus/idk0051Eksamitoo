package enviromentstrategies;

import car.DieselEngine;
import car.Engine;
import car.GassolineEngine;

import java.util.ArrayList;
import java.util.List;

public class EnviromentStrategyHarsh implements EnviromentStrategy {
    public List<Class<? extends Engine>> getAllowedEngines() {
        List<Class<? extends Engine>> allowedEngines = new ArrayList<>();
        allowedEngines.add(DieselEngine.class);
        allowedEngines.add(GassolineEngine.class);
        return allowedEngines;
    }
}
