package com.example.poem;

import android.text.ClipboardManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.widget.TabHost;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	mySqlHelper dbHelper;
	ArrayList<String> arr_cars = new ArrayList<String>();

	protected Cursor cursor;
	protected ListAdapter adapter;
	protected ListView numberList, recordList;
	protected String str = "";
	protected TextView tvPlease;
	private int id = -1;
	TabHost tabs;

	final CharSequence[] items = { "Edit", "Delete", "Berbagi", "Copy" };

	ListView userList;
	UserCustomAdapter userAdapter;
	ArrayList<User> userArray = new ArrayList<User>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TabHost tabs = (TabHost)findViewById(R.id.tab2);
		tabs = (TabHost) findViewById(R.id.tabhost); // @android:id/tabhost
		tabs.setup();

		TabHost.TabSpec spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Pencarian",
				getResources().getDrawable(R.drawable.icon_photos_tab));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Membuat sajak",
				getResources().getDrawable(R.drawable.green));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Konsep");
		tabs.addTab(spec);

		dbHelper = new mySqlHelper(this);
		tvPlease = (TextView) findViewById(R.id.tv_date);
		this.numberList = (ListView) this.findViewById(R.id.listView1);
		try {
			dbHelper.createDataBase();
		} catch (Exception ioe) {
			Log.e("err", "Unable to create database");
		}
		this.recordList = (ListView) this.findViewById(R.id.listRecord);
		view();

		final EditText et = (EditText) findViewById(R.id.editText1);
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				str = et.getText().toString();
				System.out.println(str);
				if (str.isEmpty()) {
					try {
						// numberList.setVisibility(View.GONE);
						et.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.icon_search_5, 0);
						tvPlease.setText(R.string.empty_list);

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					SQLiteDatabase db = dbHelper.getReadableDatabase();
					int countAdapter = 0;
					et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

					try {
						// numberList.setVisibility(View.VISIBLE);
						cursor = db.rawQuery(
								"SELECT  * FROM data WHERE number LIKE '%"
										+ str + "'", null);
						adapter = new SimpleCursorAdapter(MainActivity.this,
								R.layout.view2, cursor,
								new String[] { "number" },
								new int[] { R.id.number });
						countAdapter = adapter.getCount();
						System.out.println("cek 1 countAdapter: "
								+ countAdapter);
						numberList.setAdapter(adapter);
						numberList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								// TODO Auto-generated method stub
								String pos = cursor.getString(cursor.getColumnIndex("number"));
								android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE); 
								clipboard.setText(pos); 
								Toast.makeText(getApplicationContext(), "Kata '" + pos + "' dicopy." , Toast.LENGTH_SHORT).show();
							}
						});
						if (countAdapter > 0)
							tvPlease.setText("");
						else
							tvPlease.setText(R.string.not_found);
					} catch (Exception e) {
						Log.e("error", e.toString());
					}
				}
			}
		});
		// view();
	}

	@SuppressWarnings("deprecation")
	private void view() {

		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			String arrStr[] = { "poem", "date" };
			// ///////
			cursor = db.rawQuery("SELECT * FROM record", null);
			adapter = new SimpleCursorAdapter(MainActivity.this,
					R.layout.view_record, cursor, arrStr,//
					new int[] { R.id.poem, R.id.date });
			System.out.println("--> check adapter... " + adapter);
			

			recordList.setAdapter(adapter); //

			recordList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					System.out.println("Check position: " + position);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setTitle("Apa yang Anda inginkan?");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								@SuppressLint("ResourceType")
								public void onClick(DialogInterface dialog,
													int item) {
									if(item == 0){
										System.out.println("--> check pilih " + item);
										System.out.println("--> check date " + date());
										Toast.makeText(MainActivity.this,
												"Edited", Toast.LENGTH_SHORT)
												.show();
										String strPoem = cursor.getString(cursor.getColumnIndex("poem"));
										EditText et_Text = (EditText) findViewById(R.id.et_put);
										et_Text.setText(strPoem);										
										tabs.setCurrentTab(1);										
									} else if (item == 1) {
									    String strDate = cursor.getString(cursor.getColumnIndex("date"));
									    System.out.println("--> check date: " + strDate);									    
										System.out.println("--> check pilih " + item);
										try {
											SQLiteDatabase db1 = dbHelper.getWritableDatabase();
											db.delete("record", "date='"+strDate +"'", null);
											System.out.println("--> check berhasil delete.");										
										} catch (Exception e) {								    
											System.out.println("--> check GAGAL delete." + e);		
										}
										Toast.makeText(MainActivity.this,
												items[item], Toast.LENGTH_SHORT)
												.show();
									} else if(item == 2) {

									    String strPoem = cursor.getString(cursor.getColumnIndex("poem"));
									    
										if (strPoem.length() > 0) {
											System.out.println(strPoem.length());
											Intent sharingIntent = new Intent(
													android.content.Intent.ACTION_SEND);
											sharingIntent.setType("text/plain");
											String shareBody = strPoem;
											// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
											// "Lala Poem:"); // Jadi judul
											sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
													shareBody);
											startActivity(Intent.createChooser(sharingIntent,
													(CharSequence) findViewById(R.string.share_via)));
										} else {
											Toast toast = Toast.makeText(getBaseContext(),
													(CharSequence) findViewById(R.string.empty_text), 2000);
											toast.setGravity(Gravity.CENTER, 0, 0);
											toast.show();
										}										
									} else {
										try {
											String strPoem = cursor.getString(cursor.getColumnIndex("poem"));
//											ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//											ClipData clip = ClipData.newPlainText("label","Your Text");
//											clipboard.setPrimaryClip(clip);
											android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE); 
											clipboard.setText(strPoem); 
											Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
										} catch (Exception e) {
											// TODO: handle exception
										}
									}
									dialog.dismiss();

									view();
								}
							}).show();

				}
			});
		} catch (Exception e) {
			System.err.print(e);
		}

	}

	public void bt_emptyClick(View v) {
		EditText et_Text = (EditText) findViewById(R.id.editText1);
		try {
			if (et_Text.getTextSize() > 0) {
				// AlertDialog ad =
				et_Text.setText("");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void bt_exit(View v) {
		System.exit(0);
	}

	/*
	 * public void bt_save(View v) { EditText et_Text = (EditText)
	 * findViewById(R.id.editText1); try { if (et_Text.getTextSize() > 0) {
	 * String text = String.valueOf(et_Text.getText()); SQLiteDatabase db =
	 * dbHelper.getWritableDatabase(); // try { //
	 * db.execSQL("INSERT INTO record VALUES(null,'" + text + "', null, 1)"); //
	 * } catch (Exception e) { // System.err.println(e); // } } } catch
	 * (Exception e) { // TODO: handle exception } }
	 */

	private void addData(String num) {// , String name) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String dt = date();

		System.out.println("Date " + dt);
		try {
			db.execSQL("insert into record values(null,'" + num + "','" + dt
					+ "', null)");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String date() {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		/* Do "DateFormat" using "simple" format. */
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		String inputX = df.format(now.getTime());
		System.out.println(inputX);

		DateFormat outputformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = null;
		String output = null;
		try {
			// Converting the input String to Date
			date = df.parse(inputX);
			// Changing the format of date and storing it in String
			output = outputformat.format(date);
			// Displaying the date
			System.out.println(output);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return output;
	}

	public void bt_save(View v) {
		EditText ed_num = (EditText) findViewById(R.id.et_put);
		addData(ed_num.getText().toString());// , ed_name.getText().toString());
		System.out.println("Check 1111");
		view();
		Toast.makeText(getApplicationContext(), "Tersimpan di Tab Konsep!", Toast.LENGTH_SHORT).show();
	}

	@SuppressLint({"ShowToast", "ResourceType"})
	public void bt_share(View v) {
		EditText et_Text = (EditText) findViewById(R.id.et_put);
		try {
			if (et_Text.getText().length() > 0) {
				System.out.println(et_Text.getText().length());
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = String.valueOf(et_Text.getText());
				// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				// "Lala Poem:"); // Jadi judul
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				startActivity(Intent.createChooser(sharingIntent,
						(CharSequence) findViewById(R.string.share_via)));
			} else {
				Toast toast = Toast.makeText(getBaseContext(),
						(CharSequence) findViewById(R.string.empty_text), 2000);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressLint("ShowToast")
	public void bt_clear(View v) {
		EditText et_Text = (EditText) findViewById(R.id.et_put);
		try {
			if (et_Text.getText().length() > 0) {
				et_Text.setText("");
			} else {
				Toast toast = Toast.makeText(getBaseContext(),
						(CharSequence) findViewById(R.string.empty_text), 2000);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// copy-paste-select all
	public void click_et_put(View v) {
		EditText et_put = (EditText) findViewById(R.id.et_put);
		ClipboardManager cm = (ClipboardManager) getBaseContext()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(et_put.getText());
		Toast.makeText(getBaseContext(), "Copied to clipboard",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);//
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(MainActivity.this, AboutUs.class);
		startActivity(i);
		return super.onOptionsItemSelected(item);
	}
}
