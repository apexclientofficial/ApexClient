package com.apex.client.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpotifyManager {

    private static String currentTrack = "";
    private static Thread pollThread;

    public static void start() {
        if (pollThread != null && pollThread.isAlive()) return;

        pollThread = new Thread(() -> {
            while (true) {
                try {
                    String track = getSpotifyWindowTitle();
                    if (track != null && !track.isEmpty() && !track.equals("Spotify") && !track.equals("Spotify Premium")) {
                        currentTrack = track;
                    } else {
                        currentTrack = "";
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    try { Thread.sleep(5000); } catch (Exception ignored) {}
                }
            }
        });
        pollThread.setDaemon(true);
        pollThread.start();
    }

    public static String getCurrentTrack() {
        return currentTrack;
    }

    private static String getSpotifyWindowTitle() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("powershell", "-Command", "Get-Process | Where-Object {$_.ProcessName -eq 'Spotify' -and $_.MainWindowTitle -ne ''} | Select-Object -ExpandProperty MainWindowTitle -First 1");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine();
            if (line != null) {
                return line.trim();
            }
        }
        return null;
    }
}
