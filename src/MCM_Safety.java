public class MCM_Safety {

	public double computeSafety(int vx, int v1, int v2, int v3, int v4, int l1,
			int l0, int l0b, int l1b) {
		double r1, r2, r3, r4;
		if (vx > v1) {
			r1 = Math.pow(l1, (((double) vx) - v1));
		} else {
			r1 = 1 / Math.pow(l1, (((double) vx) - v1));
		}
		if (vx > v2) {
			r2 = Math.pow(l0, (((double) vx) - v2));
		} else {
			r2 = 1 / Math.pow(l0, (((double) vx) - v2));
		}
		if (vx > v3) {
			r3 = Math.pow(l0b, (((double) vx) - v3));
		} else {
			r3 = 1 / Math.pow(l0b, (((double) vx) - v3));
		}
		if (vx > v4) {
			r4 = Math.pow(l1b, (((double) vx) - v4));
		} else {
			r4 = 1 / Math.pow(l1b, (((double) vx) - v4));
		}
		return Math.log(r1 * r2 * r3 * r4 * (1 / vx));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MCM_Safety a = new MCM_Safety();
		System.out.println(a.computeSafety(1, 2, 1, 5, 10, 1, 1, 1, 1));
	}

}
