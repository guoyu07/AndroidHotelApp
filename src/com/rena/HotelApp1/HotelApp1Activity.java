package com.rena.HotelApp1;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.DBHandlers.DatabaseHandler;
import com.DBHandlers.DeviceConfig;

public class HotelApp1Activity extends Activity implements OnTouchListener {

	/** Called when the activity is first created. */
	int windowwidth;
	int windowheight;
	TextView lblFirstLeft, lblFirstMiddle, lblFirstRight, lblSecondLeft,
			lblSecondMiddle, lblSecondRight;
	TextView seperateTextView;
	TEMPLATEIMGPOSINFO TImgPosInfo[];
	TEMPLATEIMGPOSINFO DraggingCtrlPosInfo[];
	TEMPLATEIMGPOSINFO[] DraggingLblInfo = new TEMPLATEIMGPOSINFO[1];
	private android.widget.RelativeLayout.LayoutParams layoutParams;
	static int imagarrayIndexOnArrayCreate = 0;
	boolean isFirstTouch = true;
	int PagerTopPos;
	int PagerLeftPos;
	RelativeLayout rl;
	int MENU_OPTIONS = 0;
	final int MENU_CHNGPWD = 1;
	final int MENU_RESETMAP = 20;
	int MENU_QUIT = 3;
	int skin = 1;

	final int HELP_DIALOG_ID = 2;
	RelativeLayout topMostRelLayout;
	// used in UpdateDraggedCtrlToNewPos(int DraggedCtrlId, final int
	// DroppedCtrlId) method
	Boolean isRecNeedToUpdate = Boolean.FALSE;
	ProgressDialog progressDialog;
	Button btnSaveMapping;

	/**
	 * Creates the menu items
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_OPTIONS, 0, "Options");
		menu.add(0, MENU_CHNGPWD, 0, "Change Password");
		menu.add(0, MENU_RESETMAP, 0, "Reset Mapping");
		menu.add(0, MENU_QUIT, 0, "Sign Out");
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
		} else if (item.getItemId() == 1) {
			showDialog(MENU_CHNGPWD);
		} else if (item.getItemId() == 20) {
			showDialog(MENU_RESETMAP);
		} else // user wishes to quit the game
		{
			Intent adminScreen = new Intent(HotelApp1Activity.this,
					UserScreen.class);
			startActivity(adminScreen);
			finish();
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
		topMostRelLayout.setBackgroundColor(color);
	}

	/**
	 * Dialog interface for entering the name.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		final Dialog mdialog = new Dialog(this);
		switch (id) {
		case MENU_CHNGPWD:
			mdialog.setContentView(R.layout.user_chngpwd);
			mdialog.setTitle("Change Password");
			mdialog.setCancelable(true);

			final EditText CurrPwd = (EditText) mdialog
					.findViewById(R.id.CurrPwd);
			final EditText NewPwd = (EditText) mdialog
					.findViewById(R.id.NewPwd);
			final EditText ConfNewPwd = (EditText) mdialog
					.findViewById(R.id.ConfirmNewPwd);
			Button ok_b = (Button) mdialog.findViewById(R.id.ok);
			Button cancel_b = (Button) mdialog.findViewById(R.id.cancel);
			ok_b.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CurrPwd.setText("");
					NewPwd.setText("");
					ConfNewPwd.setText("");
					CurrPwd.setHint("Current Password");
					NewPwd.setHint("New Password");
					ConfNewPwd.setHint("Confirm New Password");
					mdialog.hide();
				}
			});
			cancel_b.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					mdialog.hide();
				}
			});
			break;
		case MENU_RESETMAP:

			mdialog.setContentView(R.layout.confirm);
			mdialog.setTitle("Are you sure to reset the mapping");
			mdialog.setCancelable(true);
			Button ok_b1 = (Button) mdialog.findViewById(R.id.ok1);
			Button cancel_b1 = (Button) mdialog.findViewById(R.id.cancel1);

			ok_b1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final DatabaseHandler db = new DatabaseHandler(
							HotelApp1Activity.this);
					db.DeleteAll();
					// InsertDataIntoTable();

					GetImagesInfo();
					progressDialog = ProgressDialog.show(
							HotelApp1Activity.this, "Please Wait.....",
							"Receiving Data from WIFI.....");
					new Thread() {
						public void run() {
							try {
								mdialog.hide();
								sleep(10000);

								ToastMsg(R.layout.toast_resetmap,
										R.id.toastResetMap);
								finish();
								mdialog.hide();
							} catch (Exception e) {
								Log.e("tag", e.getMessage());
							}
							// dismiss the progress dialog
							progressDialog.dismiss();
							Intent i = new Intent(HotelApp1Activity.this,
									HotelApp1Activity.class);
							startActivity(i);
							db.close();
						}
					}.start();
				}
			});
			cancel_b1.setOnClickListener(new OnClickListener() {
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

	public void InsertDataIntoTable() {
		/*********** Insert the required data into the table **********/
		DatabaseHandler db = new DatabaseHandler(this);

		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView1")), "FAN", "OX1234", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView2")), "AC", "OX1235", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView3")), "Light", "OX1236", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView4")), "Comp", "OX1239", 0));

		db
				.addDeviceConfig(new DeviceConfig(String
						.valueOf(GetIntId("seperateTextView5")), "Fridge",
						"OX1234", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView6")), "Lamp", "OX1235", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView7")), "CDPlayer", "OX1236",
				0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView8")), "TV", "OX1234", 0));
		db.addDeviceConfig(new DeviceConfig(String
				.valueOf(GetIntId("seperateTextView9")), "WashingMachine",
				"OX1235", 0));
		db.close();
		/*********** Insert the required data into the table *********/
	}

	private int GetIntId(String id) {
		return getResources().getIdentifier(id, "id", getPackageName());
	}

	@Override
	public void onBackPressed() {
		ToastMsg(R.layout.toast_warning_plssignout, R.id.toastwarningLLayout);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/***************************************************/
		progressDialog = new ProgressDialog(HotelApp1Activity.this);
		progressDialog.setTitle("Please Wait.....");
		progressDialog.setMessage("Receiving Data from WIFI.....");
		// new MyTask().execute();
		progressDialog = ProgressDialog.show(HotelApp1Activity.this,
				"Please Wait.....", "Receiving Data from WIFI.....");
		new Thread() {
			public void run() {
				try {
					sleep(10000);

				} catch (Exception e) {
					Log.e("tag", e.getMessage());
				}
				// dismiss the progress dialog
				progressDialog.dismiss();
			}
		}.start();
		/**************************************************/

		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		windowheight = getWindowManager().getDefaultDisplay().getHeight();
		topMostRelLayout = (RelativeLayout) findViewById(R.id.topMostRelLayout);

		/***** Icons view pager - Start *****/
		IconsViewPager objIVP = new IconsViewPager();
		ViewPager ObjIconPager = (ViewPager) findViewById(R.id.IconsPager);
		ObjIconPager.getTop();
		ObjIconPager.setAdapter(objIVP);
		ObjIconPager.setCurrentItem(1);
		rl = (RelativeLayout) findViewById(R.id.rlytTxtViews);
		PagerTopPos = ObjIconPager.getTop();
		PagerLeftPos = ObjIconPager.getLeft();
		ObjIconPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				if (isFirstTouch) {
					GetImagesInfo();
					isFirstTouch = false;
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) { // TODO
				// Auto-generated method stub
			}

			public void onPageScrollStateChanged(int arg0) { // TODO
				// Auto-generated method stub
			}
		});
		RelativeLayout rlytTxtViews = (RelativeLayout) findViewById(R.id.rlytTxtViews);
		for (int i = 0; i < rlytTxtViews.getChildCount(); i++) {

			if (rlytTxtViews.getChildAt(i) instanceof TextView) {
				seperateTextView = (TextView) findViewById(rlytTxtViews
						.getChildAt(i).getId());
				seperateTextView.setOnTouchListener(this);
			}
		}
		btnSaveMapping = (Button) findViewById(R.id.btnSaveMapping);
		btnSaveMapping.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(HotelApp1Activity.this, "Save Clicked",
				// Toast.LENGTH_LONG).show();
				ToastMyMsg("Mapping has saved successfully.");
				SaveMappedImages();
			}
		});
	}
	private void ToastMyMsg(String Msg) {
		Toast t = new Toast(HotelApp1Activity.this);
		LinearLayout ll = new LinearLayout(HotelApp1Activity.this);
		ll.setBackgroundResource(R.drawable.border_round_corners);
		ll.setPadding(20, 20, 20, 20);
		TextView tv = new TextView(HotelApp1Activity.this);
		tv.setText(Msg);
		tv.setTextColor(Color.parseColor("#0040FF"));
		tv.setTextSize(20);
		tv.setPadding(10, 3, 0, 0);
		ll.addView(tv);
		t.setView(ll);
		t.show();

	}
	private class IconsViewPager extends PagerAdapter {

		@Override
		public Object instantiateItem(View collection, int position) {
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.secondmiddle;
				isFirstTouch = true;
				break;
			case 1:
				resId = R.layout.secondright;
				isFirstTouch = true;
				break;

			}
			View view = inflater.inflate(resId, null);
			((ViewPager) collection).addView(view, 0);
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
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

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isFirstTouch) {
				GetImagesInfo();
				isFirstTouch = false;
			}
			OnDragStart(v);
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();
			System.out.println("value of x" + x_cord);
			System.out.println("value of y" + y_cord);
			if (x_cord > windowwidth) {
				x_cord = windowwidth;
			}
			if (y_cord > windowheight) {
				y_cord = windowheight;
			}
			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 25;
			v.setLayoutParams(layoutParams);
			break;
		case MotionEvent.ACTION_CANCEL:

			break;
		case MotionEvent.ACTION_UP:
			OnDropComplete(v);

			break;
		default:
			break;
		}
		return true;
	}

	public void GetImagesInfo() {

		TableLayout ImagesLayout = (TableLayout) findViewById(R.id.tbImglyt);
		/* Toast.makeText(this, "GetImagesInfo", Toast.LENGTH_LONG).show(); */
		int totalimages = 0;

		if (ImagesLayout.getChildCount() > 0) {

			TImgPosInfo = new TEMPLATEIMGPOSINFO[ImagesLayout.getChildCount()];
			TableRow tr = null;
			for (int i = 0; i < ImagesLayout.getChildCount(); i++) {
				if (ImagesLayout != null
						&& ImagesLayout.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) ImagesLayout.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						if (tr.getChildAt(i).getClass().getName().equals(
								"android.widget.ImageButton")) {
							totalimages++;
						}
					}
				}
			}
			imagarrayIndexOnArrayCreate = 0;
		}
		if (ImagesLayout.getChildCount() > 0) {

			TImgPosInfo = new TEMPLATEIMGPOSINFO[totalimages];
			TableRow tr = null;
			for (int i = 0; i < ImagesLayout.getChildCount(); i++) {
				if (ImagesLayout.getChildAt(i) instanceof TableRow) {
					tr = (TableRow) ImagesLayout.getChildAt(i);
					for (int j = 0; j < tr.getChildCount(); j++) {
						setImagesInfoArray(tr.getChildAt(j));
					}
					tr = null;
				}
			}
		}
	}

	public void setImagesInfoArray(View v) {
		TImgPosInfo[imagarrayIndexOnArrayCreate] = new TEMPLATEIMGPOSINFO();
		if (v.getClass().getName().equals("android.widget.ImageButton")) {
			ImageButton childView = ((ImageButton) v);
			View parentView = (View) v.getParent();
			TImgPosInfo[imagarrayIndexOnArrayCreate].IMGID = childView.getId();
			TImgPosInfo[imagarrayIndexOnArrayCreate].LEFT = childView.getLeft()
					- parentView.getLeft();
			TImgPosInfo[imagarrayIndexOnArrayCreate].TOP = childView.getTop()
					+ parentView.getTop();
			TImgPosInfo[imagarrayIndexOnArrayCreate].RIGHT = childView
					.getRight()
					+ parentView.getRight() - 200;
			TImgPosInfo[imagarrayIndexOnArrayCreate].BOTTOM = childView
					.getBottom()
					+ parentView.getBottom() - 30;
			imagarrayIndexOnArrayCreate++;
			childView = null;
		}
	}

	public void OnDragStart(View DraggingCtrl) {
		DraggingLblInfo[0] = new TEMPLATEIMGPOSINFO();
		DraggingLblInfo[0].IMGID = DraggingCtrl.getId();
		DraggingLblInfo[0].TOP = DraggingCtrl.getTop();
		DraggingLblInfo[0].BOTTOM = DraggingCtrl.getBottom();
		DraggingLblInfo[0].LEFT = DraggingCtrl.getLeft();
		DraggingLblInfo[0].RIGHT = DraggingCtrl.getRight();
	}

	private void OnDropComplete(View DraggedCtrl) {
		Rect rectImg, rectLbl;
		int left = 0, top = 0, right = 0, bottom = 0, ImgId = 0;
		boolean isDraggedOnRect = false;
		if (DraggedCtrl.getId() == DraggingLblInfo[0].IMGID) {
			for (int tempImgsInfo = 0; tempImgsInfo < TImgPosInfo.length; tempImgsInfo++) {
				ImgId = TImgPosInfo[tempImgsInfo].IMGID;
				left = TImgPosInfo[tempImgsInfo].LEFT;
				top = TImgPosInfo[tempImgsInfo].TOP;
				right = TImgPosInfo[tempImgsInfo].RIGHT;
				bottom = TImgPosInfo[tempImgsInfo].BOTTOM;
				rectImg = new Rect(left, top, right - 100, bottom);
				rectLbl = new Rect(DraggedCtrl.getLeft(), DraggedCtrl.getTop(),
						DraggedCtrl.getRight(), DraggedCtrl.getBottom());
				if (rectLbl.contains(rectImg) || rectImg.contains(rectLbl)) {
					ImageButton ib = (ImageButton) findViewById(ImgId);
					// DraggedCtrl.setVisibility(View.GONE);
					/*
					 * ib.setBackgroundDrawable(getResources().getDrawable(
					 * R.drawable.border));
					 */
					UpdateDraggedCtrlToNewPos(DraggedCtrl.getId(),
							TImgPosInfo[tempImgsInfo].IMGID);
					break;
				}
			}
			if (!isDraggedOnRect) {
				UpdateDraggedCtrlToPrePos();
			}
		}
	}

	private void ToastMsg(int currLayout, int toastLayout) {
		// Retrieve the layout inflator
		LayoutInflater inflater = getLayoutInflater();
		// Assign the custom layout to view
		View layout = inflater.inflate(currLayout,
				(ViewGroup) findViewById(toastLayout));
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

	private void UpdateDraggedCtrlToPrePos() {

		TextView tv = (TextView) findViewById(DraggingLblInfo[0].IMGID);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlytTxtViews);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tv
				.getLayoutParams());
		params.leftMargin = DraggingLblInfo[0].LEFT;
		params.topMargin = DraggingLblInfo[0].TOP;
		params.bottomMargin = DraggingLblInfo[0].BOTTOM;
		params.rightMargin = DraggingLblInfo[0].RIGHT;
		tv.setLayoutParams(params);
	}

	private void UpdateDraggedCtrlToNewPos(int DraggedCtrlId,
			final int DroppedCtrlId) {
		final TextView tvDragged = (TextView) findViewById(DraggedCtrlId);
		final ImageButton imgBtnDroppd = (ImageButton) findViewById(DroppedCtrlId);
		final DatabaseHandler db = new DatabaseHandler(this);
		DeviceConfig df = new DeviceConfig();
		List<DeviceConfig> configs = db.getAllDeviceConfigs();
		for (DeviceConfig cn : configs) {
			if (Integer.parseInt(cn.getImgBtnId()) == DroppedCtrlId) {
				df = cn;
				break;
			}
			if (Integer.parseInt(cn.getImgBtnId()) == DraggedCtrlId) {

				df = cn;
				break;
			}
		}
		/*
		 * Toast.makeText(this, "is configed aldeady : " + df.getIsConfigured(),
		 * Toast.LENGTH_LONG).show();
		 */
		if (df.getIsConfigured() == 1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Device Already Configured").setMessage(
					"Are you sure to overwrite the configuration?").setIcon(
					android.R.drawable.ic_dialog_alert).setPositiveButton(
					"Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Yes button clicked, do something
							isRecNeedToUpdate = Boolean.TRUE;
						}
					}).setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Yes button clicked, do something
							UpdateDraggedCtrlToPrePos();
							isRecNeedToUpdate = Boolean.FALSE;
						}
					}) // Do nothing on no
					.show();

		} else {
			isRecNeedToUpdate = Boolean.TRUE;
		}
		if (isRecNeedToUpdate) {
			db.deleteDeviceConfig(df);
			df.setImgBtnId(String.valueOf(DroppedCtrlId));
			df.setHexaCode(tvDragged.getText().toString());
			df.setDeviceId(tvDragged.getText().toString());
			df.setIsConfigured(1);
			db.insertDeviceConfig(df);
			Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(50); // You can manage the time of the blink with
			// this parameter
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
			anim.setRepeatCount(3);
			tvDragged.startAnimation(anim);
			tvDragged.setVisibility(View.GONE);
			tvDragged.setPadding(0, 0, 0, 0);

			imgBtnDroppd.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.border));
			db.close();
		}
	}
	private void SaveMappedImages() {
		final DatabaseHandler db = new DatabaseHandler(this);
		try {
			final List<DeviceConfig> configs = db.getAllDeviceConfigs();
			DeviceConfig tempConfig = new DeviceConfig();
			for (final DeviceConfig cn : configs) {
				if (cn.getIsConfigured() == 1) {
					tempConfig = new DeviceConfig();
					tempConfig = cn;
					db.deleteDeviceConfig(cn);
					cn.setImgBtnId(tempConfig.getImgBtnId());
					cn.setHexaCode(tempConfig.getHexaCode());
					cn.setDeviceId(tempConfig.getDeviceId());
					cn.setIsConfigured(2);
					db.insertDeviceConfig(cn);
					tempConfig = null;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
	}

	public class MyTask extends AsyncTask<Void, Void, Void> {

		public void onPreExecute(Void un) {
			super.onPreExecute();
			progressDialog.show();
		}

		public void onPostExecute(Void unused) {
			new Thread() {
				public void run() {
					try {
						sleep(10000);

					} catch (Exception e) {
						Log.e("tag", e.getMessage());
					}
					// dismiss the progress dialog
					progressDialog.dismiss();
				}
			}.start();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}