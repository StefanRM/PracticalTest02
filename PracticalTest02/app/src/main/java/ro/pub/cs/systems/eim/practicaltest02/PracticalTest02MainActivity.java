package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private Button serverButton = null;

    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText clienHourEditText = null;
    private EditText clientMinuteEditText = null;
    private TextView resultTextView = null;
    private Button clientSetButton = null;
    private Button clientResetButton = null;
    private Button clientPollButton = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ServerButtonClickListener serverButtonClickListener = new ServerButtonClickListener();
    private class ServerButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.d(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private SetClickListener setClickListener = new SetClickListener();
    private class SetClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String hour = clienHourEditText.getText().toString();
            String minute = clientMinuteEditText.getText().toString();
            String informationType = Constants.SET;
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty()
                    || informationType == null || informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), hour, minute, informationType, resultTextView);
            clientThread.start();
        }
    }

    private ResetClickListener resetClickListener = new ResetClickListener();
    private class ResetClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String hour = clienHourEditText.getText().toString();
            String minute = clientMinuteEditText.getText().toString();
            String informationType = Constants.RESET;
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty()
                    || informationType == null || informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), hour, minute, informationType, resultTextView);
            clientThread.start();
        }
    }

    private PollClickListener pollClickListener = new PollClickListener();
    private class PollClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String hour = clienHourEditText.getText().toString();
            String minute = clientMinuteEditText.getText().toString();
            String informationType = Constants.POLL;
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty()
                    || informationType == null || informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), hour, minute, informationType, resultTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText) findViewById(R.id.server_port);
        serverButton = (Button) findViewById(R.id.server_button);
        serverButton.setOnClickListener(serverButtonClickListener);

        clientAddressEditText = (EditText) findViewById(R.id.client_address);
        clientPortEditText = (EditText) findViewById(R.id.client_port);
        clienHourEditText = (EditText) findViewById(R.id.client_hour);
        clientMinuteEditText = (EditText) findViewById(R.id.client_minute);
        resultTextView = (TextView) findViewById(R.id.result_info);
        clientSetButton = (Button) findViewById(R.id.client_set);
        clientResetButton = (Button) findViewById(R.id.client_reset);
        clientPollButton = (Button) findViewById(R.id.client_poll);
        clientSetButton.setOnClickListener(setClickListener);
        clientResetButton.setOnClickListener(resetClickListener);
        clientPollButton.setOnClickListener(pollClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.d(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
