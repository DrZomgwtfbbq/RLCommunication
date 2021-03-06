package commSynchro1;
/* module for cat and mouse game.  used by applet, very specific parameters
   used by cat and mouse game applet.  controls RLearner. */

import javax.swing.*;

import Basic.BasicApplet;

public class RLController extends Thread {
	public RLearner learner;
	TwoAgentsApplet a;
	public int epochswaiting = 0, epochsdone = 0, totaldone = 0;
	long delay;
	int UPDATE_EPOCHS = 100;
	
	public boolean newInfo;
	
	public RLController(TwoAgentsApplet twoAgentsApplet, TwoAgentsWorld world, long waitperiod) {
		// create RLearner
		learner = new RLearner(world);
		this.a = twoAgentsApplet;
		delay = waitperiod;
	}
	
	public void run() {
		try {
			while(true) {
			 	if (epochswaiting > 0) {
					System.out.println("Running "+epochswaiting+" epochs");
					learner.running = true;
					while(epochswaiting > 0) {
						epochswaiting--;
						epochsdone++;
						learner.runEpoch();
						
						if (epochswaiting % UPDATE_EPOCHS == 0) 
							SwingUtilities.invokeLater(a);
					}
					totaldone += epochsdone;
					epochsdone = 0;
					learner.running = false;
					
					newInfo = true;
									
					// inform applet we're finished
					SwingUtilities.invokeLater(a);
				}
				
				sleep(delay);
			}
		} catch (InterruptedException e) {
			System.out.println("Controller interrupted.");
		}
	}
	
	public void setEpisodes(int episodes) { 
		System.out.println("Setting "+episodes+" episodes");
		this.epochswaiting += episodes;
	}
	public void stopLearner() {
		System.out.println("Stopping learner.");
		newInfo = false;
		epochswaiting = 0;
		totaldone += epochsdone;
		epochsdone = 0;
		
		// inform applet we're finished
		SwingUtilities.invokeLater(a);

		learner.running = false;
	}
	
	public synchronized RLPolicy resetLearner1() {
		totaldone = 0;
		epochsdone = 0;
		epochswaiting = 0;
	
		return learner.newPolicy1();		
	}
	public synchronized RLPolicy getPolicy2() {
		
		return learner.newPolicy2();		
	}
	
}
