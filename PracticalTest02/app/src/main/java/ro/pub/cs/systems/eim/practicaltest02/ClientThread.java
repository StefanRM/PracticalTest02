package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String hour;
    private String minute;
    private String informationType;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(String address, int port, String hour, String minute, String informationType, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.hour = hour;
        this.minute = minute;
        this.informationType = informationType;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.d(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.d(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(hour);
            printWriter.flush();
            Log.d(Constants.TAG, "[CLIENT THREAD] Sent: " + hour);
            printWriter.println(minute);
            printWriter.flush();
            Log.d(Constants.TAG, "[CLIENT THREAD] Sent: " + minute);
            printWriter.println(informationType);
            printWriter.flush();
            Log.d(Constants.TAG, "[CLIENT THREAD] Sent: " + informationType);
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                final String finalizedInformation = info;
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(finalizedInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.d(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.d(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
