package saiso;

import java.util.concurrent.Delayed;

public class saiso_ofme {

	public static void main(String[] args) {
		double AD[] = { 350, 250, 250 };
		double Q_encoder[][] = new double[100][3];
		double delta_q[] = new double[4];
		delta_q[1] = 2 * Math.PI / 6000;
		delta_q[0] = 2 * Math.PI / 6000;
		delta_q[2] = 2 * Math.PI / 1000;
		for (int i = 0; i <= 3; i++) {
			hamlietke(i);
		}
		// nap bien khop vao mang de su dung
		for (int j = 1; j <= 6; j++) {
			double Q[] = Q(Pi(j), AD);
			// double q1=Math.round(Q[0]/delta_q1)*delta_q1;
			// double q2=Math.round(Q[1]/delta_q2)*delta_q2;
			for (int k = 0; k <= 2; k++) {
				if (k == 2) {
					Q_encoder[j][k] = Math.round(Q[k + 1] / delta_q[k]) * delta_q[k];
				} else {
					Q_encoder[j][k] = Math.round(Q[k] / delta_q[k]) * delta_q[k];
				}
				// System.out.println(Q_encoder[j][k]);
			}
			// System.out.println();
		}
		// tinh toan sai so Q_ss khi biet ad
		for (int a = 1; a <= 6; a++) {
			int b = 0;
			double Q_[] = { Q_encoder[a][b], Q_encoder[a][b + 1], Q(Pi(a), AD)[2], Q_encoder[a][b + 2] };

			for (int h = 1; h <= 13; h++) {
				double P[] = Pdr(Pi(a), h, 0.5, Math.PI / 180);
				double AD_[] = AD(Q_, P);

				double P1[] = P(Q_, AD_);
				double P11[] = P(Q_, AD);
				// sai so ban kinh R theo Q_encoder
				double R = R(P11, Pi(a));
				if (R > 0.163) {
					// System.out.println(P11[2]);
			}
			System.out.println();
			}
		}
		double QT[][]=QuyTich_P();
	/*	for(int j=0;j<=671287;j++) {
			for(int i=0;i<=1;i++) {
				System.out.println(QT[j][i]+" ");
			}System.out.println();
		}*/
		Q_SS_(QT, AD);
		
	}

	public static void hamlietke(int chon) {
		double AD[] = { 350, 250, 250 };
		double dr = 0.5;
		double dp = Math.PI / 180;
		double Pi[] = Pi(6);
		for (int i = 1; i <= 13; i++) {
			double Pdr[] = Pdr(Pi, i, dr, dp);
			double Q_SS[] = Q(Pdr, AD);
			// System.out.println(Pdr[chon]);
			// System.out.println(Q_SS[chon]);
		}
		System.out.println();
	}

	// tinh toan vi tri diem cuoi khi biet Q AD
	public static double[] P(double Q[], double AD[]) {
		double q1 = Q[0];
		double q2 = Q[1];
		double q3 = Q[2];
		double q4 = Q[3];
		double a1 = AD[0];
		double a2 = AD[1];
		double d14 = AD[2];

		double px = a2 * cos(q1 + q2) + a1 * cos(q1);
		double py = a2 * sin(q1 + q2) + a1 * sin(q1);
		double pz = d14 - q3;
		double phi = q4 - q1 + q2;

		double P[] = { px, py, pz, phi };
		return P;
	}

	// sai so cho phep cua diem cuoi vs dr=0.5mm va dp=1 degree
	public static double[] Pdr(double P[], int chose, double dr, double dp) {
		double px = P[0];
		double py = P[1];
		double pz = P[2];
		double phi = P[3];
		double Pdr[] = { 0, 0, 0, 0 };
		switch (chose) {
		case 1:
			Pdr = new double[] { px, py, pz, phi };
			break;
		case 2:
			Pdr = new double[] { px + dr, py, pz, phi + dp };
			break;
		case 3:
			Pdr = new double[] { px + dr, py, pz, phi - dp };
			break;
		case 4:
			Pdr = new double[] { px, py + dr, pz, phi + dp };
			break;
		case 5:
			Pdr = new double[] { px, py + dr, pz, phi - dp };
			break;
		case 6:
			Pdr = new double[] { px - dr, py, pz, phi + dp };
			break;
		case 7:
			Pdr = new double[] { px - dr, py, pz, phi - dp };
			break;
		case 8:
			Pdr = new double[] { px, py - dr, pz, phi + dp };
			break;
		case 9:
			Pdr = new double[] { px, py - dr, pz, phi - dp };
			break;
		case 10:
			Pdr = new double[] { px, py, pz - dr, phi + dp };
			break;
		case 11:
			Pdr = new double[] { px, py, pz - dr, phi - dp };
			break;
		case 12:
			Pdr = new double[] { px, py, pz + dr, phi + dp };
			break;
		case 13:
			Pdr = new double[] { px, py, pz + dr, phi - dp };
			break;
		default:
			break;
		}
		return Pdr;

	}

	public static double sin(double a) {
		return Math.sin(a);
	}

	public static double cos(double a) {
		return Math.cos(a);
	}

	public static double bp(double a) {
		return Math.pow(a, 2);
	}

	// tinh toan bien khop q khi biet P, AD
	public static double[] Q(double P[], double[] AD) {
		double px = P[0];
		double py = P[1];
		double pz = P[2];
		double phi = P[3];
		double a1 = AD[0];
		double a2 = AD[1];
		double d14 = AD[2];

		double cq2 = (bp(px) + bp(py) - bp(a1) - bp(a2)) / (2 * a1 * a2);
		double sq2 = Math.sqrt(1 - bp(cq2));
		double q2 = Math.atan2(sq2, cq2);
		double cq1 = (a1 * px + a2 * (px * cq2 + py * sq2)) / (bp(px) + bp(py));
		double sq1 = (a1 * py + a2 * (py * cq2 - px * sq2)) / (bp(px) + bp(py));
		double q1 = Math.atan2(sq1, cq1);

		double q3 = d14 - pz;
		double q4 = -Math.atan2(Math.sin(-phi), Math.cos(phi)) + q1 + q2;
		double Q[] = { q1, q2, q3, q4 };
		return Q;
	}

	// tinh toan AD khi biet Q va P
	public static double[] AD(double[] Q, double P[]) {
		double px = P[0];
		double py = P[1];
		double pz = P[2];
		double phi = P[3];
		double q1 = Q[0];
		double q2 = Q[1];
		double q3 = Q[2];
		double q4 = Q[3];
		double a2 = (py * cos(q1) - px * sin(q1)) / sin(q2);
		double a1 = px * cos(q1) - a2 * cos(q2) + py * sin(q1);
		double d14 = pz + q3;

		double AD[] = { a1, a2, d14 };
		return AD;
	}

	public static double[] Pi(int chose) {

		double Pi[] = { 0, 0, 0, 0 };
		switch (chose) {
		case 1:
			Pi = new double[] { 350, 200, 160, 30 * Math.PI / 180 };
			break;
		case 2:
			Pi = new double[] { 300, 250, 170, 60 * Math.PI / 180 };
			break;
		case 3:
			Pi = new double[] { 200, 100, 180, 90 * Math.PI / 180 };
			break;
		case 4: {
			Pi = new double[] { 100, 100, 190, 120 * Math.PI / 180 };
			break;
		}
		case 5: {
			Pi = new double[] { 550, 100, 200, 150 * Math.PI / 180 };
			break;
		}
		case 6: {
			Pi = new double[] { 250, 500, 220, 180 * Math.PI / 180 };
			break;
		}
		}
		return Pi;
	}

	public static double R(double P1[], double P2[]) {
		return Math.sqrt(bp(P1[0] - P2[0]) + bp(P1[1] - P2[1]) + bp(P1[2] - P2[2]));
	}

	public static double [][] QuyTich_P() {
		double QT[][]=new double [671288][2];
		int a=0;int b=0;
		double a1=350;double a2=250;double d14=250;
		for(int i=-300;i<=600;i++) {
			for(int j=-600;j<=600;j++) {
				double px=i;double py=j;
				double cq2 = (bp(px) + bp(py) - bp(a1) - bp(a2)) / (2 * a1 * a2);
				double sq2 = Math.sqrt(1 - bp(cq2));
				double q2 = Math.atan2(sq2, cq2);
				double cq1 = (a1 * px + a2 * (px * cq2 + py * sq2)) / (bp(px) + bp(py));
				double sq1 = (a1 * py + a2 * (py * cq2 - px * sq2)) / (bp(px) + bp(py));
				double q1 = Math.atan2(sq1, cq1);
				
				if(q2>2*Math.PI) q2=q2-2*Math.PI;
				if(q2<-2*Math.PI) q2=q2+2*Math.PI;
				if(q1>2*Math.PI) q1=q1-2*Math.PI;
				if(q1<-2*Math.PI) q1=q1+2*Math.PI;
				
				if(q1>=-96*Math.PI/180&&q1<=96*Math.PI) {
					if(q2>=-115*Math.PI/180&&q2<=115*Math.PI) {
						//System.out.println(i+" "+j);
						QT[b][0]=i;QT[b][1]=j;
						b++;
					}
				}
			}
		}
		return QT;
			
	}

	// tinh rieng q1 q2  
	public static void Q_SS_(double QT[][],double []AD){
		double min1=0.02;double min2=0.02;double delta_q1 = 1;double delta_q2=1;
		double delta_q[][]=new double [671288][2];
		double QT1[][]=QuyTich_P();
		for(int i=0;i<=671287;i++) {
			double qt_q1[] = new double [5];double qt_q2[] = new double[5];int b=0;
			for(int j=1;j<=5;j++) {
				double Pdr[]=Pdr_(QT1[i][0],QT1[i][1], j, 0.5, Math.PI/180);
				double px=Pdr[0];double py=Pdr[1];
				double a1 = AD[0];
				double a2 = AD[1];
				double d14 = AD[2];

				double cq2 = (bp(px) + bp(py) - bp(a1) - bp(a2)) / (2 * a1 * a2);
				double sq2 = Math.sqrt(1 - bp(cq2));
				double q2 = Math.atan2(sq2, cq2);
				double cq1 = (a1 * px + a2 * (px * cq2 + py * sq2)) / (bp(px) + bp(py));
				double sq1 = (a1 * py + a2 * (py * cq2 - px * sq2)) / (bp(px) + bp(py));
				double q1 = Math.atan2(sq1, cq1);
				
				if(Math.abs(q1)<min1) min1=Math.abs(q1);
				if(Math.abs(q2)<min2) min2=Math.abs(q2);
				
				qt_q1[b]=q1;
				qt_q2[b]=q2;b++;

			}
			 delta_q1=max(qt_q1)-min(qt_q1);
			 delta_q2=max(qt_q2)-min(qt_q2);
			 
			 delta_q[i][0]=delta_q1;delta_q[i][1]=delta_q2;	
			
		}
		    
		    System.out.println(min_1(delta_q));
		    System.out.println(min_2(delta_q));
			
	}
	
	
	public static double[] Pdr_(double px,double py, int chose, double dr, double dp) {
		
		double Pdr[] = { 0, 0 };
		switch (chose) {
		case 1:
			Pdr = new double[] { px, py };
			break;
		case 2:
			Pdr = new double[] { px + dr, py };
			break;
		case 3:
			Pdr = new double[] { px - dr, py};
			break;
		case 4:
			Pdr = new double[] { px, py + dr };
			break;
		case 5:
			Pdr = new double[] { px, py - dr };
			break;
		
		default:
			break;
		}
		return Pdr;

	}
	public static double max(double a[]) {
		double max=0;
		for(int i=0;i<a.length;i++) {
			if(max<Math.abs(a[i])) max=Math.abs(a[i]);
		}
		return max;
	}
	public static double min(double a[]) {
		double min=1;
		for(int i=0;i<a.length;i++) {
			if(min>Math.abs(a[i])) min=Math.abs(a[i]);
		}
		return min;
	}
	public static double min_1(double a[][]) {
		double min=1;
		for(int i=0;i<a.length;i++) {	
				if(min>Math.abs(a[i][0])) min=Math.abs(a[i][0]);
		}return min;
	}
	public static double min_2(double a[][]) {
		double min=1;
		for(int i=0;i<a.length;i++) {	
				if(min>Math.abs(a[i][1])) min=Math.abs(a[i][1]);
		}return min;
	}
}