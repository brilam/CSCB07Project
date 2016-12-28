package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cs.b07.cscb07project.R;

/**
 * A class that displays the flight search results found to match the inputted values in the prompt.
 */
public class FlightSearchResults extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flight_search_results);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ListView flights = (ListView) findViewById(R.id.flight_list);
    Intent oldIntent = getIntent();
    // Displays all searched flights in a list view
    String[] flightInfo = (String[]) oldIntent.getSerializableExtra("results");
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, flightInfo);
    flights.setAdapter(adapter);
  }


}
