package com.dev.dominapp.examplessacmakers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int toasts = 0;
    boolean aTurn = true;
    Monster a,b;
    int currentImage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.image_view_layout);
        resetMonsterFight();
    }

    //handle changing views
    public void goToMonsterView(View v){setContentView(R.layout.monster_madness);}
    public void goToQuadratic(View v){setContentView(R.layout.quadratic);}
    public void goToRocket(View v){setContentView(R.layout.rocket);}
    public void backToMain(View v){
        setContentView(R.layout.activity_main);
    }
    public void goToImageView(View v){setContentView(R.layout.image_view_layout);}
    //toast example
    public void toastExample(View v){
        toasts++;
        if (toasts > 1) tToast("You have clicked the button " + toasts + " times.");
        else tToast("You have clicked the button once.");
    }

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }
    public void resetMonsterFight(){
        a = new Monster();
        b = new Monster();
        a.name = "Aardvark the aweful";
        b.name = "Bilbo of Bagginsses";

    }
    public void resetMonsterFight(View v){
        resetMonsterFight();
    }
    public void nextAction(View v){

        TextView textView = (TextView) findViewById(R.id.monsterTextView);
        if(a.head.isMissing || b.head.isMissing || a.torso.isMissing ||b.torso.isMissing){
            textView.setText("A monster is victorious the other is quite dead.");
        }
        else {
            if (aTurn) {
                a.act(b);
                aTurn = false;
                textView.setText(a.currentMessage);
            } else {
                b.act(a);
                aTurn = true;
                textView.setText(b.currentMessage);
            }
        }
    }
    //quadratic example
    public void calculateQuadratic(View v){
        EditText ta = (EditText) findViewById(R.id.t_a);
        EditText tb = (EditText) findViewById(R.id.t_b);
        EditText tc = (EditText) findViewById(R.id.t_c);
        boolean validInputs = false;
        float a=0,b=0,c=0;

        try {
            a = Float.parseFloat(ta.getText().toString());
            b = Float.parseFloat(tb.getText().toString());
            c = Float.parseFloat(tc.getText().toString());
            validInputs = true;
        }
        catch (Exception e){
            // invalid inputs stays false.
        }
        if (validInputs){
            TextView answer = (TextView) findViewById(R.id.t_answer);
            float u = b*b-4*a*c;
            float xm,xp;
            if (u<0)answer.setText("Answer does not exist.");
            else{
                u = (float) Math.pow((double)(u),.5);
                xm = (-b-u)/(2*a);
                xp = (-b+u)/(2*a);
                if (xm != xp) answer.setText("X = " + xm + " and " + xp);
                else answer.setText("X = " + xm);
            }


        }
    }
    //thrusters example
    public void calculateRocket(View v) {
        EditText t_thrusters = (EditText) findViewById(R.id.t_thrusters);
        EditText t_fuel = (EditText) findViewById(R.id.t_fuel);
        EditText t_chassis = (EditText) findViewById(R.id.t_chassis);
        TextView outcome = (TextView) findViewById(R.id.t_outcome);
        TextView chassisSafetyMargin = (TextView) findViewById(R.id.t_safetyMargin);
        TextView maxVelocity = (TextView) findViewById(R.id.t_maxVelocity);
        double thrusters=0, fuel=0, chassis=0, mass = 0, m_thrusters = 10000, m_fuel = 1, rocketThrust = 363200,
        distanceToCenterOfEarth = 6371000, massOfEarth = 5.972 * Math.pow(10,24);
        boolean validInputs = false;
        outcome.setText("");
        chassisSafetyMargin.setText("");
        maxVelocity.setText("");
        try {
            thrusters = Double.parseDouble(t_thrusters.getText().toString());
            fuel = Double.parseDouble(t_fuel.getText().toString());
            chassis = Double.parseDouble(t_chassis.getText().toString());
            validInputs = true;
        } catch (Exception e) {
            // invalid inputs stays false.
        }
        if (validInputs){

            double rocketVelocity = 0;
            double rocketAltitude = 0;
            boolean failedLiftoff = false;
            mass = m_thrusters*thrusters + m_fuel * fuel + chassis;
            if (mass*.5>chassis)outcome.setText("Chassis is insufficient.");
            else{

                chassisSafetyMargin.setText("Safety Margin = " + (int)(((chassis-mass*.5)/(mass*.5))*10000)/100+"%");
                if ((rocketThrust*thrusters)/mass-calculateForceDueToGravity(mass,massOfEarth,(distanceToCenterOfEarth+rocketAltitude))/mass<0)
                failedLiftoff = true;

                while(fuel>0 && !failedLiftoff){
                    fuel -= thrusters*1.6;
                    rocketVelocity -= calculateForceDueToGravity(mass,massOfEarth,(distanceToCenterOfEarth+rocketAltitude))/mass;
                    rocketVelocity += (rocketThrust*thrusters)/mass;
                    rocketAltitude += rocketVelocity;
                }
                maxVelocity.setText("Max Velocity: "+(int)rocketVelocity + "m/sec");

                while(rocketVelocity>=0 && rocketAltitude < 9*1.6*Math.pow(10,9)){
                    rocketVelocity -= calculateForceDueToGravity(mass,massOfEarth,(distanceToCenterOfEarth+rocketAltitude))/mass;
                    rocketAltitude+=rocketVelocity;
                }
                if (rocketVelocity <0){
                    outcome.setText("Insufficient thrust to exit solar system.");
                }
                else outcome.setText("Successful launch out of solar system. Final Velocity = " + (int)rocketVelocity + "m/sec" );
            }
        }
    }
    private double calculateForceDueToGravity(double mass1, double mass2, double distance){
        double ret = 0;
        double G = 6.67*Math.pow(10,-11);
        ret = (G*mass1*mass2/(distance*distance));
        double x = ret/mass1;
        return ret;
    }
    //image view
    public void moveImageLeft(View v){
        if (currentImage > 1) currentImage--;
        else tToast("No more images that way.");
        assignPNGtoImageView();
    }
    public void moveImageRight(View v){
        if (currentImage <3) currentImage++;
        else tToast("No more images that way.");
        assignPNGtoImageView();
    }
    private void assignPNGtoImageView(){
        ImageView img= (ImageView) findViewById(R.id.image_view_numbers);

        if(currentImage == 1){img.setImageResource(R.drawable.one);}
        if(currentImage == 2){img.setImageResource(R.drawable.two);}
        if(currentImage == 3){img.setImageResource(R.drawable.three);}
    }

}
