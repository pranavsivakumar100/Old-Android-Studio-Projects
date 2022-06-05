package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessActivity extends AppCompatActivity {
    private String serverDomain;
    private int serverPort;

    private RadioGroup operationRadioGroup;
    private RadioButton alphagRadioButton;
    private RadioButton longestRadioButton;
    private Spinner paragraphFileSpinner;
    private Button processButton;
    private TextView resultTextView;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);


        serverDomain = getIntent().getStringExtra("SERVER_DOMAIN");
        serverPort = getIntent().getIntExtra("SERVER_PORT", 0);

        operationRadioGroup = findViewById(R.id.operationRadioGroup);
        alphagRadioButton = findViewById(R.id.alphagRadioButton);
        longestRadioButton = findViewById(R.id.longestRadioButton);
        paragraphFileSpinner = findViewById(R.id.paragraphFileSpinner);
        processButton = findViewById(R.id.processButton);
        resultTextView = findViewById(R.id.resultTextView);


        loadParagraphFiles();

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected operation
                String operation;
                int selectedRadioButtonId = operationRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId == alphagRadioButton.getId()) {
                    operation = "alphag";
                } else if (selectedRadioButtonId == longestRadioButton.getId()) {
                    operation = "longest";
                } else {
                    Toast.makeText(ProcessActivity.this, "Please select an operation", Toast.LENGTH_SHORT).show();
                    return;
                }

                String paragraphFile = paragraphFileSpinner.getSelectedItem().toString();

                processParagraph(operation, paragraphFile);
            }
        });
    }

    private void loadParagraphFiles() {
        try {
            AssetManager assetManager = getAssets();
            String[] files = assetManager.list("");

            List<String> paragraphFiles = new ArrayList<>();
            for (String file : files) {
                if (file.endsWith(".txt")) {
                    paragraphFiles.add(file);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, paragraphFiles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            paragraphFileSpinner.setAdapter(adapter);

        } catch (IOException e) {
            Toast.makeText(this, "Error loading paragraph files: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void processParagraph(final String operation, final String paragraphFile) {
        processButton.setEnabled(false);
        resultTextView.setText("Processing...");

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> paragraph = readParagraphFromAssets(paragraphFile);

                    Socket socket = new Socket(serverDomain, serverPort);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    Scanner in = new Scanner(socket.getInputStream());

                    out.println(operation);

                    out.println(paragraph.size());
                    for (String line : paragraph) {
                        out.println(line);
                    }

                    int numResultLines = in.nextInt();
                    in.nextLine();

                    final StringBuilder resultBuilder = new StringBuilder();
                    for (int i = 0; i < numResultLines; i++) {
                        resultBuilder.append(in.nextLine()).append("\n");
                    }

                    socket.close();

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText(resultBuilder.toString().trim());
                            processButton.setEnabled(true);
                        }
                    });

                } catch (final Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText("Error: " + e.getMessage());
                            processButton.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    private List<String> readParagraphFromAssets(String fileName) throws IOException {
        List<String> paragraph = new LinkedList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            paragraph.add(line);
        }
        reader.close();

        return paragraph;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}