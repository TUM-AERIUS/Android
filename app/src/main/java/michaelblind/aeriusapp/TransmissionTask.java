package michaelblind.aeriusapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/** Created by Michael on 3/11/2017. */
public class TransmissionTask extends AsyncTask<Void, Void, Void> {
    JSONObject message;
    Socket socket;

    public TransmissionTask(int velocity, int steering, Socket socket) {
        this.socket = socket;
        try {setMessage(velocity, steering);}
        catch (JSONException e) {e.printStackTrace();}
    }

    private void setMessage(int velocity, int steering) throws JSONException {
        message = new JSONObject();
        message.put("velocity", String.format("%d", velocity));
        message.put("steering", String.format("%d", steering));
    }

    @Override
    protected Void doInBackground(Void... voids) {

        DataOutputStream out = null;

        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message.toString());
        } catch (IOException e) {print(e);}
        finally {
            if (out    != null) close(out);
            if (socket != null) close(socket);
        }

        System.out.println("Sent");

        return null;
    }

    private void close(Closeable c) {
        try {c.close();}
        catch (IOException e) {print(e);}
    }

    private void print(IOException e) {e.printStackTrace();}

}
