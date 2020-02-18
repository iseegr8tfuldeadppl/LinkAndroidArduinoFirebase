package get.bamboozled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView onlineoffline;
    private DatabaseReference mainRef;
    private DatabaseReference mostrecentrequestRef;
    private ImageView bulb;
    private ImageView her;
    private ImageView him;
    private Animation fadeout;
    private Animation fadein;
    private Animation fadeout2;
    private Animation fadein2;
    private Animation slideinfromleft;
    private Animation slideinfromright;
    private FrameLayout darkbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mainRef = database.getReference("controlpanel");
        mostrecentrequestRef = database.getReference("controlpanel").child("appside").child("mostrecentrequest");
        //myRef.keepSynced(true);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeout2 = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadein2 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        slideinfromright = AnimationUtils.loadAnimation(this, R.anim.slideinfromright);
        slideinfromleft = AnimationUtils.loadAnimation(this, R.anim.slideinfromleft);
        bulb = findViewById(R.id.bulb);
        darkbackground = findViewById(R.id.darkbackground);
        onlineoffline = findViewById(R.id.onlineoffline);
        him = findViewById(R.id.him);
        her = findViewById(R.id.her);

        try {
            Glide.with(this).load(R.drawable.onbulb).into(bulb);
        } catch (Exception ignored) {
            bulb.setImageDrawable(getResources().getDrawable(R.drawable.onbulb));
        }
        try {
            Glide.with(this).load(R.drawable.him).into(him);
        } catch (Exception ignored) {
            him.setImageDrawable(getResources().getDrawable(R.drawable.him));
        }
        try {
            Glide.with(this).load(R.drawable.her).into(her);
        } catch (Exception ignored) {
            her.setImageDrawable(getResources().getDrawable(R.drawable.her));
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        onlineoffline.setTypeface(font);
        bulb.setClickable(false);
        bulb.setFocusable(false);

        firebaseListener();
        bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                him.setVisibility(View.INVISIBLE);
                her.setVisibility(View.INVISIBLE);
                noinvisible = false;
                bulb.setClickable(false);
                bulb.setFocusable(false);
                bulb.startAnimation(fadeout);
                fadeout.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                    if(!noinvisible)bulb.setVisibility(View.INVISIBLE);
                }});
                clicked = true;
            }
        });




        slideminn();
    }

    private void slideminn() {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                long futuretime = System.currentTimeMillis() + 1500;

                while (System.currentTimeMillis() < futuretime){
                    synchronized (this){
                        try{
                            wait(futuretime - System.currentTimeMillis());
                        } catch( Exception ignored){}
                    }
                }

                slidemin.sendEmptyMessage(0);
            }
        };
        Thread getMythread3 = new Thread(r);
        getMythread3.start();
    }

    private Handler slidemin = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(her.getVisibility()!=View.VISIBLE) {
                her.startAnimation(slideinfromleft);
                him.startAnimation(slideinfromright);
                slideinfromleft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        her.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                slideinfromright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        him.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
            return true;
        }
    });



    private boolean noinvisible = false;
    private String lastseen;
    private String state;
    private boolean buttonstate = false;
    private String mostrecentrequest;
    private boolean onetime = true;
    private String[] temp2, temp3, temp4;
    private boolean online = false;
    private void firebaseListener(){
        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    mostrecentrequest = String.valueOf(dataSnapshot.child("appside").child("mostrecentrequest").getValue());
                    lastseen = String.valueOf(dataSnapshot.child("computerside").child("lastseen").getValue());
                    state = String.valueOf(dataSnapshot.child("computerside").child("state").getValue());
                    temp2 = lastseen.split(" ")[0].split("/");
                    temp3 = lastseen.split(" ")[1].split(":");
                    temp4 = String.valueOf(new Date()).split(" ");
                    if(!Integer.valueOf(temp2[0]).equals(Integer.valueOf(temp4[2]))){
                        online = false;
                    } else if(!Integer.valueOf(temp4[3].split(":")[0]).equals(Integer.valueOf(temp3[0]))){
                        online = false;
                    } else online = Integer.valueOf(temp4[3].split(":")[1]) - Integer.valueOf(temp3[1]) <= 1;

                    if(online){
                        onlineoffline.setText("Online");
                        onlineoffline.setTextColor(getResources().getColor(R.color.green));
                        if(buttonstate)
                            onlineoffline.setBackgroundColor(getResources().getColor(R.color.lightmodebck));
                        else
                            onlineoffline.setBackground(null);
                    } else{
                        onlineoffline.setText("Offline");
                        onlineoffline.setTextColor(getResources().getColor(R.color.red));
                        if(buttonstate)
                            onlineoffline.setBackgroundColor(getResources().getColor(R.color.lightmodebck));
                        else
                            onlineoffline.setBackground(null);
                    }

                    if(onetime){
                        if(state.equals("on")){
                            buttonstate = false;
                            setbuttonOn();
                        } else if(state.equals("off")){
                            buttonstate = true;
                            setbuttonOff();
                        }
                    } else if (state.equals(mostrecentrequest)) {
                        if (state.equals("on"))
                            setbuttonOn();
                        else if (state.equals("off"))
                            setbuttonOff();
                        else {
                            if(buttonstate)
                                setbuttonOff();
                            else
                                setbuttonOn();
                        }
                    }

                    if (clicked) {
                        clicked = false;
                        if (mostrecentrequest.equals("on"))
                            mostrecentrequestRef.setValue("off");
                        else if (mostrecentrequest.equals("off"))
                            mostrecentrequestRef.setValue("on");
                    }
                } catch(Exception ignored){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                print("loading data failed");
            }
        });
    }

    private void setbuttonOff() {
        if(buttonstate)
            rotator();
        buttonstate = false;
    }

    private void setbuttonOn() {
        if(!buttonstate)
            rotator();
        buttonstate = true;
    }

    private void rotator() {

        if(onetime){
            onetime = false;
            if(buttonstate)
                darkbackground.setVisibility(View.VISIBLE);
            else
                darkbackground.setVisibility(View.INVISIBLE);
        } else slideminn();

        if(!buttonstate) {
            try {
                Glide.with(this).load(R.drawable.him).into(him);
            } catch (Exception ignored) {
                him.setImageDrawable(getResources().getDrawable(R.drawable.him));
            }
            try {
                Glide.with(this).load(R.drawable.her).into(her);
            } catch (Exception ignored) {
                her.setImageDrawable(getResources().getDrawable(R.drawable.her));
            }
        } else {
            try {
                Glide.with(this).load(R.drawable.himlight).into(him);
            } catch (Exception ignored) {
                him.setImageDrawable(getResources().getDrawable(R.drawable.himlight));
            }
            try {
                Glide.with(this).load(R.drawable.herlight).into(her);
            } catch (Exception ignored) {
                her.setImageDrawable(getResources().getDrawable(R.drawable.herlight));
            }

        }

        noinvisible = true;
        bulb.startAnimation(fadein);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
            bulb.setVisibility(View.VISIBLE);
            bulb.setClickable(true);
            bulb.setFocusable(true);
        }});
        if(buttonstate){
            darkbackground.startAnimation(fadein2);
            fadein2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                darkbackground.setVisibility(View.VISIBLE);
            }});
        } else {
            darkbackground.startAnimation(fadeout2);
            fadeout2.setAnimationListener(new Animation.AnimationListener() {@Override public void onAnimationStart(Animation animation) { }@Override public void onAnimationRepeat(Animation animation) {}@Override public void onAnimationEnd(Animation animation) {
                darkbackground.setVisibility(View.INVISIBLE);
            }});
        }

        if(!buttonstate)
            bulb.setImageDrawable(getDrawable(R.drawable.onbulb));
        else
            bulb.setImageDrawable(getDrawable(R.drawable.offbulb));
    }

    private void print(Object log){
        Toast.makeText(getApplicationContext(), String.valueOf(log), Toast.LENGTH_SHORT).show();
    }

    private boolean clicked = false;
    public void changestateClicked(View view) {
        clicked = true;
    }
}
