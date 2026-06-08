package com.noteS.backend; // 1. Matches your backend folder

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.noteS.dest.Gui; // 2. Tells Spring Boot where to find your Gui!

@RestController
@RequestMapping("/api/v1")
public class DesktopNotepadController {

    private Gui myGui; 

    public DesktopNotepadController() {
        // 3. Launches your custom Notepad!
        myGui = new Gui();
        // (Your Gui constructor already handles making the window visible)
    }

    @PostMapping("/scan")
    public ResponseEntity<String> receiveScan(@RequestBody ScanRequest request) {
        
        // 4. Sends the phone's text directly into your Notepad's method
        myGui.appendTextFromPhone(request.getContent());
        
        return ResponseEntity.ok("Successfully pasted to PC!");
    }
}