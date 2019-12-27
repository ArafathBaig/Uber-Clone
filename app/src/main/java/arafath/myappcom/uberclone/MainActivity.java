package arafath.myappcom.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        if(userOneTime.getText().toString().equals("Driver") || userOneTime.getText().toString().equals("Passenger")) {

            if (ParseUser.getCurrentUser() == null) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            FancyToast.makeText(MainActivity.this, "Anonymous User Logged In", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();
                            user.put("as",userOneTime.getText().toString());
                            user.saveInBackground();
                        } else {

                            FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.INFO, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        }
    }

    enum State{
        SINGUP,LOGIN;
    }

    EditText userSign, passSign, userOneTime;
    Button signInbtn, oneTimebtn;
    RadioGroup radioGroup;
    RadioButton driver,passenger;

    private State state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = State.SINGUP;
        userSign = findViewById(R.id.usernameEditText);
        passSign = findViewById(R.id.passwordEditText);
        userOneTime = findViewById(R.id.usernameOneTime);

        signInbtn = findViewById(R.id.signUpsign);
        oneTimebtn = findViewById(R.id.oneTimeSign);

        radioGroup = findViewById(R.id.radioGroup);
        driver = findViewById(R.id.driverRD);
        passenger = findViewById(R.id.passenRD);

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Log.i("Check","Success");
                    }
                }
            });
        }

        oneTimebtn.setOnClickListener(this);

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == State.SINGUP){

                    if( (driver.isChecked() == false && passenger.isChecked() == false) || userSign.getText().toString().equals("") || passSign.getText().toString().equals("") ){
                        FancyToast.makeText(MainActivity.this, "Username, password, driver or passenger required", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(userSign.getText().toString());
                    appUser.setPassword(passSign.getText().toString());

                    if(driver.isChecked()){
                        appUser.put("as","Driver");
                    }else if(passenger.isChecked()){
                        appUser.put("as","Passenger");
                    }

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(MainActivity.this, "Successfully Signed Up", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();

                            }else{

                                FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.INFO, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                }else if(state == State.LOGIN){

                    ParseUser.logInInBackground(userSign.getText().toString(), passSign.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(e == null && user != null){
                                FancyToast.makeText(MainActivity.this, "Successfully Logged In", FancyToast.INFO, Toast.LENGTH_SHORT, true).show();

                            }else{

                                FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.INFO, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logInuser:
                if(state == State.SINGUP){
                    state = State.LOGIN;
                    item.setTitle("Sign Up");
                    signInbtn.setText("Log In");
                }else if(state == State.LOGIN){
                    state = State.SINGUP;
                    item.setTitle("Log In");
                    signInbtn.setText("Sign up");
                }

        }

        return super.onOptionsItemSelected(item);
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
