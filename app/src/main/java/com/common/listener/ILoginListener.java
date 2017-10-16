package com.common.listener;

/**
 * Created by billy on 2017/10/16.
 */

public abstract class ILoginListener {
    abstract public void loginSucc();
    public void loginFailure(){};
}
