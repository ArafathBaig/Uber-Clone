package arafath.myappcom.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

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
}
