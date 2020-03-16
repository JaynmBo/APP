package com.caobo.app.db.manager;

/**
 * Created by caobo
 */
public class UserManager extends BaseModelManager {
    private static UserManager userManager;

    public UserManager() {
    }

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }


}
