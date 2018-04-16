package enviromentstrategies;

import car.Engine;

import java.util.List;

public interface EnviromentStrategy {
    public List<Class<? extends Engine>> getAllowedEngines();
}
