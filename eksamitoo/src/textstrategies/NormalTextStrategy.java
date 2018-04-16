package textstrategies;

import car.Car;
import car.ServiceCar;
import enviromentcenter.EnviromentCenter;

import java.util.ArrayList;
import java.util.List;

public class NormalTextStrategy implements TextStrategy{
    public String getCurrentSituationAsText(EnviromentCenter enviromentCenter) {
        String string = "";
        string += "*********************   START OF INFO   *****************************\n";
        string += "Cars in the city:\n";
        for (Car car: enviromentCenter.getCarsInCity()) {
            string += "\n";
            string += "\nid: " + car + ",";
            string += "\nlocation: " + car.getCurrentIntersection() + ",";
            string += "\ntires: " + car.getTires() + ",";
            string += "\nengine: " + car.getEngine();
        }
        string += "\n\n";

        string += "ServiceCars in use:\n";
        List<ServiceCar> tempCarList = new ArrayList<ServiceCar>(enviromentCenter.getServiceCarsInCity());
        for (ServiceCar car: tempCarList) {
            string += "\n";
            string += "\nid: " + car + ",";
            string += "\nlocation: " + car.getCurrentIntersection();
        }
        string += "\n\n";

        string += "Enviroment status: \n";
        string += "\n";
        string += "\nPollution: " + enviromentCenter.getPollution() + ",";
        string += "\nEnviromentStrategy: " + enviromentCenter.getEnviromentStrategy();
        string += "*******************   END OF INFO  *******************************\n";


        return string;
    }
}
