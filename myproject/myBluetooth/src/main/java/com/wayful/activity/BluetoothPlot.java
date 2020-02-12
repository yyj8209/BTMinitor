/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wayful.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wayful.Bluetooth.BluetoothChatService;
import com.wayful.Bluetooth.Data_syn;
import com.wayful.Bluetooth.DeviceListActivity;
import com.wayful.Bluetooth.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示通信信息的主Activity。
 */
public class BluetoothPlot extends Activity {
	// Debugging
	private static final String TAG = "BluetoothPlot";
	private static final boolean D = true;
	private static final float A = 5000.0f/32768;
    //返回页面标志
	private boolean exit =false;

	// 来自BluetoothChatService Handler的消息类型
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// 来自BluetoothChatService Handler的关键名
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	// Intent请求代码
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// 曲线颜色：绿色，紫色，红色
	public static final int[] LINE_COLORS = {
			Color.rgb(140, 210, 118),
			Color.rgb(159, 143, 186),
			Color.rgb(233, 197, 23)
	};
//    public static final int BACKGROUND_GIRD_COLOR = Color.rgb(50, 50, 50);
//    public static final int BACKGROUND_GIRD_COLOR1 = Color.rgb(200, 200, 10);

    private Button search;
	private Button disc;
	private Button settings;
	LineChart lineChart;
	ArrayList<Entry> values;
	LineDataSet set1,set2,set3;

	private TextView mTitle;

	// 声明复选按钮
	private CheckBox in16;
	private CheckBox autosend;
	private CheckBox out16;

	// 用来保存存储的文件名
	public String filename = "";
	// 保存用数据缓存
	private String fmsg = "";

	// 已连接设备的名称
	private String mConnectedDeviceName = null;
	// 输出流缓冲区
//	private StringBuffer mOutStringBuffer;

	// 本地蓝牙适配器
	private BluetoothAdapter mBluetoothAdapter = null;
	// 用于通信的服务
	private BluetoothChatService mChatService = null;
	// CheckBox用

	@Override
 	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");
		// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_bluetooth_plot_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		//布局控件初始化函数，注册相关监听器
		initUI();
		// 获取本地蓝牙适配器
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 如果没有蓝牙适配器，则不支持
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		search.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
			}
		} );
		disc.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				ensureDiscoverable();
			}
		} );
		settings.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				settings();
			}
		} );

		initData();
		initChart();
//		setData(values);
//		lineChart.invalidate();
	}

	public void settings(){
		Intent settingsIntent = new Intent( BluetoothPlot.this,BluetoothChat.class );
		startActivity( settingsIntent );
	}

	private void initUI(){
		// 通过findViewById获得CheckBox对象
		in16 = (CheckBox) findViewById(R.id.in16);
		autosend = (CheckBox) findViewById(R.id.autosend);
		out16 = (CheckBox) findViewById(R.id.out16);

		// 获得button的对象
		search = (Button) findViewById( R.id.search );
		disc = (Button) findViewById( R.id.discoverable1 );
		settings = (Button) findViewById( R.id.settings );
		//获取选择控件的值
		// 设置custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.plot_activity_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);
	}

	private void initData()
	{
	    set1 = new LineDataSet(values, "通道 1 数据");
        set2 = new LineDataSet(values, "通道 2 数据");
        set3 = new LineDataSet(values, "通道 3 数据");
		values = new ArrayList<Entry>();
//		values.add(new Entry(1, 0));
//		values.add(new Entry(2, 0));
//		values.add(new Entry(3, 0));
//		values.add(new Entry(4, 0));
//		values.add(new Entry(5, 0));
//		values.add(new Entry(6, 0));
//		values.add(new Entry(7, 0));
//		values.add(new Entry(8, 0));
//		values.add(new Entry(9, 0));
	}

	private void initChart(){
		lineChart = (LineChart) findViewById(R.id.line_chart);
		YAxis rightAxis = lineChart.getAxisRight();
		rightAxis.setEnabled(false);//设置图表右边的y轴禁用
		XAxis xAxis = lineChart.getXAxis();
		xAxis.setPosition( XAxis.XAxisPosition.BOTTOM );

		lineChart.setDrawGridBackground(true);  //背景绘制
//        lineChart.setBackgroundColor( BACKGROUND_COLOR);
        lineChart.setGridBackgroundColor( Color.BLACK );
        lineChart.setBorderColor( Color.YELLOW );
		lineChart.getDescription().setEnabled(false);//设置描述文本
//		lineChart.setOnTouchListener( new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				return false;
//			}
//		} );
//		lineChart.setOnChartGestureListener( new OnChartGestureListener() {
//			@Override
//			public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//			}
//
//			@Override
//			public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//
//			}
//
//			@Override
//			public void onChartLongPressed(MotionEvent me) {
//
//			}
//
//			@Override
//			public void onChartDoubleTapped(MotionEvent me) {
//
//			}
//
//			@Override
//			public void onChartSingleTapped(MotionEvent me) {
//
//			}
//
//			@Override
//			public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//
//			}
//
//			@Override
//			public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//
//			}
//
//			@Override
//			public void onChartTranslate(MotionEvent me, float dX, float dY) {
//
//			}
//		} );
		lineChart.setTouchEnabled(true);//设置支持触控手势
		lineChart.setDragEnabled(true);//设置缩放
		lineChart.setScaleEnabled(true);//设置推动
		lineChart.setPinchZoom(true);//如果禁用,扩展可以在x轴和y轴分别完成
		Legend legend = lineChart.getLegend();
		legend.setForm(Legend.LegendForm.LINE);
		lineChart.animateX(10);//默认动画
	}

	private void setData(ArrayList<Entry> values){
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
		if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
			set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
			set1.setValues(values);
			set1.setLabel( "通道 1 数据" );
            //添加数据集
            dataSets.add(set1);
            setChartLineStyle(set1,LINE_COLORS[0]);
			lineChart.getData().notifyDataChanged();
			lineChart.notifyDataSetChanged();
		} else {
			// 创建一个数据集,并给它一个类型
			set1 = new LineDataSet(values, "测试数据");
            //添加数据集
            dataSets.add(set1);
			// 在这里设置线
            setChartLineStyle(set1,LINE_COLORS[0]);
			}
		//创建一个数据集的数据对象
		LineData data = new LineData(dataSets);
		//谁知数据
		lineChart.setData(data);
	}

    private void setData(ArrayList<Entry> values,LineDataSet set, int n){
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(n);
            set.setValues(values);
            //添加数据集
            dataSets.add(set);
            setChartLineStyle(set,LINE_COLORS[n]);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set = new LineDataSet(values, "测试数据");
            //添加数据集
            dataSets.add(set);
            // 在这里设置线
            setChartLineStyle(set,LINE_COLORS[n]);
        }
        //创建一个数据集的数据对象
        LineData data = new LineData(dataSets);
        //谁知数据
        lineChart.setData(data);
    }

	private void setChartLineStyle(LineDataSet set, int color){
        set.disableDashedLine();
//			set1.enableDashedHighlightLine(10f, 5f, 0f);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setColor(color);  // Color.BLACK);
        set.setLineWidth(1f);
		set.setDrawCircles( false );
		set.setCircleColor(color);
//        set.setCircleRadius(1f);
//        set.setDrawCircleHole(false);
        set.setValueTextSize(0f);
        set.setDrawFilled(false);
        set.setFormSize(15.f);
    }

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");


		//如果BT未打开，请求启用。
		// 然后在onActivityResult期间调用setupChat（）
		if (!mBluetoothAdapter.isEnabled())
		{
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// 否则，设置聊天会话
		}
		else
		{

			if (mChatService == null)
				setupChat();
			else {
				try {
					mChatService.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void search(){
		Intent serverIntent = new Intent(BluetoothPlot.this,
				DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	//300秒内搜索
	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			//设置本机蓝牙可让发现
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}


//初始化
	private void setupChat() {
		Log.d(TAG, "setupChat()");

			// 初始化BluetoothChatService以执行app_incon_bluetooth连接
		mChatService = new BluetoothChatService(this, mHandler);

		//初始化外发消息的缓冲区
//		mOutStringBuffer = new StringBuffer("");
	}

	//重写发送函数，参数不同。
	private void sendMessage(String message) {
		// 确保已连接
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// 检测是否有字符串发送
		if (message.length() > 0) {
			// 获取 字符串并告诉BluetoothChatService发送
				byte[] send = Data_syn.hexStr2Bytes(message);
				mChatService.write(send);//回调service
		}
		else {
			Toast.makeText(this,"发送内容不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

	// 该Handler从BluetoothChatService中获取信息
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                    	Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);

                    switch (msg.arg1)
                    {
                        case BluetoothChatService.STATE_CONNECTED:
							 mTitle.setText(R.string.title_connected_to);
							 mTitle.append(mConnectedDeviceName);
							 break;

                        case BluetoothChatService.STATE_CONNECTING:
							mTitle.setText(R.string.title_connecting);
							break;

                        case BluetoothChatService.STATE_LISTEN:

                        case BluetoothChatService.STATE_NONE:
							mTitle.setText(R.string.title_not_connected);
							break;
                    }
                	break;

                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    int len = msg.arg1/32;    // 32个字节，目前测试：回传数据均为32的整数倍。
                    float [][]CHData = new float[3][len];
                    CHData =  Data_syn.bytesToFloat(readBuf, msg.arg1);
                    int nValues = values.size();

                    for(int i = 0; i < len; i++)
                        values.add(new Entry( nValues + i, A * CHData[0][i]));
                    setData(values);
                    lineChart.invalidate();

                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存已连接设备的名称
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "连接到 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
			}
		}
	};

	/**
	 * 绘制线图，默认最多绘制三种颜色。所有线均依赖左侧y轴显示。
	 *
	 * @param lineChart
	 * @param xAxisValue x轴的轴
	 * @param yXAxisValues y轴的值
	 * @param titles 每一个数据系列的标题
	 * @param showSetValues 是否在折线上显示数据集的值。true为显示，此时y轴上的数值不可见，否则相反。
	 * @param lineColors 线的颜色数组。为null时取默认颜色，此时最多绘制三种颜色。
	 */
	public static void setLinesChart(LineChart lineChart, List<String> xAxisValue, List<List<Float>> yXAxisValues, List<String> titles, boolean showSetValues, int[] lineColors) {
		lineChart.getDescription().setEnabled(false);//设置描述
		lineChart.setPinchZoom(true);//设置按比例放缩柱状图

		//x坐标轴设置
		XAxis xAxis = lineChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawGridLines(false);
		xAxis.setGranularity(1f);
		xAxis.setLabelCount(xAxisValue.size());
		/*xAxis.setAxisLineWidth(2f);*/

		//y轴设置
		YAxis leftAxis = lineChart.getAxisLeft();
		leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setDrawGridLines(false);
		if (showSetValues) {
			leftAxis.setDrawLabels(false);//折线上显示值，则不显示坐标轴上的值
		}
		//leftAxis.setDrawZeroLine(true);
		/*leftAxis.setAxisMinimum(0f);*/
		/*leftAxis.setAxisLineWidth(2f);*/

		lineChart.getAxisRight().setEnabled(false);

		//图例设置
		Legend legend = lineChart.getLegend();
		legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
		legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
		legend.setDrawInside(false);
		legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		legend.setForm(Legend.LegendForm.LINE);
		legend.setTextSize(12f);

		//设置折线图数据
		setLinesChartData(lineChart, yXAxisValues, titles, showSetValues,lineColors);

		lineChart.setExtraOffsets(10, 30, 20, 10);
		lineChart.animateX(500);//数据显示动画，从左往右依次显示
	}

	private static void setLinesChartData(LineChart lineChart, List<List<Float>> yXAxisValues, List<String> titles, boolean showSetValues, int[] lineColors) {

		List<List<Entry>> entriesList = new ArrayList<>();
		for (int i = 0; i < yXAxisValues.size(); ++i) {
			ArrayList<Entry> entries = new ArrayList<>();
			for (int j = 0, n = yXAxisValues.get(i).size(); j < n; j++) {
				entries.add(new Entry(j, yXAxisValues.get(i).get(j)));
			}
			entriesList.add(entries);
		}

		if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {

			for (int i = 0; i < lineChart.getData().getDataSetCount(); ++i) {
				LineDataSet set = (LineDataSet) lineChart.getData().getDataSetByIndex(i);
				set.setValues(entriesList.get(i));
				set.setLabel(titles.get(i));
			}

			lineChart.getData().notifyDataChanged();
			lineChart.notifyDataSetChanged();
		} else {
			ArrayList<ILineDataSet> dataSets = new ArrayList<>();

			for (int i = 0; i < entriesList.size(); ++i) {
				LineDataSet set = new LineDataSet(entriesList.get(i), titles.get(i));
				if(lineColors!=null){
					set.setColor(lineColors[i % entriesList.size()]);
					set.setCircleColor(lineColors[i % entriesList.size()]);
					set.setCircleColorHole(Color.WHITE);
				} else {
					set.setColor(LINE_COLORS[i % 3]);
					set.setCircleColor(LINE_COLORS[i % 3]);
					set.setCircleColorHole(Color.WHITE);
				}

				if (entriesList.size() == 1) {
					set.setDrawFilled(true);
				}
				dataSets.add(set);
			}

			LineData data = new LineData(dataSets);
			if (showSetValues) {
				data.setValueTextSize(10f);
				data.setValueFormatter(new IValueFormatter() {
					@Override
					public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
						return Double.toString(value);
					}
				});
			} else {
				data.setDrawValues(false);
			}

			lineChart.setData(data);
		}
	}

    //返回该Activity回调函数
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);

		switch (requestCode) {

//search返回的
            case REQUEST_CONNECT_DEVICE:

                // DeviceListActivity返回时要连接的设备
                if (resultCode == Activity.RESULT_OK) {
                    // 获取设备的MAC地址
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 获取BLuetoothDevice对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 尝试连接到设备
                    mChatService.connect(device);
                }
                break;
    //start返回的（遇到蓝牙不可用退出）
            case REQUEST_ENABLE_BT:
                // 当启用蓝牙的请求返回时
                if (resultCode == Activity.RESULT_OK)
                {
                    //蓝牙已启用，因此设置聊天会话
                    setupChat();//初始化文本
                }
                else
                {
                    // 用户未启用蓝牙或发生错误
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
	}

	// 保存功能实现
	private void Save() {
		// 显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(BluetoothPlot.this); // 图层模板生成器句柄
		final View DialogView = factory.inflate(R.layout.sname, null); // 用sname.xml模板生成视图模板
		new AlertDialog.Builder(BluetoothPlot.this).setTitle("文件名")
				.setView(DialogView) // 设置视图模板
				.setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
						{
							public void onClick(DialogInterface dialog,
									int whichButton) {
								EditText text1 = (EditText) DialogView
										.findViewById(R.id.sname); // 得到文件名输入框句柄
								filename = text1.getText().toString(); // 得到文件名

								try {
									if (Environment.getExternalStorageState()
											.equals(Environment.MEDIA_MOUNTED)) { // 如果SD卡已准备好

										filename = filename + ".txt"; // 在文件名末尾加上.txt
										File sdCardDir = Environment
												.getExternalStorageDirectory(); // 得到SD卡根目录
										File BuildDir = new File(sdCardDir,
												"/BluetoothSave"); // 打开BluetoothSave目录，如不存在则生成
										if (BuildDir.exists() == false)
											BuildDir.mkdirs();
										File saveFile = new File(BuildDir,
												filename); // 新建文件句柄，如已存在仍新建文档
										FileOutputStream stream = new FileOutputStream(
												saveFile); // 打开文件输入流
										stream.write(fmsg.getBytes());
										stream.close();
										Toast.makeText(BluetoothPlot.this,
												"存储成功！", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(BluetoothPlot.this,
												"没有存储卡！", Toast.LENGTH_LONG)
												.show();
									}
								} catch (IOException e) {
									return;
								}
							}
						})
				.setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show(); // 显示对话框
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");
		// 在onResume（）中执行此检查包括在onStart（）期间未启用BT的情况，
		// 因此我们暂停启用它...
		// onResume（）将在ACTION_REQUEST_ENABLE活动时被调用返回.
		if (mChatService != null) {
			// 只有状态是STATE_NONE，我们知道我们还没有启动蓝牙
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// 启动BluetoothChat服务
				mChatService.start();
			}
		}

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		// 停止蓝牙通信连接服务
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}
	@Override
	public void onBackPressed()
	{
		exit();
	}

	public void exit()
	{
		exit = true;


		if(exit  ==  true)
		{
			this.finish();
		}

	}
}