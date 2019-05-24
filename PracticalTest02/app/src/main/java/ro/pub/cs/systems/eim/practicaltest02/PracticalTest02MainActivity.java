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
    private EditText clientQueryEditText = null;
    private Spinner resultInfoSpinner = null;
    private TextView resultTextView = null;
    private Button clientButton = null;

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

    private GetResultInfoClickListener getResultInfoClickListener = new GetResultInfoClickListener();
    private class GetResultInfoClickListener implements Button.OnClickListener {
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
            String query = clientQueryEditText.getText().toString();
            String informationType = resultInfoSpinner.getSelectedItem().toString();
            if (query == null || query.isEmpty()
                    || informationType == null || informationType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), query, informationType, resultTextView);
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
        clientQueryEditText = (EditText) findViewById(R.id.client_query);
        resultInfoSpinner = (Spinner) findViewById(R.id.client_spinner);
        resultTextView = (TextView) findViewById(R.id.result_info);
        clientButton = (Button) findViewById(R.id.client_button);
        clientButton.setOnClickListener(getResultInfoClickListener);
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
