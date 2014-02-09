import java.util.*;

public class CarModel {
	//public static final int LANE_LENGTH = 10;
	
	ArrayList<ArrayList<Car>> lanes;
	
	ArrayList<Car> myLeft = new ArrayList<Car>();
	ArrayList<Car> myRight = new ArrayList<Car>();
	ArrayList<Car> resultsLeft = new ArrayList<Car>(); // results stored in this arraylist
	ArrayList<Car> resultsRight= new ArrayList<Car>(); // results stored in this arraylist
	
	int slowV = 4;
	int fastV = 5;
	
	public CarModel(int laneLength){
		lanes = new ArrayList<ArrayList<Car>>();
		for(int i = 0; i < laneLength; i++)
			lanes.add(new ArrayList<Car>());
	}
	public void insertCar(Car car){
		System.out.println("Inserting Car: " + car);
		lanes.get(0).add(car);
		System.out.println("Inserted Car: " + lanes.get(0).get(lanes.get(0).size() - 1));
	}
}
