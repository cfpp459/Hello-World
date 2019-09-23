package com.example.zhaojing5.myapplication.binderProxy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BankImpl extends Binder implements IBank {

    BankImpl(){
        this.attachInterface(this,DESCRIPTION);
    }

    @Override
    public long querryMoney(int money) throws RemoteException {
        return money * 10l;
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public IBank asInterface(IBinder iBinder){
        if(iBinder == null){
            return null;
        }
        IInterface iInterface = this.queryLocalInterface(DESCRIPTION);
        if(iInterface != null && iInterface instanceof IBank){
            return (IBank) iInterface;
        }
        return new BankImpl.Proxy(iBinder);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch(code){
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTION);
                return true;
            case TRANSACTION_querryMoney:
                data.enforceInterface(DESCRIPTION);
                int money = data.readInt();
                long result = this.querryMoney(money);
                reply.writeNoException();
                reply.writeLong(result);
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IBank{
        private IBinder remote;

        Proxy(IBinder iBinder){
            this.remote = iBinder;
        }

        @Override
        public long querryMoney(int money) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            long result;
            data.writeInterfaceToken(DESCRIPTION);
            data.writeInt(money);
            remote.transact(TRANSACTION_querryMoney,data,replay,0);
            replay.readException();
            result = replay.readLong();
            replay.recycle();
            data.recycle();
            return result;
        }

        @Override
        public IBinder asBinder() {
            return remote;
        }

        public String getInterfaceDescripter(){
            return DESCRIPTION;
        }
    }

}
