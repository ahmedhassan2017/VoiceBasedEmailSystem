package com.example.abanoub.voicebasedemailsystem.Shaking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Mahmoud Heshmat on 3/7/2018.
 */

public class ReceiverCall  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhhhhh");
        context.startService(new Intent(context, MyService.class));
    }

}