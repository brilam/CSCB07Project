package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.data.Itinerary;
import cs.b07.cscb07project.backend.databases.ItineraryDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class prompts the user to search for itinerary.
 */
public class ItinerarySearchPrompt extends AppCompatActivity implements OnItemSelectedListener {

  /**
   * The type of search to perform.
   */
  private String searchType;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_itinerary_search_prompt);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Spinner searchSpinner = (Spinner) findViewById(R.id.search_options_spinner);

    ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.itinerary_search_options,
        android.R.layout.simple_spinner_dropdown_item);

    searchSpinner.setAdapter(adapter);
    searchSpinner.setOnItemSelectedListener(this);
  }

  /**
   * Queries for itineraries matching the given origin, destination, and departure date provided
   * that the given information are all valid inputs.
   * 
   * @param view the view that was clicked
   */
  public void searchItineraries(View view) {
    EditText originText = (EditText) findViewById(R.id.origin_field);
    EditText destinationText = (EditText) findViewById(R.id.destination_field);
    EditText dateText = (EditText) findViewById(R.id.date_field);
    DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_FORMAT);

    Intent oldIntent = getIntent();
    String loginId = (String) oldIntent.getSerializableExtra("loginID");

    View focusView = originText;
    originText.setError(null);
    destinationText.setError(null);
    dateText.setError(null);

    ArrayList<Itinerary> searchResults = new ArrayList<Itinerary>();

    String originInput = originText.getText().toString();
    String destinationInput = destinationText.getText().toString();
    String dateInput = dateText.getText().toString();

    // Checks empty origin, destination, date
    if (FieldChecks.isEmpty(originInput)) {
      originText.setError(getString(R.string.empty_field));
      focusView = originText;
    } else if (FieldChecks.isEmpty(destinationInput)) {
      destinationText.setError(getString(R.string.empty_field));
      focusView = destinationText;
    } else if (FieldChecks.isEmpty(dateInput)) {
      dateText.setError(getString(R.string.empty_field));
      focusView = dateText;
    } else if (!(FieldChecks.isValidDate(dateInput))) {
      dateText.setError(getString(R.string.invalid_date));
      focusView = dateText;
    } else {

      String correctDateInput = "";
      try {
        correctDateInput = df.format(df.parse(dateInput));
      } catch (ParseException e) {
        dateText.setError(getString(R.string.invalid_date));
        focusView = dateText;
      }

      // Check which method of search to perform
      if (searchType.equals(getString(R.string.unsorted))) {
        searchResults =
            ItineraryDatabase.getItineraries(correctDateInput, originInput, destinationInput);

      } else if (searchType.equals(getString(R.string.sort_by_time))) {
        searchResults = ItineraryDatabase.getItinerariesSortedByTime(correctDateInput, originInput,
            destinationInput);

      } else if (searchType.equals(getString(R.string.sort_by_cost))) {
        searchResults = ItineraryDatabase.getItinerariesSortedByCost(correctDateInput, originInput,
            destinationInput);
      }
      // Get results to search and send through to the next
      // next activity
      Intent intent = new Intent(this, ItinerarySearchResults.class);
      intent.putExtra("results", searchResults);
      intent.putExtra("loginID", loginId);
      startActivity(intent);
    }
    focusView.requestFocus();
  }



  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    searchType = parent.getItemAtPosition(position).toString();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {}
}
