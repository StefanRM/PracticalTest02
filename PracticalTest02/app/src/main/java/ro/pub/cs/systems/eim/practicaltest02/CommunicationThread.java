package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    private class NISTCommunicationAsyncTask extends AsyncTask<Void, Void, String> {
        public String time = null;

        @Override
        protected String doInBackground(Void... params) {
            String dayTimeProtocol = null;
            try {
                Socket socket = new Socket(Constants.NIST_SERVER_HOST, Constants.NIST_SERVER_PORT);
                BufferedReader bufferedReader = Utilities.getReader(socket);
                dayTimeProtocol = bufferedReader.readLine();
                Log.d(Constants.TAG, "The server returned: " + dayTimeProtocol);
                this.time = dayTimeProtocol;
            } catch (UnknownHostException unknownHostException) {
                Log.d(Constants.TAG, unknownHostException.getMessage());
                if (Constants.DEBUG) {
                    unknownHostException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.d(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            return dayTimeProtocol;
        }

    }

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.d(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();
            String informationType = bufferedReader.readLine();
            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty() || informationType == null || informationType.isEmpty()) {
                Log.d(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Received:" + hour + " " + minute + " " + informationType);

            HashMap<String, ServerData> data = serverThread.getData();
            ServerData serverData = null;

            String ipAddr = socket.getInetAddress().toString();

            if (data.containsKey(ipAddr)) {
                Log.d(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                serverData = data.get(ipAddr);
            } else {
                Log.d(Constants.TAG, "[COMMUNICATION THREAD] Server Data Information is null!");
                if (informationType.equals(Constants.SET)) {
                    serverData = new ServerData(hour, minute);
                    serverThread.setData(ipAddr, serverData);

                    printWriter.println(informationType + " " + serverData.toString());
                    printWriter.flush();
                    return;
                } else {
                    printWriter.println("none");
                    printWriter.flush();
                    return;
                }
            }

            String result = "";
            switch (informationType) {
                case Constants.RESET:
                    serverThread.removeData(ipAddr);
                    break;

                case Constants.POLL:
//                    NISTCommunicationAsyncTask nistCommunicationAsyncTask = new NISTCommunicationAsyncTask();
//                    nistCommunicationAsyncTask.execute();
//                    result = nistCommunicationAsyncTask.time;
                      result = Constants.DATE;
                      String[] splited = result.split(" ");
                      String resultHout = splited[4].charAt(0) + "" + splited[4].charAt(1);
                      String resultMin = splited[4].charAt(3) + "" + splited[4].charAt(4);

                      result = resultHout + resultMin;
                      int h = Integer.parseInt(resultHout);
                      int m = Integer.parseInt(resultMin);

                      if (h < serverData.getHour() || (h == serverData.getHour() && m < serverData.getMinute()))
                          result = "active " + serverData.toString();
                      else
                          result = "inactive " + serverData.toString();

                    break;

                default:
                    result = "[COMMUNICATION THREAD] Wrong information type (all / country / timezone)!";
            }

            printWriter.println(informationType + " " + result);
            printWriter.flush();


        } catch (IOException ioException) {
            Log.d(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
//        } catch (JSONException jsonException) {
//            Log.d(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.d(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
