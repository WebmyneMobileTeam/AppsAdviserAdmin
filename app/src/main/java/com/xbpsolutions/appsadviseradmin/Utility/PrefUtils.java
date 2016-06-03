package com.xbpsolutions.appsadviseradmin.Utility;


import android.content.Context;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {


    public static void setUserData(Context context, boolean b) {
        Prefs.with(context).save("userdata", b);
    }

    public static boolean isUserDataSaved(Context ctx) {
        return Prefs.with(ctx).getBoolean("userdata", false);
    }

}
