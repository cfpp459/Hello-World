package com.example.zhaojing5.myapplication.binderProxy;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IBank extends IInterface {
    static final String DESCRIPTION = "com.example.zhaojing5.myapplication.binderProxy";
    static final int TRANSACTION_querryMoney = IBinder.FIRST_CALL_TRANSACTION + 0;
    public long querryMoney(int money) throws RemoteException;
}
