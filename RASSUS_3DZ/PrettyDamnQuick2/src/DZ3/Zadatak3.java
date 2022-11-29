package DZ3;

import com.perfdynamics.pdq.Job;
import com.perfdynamics.pdq.Methods;
import com.perfdynamics.pdq.Node;
import com.perfdynamics.pdq.PDQ;
import com.perfdynamics.pdq.QDiscipline;

public class Zadatak3 {
	
	public static void main(String[] args) {
		PDQ pdq = new PDQ();
		
		final float  S1 = 0.01f;
		final float  S2 = 0.05f;
		final float  S3 = 0.5f;
		final float  S4 = 0.2f;
		final float  S5 = 0.1f;
		final float  S6 = 0.13f;
		final float  S7 = 0.15f;
		
		final float v1 = 0.2f;
		final float v2 = 0.411f;
		final float v3 = 0.5f;
		final float v4 = 1.111f;
		final float v5 = 1f;
		final float v6 = 0.713f;
		final float v7 = 1.021f;
		
		
		//for(float lambda = 0.5f; lambda <= 5; lambda += 0.5 ) {
		final float lambda=1;
			pdq.Init("Main");
			
			pdq.CreateOpen("Zahtjevi", lambda);
			
			pdq.CreateNode("Posluzitelj1", Node.CEN, QDiscipline.FCFS);
			pdq.CreateNode("Posluzitelj2", Node.CEN, QDiscipline.FCFS);
			pdq.CreateNode("Posluzitelj3", Node.CEN, QDiscipline.FCFS);
			pdq.CreateNode("Posluzitelj4", Node.CEN, QDiscipline.FCFS);
			pdq.CreateNode("Posluzitelj5", Node.CEN, QDiscipline.FCFS);
//			pdq.CreateNode("Posluzitelj6", Node.CEN, QDiscipline.FCFS);
//			pdq.CreateNode("Posluzitelj7", Node.CEN, QDiscipline.FCFS);
			
			
			pdq.SetVisits("Posluzitelj1", "Zahtjevi", v1, S1);
			pdq.SetVisits("Posluzitelj2", "Zahtjevi", v2, S2);
			pdq.SetVisits("Posluzitelj3", "Zahtjevi", v3, S3);
			pdq.SetVisits("Posluzitelj4", "Zahtjevi", v4, S4);
			pdq.SetVisits("Posluzitelj5", "Zahtjevi", v5, S5);
//			pdq.SetVisits("Posluzitelj6", "Zahtjevi", v6, S6);
//			pdq.SetVisits("Posluzitelj7", "Zahtjevi", v7, S7);
			
			pdq.Solve(Methods.CANON);
			
			double N1 = pdq.GetQueueLength("Posluzitelj1", "Zahtjevi", Job.TRANS);
	        double N2 = pdq.GetQueueLength("Posluzitelj2", "Zahtjevi", Job.TRANS);
	        double N3 = pdq.GetQueueLength("Posluzitelj3", "Zahtjevi", Job.TRANS);
	        double N4 = pdq.GetQueueLength("Posluzitelj4", "Zahtjevi", Job.TRANS);
	        double N5 = pdq.GetQueueLength("Posluzitelj5", "Zahtjevi", Job.TRANS);
//	        double N6 = pdq.GetQueueLength("Posluzitelj6", "Zahtjevi", Job.TRANS);
//	        double N7 = pdq.GetQueueLength("Posluzitelj7", "Zahtjevi", Job.TRANS);
			
			
			double T1 = pdq.GetResidenceTime("Posluzitelj1", "Zahtjevi", Job.TRANS);
	        double T2 = pdq.GetResidenceTime("Posluzitelj2", "Zahtjevi", Job.TRANS);
	        double T3 = pdq.GetResidenceTime("Posluzitelj3", "Zahtjevi", Job.TRANS);
	        double T4 = pdq.GetResidenceTime("Posluzitelj4", "Zahtjevi", Job.TRANS);
	        double T5 = pdq.GetResidenceTime("Posluzitelj5", "Zahtjevi", Job.TRANS);
//	        double T6 = pdq.GetResidenceTime("Posluzitelj6", "Zahtjevi", Job.TRANS);
//	        double T7 = pdq.GetResidenceTime("Posluzitelj7", "Zahtjevi", Job.TRANS);
	       
	        double T = pdq.GetResponse(Job.TRANS, "Zahtjevi");
	        
	        System.out.println("Lambda = " + lambda);
	        
	        
			System.out.println("N1 = " + N1);
			System.out.println("N2 = " + N2);
			System.out.println("N3 = " + N3);
//			System.out.println("N4 = " + N4);
//			System.out.println("N5 = " + N5);
//			System.out.println("N6 = " + N6);
//			System.out.println("N7 = " + N7);
			
			System.out.println();
			
			System.out.println("T1 = " + T1);
			System.out.println("T2 = " + T2);
			System.out.println("T3 = " + T3);
//			System.out.println("T4 = " + T4);
//			System.out.println("T5 = " + T5);
//			System.out.println("T6 = " + T6);
//			System.out.println("T7 = " + T7);
			
			
			System.out.println("T = " + T);
			
			
			System.out.println("=========================================");
		}
	//}
}
