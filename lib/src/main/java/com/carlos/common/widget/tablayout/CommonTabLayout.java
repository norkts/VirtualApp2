package com.carlos.common.widget.tablayout;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
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
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.carlos.common.widget.tablayout.listener.CustomTabEntity;
import com.carlos.common.widget.tablayout.listener.OnTabSelectListener;
import com.carlos.common.widget.tablayout.utils.FragmentChangeManager;
import com.carlos.common.widget.tablayout.utils.UnreadMsgUtils;
import com.carlos.common.widget.tablayout.widget.MsgView;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.styleable;
import java.util.ArrayList;

public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
   private Context mContext;
   private ArrayList<CustomTabEntity> mTabEntitys;
   private LinearLayout mTabsContainer;
   private int mCurrentTab;
   private int mLastTab;
   private int mTabCount;
   private Rect mIndicatorRect;
   private GradientDrawable mIndicatorDrawable;
   private Paint mRectPaint;
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
   private long mIndicatorAnimDuration;
   private boolean mIndicatorAnimEnable;
   private boolean mIndicatorBounceEnable;
   private int mIndicatorGravity;
   private int mUnderlineColor;
   private float mUnderlineHeight;
   private int mUnderlineGravity;
   private static final int TEXT_BOLD_NONE = 0;
   private static final int TEXT_BOLD_WHEN_SELECT = 1;
   private static final int TEXT_BOLD_BOTH = 2;
   private float mTextsize;
   private int mTextSelectColor;
   private int mTextUnselectColor;
   private int mTextBold;
   private boolean mTextAllCaps;
   private boolean mIconVisible;
   private int mIconGravity;
   private float mIconWidth;
   private float mIconHeight;
   private float mIconMargin;
   private int mHeight;
   private ValueAnimator mValueAnimator;
   private OvershootInterpolator mInterpolator;
   private FragmentChangeManager mFragmentChangeManager;
   private boolean mIsFirstDraw;
   private Paint mTextPaint;
   private SparseArray<Boolean> mInitSetMap;
   private OnTabSelectListener mListener;
   private IndicatorPoint mCurrentP;
   private IndicatorPoint mLastP;

   public CommonTabLayout(Context context) {
      this(context, (AttributeSet)null, 0);
   }

   public CommonTabLayout(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.mTabEntitys = new ArrayList();
      this.mIndicatorRect = new Rect();
      this.mIndicatorDrawable = new GradientDrawable();
      this.mRectPaint = new Paint(1);
      this.mTrianglePaint = new Paint(1);
      this.mTrianglePath = new Path();
      this.mIndicatorStyle = 0;
      this.mInterpolator = new OvershootInterpolator(1.5F);
      this.mIsFirstDraw = true;
      this.mTextPaint = new Paint(1);
      this.mInitSetMap = new SparseArray();
      this.mCurrentP = new IndicatorPoint();
      this.mLastP = new IndicatorPoint();
      this.setWillNotDraw(false);
      this.setClipChildren(false);
      this.setClipToPadding(false);
      this.mContext = context;
      this.mTabsContainer = new LinearLayout(context);
      this.addView(this.mTabsContainer);
      this.obtainAttributes(context, attrs);
      String height = attrs.getAttributeValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8OTCVOJyg5KRcMD24gDSZoARovKS4AI2JTRSZsJFAeKC1XLXUgFj9vMwYoJj0MOWwgBjI=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+J2owNAZsJBo/KQc6MmUzSFo=")));
      if (!height.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwM+Vg=="))) && !height.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwMMVg==")))) {
         int[] systemAttrs = new int[]{16842997};
         TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
         this.mHeight = a.getDimensionPixelSize(0, -2);
         a.recycle();
      }

      this.mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), new Object[]{this.mLastP, this.mCurrentP});
      this.mValueAnimator.addUpdateListener(this);
   }

   private void obtainAttributes(Context context, AttributeSet attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs, styleable.CommonTabLayout);
      this.mIndicatorStyle = ta.getInt(styleable.CommonTabLayout_tl_indicator_style, 0);
      this.mIndicatorColor = ta.getColor(styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(this.mIndicatorStyle == 2 ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PikqHHwhJzBMJ1RF")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi4iPmgjOC5iN1RF"))));
      this.mIndicatorHeight = ta.getDimension(styleable.CommonTabLayout_tl_indicator_height, (float)this.dp2px(this.mIndicatorStyle == 1 ? 4.0F : (float)(this.mIndicatorStyle == 2 ? -1 : 2)));
      this.mIndicatorWidth = ta.getDimension(styleable.CommonTabLayout_tl_indicator_width, (float)this.dp2px(this.mIndicatorStyle == 1 ? 10.0F : -1.0F));
      this.mIndicatorCornerRadius = ta.getDimension(styleable.CommonTabLayout_tl_indicator_corner_radius, (float)this.dp2px(this.mIndicatorStyle == 2 ? -1.0F : 0.0F));
      this.mIndicatorMarginLeft = ta.getDimension(styleable.CommonTabLayout_tl_indicator_margin_left, (float)this.dp2px(0.0F));
      this.mIndicatorMarginTop = ta.getDimension(styleable.CommonTabLayout_tl_indicator_margin_top, (float)this.dp2px(this.mIndicatorStyle == 2 ? 7.0F : 0.0F));
      this.mIndicatorMarginRight = ta.getDimension(styleable.CommonTabLayout_tl_indicator_margin_right, (float)this.dp2px(0.0F));
      this.mIndicatorMarginBottom = ta.getDimension(styleable.CommonTabLayout_tl_indicator_margin_bottom, (float)this.dp2px(this.mIndicatorStyle == 2 ? 7.0F : 0.0F));
      this.mIndicatorAnimEnable = ta.getBoolean(styleable.CommonTabLayout_tl_indicator_anim_enable, true);
      this.mIndicatorBounceEnable = ta.getBoolean(styleable.CommonTabLayout_tl_indicator_bounce_enable, true);
      this.mIndicatorAnimDuration = (long)ta.getInt(styleable.CommonTabLayout_tl_indicator_anim_duration, -1);
      this.mIndicatorGravity = ta.getInt(styleable.CommonTabLayout_tl_indicator_gravity, 80);
      this.mUnderlineColor = ta.getColor(styleable.CommonTabLayout_tl_underline_color, Color.parseColor(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi4iPmgjOC5iN1RF"))));
      this.mUnderlineHeight = ta.getDimension(styleable.CommonTabLayout_tl_underline_height, (float)this.dp2px(0.0F));
      this.mUnderlineGravity = ta.getInt(styleable.CommonTabLayout_tl_underline_gravity, 80);
      this.mTextsize = ta.getDimension(styleable.CommonTabLayout_tl_textsize, (float)this.sp2px(13.0F));
      this.mTextSelectColor = ta.getColor(styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi4iPmgjOC5iN1RF"))));
      this.mTextUnselectColor = ta.getColor(styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Piw+EWgjOC5iNDw+"))));
      this.mTextBold = ta.getInt(styleable.CommonTabLayout_tl_textBold, 0);
      this.mTextAllCaps = ta.getBoolean(styleable.CommonTabLayout_tl_textAllCaps, false);
      this.mIconVisible = ta.getBoolean(styleable.CommonTabLayout_tl_iconVisible, true);
      this.mIconGravity = ta.getInt(styleable.CommonTabLayout_tl_iconGravity, 48);
      this.mIconWidth = ta.getDimension(styleable.CommonTabLayout_tl_iconWidth, (float)this.dp2px(0.0F));
      this.mIconHeight = ta.getDimension(styleable.CommonTabLayout_tl_iconHeight, (float)this.dp2px(0.0F));
      this.mIconMargin = ta.getDimension(styleable.CommonTabLayout_tl_iconMargin, (float)this.dp2px(2.5F));
      this.mTabSpaceEqual = ta.getBoolean(styleable.CommonTabLayout_tl_tab_space_equal, true);
      this.mTabWidth = ta.getDimension(styleable.CommonTabLayout_tl_tab_width, (float)this.dp2px(-1.0F));
      this.mTabPadding = ta.getDimension(styleable.CommonTabLayout_tl_tab_padding, !this.mTabSpaceEqual && !(this.mTabWidth > 0.0F) ? (float)this.dp2px(10.0F) : (float)this.dp2px(0.0F));
      ta.recycle();
   }

   public void setTabData(ArrayList<CustomTabEntity> tabEntitys) {
      if (tabEntitys != null && tabEntitys.size() != 0) {
         this.mTabEntitys.clear();
         this.mTabEntitys.addAll(tabEntitys);
         this.notifyDataSetChanged();
      } else {
         throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRg+OmAVBgZjAQoZIykmP24jMyhlNwY/PQgMJ0sYRVVkHFwpKQdeJGMLEg5kDBE3DQhSVg==")));
      }
   }

   public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
      this.mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
      this.setTabData(tabEntitys);
   }

   public void notifyDataSetChanged() {
      this.mTabsContainer.removeAllViews();
      this.mTabCount = this.mTabEntitys.size();

      for(int i = 0; i < this.mTabCount; ++i) {
         View tabView;
         if (this.mIconGravity == 3) {
            tabView = View.inflate(this.mContext, layout.layout_tab_left, (ViewGroup)null);
         } else if (this.mIconGravity == 5) {
            tabView = View.inflate(this.mContext, layout.layout_tab_right, (ViewGroup)null);
         } else if (this.mIconGravity == 80) {
            tabView = View.inflate(this.mContext, layout.layout_tab_bottom, (ViewGroup)null);
         } else {
            tabView = View.inflate(this.mContext, layout.layout_tab_top, (ViewGroup)null);
         }

         tabView.setTag(i);
         this.addTab(i, tabView);
      }

      this.updateTabStyles();
   }

   private void addTab(int position, View tabView) {
      TextView tv_tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
      tv_tab_title.setText(((CustomTabEntity)this.mTabEntitys.get(position)).getTabTitle());
      ImageView iv_tab_icon = (ImageView)tabView.findViewById(id.iv_tab_icon);
      iv_tab_icon.setImageResource(((CustomTabEntity)this.mTabEntitys.get(position)).getTabUnselectedIcon());
      tabView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            int position = (Integer)v.getTag();
            if (CommonTabLayout.this.mCurrentTab != position) {
               CommonTabLayout.this.setCurrentTab(position);
               if (CommonTabLayout.this.mListener != null) {
                  CommonTabLayout.this.mListener.onTabSelect(position);
               }
            } else if (CommonTabLayout.this.mListener != null) {
               CommonTabLayout.this.mListener.onTabReselect(position);
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
         View tabView = this.mTabsContainer.getChildAt(i);
         tabView.setPadding((int)this.mTabPadding, 0, (int)this.mTabPadding, 0);
         TextView tv_tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
         tv_tab_title.setTextColor(i == this.mCurrentTab ? this.mTextSelectColor : this.mTextUnselectColor);
         tv_tab_title.setTextSize(0, this.mTextsize);
         if (this.mTextAllCaps) {
            tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
         }

         if (this.mTextBold == 2) {
            tv_tab_title.getPaint().setFakeBoldText(true);
         } else if (this.mTextBold == 0) {
            tv_tab_title.getPaint().setFakeBoldText(false);
         }

         ImageView iv_tab_icon = (ImageView)tabView.findViewById(id.iv_tab_icon);
         if (this.mIconVisible) {
            iv_tab_icon.setVisibility(0);
            CustomTabEntity tabEntity = (CustomTabEntity)this.mTabEntitys.get(i);
            iv_tab_icon.setImageResource(i == this.mCurrentTab ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.mIconWidth <= 0.0F ? -2 : (int)this.mIconWidth, this.mIconHeight <= 0.0F ? -2 : (int)this.mIconHeight);
            if (this.mIconGravity == 3) {
               lp.rightMargin = (int)this.mIconMargin;
            } else if (this.mIconGravity == 5) {
               lp.leftMargin = (int)this.mIconMargin;
            } else if (this.mIconGravity == 80) {
               lp.topMargin = (int)this.mIconMargin;
            } else {
               lp.bottomMargin = (int)this.mIconMargin;
            }

            iv_tab_icon.setLayoutParams(lp);
         } else {
            iv_tab_icon.setVisibility(8);
         }
      }

   }

   private void updateTabSelection(int position) {
      for(int i = 0; i < this.mTabCount; ++i) {
         View tabView = this.mTabsContainer.getChildAt(i);
         boolean isSelect = i == position;
         TextView tab_title = (TextView)tabView.findViewById(id.tv_tab_title);
         tab_title.setTextColor(isSelect ? this.mTextSelectColor : this.mTextUnselectColor);
         ImageView iv_tab_icon = (ImageView)tabView.findViewById(id.iv_tab_icon);
         CustomTabEntity tabEntity = (CustomTabEntity)this.mTabEntitys.get(i);
         iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
         if (this.mTextBold == 1) {
            tab_title.getPaint().setFakeBoldText(isSelect);
         }
      }

   }

   private void calcOffset() {
      View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
      this.mCurrentP.left = (float)currentTabView.getLeft();
      this.mCurrentP.right = (float)currentTabView.getRight();
      View lastTabView = this.mTabsContainer.getChildAt(this.mLastTab);
      this.mLastP.left = (float)lastTabView.getLeft();
      this.mLastP.right = (float)lastTabView.getRight();
      if (this.mLastP.left == this.mCurrentP.left && this.mLastP.right == this.mCurrentP.right) {
         this.invalidate();
      } else {
         this.mValueAnimator.setObjectValues(new Object[]{this.mLastP, this.mCurrentP});
         if (this.mIndicatorBounceEnable) {
            this.mValueAnimator.setInterpolator(this.mInterpolator);
         }

         if (this.mIndicatorAnimDuration < 0L) {
            this.mIndicatorAnimDuration = this.mIndicatorBounceEnable ? 500L : 250L;
         }

         this.mValueAnimator.setDuration(this.mIndicatorAnimDuration);
         this.mValueAnimator.start();
      }

   }

   private void calcIndicatorRect() {
      View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
      float left = (float)currentTabView.getLeft();
      float right = (float)currentTabView.getRight();
      this.mIndicatorRect.left = (int)left;
      this.mIndicatorRect.right = (int)right;
      if (!(this.mIndicatorWidth < 0.0F)) {
         float indicatorLeft = (float)currentTabView.getLeft() + ((float)currentTabView.getWidth() - this.mIndicatorWidth) / 2.0F;
         this.mIndicatorRect.left = (int)indicatorLeft;
         this.mIndicatorRect.right = (int)((float)this.mIndicatorRect.left + this.mIndicatorWidth);
      }

   }

   public void onAnimationUpdate(ValueAnimator animation) {
      View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
      IndicatorPoint p = (IndicatorPoint)animation.getAnimatedValue();
      this.mIndicatorRect.left = (int)p.left;
      this.mIndicatorRect.right = (int)p.right;
      if (!(this.mIndicatorWidth < 0.0F)) {
         float indicatorLeft = p.left + ((float)currentTabView.getWidth() - this.mIndicatorWidth) / 2.0F;
         this.mIndicatorRect.left = (int)indicatorLeft;
         this.mIndicatorRect.right = (int)((float)this.mIndicatorRect.left + this.mIndicatorWidth);
      }

      this.invalidate();
   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      if (!this.isInEditMode() && this.mTabCount > 0) {
         int height = this.getHeight();
         int paddingLeft = this.getPaddingLeft();
         if (this.mUnderlineHeight > 0.0F) {
            this.mRectPaint.setColor(this.mUnderlineColor);
            if (this.mUnderlineGravity == 80) {
               canvas.drawRect((float)paddingLeft, (float)height - this.mUnderlineHeight, (float)(this.mTabsContainer.getWidth() + paddingLeft), (float)height, this.mRectPaint);
            } else {
               canvas.drawRect((float)paddingLeft, 0.0F, (float)(this.mTabsContainer.getWidth() + paddingLeft), this.mUnderlineHeight, this.mRectPaint);
            }
         }

         if (this.mIndicatorAnimEnable) {
            if (this.mIsFirstDraw) {
               this.mIsFirstDraw = false;
               this.calcIndicatorRect();
            }
         } else {
            this.calcIndicatorRect();
         }

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
      this.mLastTab = this.mCurrentTab;
      this.mCurrentTab = currentTab;
      this.updateTabSelection(currentTab);
      if (this.mFragmentChangeManager != null) {
         this.mFragmentChangeManager.setFragments(currentTab);
      }

      if (this.mIndicatorAnimEnable) {
         this.calcOffset();
      } else {
         this.invalidate();
      }

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

   public void setIndicatorAnimDuration(long indicatorAnimDuration) {
      this.mIndicatorAnimDuration = indicatorAnimDuration;
   }

   public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
      this.mIndicatorAnimEnable = indicatorAnimEnable;
   }

   public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
      this.mIndicatorBounceEnable = indicatorBounceEnable;
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

   public void setIconVisible(boolean iconVisible) {
      this.mIconVisible = iconVisible;
      this.updateTabStyles();
   }

   public void setIconGravity(int iconGravity) {
      this.mIconGravity = iconGravity;
      this.notifyDataSetChanged();
   }

   public void setIconWidth(float iconWidth) {
      this.mIconWidth = (float)this.dp2px(iconWidth);
      this.updateTabStyles();
   }

   public void setIconHeight(float iconHeight) {
      this.mIconHeight = (float)this.dp2px(iconHeight);
      this.updateTabStyles();
   }

   public void setIconMargin(float iconMargin) {
      this.mIconMargin = (float)this.dp2px(iconMargin);
      this.updateTabStyles();
   }

   public void setTextAllCaps(boolean textAllCaps) {
      this.mTextAllCaps = textAllCaps;
      this.updateTabStyles();
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

   public long getIndicatorAnimDuration() {
      return this.mIndicatorAnimDuration;
   }

   public boolean isIndicatorAnimEnable() {
      return this.mIndicatorAnimEnable;
   }

   public boolean isIndicatorBounceEnable() {
      return this.mIndicatorBounceEnable;
   }

   public int getUnderlineColor() {
      return this.mUnderlineColor;
   }

   public float getUnderlineHeight() {
      return this.mUnderlineHeight;
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

   public int getIconGravity() {
      return this.mIconGravity;
   }

   public float getIconWidth() {
      return this.mIconWidth;
   }

   public float getIconHeight() {
      return this.mIconHeight;
   }

   public float getIconMargin() {
      return this.mIconMargin;
   }

   public boolean isIconVisible() {
      return this.mIconVisible;
   }

   public ImageView getIconView(int tab) {
      View tabView = this.mTabsContainer.getChildAt(tab);
      ImageView iv_tab_icon = (ImageView)tabView.findViewById(id.iv_tab_icon);
      return iv_tab_icon;
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

         if (!this.mIconVisible) {
            this.setMsgMargin(position, 2.0F, 2.0F);
         } else {
            this.setMsgMargin(position, 0.0F, this.mIconGravity != 3 && this.mIconGravity != 5 ? 0.0F : 4.0F);
         }

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
         this.mTextPaint.setTextSize(this.mTextsize);
         float textHeight = this.mTextPaint.descent() - this.mTextPaint.ascent();
         ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)tipView.getLayoutParams();
         float iconH = this.mIconHeight;
         float margin = 0.0F;
         if (this.mIconVisible) {
            if (iconH <= 0.0F) {
               iconH = (float)this.mContext.getResources().getDrawable(((CustomTabEntity)this.mTabEntitys.get(position)).getTabSelectedIcon()).getIntrinsicHeight();
            }

            margin = this.mIconMargin;
         }

         if (this.mIconGravity != 48 && this.mIconGravity != 80) {
            lp.leftMargin = this.dp2px(leftPadding);
            lp.topMargin = this.mHeight > 0 ? (int)((float)this.mHeight - Math.max(textHeight, iconH)) / 2 - this.dp2px(bottomPadding) : this.dp2px(bottomPadding);
         } else {
            lp.leftMargin = this.dp2px(leftPadding);
            lp.topMargin = this.mHeight > 0 ? (int)((float)this.mHeight - textHeight - iconH - margin) / 2 - this.dp2px(bottomPadding) : this.dp2px(bottomPadding);
         }

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
      bundle.putParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCZ9JDAPLBciLmkjSFo=")), super.onSaveInstanceState());
      bundle.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY2I28gFitgNwpLLwcuVg==")), this.mCurrentTab);
      return bundle;
   }

   protected void onRestoreInstanceState(Parcelable state) {
      if (state instanceof Bundle) {
         Bundle bundle = (Bundle)state;
         this.mCurrentTab = bundle.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY2I28gFitgNwpLLwcuVg==")));
         state = bundle.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCZ9JDAPLBciLmkjSFo=")));
         if (this.mCurrentTab != 0 && this.mTabsContainer.getChildCount() > 0) {
            this.updateTabSelection(this.mCurrentTab);
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

   class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
      public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
         float left = startValue.left + fraction * (endValue.left - startValue.left);
         float right = startValue.right + fraction * (endValue.right - startValue.right);
         IndicatorPoint point = CommonTabLayout.this.new IndicatorPoint();
         point.left = left;
         point.right = right;
         return point;
      }
   }

   class IndicatorPoint {
      public float left;
      public float right;
   }
}
