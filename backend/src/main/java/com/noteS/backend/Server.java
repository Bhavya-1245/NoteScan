package com.noteS.backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

// import com.noteS.dest.Gui;

@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		// This single line turns off headless mode AND starts the server safely
		// Integer.
		System.out.println(
			"Before Spring Boot: " +
			java.awt.GraphicsEnvironment.isHeadless()
		);

        new SpringApplicationBuilder(Server.class)
                .headless(false) 
                .run(args);
                

				
			
				
			
				System.out.println(
					"After Spring Boot: " +
					java.awt.GraphicsEnvironment.isHeadless()
				);
			
				
        // Notice we REMOVED the "new Gui();" from here! 
        // The DesktopNotepadController will handle opening the window automatically.
	}
	
}
