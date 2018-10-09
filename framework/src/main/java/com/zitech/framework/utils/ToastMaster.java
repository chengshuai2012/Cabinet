/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.zitech.framework.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zitech.framework.BaseApplication;
import com.zitech.framework.R;

public class ToastMaster {

	private ToastMaster() {

	}

	public static void popToast(Context context, String toastText, int during) {
		if (context == null) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast = Toast.makeText(context, toastText, during);
		sToast.show();
	}

	public static void popToast(Context context, int textId, int during) {
		if (context == null) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast = Toast.makeText(context, textId, during);
		sToast.show();
	}
	public static void centerToast(Context context,int  textid){
		if (context==null){
			return;
		}
		if (context instanceof Activity){
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast=Toast.makeText(context,textid,Toast.LENGTH_LONG);
		sToast.setGravity(Gravity.CENTER,sToast.getXOffset()/2,sToast.getYOffset()/2);
		sToast.show();
	}
	public static void centerToast(Context context,String   text){
		if (context==null){
			return;
		}
		if (context instanceof Activity){
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast=Toast.makeText(context,text,Toast.LENGTH_LONG);
		sToast.setGravity(Gravity.CENTER,sToast.getXOffset()/2,sToast.getYOffset()/2);
		sToast.show();
	}
	public static void toastAboveBottom(Context context,String   text){
		if (context==null){
			return;
		}
		if (context instanceof Activity){
			if (((Activity) context).isFinishing())
				return;
		}
		Toast sToast=Toast.makeText(context,text,Toast.LENGTH_LONG);

		sToast.setGravity(Gravity.TOP, sToast.getXOffset()/2, (int) (ViewUtils.getDisplayHeight()/1.8f));
//		sToast.setGravity(Gravity.BOTTOM,sToast.getXOffset()/2, (int) (sToast.getYOffset()/3.3f));
		sToast.show();
	}
	public static void popToast(Context context, int textId) {
		popToast(context, textId, Toast.LENGTH_SHORT);
	}

	public static void popToast(Context context, String toastText) {
		popToast(context, toastText, Toast.LENGTH_SHORT);
	}

	public static void shortToast(String toastText) {
		popToast(BaseApplication.getInstance().getApplicationContext(), toastText, Toast.LENGTH_SHORT);
	}

	public static void shortToast(int textId) {
		popToast(BaseApplication.getInstance().getApplicationContext(), textId, Toast.LENGTH_SHORT);
	}

	public static void longToast(String toastText) {
		popToast(BaseApplication.getInstance().getApplicationContext(), toastText, Toast.LENGTH_LONG);
	}

	public static void longToast(int textId) {
		popToast(BaseApplication.getInstance().getApplicationContext(), textId, Toast.LENGTH_LONG);
	}

	/**
	 * 默认是录音时间太短
	 * @param context
	 * @param imageResId
	 * @param tips
	 */
	@SuppressLint("InflateParams")
	public static void popCenterTips(Context context, int imageResId, String tips) {
		if (context == null) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity) context).isFinishing())
				return;
		}
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_tips, null);
		ImageView img = (ImageView) layout.findViewById(R.id.mall_tips1);
		TextView text = (TextView) layout.findViewById(R.id.mall_tips2);
		if (imageResId>0)
			img.setBackgroundResource(imageResId);
		if (!TextUtils.isEmpty(tips))
			text.setText(tips);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@SuppressLint("InflateParams")
	public static void popCenterTips(Context context, String tips) {
		if (context == null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_center_tips, null);
		TextView text = (TextView) layout.findViewById(R.id.mall_tips);
		if (!TextUtils.isEmpty(tips))
			text.setText(tips);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}
