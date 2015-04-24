package Embed;

public final class NoOnetimePadProvidedException extends Exception {
	public NoOnetimePadProvidedException() {
		super("In order to use an external one time pad, you must provide one to use!");
	}
}
