package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText serverDomainEditText;
    private EditText serverPortEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverDomainEditText = findViewById(R.id.serverDomainEditText);
        serverPortEditText = findViewById(R.id.serverPortEditText);
        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverDomain = serverDomainEditText.getText().toString().trim();
                String serverPortStr = serverPortEditText.getText().toString().trim();

                if (serverDomain.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter server domain", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (serverPortStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter server port", Toast.LENGTH_SHORT).show();
                    return;
                }

                int serverPort;
                try {
                    serverPort = Integer.parseInt(serverPortStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid port number", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
                intent.putExtra("SERVER_DOMAIN", serverDomain);
                intent.putExtra("SERVER_PORT", serverPort);
                startActivity(intent);
            }
        });
    }
}