package com.carlos.common.widget.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.carlos.common.widget.tablayout.listener.OnTabSelectListener;
import com.carlos.common.widget.tablayout.utils.UnreadMsgUtils;
import com.carlos.common.widget.tablayout.widget.MsgView;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.styleable;
import java.util.ArrayList;
import java.util.Collections;

public class SlidingTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
   private Context mContext;
   private ViewPager mViewPager;
   private ArrayList<String> mTitles;
   private LinearLayout mTabsContainer;
   private int mCurrentTab;
   private float mCurrentPositionOffset;
   private int mTabCount;
   private Rect mIndicatorRect;
   private Rect mTabRect;
   private GradientDrawable mIndicatorDrawable;
   private Paint mRectPaint;
   private Paint mDividerPaint;
   private Paint mTrianglePaint;
   private Path mTrianglePath;
   private static final int STYLE_NORMAL = 0;
   private static final int STYLE_TRIANGLE = 1;
   private static final int STYLE_BLOCK = 2;
   private int mIndicatorStyle;
   private float mTabPadding;
   private boolean mTabSpaceEqual;
   private float mTabWidth;
   private int mIndicatorColor;
   private float mIndicatorHeight;
   private float mIndicatorWidth;
   private float mIndicatorCornerRadius;
   private float mIndicatorMarginLeft;
   private float mIndicatorMarginTop;
   private float mIndicatorMarginRight;
   private float mIndicatorMarginBottom;
   private int mIndicatorGravity;
   private boolean mIndicatorWidthEqualTitle;
   private int mUnderlineColor;
   private float mUnderlineHeight;
   private int mUnderlineGravity;
   private int mDividerColor;
   private float mDividerWidth;
   private float mDividerPadding;
   private static final int TEXT_BOLD_NONE = 0;
   private static final int TEXT_BOLD_WHEN_SELECT = 1;
   private static final int TEXT_BOLD_BOTH = 2;
   private float mTextsize;
   private int mTextSelectColor;
   private int mTextUnselectColor;
   private int mTextBold;
   private boolean mTextAllCaps;
   private int mLastScrollX;
   private int mHeight;
   private boolean mSnapOnTabClick;
   private float margin;
   private Paint mTextPaint;
   private SparseArray<Boolean> mInitSetMap;
   private OnTabSelectListener mListener;

   public SlidingTabLayout(Context context) {
      this(context, (AttributeSet)null, 0);
   }

   public SlidingTabLayout(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.mIndicatorRect = new Rect();
      this.mTabRect = new Rect();
      this.mIndicatorDrawable = new GradientDrawable();
      this.mRectPaint = new Paint(1);
      this.mDividerPaint = new Paint(1);
      this.mTrianglePaint = new Paint(1);
      this.mTrianglePath = new Path();
      this.mIndicatorStyle = 0;
      this.mTextPaint = new Paint(1);
      this.mInitSetMap = new SparseArray();
      this.setFillViewport(true);
      this.setWillNotDraw(false);
      this.setClipChildren(false);
      this.setClipToPadding(false);
      this.mContext = context;
      this.mTabsContainer = new LinearLayout(context);
      this.addView(this.mTabsContainer);
      this.obtainAttributes(context, attrs);
      String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
      if (!height.equals("-1") && !height.equals("-2")) {
         int[] systemAttrs = new int[]{16842997};
         TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
         this.mHeight = a.getDimensionPixelSize(0, -2);
         a.recycle();
      }

   }

   private void obtainAttributes(Context context, AttributeSet attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs, styleable.SlidingTabLayout);
      this.mIndicatorStyle = ta.getInt(styleable.SlidingTabLayout_tl_indicator_style, 0);
      this.mIndicatorColor = ta.getColor(styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(this.mIndicatorStyle == 2 ? "#4B6A87" : "#ffffff"));
      this.mIndicatorHeight = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_height, (float)this.dp2px(this.mIndicatorStyle == 1 ? 4.0F : (float)(this.mIndicatorStyle == 2 ? -1 : 2)));
      this.mIndicatorWidth = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_width, (float)this.dp2px(this.mIndicatorStyle == 1 ? 10.0F : -1.0F));
      this.mIndicatorCornerRadius = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_corner_radius, (float)this.dp2px(this.mIndicatorStyle == 2 ? -1.0F : 0.0F));
      this.mIndicatorMarginLeft = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_margin_left, (float)this.dp2px(0.0F));
      this.mIndicatorMarginTop = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_margin_top, (float)this.dp2px(this.mIndicatorStyle == 2 ? 7.0F : 0.0F));
      this.mIndicatorMarginRight = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_margin_right, (float)this.dp2px(0.0F));
      this.mIndicatorMarginBottom = ta.getDimension(styleable.SlidingTabLayout_tl_indicator_margin_bottom, (float)this.dp2px(this.mIndicatorStyle == 2 ? 7.0F : 0.0F));
      this.mIndicatorGravity = ta.getInt(styleable.SlidingTabLayout_tl_indicator_gravity, 80);
      this.mIndicatorWidthEqualTitle = ta.getBoolean(styleable.SlidingTabLayout_tl_indicator_width_equal_title, false);
      this.mUnderlineColor = ta.getColor(styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
      this.mUnderlineHeight = ta.getDimension(styleable.SlidingTabLayout_tl_underline_height, (float)this.dp2px(0.0F));
      this.mUnderlineGravity = ta.getInt(styleable.SlidingTabLayout_tl_underline_gravity, 80);
      this.mDividerColor = ta.getColor(styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
      this.mDividerWidth = ta.getDimension(styleable.SlidingTabLayout_tl_divider_width, (float)this.dp2px(0.0F));
      this.mDividerPadding = ta.getDimension(styleable.SlidingTabLayout_tl_divider_padding, (float)this.dp2px(12.0F));
      this.mTextsize = ta.getDimension(styleable.SlidingTabLayout_tl_textsize, (float)this.sp2px(14.0F));
      this.mTextSelectColor = ta.getColor(styleable.SlidingTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
      this.mTextUnselectColor = ta.getColor(styleable.SlidingTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
      this.mTextBold = ta.getInt(styleable.SlidingTabLayout_tl_textBold, 0);
      this.mTextAllCaps = ta.getBoolean(styleable.SlidingTabLayout_tl_textAllCaps, false);
      this.mTabSpaceEqual = ta.getBoolean(styleable.SlidingTabLayout_tl_tab_space_equal, false);
      this.mTabWidth = ta.getDimension(styleable.SlidingTabLayout_tl_tab_width, (float)this.dp2px(-1.0F));
      this.mTabPadding = ta.getDimension(styleable.SlidingTabLayout_tl_tab_padding, !this.mTabSpaceEqual && !(this.mTabWidth > 0.0F) ? (float)this.dp2px(20.0F) : (float)this.dp2px(0.0F));
      ta.recycle();
   }

   public void setViewPager(ViewPager vp) {
      if (vp != null && vp.getAdapter() != null) {
         this.mViewPager = vp;
         this.mViewPager.removeOnPageChangeListener(this);
         this.mViewPager.addOnPageChangeListener(this);
         this.notifyDataSetChanged();
      } else {
         throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
      }
   }

   public void setViewPager(ViewPager vp, String[] titles) {
      if (vp != null && vp.getAdapter() != null) {
         if (titles != null && titles.length != 0) {
            if (titles.length != vp.getAdapter().getCount()) {
               throw new IllegalStateException("Titles length must be the same as the page count !");
            } else {
               this.mViewPager = vp;
               this.mTitles = new ArrayList();
               Collections.addAll(this.mTitles, titles);
               this.mViewPager.removeOnPageChangeListener(this);
               this.mViewPager.addOnPageChangeListener(this);
               this.notifyDataSetChanged();
            }
         } else {
            throw new IllegalStateException("Titles can not be EMPTY !");
         }
      } else {
         throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
      }
   }

   public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
      if (vp == null) {
         throw new IllegalStateException("ViewPager can not be NULL !");
      } else if (titles != null && titles.length != 0) {
         this.mViewPager = vp;
         this.mViewPager.setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));
         this.mViewPager.removeOnPageChangeListener(this);
         this.mViewPager.addOnPageChangeListener(this);
         this.notifyDataSetChanged();
      } else {
         throw new IllegalStateException("Titles can not be EMPTY !");
      }
   }

   public void notifyDataSetChanged() {
      this.mTabsContainer.removeAllViews();
      this.mTabCount = this.mTitles == null ? this.mViewPager.getAdapter().getCount() : this.mTitles.size();

      for(int i = 0; i < this.mTabCount; ++i) {
         View tabView = View.inflate(this.mContext, layout.layout_tab, (ViewGroup)null);
         CharSequence pageTitle = this.mTitles == null ? this.mViewPager.getAdapter().getPageTitle(i) : (CharSequence)this.mTitles.get(i);
         this.addTab(i, pageTitle.toString(), tabView);
      }

      this.updateTabStyles();
   }

   public void addNewTab(String title) {
      View tabView = View.inflate(this.mContext, layout.layout_tab, (ViewGroup)null);
      if (this.mTitles != null) {
         this.mTitles.add(title);
      }

      CharSequence pageTitle = this.mTitles == null ? this.mViewPager.getAdapter().getPageTitle(this.mTabCount) : (CharSequence)this.mTitles.get(this.mTabCount);
      this.addTab(this.mTabCount, pageTitle.toString(), tabView);
      this.mTabCount = this.mTitles == null ? this.mViewPager.getAdapter().getCount() : this.mTitles.size();
      this.updateTabStyles();
   }

   private void addTab(int position, String title, View tabView) {
      TextView tv_tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
      if (tv_tab_title != null && title != null) {
         tv_tab_title.setText(title);
      }

      tabView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            int position = SlidingTabLayout.this.mTabsContainer.indexOfChild(v);
            if (position != -1) {
               if (SlidingTabLayout.this.mViewPager.getCurrentItem() != position) {
                  if (SlidingTabLayout.this.mSnapOnTabClick) {
                     SlidingTabLayout.this.mViewPager.setCurrentItem(position, false);
                  } else {
                     SlidingTabLayout.this.mViewPager.setCurrentItem(position);
                  }

                  if (SlidingTabLayout.this.mListener != null) {
                     SlidingTabLayout.this.mListener.onTabSelect(position);
                  }
               } else if (SlidingTabLayout.this.mListener != null) {
                  SlidingTabLayout.this.mListener.onTabReselect(position);
               }
            }

         }
      });
      LinearLayout.LayoutParams lp_tab = this.mTabSpaceEqual ? new LinearLayout.LayoutParams(0, -1, 1.0F) : new LinearLayout.LayoutParams(-2, -1);
      if (this.mTabWidth > 0.0F) {
         lp_tab = new LinearLayout.LayoutParams((int)this.mTabWidth, -1);
      }

      this.mTabsContainer.addView(tabView, position, lp_tab);
   }

   private void updateTabStyles() {
      for(int i = 0; i < this.mTabCount; ++i) {
         View v = this.mTabsContainer.getChildAt(i);
         TextView tv_tab_title = (TextView)v.findViewById(id.tv_tab_title);
         if (tv_tab_title != null) {
            tv_tab_title.setTextColor(i == this.mCurrentTab ? this.mTextSelectColor : this.mTextUnselectColor);
            tv_tab_title.setTextSize(0, this.mTextsize);
            tv_tab_title.setPadding((int)this.mTabPadding, 0, (int)this.mTabPadding, 0);
            if (this.mTextAllCaps) {
               tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
            }

            if (this.mTextBold == 2) {
               tv_tab_title.getPaint().setFakeBoldText(true);
            } else if (this.mTextBold == 0) {
               tv_tab_title.getPaint().setFakeBoldText(false);
            }
         }
      }

   }

   public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      this.mCurrentTab = position;
      this.mCurrentPositionOffset = positionOffset;
      this.scrollToCurrentTab();
      this.invalidate();
   }

   public void onPageSelected(int position) {
      this.updateTabSelection(position);
   }

   public void onPageScrollStateChanged(int state) {
   }

   private void scrollToCurrentTab() {
      if (this.mTabCount > 0) {
         int offset = (int)(this.mCurrentPositionOffset * (float)this.mTabsContainer.getChildAt(this.mCurrentTab).getWidth());
         int newScrollX = this.mTabsContainer.getChildAt(this.mCurrentTab).getLeft() + offset;
         if (this.mCurrentTab > 0 || offset > 0) {
            newScrollX -= this.getWidth() / 2 - this.getPaddingLeft();
            this.calcIndicatorRect();
            newScrollX += (this.mTabRect.right - this.mTabRect.left) / 2;
         }

         if (newScrollX != this.mLastScrollX) {
            this.mLastScrollX = newScrollX;
            this.scrollTo(newScrollX, 0);
         }

      }
   }

   private void updateTabSelection(int position) {
      for(int i = 0; i < this.mTabCount; ++i) {
         View tabView = this.mTabsContainer.getChildAt(i);
         boolean isSelect = i == position;
         TextView tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
         if (tab_title != null) {
            tab_title.setTextColor(isSelect ? this.mTextSelectColor : this.mTextUnselectColor);
            if (this.mTextBold == 1) {
               tab_title.getPaint().setFakeBoldText(isSelect);
            }
         }
      }

   }

   private void calcIndicatorRect() {
      View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
      float left = (float)currentTabView.getLeft();
      float right = (float)currentTabView.getRight();
      float nextTabLeft;
      if (this.mIndicatorStyle == 0 && this.mIndicatorWidthEqualTitle) {
         TextView tab_title = (TextView)currentTabView.findViewById(id.tv_tab_title);
         this.mTextPaint.setTextSize(this.mTextsize);
         nextTabLeft = this.mTextPaint.measureText(tab_title.getText().toString());
         this.margin = (right - left - nextTabLeft) / 2.0F;
      }

      if (this.mCurrentTab < this.mTabCount - 1) {
         View nextTabView = this.mTabsContainer.getChildAt(this.mCurrentTab + 1);
         nextTabLeft = (float)nextTabView.getLeft();
         float nextTabRight = (float)nextTabView.getRight();
         left += this.mCurrentPositionOffset * (nextTabLeft - left);
         right += this.mCurrentPositionOffset * (nextTabRight - right);
         if (this.mIndicatorStyle == 0 && this.mIndicatorWidthEqualTitle) {
            TextView next_tab_title = (TextView)nextTabView.findViewById(id.tv_tab_title);
            this.mTextPaint.setTextSize(this.mTextsize);
            float nextTextWidth = this.mTextPaint.measureText(next_tab_title.getText().toString());
            float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2.0F;
            this.margin += this.mCurrentPositionOffset * (nextMargin - this.margin);
         }
      }

      this.mIndicatorRect.left = (int)left;
      this.mIndicatorRect.right = (int)right;
      if (this.mIndicatorStyle == 0 && this.mIndicatorWidthEqualTitle) {
         this.mIndicatorRect.left = (int)(left + this.margin - 1.0F);
         this.mIndicatorRect.right = (int)(right - this.margin - 1.0F);
      }

      this.mTabRect.left = (int)left;
      this.mTabRect.right = (int)right;
      if (!(this.mIndicatorWidth < 0.0F)) {
         float indicatorLeft = (float)currentTabView.getLeft() + ((float)currentTabView.getWidth() - this.mIndicatorWidth) / 2.0F;
         if (this.mCurrentTab < this.mTabCount - 1) {
            View nextTab = this.mTabsContainer.getChildAt(this.mCurrentTab + 1);
            indicatorLeft += this.mCurrentPositionOffset * (float)(currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
         }

         this.mIndicatorRect.left = (int)indicatorLeft;
         this.mIndicatorRect.right = (int)((float)this.mIndicatorRect.left + this.mIndicatorWidth);
      }

   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      if (!this.isInEditMode() && this.mTabCount > 0) {
         int height = this.getHeight();
         int paddingLeft = this.getPaddingLeft();
         if (this.mDividerWidth > 0.0F) {
            this.mDividerPaint.setStrokeWidth(this.mDividerWidth);
            this.mDividerPaint.setColor(this.mDividerColor);

            for(int i = 0; i < this.mTabCount - 1; ++i) {
               View tab = this.mTabsContainer.getChildAt(i);
               canvas.drawLine((float)(paddingLeft + tab.getRight()), this.mDividerPadding, (float)(paddingLeft + tab.getRight()), (float)height - this.mDividerPadding, this.mDividerPaint);
            }
         }

         if (this.mUnderlineHeight > 0.0F) {
            this.mRectPaint.setColor(this.mUnderlineColor);
            if (this.mUnderlineGravity == 80) {
               canvas.drawRect((float)paddingLeft, (float)height - this.mUnderlineHeight, (float)(this.mTabsContainer.getWidth() + paddingLeft), (float)height, this.mRectPaint);
            } else {
               canvas.drawRect((float)paddingLeft, 0.0F, (float)(this.mTabsContainer.getWidth() + paddingLeft), this.mUnderlineHeight, this.mRectPaint);
            }
         }

         this.calcIndicatorRect();
         if (this.mIndicatorStyle == 1) {
            if (this.mIndicatorHeight > 0.0F) {
               this.mTrianglePaint.setColor(this.mIndicatorColor);
               this.mTrianglePath.reset();
               this.mTrianglePath.moveTo((float)(paddingLeft + this.mIndicatorRect.left), (float)height);
               this.mTrianglePath.lineTo((float)(paddingLeft + this.mIndicatorRect.left / 2 + this.mIndicatorRect.right / 2), (float)height - this.mIndicatorHeight);
               this.mTrianglePath.lineTo((float)(paddingLeft + this.mIndicatorRect.right), (float)height);
               this.mTrianglePath.close();
               canvas.drawPath(this.mTrianglePath, this.mTrianglePaint);
            }
         } else if (this.mIndicatorStyle == 2) {
            if (this.mIndicatorHeight < 0.0F) {
               this.mIndicatorHeight = (float)height - this.mIndicatorMarginTop - this.mIndicatorMarginBottom;
            }

            if (this.mIndicatorHeight > 0.0F) {
               if (this.mIndicatorCornerRadius < 0.0F || this.mIndicatorCornerRadius > this.mIndicatorHeight / 2.0F) {
                  this.mIndicatorCornerRadius = this.mIndicatorHeight / 2.0F;
               }

               this.mIndicatorDrawable.setColor(this.mIndicatorColor);
               this.mIndicatorDrawable.setBounds(paddingLeft + (int)this.mIndicatorMarginLeft + this.mIndicatorRect.left, (int)this.mIndicatorMarginTop, (int)((float)(paddingLeft + this.mIndicatorRect.right) - this.mIndicatorMarginRight), (int)(this.mIndicatorMarginTop + this.mIndicatorHeight));
               this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
               this.mIndicatorDrawable.draw(canvas);
            }
         } else if (this.mIndicatorHeight > 0.0F) {
            this.mIndicatorDrawable.setColor(this.mIndicatorColor);
            if (this.mIndicatorGravity == 80) {
               this.mIndicatorDrawable.setBounds(paddingLeft + (int)this.mIndicatorMarginLeft + this.mIndicatorRect.left, height - (int)this.mIndicatorHeight - (int)this.mIndicatorMarginBottom, paddingLeft + this.mIndicatorRect.right - (int)this.mIndicatorMarginRight, height - (int)this.mIndicatorMarginBottom);
            } else {
               this.mIndicatorDrawable.setBounds(paddingLeft + (int)this.mIndicatorMarginLeft + this.mIndicatorRect.left, (int)this.mIndicatorMarginTop, paddingLeft + this.mIndicatorRect.right - (int)this.mIndicatorMarginRight, (int)this.mIndicatorHeight + (int)this.mIndicatorMarginTop);
            }

            this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
            this.mIndicatorDrawable.draw(canvas);
         }

      }
   }

   public void setCurrentTab(int currentTab) {
      this.mCurrentTab = currentTab;
      this.mViewPager.setCurrentItem(currentTab);
   }

   public void setCurrentTab(int currentTab, boolean smoothScroll) {
      this.mCurrentTab = currentTab;
      this.mViewPager.setCurrentItem(currentTab, smoothScroll);
   }

   public void setIndicatorStyle(int indicatorStyle) {
      this.mIndicatorStyle = indicatorStyle;
      this.invalidate();
   }

   public void setTabPadding(float tabPadding) {
      this.mTabPadding = (float)this.dp2px(tabPadding);
      this.updateTabStyles();
   }

   public void setTabSpaceEqual(boolean tabSpaceEqual) {
      this.mTabSpaceEqual = tabSpaceEqual;
      this.updateTabStyles();
   }

   public void setTabWidth(float tabWidth) {
      this.mTabWidth = (float)this.dp2px(tabWidth);
      this.updateTabStyles();
   }

   public void setIndicatorColor(int indicatorColor) {
      this.mIndicatorColor = indicatorColor;
      this.invalidate();
   }

   public void setIndicatorHeight(float indicatorHeight) {
      this.mIndicatorHeight = (float)this.dp2px(indicatorHeight);
      this.invalidate();
   }

   public void setIndicatorWidth(float indicatorWidth) {
      this.mIndicatorWidth = (float)this.dp2px(indicatorWidth);
      this.invalidate();
   }

   public void setIndicatorCornerRadius(float indicatorCornerRadius) {
      this.mIndicatorCornerRadius = (float)this.dp2px(indicatorCornerRadius);
      this.invalidate();
   }

   public void setIndicatorGravity(int indicatorGravity) {
      this.mIndicatorGravity = indicatorGravity;
      this.invalidate();
   }

   public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop, float indicatorMarginRight, float indicatorMarginBottom) {
      this.mIndicatorMarginLeft = (float)this.dp2px(indicatorMarginLeft);
      this.mIndicatorMarginTop = (float)this.dp2px(indicatorMarginTop);
      this.mIndicatorMarginRight = (float)this.dp2px(indicatorMarginRight);
      this.mIndicatorMarginBottom = (float)this.dp2px(indicatorMarginBottom);
      this.invalidate();
   }

   public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
      this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
      this.invalidate();
   }

   public void setUnderlineColor(int underlineColor) {
      this.mUnderlineColor = underlineColor;
      this.invalidate();
   }

   public void setUnderlineHeight(float underlineHeight) {
      this.mUnderlineHeight = (float)this.dp2px(underlineHeight);
      this.invalidate();
   }

   public void setUnderlineGravity(int underlineGravity) {
      this.mUnderlineGravity = underlineGravity;
      this.invalidate();
   }

   public void setDividerColor(int dividerColor) {
      this.mDividerColor = dividerColor;
      this.invalidate();
   }

   public void setDividerWidth(float dividerWidth) {
      this.mDividerWidth = (float)this.dp2px(dividerWidth);
      this.invalidate();
   }

   public void setDividerPadding(float dividerPadding) {
      this.mDividerPadding = (float)this.dp2px(dividerPadding);
      this.invalidate();
   }

   public void setTextsize(float textsize) {
      this.mTextsize = (float)this.sp2px(textsize);
      this.updateTabStyles();
   }

   public void setTextSelectColor(int textSelectColor) {
      this.mTextSelectColor = textSelectColor;
      this.updateTabStyles();
   }

   public void setTextUnselectColor(int textUnselectColor) {
      this.mTextUnselectColor = textUnselectColor;
      this.updateTabStyles();
   }

   public void setTextBold(int textBold) {
      this.mTextBold = textBold;
      this.updateTabStyles();
   }

   public void setTextAllCaps(boolean textAllCaps) {
      this.mTextAllCaps = textAllCaps;
      this.updateTabStyles();
   }

   public void setSnapOnTabClick(boolean snapOnTabClick) {
      this.mSnapOnTabClick = snapOnTabClick;
   }

   public int getTabCount() {
      return this.mTabCount;
   }

   public int getCurrentTab() {
      return this.mCurrentTab;
   }

   public int getIndicatorStyle() {
      return this.mIndicatorStyle;
   }

   public float getTabPadding() {
      return this.mTabPadding;
   }

   public boolean isTabSpaceEqual() {
      return this.mTabSpaceEqual;
   }

   public float getTabWidth() {
      return this.mTabWidth;
   }

   public int getIndicatorColor() {
      return this.mIndicatorColor;
   }

   public float getIndicatorHeight() {
      return this.mIndicatorHeight;
   }

   public float getIndicatorWidth() {
      return this.mIndicatorWidth;
   }

   public float getIndicatorCornerRadius() {
      return this.mIndicatorCornerRadius;
   }

   public float getIndicatorMarginLeft() {
      return this.mIndicatorMarginLeft;
   }

   public float getIndicatorMarginTop() {
      return this.mIndicatorMarginTop;
   }

   public float getIndicatorMarginRight() {
      return this.mIndicatorMarginRight;
   }

   public float getIndicatorMarginBottom() {
      return this.mIndicatorMarginBottom;
   }

   public int getUnderlineColor() {
      return this.mUnderlineColor;
   }

   public float getUnderlineHeight() {
      return this.mUnderlineHeight;
   }

   public int getDividerColor() {
      return this.mDividerColor;
   }

   public float getDividerWidth() {
      return this.mDividerWidth;
   }

   public float getDividerPadding() {
      return this.mDividerPadding;
   }

   public float getTextsize() {
      return this.mTextsize;
   }

   public int getTextSelectColor() {
      return this.mTextSelectColor;
   }

   public int getTextUnselectColor() {
      return this.mTextUnselectColor;
   }

   public int getTextBold() {
      return this.mTextBold;
   }

   public boolean isTextAllCaps() {
      return this.mTextAllCaps;
   }

   public TextView getTitleView(int tab) {
      View tabView = this.mTabsContainer.getChildAt(tab);
      TextView tv_tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
      return tv_tab_title;
   }

   public void showMsg(int position, int num) {
      if (position >= this.mTabCount) {
         position = this.mTabCount - 1;
      }

      View tabView = this.mTabsContainer.getChildAt(position);
      MsgView tipView = (MsgView)tabView.findViewById(id.rtv_msg_tip);
      if (tipView != null) {
         UnreadMsgUtils.show(tipView, num);
         if (this.mInitSetMap.get(position) != null && (Boolean)this.mInitSetMap.get(position)) {
            return;
         }

         this.setMsgMargin(position, 4.0F, 2.0F);
         this.mInitSetMap.put(position, true);
      }

   }

   public void showDot(int position) {
      if (position >= this.mTabCount) {
         position = this.mTabCount - 1;
      }

      this.showMsg(position, 0);
   }

   public void hideMsg(int position) {
      if (position >= this.mTabCount) {
         position = this.mTabCount - 1;
      }

      View tabView = this.mTabsContainer.getChildAt(position);
      MsgView tipView = (MsgView)tabView.findViewById(id.rtv_msg_tip);
      if (tipView != null) {
         tipView.setVisibility(8);
      }

   }

   public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
      if (position >= this.mTabCount) {
         position = this.mTabCount - 1;
      }

      View tabView = this.mTabsContainer.getChildAt(position);
      MsgView tipView = (MsgView)tabView.findViewById(id.rtv_msg_tip);
      if (tipView != null) {
         TextView tv_tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
         this.mTextPaint.setTextSize(this.mTextsize);
         float textWidth = this.mTextPaint.measureText(tv_tab_title.getText().toString());
         float textHeight = this.mTextPaint.descent() - this.mTextPaint.ascent();
         ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)tipView.getLayoutParams();
         lp.leftMargin = this.mTabWidth >= 0.0F ? (int)(this.mTabWidth / 2.0F + textWidth / 2.0F + (float)this.dp2px(leftPadding)) : (int)(this.mTabPadding + textWidth + (float)this.dp2px(leftPadding));
         lp.topMargin = this.mHeight > 0 ? (int)((float)this.mHeight - textHeight) / 2 - this.dp2px(bottomPadding) : 0;
         tipView.setLayoutParams(lp);
      }

   }

   public MsgView getMsgView(int position) {
      if (position >= this.mTabCount) {
         position = this.mTabCount - 1;
      }

      View tabView = this.mTabsContainer.getChildAt(position);
      MsgView tipView = (MsgView)tabView.findViewById(id.rtv_msg_tip);
      return tipView;
   }

   public void setOnTabSelectListener(OnTabSelectListener listener) {
      this.mListener = listener;
   }

   protected Parcelable onSaveInstanceState() {
      Bundle bundle = new Bundle();
      bundle.putParcelable("instanceState", super.onSaveInstanceState());
      bundle.putInt("mCurrentTab", this.mCurrentTab);
      return bundle;
   }

   protected void onRestoreInstanceState(Parcelable state) {
      if (state instanceof Bundle) {
         Bundle bundle = (Bundle)state;
         this.mCurrentTab = bundle.getInt("mCurrentTab");
         state = bundle.getParcelable("instanceState");
         if (this.mCurrentTab != 0 && this.mTabsContainer.getChildCount() > 0) {
            this.updateTabSelection(this.mCurrentTab);
            this.scrollToCurrentTab();
         }
      }

      super.onRestoreInstanceState(state);
   }

   protected int dp2px(float dp) {
      float scale = this.mContext.getResources().getDisplayMetrics().density;
      return (int)(dp * scale + 0.5F);
   }

   protected int sp2px(float sp) {
      float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
      return (int)(sp * scale + 0.5F);
   }

   class InnerPagerAdapter extends FragmentPagerAdapter {
      private ArrayList<Fragment> fragments = new ArrayList();
      private String[] titles;

      public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
         super(fm);
         this.fragments = fragments;
         this.titles = titles;
      }

      public int getCount() {
         return this.fragments.size();
      }

      public CharSequence getPageTitle(int position) {
         return this.titles[position];
      }

      public Fragment getItem(int position) {
         return (Fragment)this.fragments.get(position);
      }

      public void destroyItem(ViewGroup container, int position, Object object) {
      }

      public int getItemPosition(Object object) {
         return -2;
      }
   }
}
