package com.example.currencyconverter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MainActivity extends ActionBarActivity {

	CharSequence selectedCurrency = "";
	ArrayAdapter<CharSequence> siglas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeSpinner();

		EditText text = (EditText) this
				.findViewById(R.id.txtValorASerConvertido);
		text.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((EditText) v).setText("", BufferType.EDITABLE);
				} else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					((EditText) v).setText("Valor", BufferType.EDITABLE);
				}
				return false;
			}
		});

		Button button = (Button) this.findViewById(R.id.btnConverter);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				converter();
			}

		});

	}

	private void initializeSpinner() {
		Spinner spinner = (Spinner) this.findViewById(R.id.cmpMoedas);
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.nomesMoedas,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// siglas
		siglas = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.siglasMoedas, android.R.layout.simple_spinner_item);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long id) {

				selectedCurrency = siglas.getItem(pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void converter() {
		HttpURLConnection connection;
		OutputStreamWriter request;
		TextView text = (TextView) this.findViewById(R.id.txtValorConvertido);
		EditText valor = (EditText) this
				.findViewById(R.id.txtValorASerConvertido);
		if (selectedCurrency.equals("")) {
			AlertDialog.Builder alertDialog = null;
			alertDialog = new AlertDialog.Builder(MainActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Blank value field!");
			alertDialog.setCancelable(true);
			alertDialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.create();
			alertDialog.show();
		} else {
			HttpClient Client = new DefaultHttpClient();
			String url = "http://rate-exchange.herokuapp.com/fetchRate?from="+selectedCurrency+"&to=BRL";
			try {
				String response = "";

				HttpGet httpget = new HttpGet(url);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = Client.execute(httpget, responseHandler);

				JSONObject jsonHandler = new JSONObject(response);
				DecimalFormat df = new DecimalFormat("#.##");

				float rate = Float.valueOf((String) jsonHandler.get("Rate"));
				float valorFinal = Float.valueOf((String) valor.getText()
						.toString()) * rate;
				text.setText("R$" + df.format(valorFinal));

			} catch (IOException ex) {
				AlertDialog.Builder alertDialog = null;
				alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Network error!");
				alertDialog.setCancelable(true);
				alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) { // dismiss the dialog
							}
						});
				alertDialog.create();
				alertDialog.show();
			} catch (JSONException ex) {
				AlertDialog.Builder alertDialog = null;
				alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Network error!");
				alertDialog.setCancelable(true);
				alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) { // dismiss the dialog
							}
						});
				alertDialog.create();
				alertDialog.show();
			} catch (NumberFormatException e) {
				AlertDialog.Builder alertDialog = null;
				alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Type only numbers in the value field!");
				alertDialog.setCancelable(true);
				alertDialog.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) { // dismiss the dialog
							}
						});
				alertDialog.create();
				alertDialog.show();
				valor.setText("");
			}

		}
	}
}
