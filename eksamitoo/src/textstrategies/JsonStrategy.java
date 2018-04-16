package textstrategies;

import car.Car;
import car.ServiceCar;
import enviromentcenter.EnviromentCenter;

public class JsonStrategy implements TextStrategy {

    public String getCurrentSituationAsText(EnviromentCenter enviromentCenter) {
        String jsonString = "";
        jsonString += "{";

        jsonString += "'Cars':[";
        for (Car car: enviromentCenter.getCarsInCity()) {
            jsonString += "{";
            jsonString += "'id': '" + car + "',";
            jsonString += "'location': '" + car.getCurrentIntersection() + "',";
            jsonString += "'tires': '" + car.getTires() + "',";
            jsonString += "'engine': '" + car.getEngine() + "'";
            jsonString += "},";
        }
        jsonString += "]";

        jsonString += "'ServiceCars':[";
        for (ServiceCar car: enviromentCenter.getServiceCarsInCity()) {
            jsonString += "{";
            jsonString += "'id': '" + car + "',";
            jsonString += "'location': '" + car.getCurrentIntersection() + "'";
            jsonString += "},";
        }
        jsonString += "]";

        jsonString += "'Enviroment':[";
        jsonString += "{";
        jsonString += "'Pollution': '" + enviromentCenter.getPollution() + "',";
        jsonString += "'EnviromentStrategy': '" + enviromentCenter.getEnviromentStrategy() + "'";
        jsonString += "},";
        jsonString += "]";

        jsonString += "}";

        return jsonString;
    }
}
