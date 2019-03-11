package com.wd.tech.greendao;

import com.wd.tech.WDApp;

public class DaoUtils {
    private ConversationDao conversationDao;

    private static DaoUtils instance;

    private DaoUtils(){
        DaoSession session = DaoMaster.newDevSession(WDApp.getContext(), ConversationDao.TABLENAME);
        conversationDao = session.getConversationDao();
    }

    public static DaoUtils getInstance() {
        if (instance==null){
            return new DaoUtils();
        }
        return instance;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }
}
