package trstpo.lab2.container;

import trstpo.lab2.user.Gal;
import trstpo.lab2.user.Guy;
import trstpo.lab2.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShowerRoom<T> extends Thread {

	private List<User> entered;
	private Class<? extends User> currentUserType;
	private long timeout;
	private T resource;
	private boolean end;

	public ShowerRoom(long timeout, T resource) {
		super();
		this.timeout = timeout;
		this.resource = resource;
		this.end = false;
		this.entered = new ArrayList<>();
		start();
	}

	public synchronized boolean enter(User user) {
		if (!end) {
			if (currentUserType == null) {
				currentUserType = user.getClass();
			}
			if (currentUserType.equals(user.getClass())) {
				entered.add(user);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public T getResource(User user) {
		if (entered.contains(user)) {
			return resource;
		}
		return null;
	}

	public void leave(User user) {
		entered.remove(user);
	}

	@Override
	public void run() {
		while (!end) {
			try {
				sleep(timeout);
				System.out.println("Switch");
				entered.clear();
				if (Gal.class.equals(currentUserType)) {
					currentUserType = Guy.class;
				} else {
					currentUserType = Gal.class;
				}
			} catch (InterruptedException e) {
				Logger.getLogger(this.getClass().getSimpleName()).info("Shower stopped working forever");
			}
		}
		entered.clear();
	}

	@Override
	public void interrupt() {
		super.interrupt();
		end = true;
	}

	public boolean isEnd() {
		return end;
	}
}
