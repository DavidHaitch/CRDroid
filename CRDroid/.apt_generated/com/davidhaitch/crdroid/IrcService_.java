//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.davidhaitch.crdroid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.googlecode.androidannotations.api.BackgroundExecutor;

public final class IrcService_
    extends IrcService
{


    private void init_() {
    }

    @Override
    public void onCreate() {
        init_();
        super.onCreate();
    }

    public static IrcService_.IntentBuilder_ intent(Context context) {
        return new IrcService_.IntentBuilder_(context);
    }

    @Override
    public void InitIrcWrapper() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    IrcService_.super.InitIrcWrapper();
                } catch (RuntimeException e) {
                    Log.e("IrcService_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, IrcService_.class);
        }

        public Intent get() {
            return intent_;
        }

        public IrcService_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startService(intent_);
        }

    }

}
