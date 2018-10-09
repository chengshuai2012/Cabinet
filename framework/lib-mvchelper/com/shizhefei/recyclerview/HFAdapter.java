package com.shizhefei.recyclerview;


import android.view.View;

public interface HFAdapter{

	void addFooter(View view);

	boolean isHeader(int position);

	boolean isFooter(int position);
}
