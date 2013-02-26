package com.rena.HotelApp1;

import java.util.List;

import com.DBHandlers.DatabaseHandler;
import com.DBHandlers.DeviceConfig;
import com.DBHandlers.UsersInfo;
import com.DBHandlers.UsersInfoDBHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UserScreen extends Activity implements OnClickListener,
		OnTouchListener {
	/** Called when the activity is first created. */
	Button tvLogin;
	ImageButton ibLogin;
	RelativeLayout rlytUserViewPager;
	TableLayout userTbImglyt;
	// menu item numbers.

	int MENU_OPTIONS = 0;
	int MENU_QUIT = 1;
	int skin = 1;
	final int LOGIN_DIALOG_ID = 1;
	final int HELP_DIALOG_ID = 2;

	/**
	 * Creates the menu items
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_OPTIONS, 0, "Options");
		menu.add(0, MENU_QUIT, 0, "Sign In");
		return true;
	}

	/**
	 * Handles item selections in the options menu.
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0)// user wishes to see the other options
		// available in the game
		{
			// starts options menu.
			options_menu();
		}

		else // user wishes to quit the game
		{
			showDialog(LOGIN_DIALOG_ID);
		}
		return true;
	}

	/**
	 * Creates an Alert Dialog for Options.
	 * */
	public void options_menu() {
		final CharSequence[] options_items = { "Change Skin", "Help" };

		AlertDialog.Builder options_builder = new AlertDialog.Builder(this);
		options_builder.setTitle("Options");
		options_builder.setItems(options_items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (options_items[item] == "Change Skin") {
							select_skin();
						} else if (options_items[item] == "Help") {
							showDialog(HELP_DIALOG_ID);
						}
						return;
					}
				});
		options_builder.show();
	}

	/**
	 * Creates an Alert Dialog for selecting the Skin for the game.
	 * */
	// Medium Purple1=#9E7BFF,Violet=#8D38C9,Steel Blue=#4863A0,Sky
	// Blue3=#659EC7,Medium Orchid1=#D462FF
	public void select_skin() {
		final CharSequence[] skin_items = { "Medium Purple", "Violet",
				"Steel Blue", "Sky Blue", "Medium Orchid" };

		AlertDialog.Builder skin_builder = new AlertDialog.Builder(this);
		skin_builder.setItems(skin_items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(getApplicationContext(),
								"Skin changed to " + skin_items[item],
								Toast.LENGTH_SHORT).show();

						if (skin_items[item] == "Medium Purple") {
							skin = 0;
							change_skin();
						} else if (skin_items[item] == "Violet") {
							skin = 1;
							change_skin();
						} else if (skin_items[item] == "Steel Blue") {
							skin = 2;
							change_skin();
						} else if (skin_items[item] == "Sky Blue") {
							skin = 3;
							change_skin();
						} else if (skin_items[item] == "Medium Orchid") {
							skin = 4;
							change_skin();
						}
					}
				});
		skin_builder.show();
	}

	/**
	 * Sets the skin for the game and starts a new game with the New Skin.
	 * */
	public void change_skin() {
		int color = color = R.color.MediumPurple1;
		if (skin == 0) {
			color = R.color.MediumPurple1;
		} else if (skin == 1) {
			color = R.color.Violet;
		} else if (skin == 2) {
			color = R.color.SteelBlue;
		} else if (skin == 3) {
			color = R.color.SkyBlue3;
		} else if (skin == 4) {
			color = R.color.MediumOrchid1;
		}
		rlytUserViewPager.setBackgroundColor(color);
	}

	private void ToastMyMsg(String Msg) {
		Toast t = new Toast(UserScreen.this);
		LinearLayout ll = new LinearLayout(UserScreen.this);
		ll.setBackgroundResource(R.drawable.border_round_corners);
		ll.setPadding(20, 20, 20, 20);
		TextView tv = new TextView(UserScreen.this);
		tv.setText(Msg);
		tv.setTextColor(Color.parseColor("#0040FF"));
		tv.setTextSize(20);
		tv.setPadding(10, 3, 0, 0);
		ll.addView(tv);
		t.setView(ll);
		t.show();

	}

	private void ToastMsg(int currLayout, int toastLayout) {
		// Retrieve the layout inflator
		LayoutInflater inflater = getLayoutInflater();
		// Assign the custom layout to view
		// Parameter 1 - Custom layout XML
		// Parameter 2 - Custom layout ID present in linearlayout tag of XML

		View layout = inflater.inflate(currLayout,
				(ViewGroup) findViewById(toastLayout));
		/*
		 * TextView tv=(TextView)findViewById(R.id.trWarning); tv.setText(Msg);
		 */
		// Return the application context
		Toast toast = new Toast(getApplicationContext());
		// Set toast gravity to bottom
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		// Set toast duration
		toast.setDuration(Toast.LENGTH_LONG);
		// Set the custom layout to Toast
		toast.setView(layout);
		// Display toast
		toast.show();

	}

	/**
	 * Dialog interface for entering the name.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog mdialog = new Dialog(this);
		GetImagesInfo();
		switch (id) {
		case LOGIN_DIALOG_ID:

			final UsersInfoDBHandler uDBHandlr = new UsersInfoDBHandler(this);

			mdialog.setContentView(R.layout.user_login);
			mdialog.setTitle("Login");
			mdialog.setCancelable(true);

			final EditText namep1 = (EditText) mdialog
					.findViewById(R.id.namep1);
			final EditText namep2 = (EditText) mdialog
					.findViewById(R.id.namep2);

			Button ok_b = (Button) mdialog.findViewById(R.id.ok);
			Button cancel_b = (Button) mdialog.findViewById(R.id.cancel);
			ok_b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String UserName = namep1.getText().toString().trim();
					String Password = namep2.getText().toString().trim();

					if (UserName == null || UserName.equals("")
							|| Password == null || Password.equals("")) {
						Toast.makeText(UserScreen.this,
								"Plese Enter User Name and Password",
								Toast.LENGTH_LONG).show();
					} else {
						/*
						 * UsersInfo loggingInUserInfo = new UsersInfo();
						 * loggingInUserInfo = uDBHandlr
						 * .getUsersInfoByUN(UserName);
						 */

						/*
						 * if (loggingInUserInfo != null &&
						 * Password.equals(loggingInUserInfo.getPWD())) {
						 * 
						 * 
						 * }
						 */if (namep1.getText().toString().equals("admin")
								&& namep2.getText().toString().equals("admin")) {

							ToastMsg(R.layout.toast_success_welcome,
									R.id.toastSuccessLLayout1);
							Intent adminScreen = new Intent(UserScreen.this,
									HotelApp1Activity.class);
							adminScreen.putExtra("LoggedInUN", namep1.getText()
									.toString());
							startActivity(adminScreen);
							namep1.setText("");
							namep2.setText("");

							namep1.setHint("Username");
							namep2.setHint("Password");
							mdialog.hide();
						} else {
							Toast
									.makeText(
											UserScreen.this,
											"Please enter correct username and password",
											Toast.LENGTH_LONG).show();
						}
					}

				}
			});
			cancel_b.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					mdialog.hide();
				}
			});
			break;
		case HELP_DIALOG_ID:
			mdialog.setContentView(R.layout.help);
			mdialog.setTitle("Help");
			mdialog.setCancelable(true);
			break;
		default:
			mdialog.dismiss();
		}
		return mdialog;
	}

	private void updateImagesWithNewConfig() {
		hideAllImages();
		// CheckImgMapped();
		showMappedImages();
	}

	private void BuildChildsToTableLayout() {

	}

	private void showMappedImages() {
		CONFIGEDDATA[] ARRCONFIGEDDATA;
		final DatabaseHandler db = new DatabaseHandler(this);
		int currentRowAdding = 0;
		int CurrentChildToRowAdding = 0;
		try {
			int ConfierdDevicesCount = db.getConfigeredDevicesCount();
			ARRCONFIGEDDATA = new CONFIGEDDATA[ConfierdDevicesCount];
			final List<DeviceConfig> configs = db.getAllDeviceConfigs();

			int count = 0;
			TableLayout tb = (TableLayout) findViewById(R.id.userTbImglyt);
			TableRow tr = new TableRow(this);

			for (final DeviceConfig cn : configs) {
				if (cn.getIsConfigured() == 2) {
					final int imgId = getResources().getIdentifier(
							cn.getImgBtnId(), "id", getPackageName());
					ImageButton img = (ImageButton) findViewById(imgId);
					/*
					 * if (CurrentChildToRowAdding <= 4) { tr.addView(img);
					 * CurrentChildToRowAdding++; } else { tb.addView(tr); tr =
					 * new TableRow(this); CurrentChildToRowAdding = 0;
					 * currentRowAdding++; }
					 */
					// tr.addView(img);
					img.setVisibility(View.VISIBLE);
					
					img.setBackgroundColor(Color.parseColor("#2EFEF7"));
					/* tb.getChildAt(0).setVisibility(View.VISIBLE);
					ImageButton imb = (ImageButton) tr1
							.getChildAt(CurrentChildToRowAdding);
					//img.setVisibility(View.VISIBLE);
					img.setBackgroundColor(Color.parseColor("#2EFEF7"));
					CurrentChildToRowAdding++;*/
					img.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							ToastMyMsg("This image Contains the code : "
									+ cn.getImgBtnId());
						}
					});
					CONFIGEDDATA.IMGBTNID = cn.getImgBtnId();
					CONFIGEDDATA.IMGBTNIDINR = imgId;

					CONFIGEDDATA.DEVICEID = cn.getDeviceId();
					CONFIGEDDATA.HEXACODE = cn.getHexaCode();
					CONFIGEDDATA.ISCONFIGURED = cn.getIsConfigured();
				}

			}

			// CheckImgMapped();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
	}

	private void CheckImgMapped() {
		userTbImglyt = (TableLayout) findViewById(R.id.userTbImglyt);
		int totalimages = 0;

		if (userTbImglyt != null && userTbImglyt.getChildCount() > 0) {

			TableRow tr = null;
			for (int i = 0; i < userTbImglyt.getChildCount(); i++) {
				if (userTbImglyt != null
						&& userTbImglyt.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) userTbImglyt.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						if (tr.getChildAt(j).getClass().getName().equals(
								"android.widget.ImageButton")) {
							totalimages++;
						}
					}
				}
			}
		}
	}

	private void hideAllImages() {
		userTbImglyt = (TableLayout) findViewById(R.id.userTbImglyt);
		/* Toast.makeText(this, "GetImagesInfo", Toast.LENGTH_LONG).show(); */
		int totalimages = 0;

		if (userTbImglyt != null && userTbImglyt.getChildCount() > 0) {

			TableRow tr = null;
			for (int i = 0; i < userTbImglyt.getChildCount(); i++) {
				if (userTbImglyt != null
						&& userTbImglyt.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) userTbImglyt.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						if (tr.getChildAt(j).getClass().getName().equals(
								"android.widget.ImageButton")) {
							totalimages++;
						}
					}
				}
			}
		}
		if (userTbImglyt != null && userTbImglyt.getChildCount() > 0) {
			TableRow tr = null;
			for (int i = 0; i < userTbImglyt.getChildCount(); i++) {
				if (userTbImglyt.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) userTbImglyt.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						tr.getChildAt(j).setVisibility(View.GONE);

					}
					tr = null;
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_screen);
		rlytUserViewPager = (RelativeLayout) findViewById(R.id.rlytUserViewPager);
		/***** Icons view pager - Start *****/
		IconsViewPager objIVP = new IconsViewPager();
		ViewPager ObjIconPager = (ViewPager) findViewById(R.id.UserIconsPager);
		if (ObjIconPager == null) {
			Toast.makeText(this, "Please Rotate the screen once",
					Toast.LENGTH_LONG).show();
		} else {
			ObjIconPager.getTop();
			ObjIconPager.setAdapter(objIVP);
			ObjIconPager.setCurrentItem(0);
			runOnUiThread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					userTbImglyt = (TableLayout) findViewById(R.id.userTbImglyt);
				}
			});
			Button btnSettings = (Button) findViewById(R.id.btnUserSettings1);
			btnSettings.setVisibility(View.GONE);
			btnSettings.post(new Runnable() {
				public void run() {
					updateImagesWithNewConfig();
				}
			});
			ObjIconPager.setOnPageChangeListener(new OnPageChangeListener() {
				public void onPageSelected(int arg0) {

				}

				public void onPageScrolled(int arg0, float arg1, int arg2) { // TODO
					// Auto-generated method stub
				}

				public void onPageScrollStateChanged(int arg0) { // TODO
					// Auto-generated method stub
				}
			});
		}

	}

	private class IconsViewPager extends PagerAdapter {

		@Override
		public Object instantiateItem(View collection, int position) {
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.user_screen1;
				break;
			/*
			 * case 1: resId = R.layout.user_screen2;
			 * 
			 * break;
			 */

			}
			View view = inflater.inflate(resId, null);
			((ViewPager) collection).addView(view, 0);
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		public float getPageWidth(int position) {
			return (0.5f);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void GetImagesInfo() {
		TableLayout ImagesLayout = (TableLayout) findViewById(R.id.userTbImglyt);
		int totalimages = 0;
		ImageButton ib = new ImageButton(this);
		if (ImagesLayout.getChildCount() > 0) {
			TableRow tr = null;
			for (int i = 0; i < ImagesLayout.getChildCount(); i++) {
				if (ImagesLayout != null
						&& ImagesLayout.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) ImagesLayout.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						if (tr.getChildAt(i) != null
								&& tr.getChildAt(i).getClass().getName() == "android.widget.ImageButton") {
							totalimages++;
							ib = (ImageButton) findViewById(tr.getChildAt(j)
									.getId());
							ib.setOnClickListener(this);
						}
					}
				}
			}
		}
	}
}
