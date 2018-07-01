package harsh.keshwala.com.carpool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RiderLoginActivity extends AppCompatActivity {

    private TextView riderSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        riderSignUp = (TextView) findViewById(R.id.riderSignUpText);
        riderSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RiderLoginActivity.this, RiderSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
