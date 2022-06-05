package com.example.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WebFileReader.OnWebFileReadListener {

    private TextView tvDisplay;
    private BoatList boatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        boatList = BoatList.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_display_list) {
            displayList();
            return true;
        } else if (id == R.id.action_load_from_web) {
            promptForWebFile();
            return true;
        } else if (id == R.id.action_add_boat) {
            promptForAssetsFile();
            return true;
        } else if (id == R.id.action_show_details) {
            promptForRegistration();
            return true;
        } else if (id == R.id.action_delete_boat) {
            promptForDeletion();
            return true;
        } else if (id == R.id.action_average_price) {
            showAveragePrice();
            return true;
        } else if (id == R.id.action_diesel_count) {
            showDieselCount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayList() {
        if (boatList.size() == 0) {
            tvDisplay.setText("The boat list is empty. Please load or add boats first.");
            return;
        }

        tvDisplay.setText(boatList.getAllBoatsAsString());
    }

    private void promptForWebFile() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText input = view.findViewById(R.id.etDialogInput);
        TextView message = view.findViewById(R.id.tvDialogMessage);

        message.setText("Enter URL of boat data file:");
        input.setHint("URL");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Load from Web")
                .setView(view)
                .setPositiveButton("Load", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = input.getText().toString().trim();
                        if (!url.isEmpty()) {
                            new WebFileReader(MainActivity.this).execute(url);
                        } else {
                            showSnackbar("URL cannot be empty.");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onWebFileRead(List<String> lines) {
        if (lines != null && !lines.isEmpty()) {
            try {
                boatList.clear();
                processBoatData(lines);
                showSnackbar("Successfully loaded " + boatList.size() + " boats from web file.");
                displayList();
            } catch (Exception e) {
                showSnackbar("Error parsing boat data: " + e.getMessage());
            }
        } else {
            showSnackbar("No data found in the web file.");
        }
    }

    @Override
    public void onWebFileError(Exception e) {
        showSnackbar("Error loading web file: " + e.getMessage());
    }

    private void processBoatData(List<String> lines) {
        int i = 0;
        while (i < lines.size()) {
            if (i + 6 < lines.size()) {
                String make = lines.get(i++);
                String registration = lines.get(i++);
                int year = Integer.parseInt(lines.get(i++));
                int length = Integer.parseInt(lines.get(i++));
                int beam = Integer.parseInt(lines.get(i++));
                String fuelType = lines.get(i++);
                double price = Double.parseDouble(lines.get(i++));

                Boat boat = new Boat(make, registration, year, length, beam, fuelType, price);
                boatList.addBoat(boat);
            } else {
                break;
            }
        }
    }

    private void promptForAssetsFile() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText input = view.findViewById(R.id.etDialogInput);
        TextView message = view.findViewById(R.id.tvDialogMessage);

        message.setText("Enter name of assets file:");
        input.setHint("Filename");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Boat from Assets")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = input.getText().toString().trim();
                        if (!fileName.isEmpty()) {
                            loadBoatFromAssets(fileName);
                        } else {
                            showSnackbar("File name cannot be empty.");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadBoatFromAssets(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String make = reader.readLine();
            String registration = reader.readLine();
            int year = Integer.parseInt(reader.readLine());
            int length = Integer.parseInt(reader.readLine());
            int beam = Integer.parseInt(reader.readLine());
            String fuelType = reader.readLine();
            double price = Double.parseDouble(reader.readLine());

            reader.close();

            Boat boat = new Boat(make, registration, year, length, beam, fuelType, price);
            boatList.addBoat(boat);

            showSnackbar("Successfully added boat: " + make);
            displayList();
        } catch (IOException e) {
            showSnackbar("Error: Could not find or read the assets file. Check the filename.");
        } catch (NumberFormatException e) {
            showSnackbar("Error: Invalid data format in the assets file.");
        } catch (Exception e) {
            showSnackbar("Error: " + e.getMessage());
        }
    }

    private void promptForRegistration() {
        if (boatList.size() == 0) {
            showSnackbar("The boat list is empty. Please load or add boats first.");
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText input = view.findViewById(R.id.etDialogInput);
        TextView message = view.findViewById(R.id.tvDialogMessage);

        message.setText("Enter registration number:");
        input.setHint("Registration #");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Show Boat Details")
                .setView(view)
                .setPositiveButton("Show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regNum = input.getText().toString().trim();
                        if (!regNum.isEmpty()) {
                            showBoatDetails(regNum);
                        } else {
                            showSnackbar("Registration number cannot be empty.");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showBoatDetails(String registrationNumber) {
        Boat boat = boatList.getBoatByRegistration(registrationNumber);
        if (boat != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Make: ").append(boat.getMake()).append("\n");
            sb.append("Year: ").append(boat.getYear()).append("\n");
            sb.append("Length: ").append(boat.getLength()).append(" ft\n");
            sb.append("Beam: ").append(boat.getFormattedBeam()).append("\n");
            sb.append("Fuel: ").append(boat.getFuelType()).append("\n");
            sb.append("Price: $").append(String.format(Locale.US, "%,.2f", boat.getPrice())).append("\n");

            // Age (if 0, display "brand new")
            if (boat.getAge() == 0) {
                sb.append("Age: brand new\n");
            } else {
                sb.append("Age: ").append(boat.getAge()).append(" years\n");
            }

            // Luxury tax (if not $0)
            double luxuryTax = boat.getLuxuryTax();
            if (luxuryTax > 0) {
                sb.append("Luxury Tax: $").append(String.format(Locale.US, "%,.2f", luxuryTax));
            }

            tvDisplay.setText(sb.toString());
        } else {
            showSnackbar("No boat found with registration number: " + registrationNumber);
        }
    }

    private void promptForDeletion() {
        if (boatList.size() == 0) {
            showSnackbar("The boat list is empty. Please load or add boats first.");
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText input = view.findViewById(R.id.etDialogInput);
        TextView message = view.findViewById(R.id.tvDialogMessage);

        message.setText("Enter registration number of boat to delete:");
        input.setHint("Registration #");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Boat")
                .setView(view)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regNum = input.getText().toString().trim();
                        if (!regNum.isEmpty()) {
                            deleteBoat(regNum);
                        } else {
                            showSnackbar("Registration number cannot be empty.");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteBoat(String registrationNumber) {
        boolean success = boatList.removeBoatByRegistration(registrationNumber);
        if (success) {
            showSnackbar("Boat with registration number " + registrationNumber + " was deleted.");
            displayList();
        } else {
            showSnackbar("No boat found with registration number: " + registrationNumber);
        }
    }

    private void showAveragePrice() {
        if (boatList.size() == 0) {
            showSnackbar("The boat list is empty. Please load or add boats first.");
            return;
        }

        double avgPrice = boatList.getAveragePrice();
        tvDisplay.setText("Average price of all boats: $" + String.format(Locale.US, "%,.2f", avgPrice));
    }

    private void showDieselCount() {
        if (boatList.size() == 0) {
            showSnackbar("The boat list is empty. Please load or add boats first.");
            return;
        }

        int count = boatList.getDieselBoatCount();
        tvDisplay.setText("Number of diesel boats: " + count);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}