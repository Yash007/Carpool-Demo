package harsh.keshwala.com.carpool;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DriverLoginActivity extends AppCompatActivity {

    private TextView driverSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        driverSignUp = (TextView) findViewById(R.id.driverSignUpText);
        driverSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLoginActivity.this, DriverSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
