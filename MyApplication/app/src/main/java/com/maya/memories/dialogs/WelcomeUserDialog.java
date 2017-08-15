package com.maya.memories.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.memories.R;
import com.maya.memories.constants.Constants;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

/**
 * Created by home on 8/2/2017.
 */

public class WelcomeUserDialog extends Dialog
{
    Context context;
    SharedPreferences PREFS;
    public WelcomeUserDialog(@NonNull Context context, SharedPreferences PREFS) {
        super(context);
        this.context=context;
        this.PREFS=PREFS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_user_dialog);
        RelativeLayout view=(RelativeLayout)findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeUserDialog.this.dismiss();
            }
        });

        ImageView imgPerson=(ImageView)findViewById(R.id.imgPerson);

        TextView tvPersonName=(TextView)findViewById(R.id.tvPersonName);

        tvPersonName.setText(Utility.getCamelCase("Welcome! "+PREFS.getString(Constants.FIRST_NAME,"")));


        Picasso
                .with(context)
                .load(PREFS.getString(Constants.USER_PHOTO_URL,""))
                .fit()
                .into((ImageView) imgPerson);

    }
}
