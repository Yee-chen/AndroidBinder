package cn.edu.zstu.bindservicedemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    public static final int MSG_SAY_HELLO = 1;

    public MessengerService() {
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "[handleMessage()]: 这是来自客户端的消息：" + msg.getData().getString("msg"));
            Toast.makeText(getApplicationContext(), "这是来自客户端的消息\n" + msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg", "Hello，Client!");
            message.setData(bundle);
            try {
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

//            switch (msg.what){
//                case MSG_SAY_HELLO:
//                    Toast.makeText(getApplicationContext(), "这是来自客户端的消息\nHello Messenger!", Toast.LENGTH_SHORT).show();
//                    Message message = new Message();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg","Hello，客户端");
//                    message.setData(bundle);
//                    try {
//                        msg.replyTo.send(message);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }
        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Toast.makeText(getApplicationContext(), "Service Binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }
}
