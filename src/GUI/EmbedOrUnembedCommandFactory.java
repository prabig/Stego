package GUI;

public abstract class EmbedOrUnembedCommandFactory {
	public static void selectEmbed(EmbedOrUnembedWindow window) {
		EmbedWindow embedWindow = new EmbedWindow();
		embedWindow.setUndecorated(true);
		embedWindow.setVisible(true);
		window.dispose();	
	}
	
	public static void selectUnembed(EmbedOrUnembedWindow window) {
		UnembedWindow unembedWindow = new UnembedWindow();
		unembedWindow.setUndecorated(true);
		unembedWindow.setVisible(true);
		window.dispose();
	}
	
}
