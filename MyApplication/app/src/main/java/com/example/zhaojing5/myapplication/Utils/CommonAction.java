package com.example.zhaojing5.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;

public class CommonAction {

   public static  void takeAction(Context context,String actionType,String actionUrl) {
//       try {
//           if (!TextUtils.isEmpty(actionType) && !TextUtils.isEmpty(actionUrl)){
//               SchemeDispatcher schemeDispatcher = SchemeDispatcher.getInstance();
//               if(actionType.equals(SchemeDispatcher.SchemeType.H5.getType())){
//                   SchemeDispatcher.H5Params h5Params =  new SchemeDispatcher.H5Params();
//                   h5Params.setUrl(actionUrl);
//                   Gson gson = new Gson();
//                   schemeDispatcher.scheme(context,SchemeDispatcher.SchemeType.H5.getType(),gson.toJson(h5Params));
//
//               }else if(actionType.equals(SchemeDispatcher.SchemeType.Native.getType())){
//                   SchemeDispatcher.NativeParams nativeParams = new SchemeDispatcher.NativeParams();
//                   nativeParams.setParams("");
//                   nativeParams.setType("");
//                   nativeParams.url = actionUrl;
//                   Gson gson = new Gson();
//                   schemeDispatcher.scheme(context,SchemeDispatcher.SchemeType.Native.getType(),gson.toJson(nativeParams));
//
//               }else if(actionType.equals(SchemeDispatcher.SchemeType.App.getType())){
//                   SchemeDispatcher.AppParams appParams = new SchemeDispatcher.AppParams();
//                   appParams.setUrl(actionUrl);
//                   appParams.params = "";
//                   Gson gson = new Gson();
//                   schemeDispatcher.scheme(context,SchemeDispatcher.SchemeType.App.getType(),gson.toJson(appParams));
//
//               } else if(actionType.equals(SchemeDispatcher.SchemeType.Notification.getType())){
//                   schemeDispatcher.scheme(context,SchemeDispatcher.SchemeType.Notification.getType(),actionUrl);
//               } else if(actionType.equals(SchemeDispatcher.SchemeType.Toast.getType())){
//                   schemeDispatcher.scheme(context,SchemeDispatcher.SchemeType.Toast.getType(),actionUrl);
//               }
//           }
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
   }

    public static void notifyCoinsStatusChanged( Context context ) {
//        if ( context != null ) {
//            Intent intent = new Intent();
//            intent.setAction( TaskConstants.ACTION_UPDATE_COINS_STATUS );
//            context.sendBroadcast( intent );
//        }
    }

}
