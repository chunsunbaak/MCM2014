import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class StimulateTwo {

	public static final int LANE_LENGTH = 2;
	public static final int NUM_CARS = 10;
	public static final int SPEED_LIMIT = 9;
	public static ArrayList<Car> cars = new ArrayList<Car>();
	static CarModel model;
	public static void main(String[] args) {
		model = new CarModel(LANE_LENGTH);
		doFrame();
	}
	public static void doFrame(){
		Scanner s = new Scanner(System.in);
		while(true){
			if(cars.size() <= NUM_CARS) insertCar();
			moveCars();
			System.out.print(printCars());
			s.nextLine();
		}
		
	}
	public static void insertCar(){
		Random rand = new Random();
		Car newCar = new Car( rand.nextInt() % 5 + SPEED_LIMIT - 5);
		cars.add(newCar);
		model.insertCar(newCar);
	}
	static void moveCars(){
		for(int i = 0; i < model.lanes.size(); i++){
			for(int j = 0; j < model.lanes.get(i).size(); j++){
				Car currentCar = model.lanes.get(i).get(j);
				int safeDistance = currentCar.myVelocity+1;
				if(i == 0){  //right lane
					if(j == 0){  // first car
						currentCar.regularMove();
					}else{
						Car carAhead = model.lanes.get(i).get(j - 1);
						int gap = carAhead.position - currentCar.position - 1;
						int futureVelocity = Math.min( SPEED_LIMIT , Math.round( (float) 0.5*( gap + carAhead.myVelocity - 1 )) );
						int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;
						if (futureDistance >= safeDistance){  //ample space
							currentCar.changeVelocity(futureVelocity);
						}else{  //not enough space, try to pass
							Car carAheadLeftLane = null;
							Car carBehindLeftLane = null;
							for(Car c: model.lanes.get(i + 1)){
								if(c.position >= currentCar.position){
									carAheadLeftLane = c;
								}
							}
							for(Car c: model.lanes.get(i + 1)){
								if(c.position < currentCar.position){
									carBehindLeftLane = c;
									break;
								}
							}
							
							int futureDistanceBehindLeftLane = Integer.MAX_VALUE;
							if(carBehindLeftLane!=null){
								futureDistanceBehindLeftLane = carBehindLeftLane.position 
										- currentCar.position + 1
										+ carBehindLeftLane.myVelocity 
										- currentCar.myVelocity;
							}
							
							int futureDistanceAheadLeftLane = Integer.MAX_VALUE;
							if(carAheadLeftLane!=null){
								futureDistanceAheadLeftLane = carAheadLeftLane.position
										- currentCar.position -1 
										+ carAheadLeftLane.myVelocity
										- currentCar.myVelocity;
							}
							
							if(carBehindLeftLane != null &&   // cannot pass due to car behind in last lane
								futureDistanceBehindLeftLane > -safeDistance){
									currentCar.changeVelocity(futureVelocity);
							}else if(carAheadLeftLane == null ||   // can switch to left lane at full speed
									futureDistanceAheadLeftLane >= safeDistance){
									if(carAheadLeftLane == null){
										currentCar.switchLeftAndChangeVelocity(SPEED_LIMIT);
									}
									else{
										futureVelocity = Math.min( SPEED_LIMIT , Math.round( (float) 0.5*( carAheadLeftLane.position 
																				- currentCar.position -1 
																				+ carAheadLeftLane.myVelocity - 1 )) ); ;
										currentCar.switchLeftAndChangeVelocity(futureVelocity);
									}
									
							}else if(futureDistanceAheadLeftLane >= futureDistance){
								futureVelocity = Math.min( SPEED_LIMIT , Math.round( (float) 0.5*( carAheadLeftLane.position 
										- currentCar.position -1 
										+ carAheadLeftLane.myVelocity - 1 )) ); 
										currentCar.switchLeftAndChangeVelocity(futureVelocity);
							}else{
								currentCar.changeVelocity(futureVelocity);
							}
						}
					}
				}
				else if (i==1){  //left lane
					Car carAheadRightLane = null;
					Car carBehindRightLane = null;
					for(Car c: model.lanes.get(i - 1)){
						if(c.position >= currentCar.position){
							carAheadRightLane = c;
						}
					}
					for(Car c: model.lanes.get(i - 1)){
						if(c.position <= currentCar.position){
							carBehindRightLane = c;
						}
						break;
					}
					
					int futureDistanceAheadRightLane = Integer.MAX_VALUE;
					if(carAheadRightLane!=null){
						 futureDistanceAheadRightLane = carAheadRightLane.position
								 				   - currentCar.position -1
								 				   + carAheadRightLane.myVelocity
								 				   - currentCar.myVelocity;
					}
					int futureDistanceBehindRightLane = Integer.MAX_VALUE;
					if(carBehindRightLane!=null){
						futureDistanceBehindRightLane = currentCar.position 
												   - carBehindRightLane.position - 2
								 				   + currentCar.myVelocity
								 				   - carBehindRightLane.myVelocity;
					}
					if(carAheadRightLane == null || futureDistanceAheadRightLane >= safeDistance){  //check to see if can join right
						if(carBehindRightLane == null || futureDistanceBehindRightLane >= safeDistance){
							if(carAheadRightLane == null){
								currentCar.switchRightAndChangeVelocity(SPEED_LIMIT);
							}
							else{
								int futureVelocity = Math.min( SPEED_LIMIT , Math.round((float) 0.5 * 
												(carAheadRightLane.position - currentCar.position -1
												+ carAheadRightLane.myVelocity)) );
								currentCar.switchRightAndChangeVelocity(futureVelocity);
							}
						}
					}else{
						if(j>0){
							Car carAhead = model.lanes.get(i).get(j-1);
							int gap = carAhead.position - currentCar.position - 1;
							int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;
							int futureVelocity = Math.min( SPEED_LIMIT , Math.round( (float) 0.5* (gap + carAhead.myVelocity - 1)) );
		
							currentCar.changeVelocity(futureVelocity);
						}
						else{
							currentCar.changeVelocity(SPEED_LIMIT);
						}
					}
				}
			}
		}
		updatePosition();
	}
	
	public static void updatePosition(){
		for(int i = 0; i < model.lanes.size(); i++){
			for(int j = 0; j < model.lanes.get(i).size(); j++){
				Car currentCar = model.lanes.get(i).get(j);
				currentCar.position = currentCar.futurePosition;
				currentCar.lane = currentCar.futureLane;
			}
			model.lanes.get(i).clear();
		}
		
		for(Car car : cars){
			int newLanes = car.lane;
			model.lanes.get(newLanes).add(car);
		}
		
		for(int i = 0; i < model.lanes.size(); i++){
			ArrayList<Car> eachLane = model.lanes.get(i);
			Collections.sort(eachLane);
		}
	}
	
	public static String printCars(){
		int maxPosition = 0; 
		for (Car car : cars){
			maxPosition = Math.max(maxPosition , car.position);
		}

		Car[][] gridForPrint = new Car[model.lanes.size()][maxPosition+1]; 
		
		String lane = "";
		for(int i = 0; i < model.lanes.size(); i++){
				for(int j = 0; j < model.lanes.get(i).size(); j++){
					Car currentCar = model.lanes.get(i).get(j);
					gridForPrint[i][currentCar.position] = currentCar;
				}
		}
		
		for(int i= 0; i < model.lanes.size(); i++){
			String thisLane = "";
			for(int j = 0; j < maxPosition; j++){
				if(gridForPrint[i][j] != null){
					thisLane += gridForPrint[i][j];
				}
				else{
					thisLane += "0";
				}
			}
			System.out.print(thisLane+"\n");
		}
		return lane;
	}
	
	
	
}
