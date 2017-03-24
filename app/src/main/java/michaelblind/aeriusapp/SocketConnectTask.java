package michaelblind.aeriusapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/** Created by Michael on 3/16/2017. */
public class SocketConnectTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        try {MainActivity.socket = new Socket("192.168.42.1", 5015);}
        catch (IOException e) {e.printStackTrace();}
        return null;
    }
}
