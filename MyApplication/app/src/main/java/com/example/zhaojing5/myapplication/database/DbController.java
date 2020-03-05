package com.example.zhaojing5.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhaojing5.myapplication.Utils.CommonUtils;
import com.example.zhaojing5.myapplication.bean.databasebean.DaoMaster;
import com.example.zhaojing5.myapplication.bean.databasebean.DaoSession;
import com.example.zhaojing5.myapplication.bean.databasebean.PersionInfo;
import com.example.zhaojing5.myapplication.bean.databasebean.PersionInfoDao;

import java.util.List;

/**
 * created by zhaojing 2020/2/17 上午11:10
 */
public class DbController {

    private DaoMaster.DevOpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    private Context context;

    private PersionInfoDao persionInfoDao;

    private static DbController mDbController;

    public static DbController getInstance(Context context){

        if(mDbController == null){
            synchronized (DbController.class){
                if(mDbController == null){
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }

    public DbController(Context context){
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        persionInfoDao = mDaoSession.getPersionInfoDao();
    }

    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    private SQLiteDatabase getReadableDatabase(){
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(context, "person.db", null);
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db;
    }

    /***
     * 会自动判断是插入还是替换
     * @param persionInfo
     */
    public void insertOrReplace(PersionInfo persionInfo){
        persionInfoDao.insertOrReplace(persionInfo);
    }


    /***
     * 插入一条记录，表中数据需要与之不同
     * @param persionInfo
     * @return
     */
    public long insert(PersionInfo persionInfo){
        try {
            return persionInfoDao.insert(persionInfo);
        }catch (Exception e){
            return -1;
        }
    }

    /***
     * 更新数据
     * @param persionInfo
     */
    public void updatePersonInfo(PersionInfo persionInfo){
        PersionInfo oldPersionInfo = persionInfoDao.queryBuilder().where(PersionInfoDao.Properties.Id.eq(persionInfo.getId())).build().unique();
        if (oldPersionInfo != null) {
            oldPersionInfo.setName(persionInfo.getName());
            persionInfoDao.update(oldPersionInfo);
        }

    }

    /***
     * 按条件查询唯一数据
     * @param wherecluse
     * @return
     */
    public List<PersionInfo> searchByWhere(String wherecluse){
        return CommonUtils.cast(persionInfoDao.queryBuilder().where(PersionInfoDao.Properties.Name.eq(wherecluse)).build().unique());
    }

    /***
     * 查询所有数据
     * @return
     */
    public List<PersionInfo> searchAll(){
        return persionInfoDao.queryBuilder().list();
    }

    public void delete(String wherecluse){
        persionInfoDao.queryBuilder().where(PersionInfoDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }


}


