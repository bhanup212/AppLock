package bhanu.applock.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bhanu.applock.R;

public class PinActivity extends AppCompatActivity {

    TextView tvZero,tvOne,tvTwo,tvThree,tvfour,tvFive,tvSix,tvSeven,tvEight,tvNine,tvClear;
    String pinCode="";
    ImageView imgOne,imgTwo,imgThree,imgFour;
    SharedPreferences sharedPref;
    String lastApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        sharedPref = getSharedPreferences("bhanupro",0);
        getReferences();
        clickListerners();
        lastApp  = getIntent().getStringExtra("lastApp");
    }
    void verifyPin(String c){
       // Toast.makeText(this,c,Toast.LENGTH_SHORT).show();

        String v = c;
        if (c.equals("c")){
            pinCode = "";
            v = "";
            clearImg();
        }
        if (v.length()==1){
            imgOne.setImageResource(R.drawable.circle2);
        }else if (v.length()==2){
            imgOne.setImageResource(R.drawable.circle2);
            imgTwo.setImageResource(R.drawable.circle2);
        }else if (v.length()==3){
            imgOne.setImageResource(R.drawable.circle2);
            imgTwo.setImageResource(R.drawable.circle2);
            imgThree.setImageResource(R.drawable.circle2);
        }else if (v.length()==4){
            imgOne.setImageResource(R.drawable.circle2);
            imgOne.setImageResource(R.drawable.circle2);
            imgThree.setImageResource(R.drawable.circle2);
            imgFour.setImageResource(R.drawable.circle2);
            if (v.equals("2345")){
              //  this.finish();
                String myapp=sharedPref.getString("lastApp", "none");

                    sharedPref.edit().putString("lastApp", lastApp).apply();
                    this.finish();


            }
        }else if (v.equals("2345")){
           //this.finish();

                sharedPref.edit().putString("lastApp", lastApp).apply();
                this.finish();
        }else {
            v="";
            pinCode = "";
            clearImg();
        }

    }

    private void clickListerners() {
        tvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvZero.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvOne.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode =pinCode + tvTwo.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvThree.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvfour.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvFive.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvSix.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode =pinCode + tvSeven.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode =pinCode + tvEight.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = pinCode + tvNine.getText().toString();
                verifyPin(pinCode);
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinCode = "";
                verifyPin("c");
            }
        });
    }

    public void getReferences(){
        tvZero = findViewById(R.id.pin_zero);
        tvOne = findViewById(R.id.pin_one);
        tvTwo = findViewById(R.id.pin_two);
        tvThree = findViewById(R.id.pin_three);
        tvfour = findViewById(R.id.pin_four);
        tvFive = findViewById(R.id.pin_five);
        tvSix = findViewById(R.id.pin_six);
        tvSeven = findViewById(R.id.pin_seven);
        tvEight = findViewById(R.id.pin_eight);
        tvNine = findViewById(R.id.pin_nine);
        tvClear = findViewById(R.id.pin_remove);

        imgOne = findViewById(R.id.imageview_circle1);
        imgTwo = findViewById(R.id.imageview_circle2);
        imgThree = findViewById(R.id.imageview_circle3);
        imgFour = findViewById(R.id.imageview_circle4);
    }
    public void clearImg(){
        imgOne.setImageResource(R.drawable.circle);
        imgTwo.setImageResource(R.drawable.circle);
        imgThree.setImageResource(R.drawable.circle);
        imgFour.setImageResource(R.drawable.circle);
    }
}
