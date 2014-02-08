import java.util.*;

public class CarModel {
	
	ArrayList<Car> myLeft = new ArrayList<Car>();
	ArrayList<Car> myRight = new ArrayList<Car>();
	ArrayList<Car> resultsLeft = new ArrayList<Car>(); // results stored in this arraylist
	ArrayList<Car> resultsRight= new ArrayList<Car>(); // results stored in this arraylist
	
	int slow=4; int fast=5;
	
	public void makeLane(int l){
		myLeft= new ArrayList<Car>();
		myRight= new ArrayList<Car>();
		for(int i=0;i<l;i++){
			myLeft.add(i,new Car(0));
			myRight.add(i,new Car(0));
		}
		return;
	}

	public void putCars(int n){
		Random generator = new Random();
		for(int i=0;i<n;i++){					// for each car...
			int ind = Math.abs(generator.nextInt() % n); // which index?
			int left = generator.nextInt() % 2; // left lane or right lane? 
			int velo = generator.nextInt() % 2; // slow or fast?
			if( left == 0){
				myLeft.add(ind, new Car(slow*velo+fast*(Math.abs(velo-1))));
			}
			else{
				myRight.add(ind, new Car(slow*velo+fast*(Math.abs(velo-1))));
			}
		}
	}
	
	public void move(){
		for(int i=0; i<myLeft.size(); i++){
				determine(i,true);
				determine(i,false);
		}
	}
	
	public int findNext(int i, boolean left){
		ArrayList<Car> laneInInterest;
		
		if(left){ laneInInterest = myLeft;}
		else{ laneInInterest = myRight;}
		
		for(int r=i+1;r<laneInInterest.size();r++){
			if(laneInInterest.get(r).velocity!=0) return r;
		}
		return -1;
	}
	
	public void determine(int i, boolean left){
		Car current;
		
		if(left) { current = myLeft.get(i); }
		else {current = myRight.get(i); }
		
		int next = findNext(i,left);
		int gap = (next-i-1); 
		int perceivedVelocity;
		
		if(left){
			perceivedVelocity= myLeft.get(next).velocity - current.velocity;
		}
		else{
			perceivedVelocity = myRight.get(next).velocity - current.velocity;
		}
		
		if(gap+perceivedVelocity>=current.velocity){ // then stay
			if(left){
				resultsLeft.add(i+fast,current);
			}
			else{
				resultsRight.add(i+fast,current);
			}
		}
		
		else{
			int next2 = findNext(i,!left);
			int gap2 = (next2-i);
			int perceivedVelocity2;
			if(left){
				perceivedVelocity2=myRight.get(next2).velocity - current.velocity;
			}
			else{
				perceivedVelocity2=myLeft.get(next2).velocity - current.velocity;
			}
			if(gap2+perceivedVelocity2>=current.velocity){
				if(left){
					resultsRight.add(i+fast,current);
				}
				else{
					resultsLeft.add(i+fast,current);
				}
			}
			else{
				
				if(gap2+perceivedVelocity2>=gap+perceivedVelocity){
					if(left){
						resultsRight.add(i+slow,current);
					}
					else{
						resultsLeft.add(i+slow,current);
					}
				}
			}
		}		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CarModel a = new CarModel();
		a.makeLane(100);
		a.putCars(100);
		System.out.println(a.myLeft);
		System.out.println(a.myRight);
		a.move();
	}

}
