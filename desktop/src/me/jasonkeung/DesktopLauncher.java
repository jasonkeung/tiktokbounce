package me.jasonkeung;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.jasonkeung.BounceSongGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);
		config.setTitle("BounceSong2");
		config.setWindowedMode(BounceSongGame.WIDTH, BounceSongGame.HEIGHT);
		config.setWindowPosition(0, 0);
		new Lwjgl3Application(new BounceSongGame(), config);
	}
}
