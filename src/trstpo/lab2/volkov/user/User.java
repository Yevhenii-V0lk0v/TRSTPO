package trstpo.lab2.volkov.user;

import trstpo.lab2.volkov.container.ShowerRoom;

import java.util.logging.Logger;

public abstract class User extends Thread {
	private ShowerRoom showerRoom;

	User(ShowerRoom showerRoom) {
		this.showerRoom = showerRoom;
	}

	@Override
	public void run() {
		try {
			while (!showerRoom.isEnd()) {
				while (!showerRoom.isEnd() && !showerRoom.enter(this)) {
					sleep(1000);
				}
				System.out.println(this.getClass().getSimpleName() + this.getId() + ": Got a resource " + showerRoom.getResource(this).toString() + " and did something with it");
				sleep(1000);
				showerRoom.leave(this);
				sleep(1000);
			}
		} catch (InterruptedException e) {
			Logger.getLogger("Interrupted by external actor");
		}
	}
}
