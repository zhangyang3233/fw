package com.zycoder.uicomp.rating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zycoder.uicomp.R;

@SuppressLint("NewApi")
public class WDRatingBar extends LinearLayout {

  private float totalWidth; // The whole length of ratingbar
  private float totalHeight; // Ratingbar height
  private int starNumber; // Number of stars
  private int rating; // Class
  private int ratingInResourceId; // Equivalent to the selected resource graph
  private int ratingOutResourceId;
  private boolean clickable;
  private ImageView[] mImageViews;
  Context context;
  private OnClickListener mOnClickListener;


  public WDRatingBar(Context context) {
    this(context, null);
  }

  public WDRatingBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WDRatingBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
    TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
        R.styleable.WDRatingBar);
    totalWidth = mTypedArray.getDimension(
        R.styleable.WDRatingBar_total_width, 40);
    totalHeight = mTypedArray.getDimension(
        R.styleable.WDRatingBar_total_height, 10);
    starNumber = mTypedArray.getInt(R.styleable.WDRatingBar_star_number,
        0);
    rating = mTypedArray.getInt(R.styleable.WDRatingBar_rating, 0);
    ratingInResourceId = mTypedArray.getResourceId(
        R.styleable.WDRatingBar_rating_in_bg, -1);
    ratingOutResourceId = mTypedArray.getResourceId(
        R.styleable.WDRatingBar_rating_out_bg, -1);
    clickable = mTypedArray.getBoolean(R.styleable.WDRatingBar_canclick,
        false);
    mTypedArray.recycle();
    if (starNumber > 0) {
      initialsView(context, clickable, 10);
    }
  }

  private void initialsView(Context context, boolean clickable, int margins) {
    removeAllViews();
    mImageViews = new ImageView[starNumber];
    for (int i = 0; i < starNumber; i++) {
      ImageView imageView = new ImageView(context, null);
      if (i < rating) {
        imageView.setImageResource(ratingInResourceId);

      } else {
        imageView.setImageResource(ratingOutResourceId);
      }
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
          (int) (totalWidth / starNumber), (int) totalHeight);
      layoutParams.setMargins(margins, margins, margins, margins);
      imageView.setClickable(clickable);
      mImageViews[i] = imageView;
      mImageViews[i].setTag(i);
      addView(imageView, layoutParams);
    }
  }

  public void setOnImageClickListener(OnClickListener listener) {
    mOnClickListener = listener;
    for (int i = 0; i < mImageViews.length; i++) {
      mImageViews[i].setOnClickListener(listener);
    }
  }

  public void setResID(int selectedid, int unselectedid) {
    ratingInResourceId = selectedid;
    ratingOutResourceId = unselectedid;
    initialsView(context, clickable, 10);
    setOnImageClickListener(mOnClickListener);
  }

  public void changeView(View v) {
    ImageView iv = (ImageView) v;
    int i = (Integer) iv.getTag();
    changeView(i);
  }

  public void changeView(int i) {
    for (int j = 0; j < starNumber; j++) {
      if (j <= i) {
        mImageViews[j].setImageResource(ratingInResourceId);

      } else {
        mImageViews[j].setImageResource(ratingOutResourceId);
      }
    }
  }

  public interface OnRatingClick {
    public void onImageClick(int i);
  }



  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
    if (starNumber > 0) {
      removeAllViews();
      initialsView(context, clickable, 10);
    }
  }

  public void setRatingShort(int rating, int margins) {
    this.rating = rating;
    if (starNumber > 0) {
      removeAllViews();
      initialsView(context, clickable, margins);
    }
  }
}
