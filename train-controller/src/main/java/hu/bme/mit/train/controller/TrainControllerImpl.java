package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;

import java.util.Timer;
import java.util.TimerTask;

public class TrainControllerImpl implements TrainController {

	private boolean exit_flag = false;

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;
	private Timer timer = new Timer(true);

	public TrainControllerImpl() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				followSpeed();
			}
		}, 500, 500);
	}

	private Thread change_speed = new Thread(() -> {
			try {
				while(!exit_flag) {
					Thread.sleep(1000);
					followSpeed();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
    });

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		exit_flag = true;
		change_speed.join();
	}

	{
		change_speed.run();
	}

	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
	}

}

