package enviromentcenter;

import car.*;
import cityinfrastructure.InfrastructureInfo;
import enviromentstrategies.EnviromentStrategy;
import enviromentstrategies.EnviromentStrategyHarsh;
import enviromentstrategies.EnviromentStrategyMild;
import enviromentstrategies.EnviromentStrategyNone;
import textstrategies.NormalTextStrategy;
import textstrategies.TextStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnviromentCenter {

    private List<Car> carsInCity;
    private List<Engine> enginesInUse;
    private List<ServiceCar> serviceCarsInCity;
    private double pollutionCounter;
    private EnviromentStrategy enviromentStrategy;
    private boolean resettingInProgress;
    private double pollutionAtDrivingBanTime;
    private Counter counter;
    private InfrastructureInfo infrastructureInfo;
    private boolean clearCity;
    private TextStrategy outputTextStrategy;
    private ExecutorService subThreadExecutor;


    public EnviromentCenter() {
        this.subThreadExecutor = Executors.newCachedThreadPool();
        this.carsInCity = new ArrayList<>();
        this.enginesInUse = new ArrayList<>();
        this.serviceCarsInCity = new ArrayList<>();
        this.pollutionCounter = 0;
        this.enviromentStrategy = new EnviromentStrategyNone();
        this.resettingInProgress = false;
        this.clearCity = false;
        this.outputTextStrategy = new NormalTextStrategy();
        this.counter = new Counter();
    }


    private synchronized void refreshEnviromentStrategy() {
        if (pollutionCounter > 500 && !(enviromentStrategy instanceof EnviromentStrategyHarsh)) {
            enviromentStrategy = new EnviromentStrategyHarsh();
            counter.incrementDieselStopped();
            counter.incrementGassollineStopped();
            System.out.println("Changing to harsh enviromental policy.");
        } else if (pollutionCounter <= 500 && pollutionCounter > 400 && !(enviromentStrategy instanceof EnviromentStrategyMild)) {
            enviromentStrategy = new EnviromentStrategyMild();
            counter.incrementDieselStopped();
            System.out.println("Changing to mild enviromental policy.");
        } else if (!(enviromentStrategy instanceof EnviromentStrategyNone) && pollutionCounter <= 400) {
            enviromentStrategy = new EnviromentStrategyNone();
            System.out.println("Changing to no enviromental policy.");
        }
    }

    private boolean hardReset() {
        int internalCombustionEngineCount = 0;
        for (Engine engine : enginesInUse) {
            if (engine instanceof DieselEngine || engine instanceof GassolineEngine) {
                internalCombustionEngineCount += 1;
            }
        }
        System.out.println("Combustion engine count: " + internalCombustionEngineCount);
        if (internalCombustionEngineCount < 70) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void resetCounter() {
        if (hardReset()) {
            System.out.println("Resetting pollution to 0");
            pollutionCounter = 0;
        } else {
            System.out.println("Resetting pollution to 40%");
            pollutionCounter = pollutionAtDrivingBanTime / 100.0 * 40.0;
        }
        resettingInProgress = false;
        refreshEnviromentStrategy();
        this.notifyAll();
    }

    public void allowDriving(Engine engine, Car car) {
        while (enviromentStrategy.getAllowedEngines().contains(engine.getClass())) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public synchronized void newCarEntering(Car car, Engine engine) {
        carsInCity.add(car);
        enginesInUse.add(engine);
    }

    public synchronized void replaceEngine(Engine oldEngine, Engine newEngine) {
        enginesInUse.remove(oldEngine);
        enginesInUse.add(newEngine);
    }

    public synchronized void notifyEnviromentCenter(Engine usedEngine) {
        if (usedEngine instanceof DieselEngine) {
            pollutionCounter += 3;
        } else if (usedEngine instanceof GassolineEngine) {
            pollutionCounter += 2;
        } else if (usedEngine instanceof LimonadeEngine) {
            pollutionCounter += 0.5;
        } else if (usedEngine instanceof ElectricEngine) {
            pollutionCounter += 0.1;
        }

        EnviromentStrategy oldStrategy = enviromentStrategy;
        refreshEnviromentStrategy();

        if (!oldStrategy.getClass().equals(enviromentStrategy.getClass()) && !resettingInProgress && !(enviromentStrategy instanceof EnviromentStrategyNone)) {
            pollutionAtDrivingBanTime = pollutionCounter;
            resettingInProgress = true;
            subThreadExecutor.execute(new PollutionCleaner(this));
        }

    }


    public synchronized int getTimesEngineStopped(Engine engine) {
        if(engine instanceof DieselEngine) {
            return counter.getTimesDieselStopped();
        } else if (engine instanceof GassolineEngine) {
            return counter.getTimesGassollineStopped();
        } else {
            return 0;
        }
    }

    public double getPollution() {
        return pollutionCounter;
    }

    public void requestServiceCar() {
        synchronized (this) {
            if(!clearCity) {
                ServiceCar serviceCar = new ServiceCar(new LimonadeEngine(), infrastructureInfo, this);
                serviceCarsInCity.add(serviceCar);
                subThreadExecutor.execute(serviceCar);
            }
        }
    }

    public void setOutputTextStrategy(TextStrategy outputTextStrategy) {
        this.outputTextStrategy = outputTextStrategy;
    }

    public String getOverview() {
        return outputTextStrategy.getCurrentSituationAsText(this);
    }

    public synchronized void removeServiceCar(ServiceCar serviceCar) {
        serviceCarsInCity.remove(serviceCar);
    }

    public void setInfrastructureInfo(InfrastructureInfo infrastructureInfo) {
        this.infrastructureInfo = infrastructureInfo;
    }

    public void setClearCity(boolean clearCity) {
        this.clearCity = clearCity;
        subThreadExecutor.shutdownNow();
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean isClearCity() {
        return clearCity;
    }

    public List<Car> getCarsInCity() {
        return carsInCity;
    }

    public List<Engine> getEnginesInUse() {
        return enginesInUse;
    }

    public List<ServiceCar> getServiceCarsInCity() {
        synchronized (this) {
            return serviceCarsInCity;
        }
    }

    public double getPollutionCounter() {
        return pollutionCounter;
    }

    public EnviromentStrategy getEnviromentStrategy() {
        return enviromentStrategy;
    }
}
