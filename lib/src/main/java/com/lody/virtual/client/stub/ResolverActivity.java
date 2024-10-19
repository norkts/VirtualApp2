package com.lody.virtual.client.stub;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PatternMatcher;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.kook.librelease.R.id;
import com.kook.librelease.R.integer;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ResolverActivity extends Activity implements AdapterView.OnItemClickListener {
   private static final String TAG = "ResolverActivity";
   private static final boolean DEBUG = false;
   protected Bundle mOptions;
   protected String mResultWho;
   protected IBinder mResultTo;
   protected int mRequestCode;
   private int mLaunchedFromUid;
   private ResolveListAdapter mAdapter;
   private PackageManager mPm;
   private boolean mAlwaysUseOption;
   private boolean mShowExtended;
   private ListView mListView;
   private Button mAlwaysButton;
   private Button mOnceButton;
   private int mIconDpi;
   private int mIconSize;
   private int mMaxColumns;
   private int mLastSelected = -1;
   private AlertDialog dialog;
   private boolean mRegistered;

   private Intent makeMyIntent() {
      Intent intent = new Intent(this.getIntent());
      intent.setComponent((ComponentName)null);
      intent.setFlags(intent.getFlags() & -8388609);
      return intent;
   }

   @SuppressLint({"MissingSuperCall"})
   protected void onCreate(Bundle savedInstanceState) {
      Intent intent = this.makeMyIntent();
      Set<String> categories = intent.getCategories();
      int titleResource;
      if ("android.intent.action.MAIN".equals(intent.getAction()) && categories != null && categories.size() == 1 && categories.contains("android.intent.category.HOME")) {
         titleResource = string.choose;
      } else {
         titleResource = string.choose;
      }

      int userId = intent.getIntExtra("android.intent.extra.user_handle", VUserHandle.getCallingUserId());
      this.onCreate(savedInstanceState, intent, this.getResources().getText(titleResource), (Intent[])null, (List)null, true, userId);
   }

   protected void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, Intent[] initialIntents, List<ResolveInfo> rList, boolean alwaysUseOption, int userid) {
      super.onCreate(savedInstanceState);
      this.mLaunchedFromUid = userid;
      this.mPm = this.getPackageManager();
      this.mAlwaysUseOption = alwaysUseOption;
      this.mMaxColumns = this.getResources().getInteger(integer.config_maxResolverActivityColumns);
      this.mRegistered = true;
      ActivityManager am = (ActivityManager)this.getSystemService("activity");
      this.mIconDpi = am.getLauncherLargeIconDensity();
      this.mIconSize = am.getLauncherLargeIconSize();
      this.mAdapter = new ResolveListAdapter(this, intent, initialIntents, rList, this.mLaunchedFromUid);
      int count = this.mAdapter.getCount();
      if (VERSION.SDK_INT >= 17 && this.mLaunchedFromUid < 0) {
         this.finish();
      } else if (count == 1) {
         this.startSelected(0, false);
         this.mRegistered = false;
         this.finish();
      } else {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle(title);
         if (count > 1) {
            this.mListView = new ListView(this);
            this.mListView.setAdapter(this.mAdapter);
            this.mListView.setOnItemClickListener(this);
            this.mListView.setOnItemLongClickListener(new ItemLongClickListener());
            builder.setView(this.mListView);
            if (alwaysUseOption) {
               this.mListView.setChoiceMode(1);
            }
         } else {
            builder.setMessage(string.noApplications);
         }

         builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
               ResolverActivity.this.finish();
            }
         });
         this.dialog = builder.show();
      }
   }

   protected void onDestroy() {
      if (this.dialog != null && this.dialog.isShowing()) {
         this.dialog.dismiss();
      }

      super.onDestroy();
   }

   @TargetApi(15)
   Drawable getIcon(Resources res, int resId) {
      Drawable result;
      try {
         result = res.getDrawableForDensity(resId, this.mIconDpi);
      } catch (Resources.NotFoundException var5) {
         result = null;
      }

      return result;
   }

   Drawable loadIconForResolveInfo(ResolveInfo ri) {
      try {
         Drawable dr;
         if (ri.resolvePackageName != null && ri.icon != 0) {
            dr = this.getIcon(this.mPm.getResourcesForApplication(ri.resolvePackageName), ri.icon);
            if (dr != null) {
               return dr;
            }
         }

         int iconRes = ri.getIconResource();
         if (iconRes != 0) {
            dr = this.getIcon(this.mPm.getResourcesForApplication(ri.activityInfo.packageName), iconRes);
            if (dr != null) {
               return dr;
            }
         }
      } catch (PackageManager.NameNotFoundException var4) {
         PackageManager.NameNotFoundException e = var4;
         VLog.e(TAG, "Couldn\'t find resources for package\n" + VLog.getStackTraceString(e));
      }

      return ri.loadIcon(this.mPm);
   }

   protected void onRestart() {
      super.onRestart();
      if (!this.mRegistered) {
         this.mRegistered = true;
      }

      this.mAdapter.handlePackagesChanged();
   }

   protected void onStop() {
      super.onStop();
      if (this.mRegistered) {
         this.mRegistered = false;
      }

      if ((this.getIntent().getFlags() & 268435456) != 0 && !this.isChangingConfigurations()) {
         this.finish();
      }

   }

   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      if (this.mAlwaysUseOption) {
         int checkedPos = this.mListView.getCheckedItemPosition();
         boolean enabled = checkedPos != -1;
         this.mLastSelected = checkedPos;
         this.mAlwaysButton.setEnabled(enabled);
         this.mOnceButton.setEnabled(enabled);
         if (enabled) {
            this.mListView.setSelection(checkedPos);
         }
      }

   }

   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      int checkedPos = this.mListView.getCheckedItemPosition();
      boolean hasValidSelection = checkedPos != -1;
      if (!this.mAlwaysUseOption || hasValidSelection && this.mLastSelected == checkedPos) {
         this.startSelected(position, false);
      } else {
         this.mAlwaysButton.setEnabled(hasValidSelection);
         this.mOnceButton.setEnabled(hasValidSelection);
         if (hasValidSelection) {
            this.mListView.smoothScrollToPosition(checkedPos);
         }

         this.mLastSelected = checkedPos;
      }

   }

   void startSelected(int which, boolean always) {
      if (!this.isFinishing()) {
         ResolveInfo ri = this.mAdapter.resolveInfoForPosition(which);
         Intent intent = this.mAdapter.intentForPosition(which);
         this.onIntentSelected(ri, intent, always);
         this.finish();
      }
   }

   protected void onIntentSelected(ResolveInfo ri, Intent intent, boolean alwaysCheck) {
      if (this.mAlwaysUseOption && this.mAdapter.mOrigResolveList != null) {
         IntentFilter filter = new IntentFilter();
         if (intent.getAction() != null) {
            filter.addAction(intent.getAction());
         }

         Set<String> categories = intent.getCategories();
         if (categories != null) {
            Iterator var6 = categories.iterator();

            while(var6.hasNext()) {
               String cat = (String)var6.next();
               filter.addCategory(cat);
            }
         }

         filter.addCategory("android.intent.category.DEFAULT");
         int cat = ri.match & 268369920;
         Uri data = intent.getData();
         if (cat == 6291456) {
            String mimeType = intent.resolveType(this);
            if (mimeType != null) {
               try {
                  filter.addDataType(mimeType);
               } catch (IntentFilter.MalformedMimeTypeException var14) {
                  IntentFilter.MalformedMimeTypeException e = var14;
                  VLog.w(TAG, "mimeType\n" + VLog.getStackTraceString(e));
                  filter = null;
               }
            }
         }

         int port;
         if (data != null && data.getScheme() != null && (cat != 6291456 || !"file".equals(data.getScheme()) && !"content".equals(data.getScheme()))) {
            filter.addDataScheme(data.getScheme());
            if (VERSION.SDK_INT >= 19) {
               Iterator<PatternMatcher> pIt = ri.filter.schemeSpecificPartsIterator();
               if (pIt != null) {
                  String ssp = data.getSchemeSpecificPart();

                  while(ssp != null && pIt.hasNext()) {
                     PatternMatcher p = (PatternMatcher)pIt.next();
                     if (p.match(ssp)) {
                        filter.addDataSchemeSpecificPart(p.getPath(), p.getType());
                        break;
                     }
                  }
               }

               Iterator<IntentFilter.AuthorityEntry> aIt = ri.filter.authoritiesIterator();
               if (aIt != null) {
                  while(aIt.hasNext()) {
                     IntentFilter.AuthorityEntry a = (IntentFilter.AuthorityEntry)aIt.next();
                     if (a.match(data) >= 0) {
                        port = a.getPort();
                        filter.addDataAuthority(a.getHost(), port >= 0 ? Integer.toString(port) : null);
                        break;
                     }
                  }
               }

               pIt = ri.filter.pathsIterator();
               if (pIt != null) {
                  String path = data.getPath();

                  while(path != null && pIt.hasNext()) {
                     PatternMatcher p = (PatternMatcher)pIt.next();
                     if (p.match(path)) {
                        filter.addDataPath(p.getPath(), p.getType());
                        break;
                     }
                  }
               }
            }
         }

         if (filter != null) {
            int N = this.mAdapter.mOrigResolveList.size();
            ComponentName[] set = new ComponentName[N];
            int bestMatch = 0;

            for(port = 0; port < N; ++port) {
               ResolveInfo r = (ResolveInfo)this.mAdapter.mOrigResolveList.get(port);
               set[port] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
               if (r.match > bestMatch) {
                  bestMatch = r.match;
               }
            }

            if (alwaysCheck) {
               this.getPackageManager().addPreferredActivity(filter, bestMatch, set, intent.getComponent());
            } else {
               try {
                  Reflect.on((Object)VClient.get().getCurrentApplication().getPackageManager()).call("setLastChosenActivity", intent, intent.resolveTypeIfNeeded(this.getContentResolver()), 65536, filter, bestMatch, intent.getComponent());
               } catch (Exception var13) {
                  VLog.d(TAG, "Error calling setLastChosenActivity\n" + VLog.getStackTraceString(var13));
               }
            }
         }
      }

      if (intent != null) {
         intent = ComponentUtils.processOutsideIntent(this.mLaunchedFromUid, VirtualCore.get().isExtPackage(), new Intent(intent));
         ActivityInfo info = VirtualCore.get().resolveActivityInfo(intent, this.mLaunchedFromUid);
         if (info == null) {
            this.startActivity(intent);
         } else {
            intent.addFlags(33554432);
            int res = VActivityManager.get().startActivity(intent, info, this.mResultTo, this.mOptions, this.mResultWho, this.mRequestCode, (String)null, this.mLaunchedFromUid);
            if (res != 0 && this.mResultTo != null && this.mRequestCode > 0) {
               VActivityManager.get().sendCancelActivityResult(this.mResultTo, this.mResultWho, this.mRequestCode);
            }
         }
      }

   }

   void showAppDetails(ResolveInfo ri) {
      Intent in = (new Intent()).setAction("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", ri.activityInfo.packageName, (String)null)).addFlags(524288);
      this.startActivity(in);
   }

   class LoadIconTask extends AsyncTask<DisplayResolveInfo, Void, DisplayResolveInfo> {
      protected DisplayResolveInfo doInBackground(DisplayResolveInfo... params) {
         DisplayResolveInfo info = params[0];
         if (info.displayIcon == null) {
            info.displayIcon = ResolverActivity.this.loadIconForResolveInfo(info.ri);
         }

         return info;
      }

      protected void onPostExecute(DisplayResolveInfo info) {
         ResolverActivity.this.mAdapter.notifyDataSetChanged();
      }
   }

   class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
         ResolveInfo ri = ResolverActivity.this.mAdapter.resolveInfoForPosition(position);
         ResolverActivity.this.showAppDetails(ri);
         return true;
      }
   }

   private final class ResolveListAdapter extends BaseAdapter {
      private final Intent[] mInitialIntents;
      private final List<ResolveInfo> mBaseResolveList;
      private final Intent mIntent;
      private final int mLaunchedFromUid;
      private final LayoutInflater mInflater;
      List<DisplayResolveInfo> mList;
      List<ResolveInfo> mOrigResolveList;
      private ResolveInfo mLastChosen;
      private int mInitialHighlight = -1;

      public ResolveListAdapter(Context context, Intent intent, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid) {
         this.mIntent = new Intent(intent);
         this.mInitialIntents = initialIntents;
         this.mBaseResolveList = rList;
         this.mLaunchedFromUid = launchedFromUid;
         this.mInflater = (LayoutInflater)context.getSystemService("layout_inflater");
         this.mList = new ArrayList();
         this.rebuildList();
      }

      public void handlePackagesChanged() {
         int oldItemCount = this.getCount();
         this.rebuildList();
         this.notifyDataSetChanged();
         int newItemCount = this.getCount();
         if (newItemCount == 0) {
            ResolverActivity.this.finish();
         }

      }

      public int getInitialHighlight() {
         return this.mInitialHighlight;
      }

      private void rebuildList() {
         this.mList.clear();
         List currentResolveList;
         if (this.mBaseResolveList != null) {
            currentResolveList = this.mBaseResolveList;
            this.mOrigResolveList = null;
         } else {
            currentResolveList = this.mOrigResolveList = ResolverActivity.this.mPm.queryIntentActivities(this.mIntent, 65536 | (ResolverActivity.this.mAlwaysUseOption ? 64 : 0));
         }

         int N;
         if (currentResolveList != null && (N = currentResolveList.size()) > 0) {
            ResolveInfo r0 = (ResolveInfo)currentResolveList.get(0);

            int start;
            for(start = 1; start < N; ++start) {
               ResolveInfo ri = (ResolveInfo)currentResolveList.get(start);
               if (r0.priority != ri.priority || r0.isDefault != ri.isDefault) {
                  while(start < N) {
                     if (this.mOrigResolveList == currentResolveList) {
                        this.mOrigResolveList = new ArrayList(this.mOrigResolveList);
                     }

                     currentResolveList.remove(start);
                     --N;
                  }
               }
            }

            if (N > 1) {
               ResolveInfo.DisplayNameComparator rComparator = new ResolveInfo.DisplayNameComparator(ResolverActivity.this.mPm);
               Collections.sort(currentResolveList, rComparator);
            }

            ResolveInfo rix;
            if (this.mInitialIntents != null) {
               for(start = 0; start < this.mInitialIntents.length; ++start) {
                  Intent ii = this.mInitialIntents[start];
                  if (ii != null) {
                     ActivityInfo ai = ii.resolveActivityInfo(ResolverActivity.this.getPackageManager(), 0);
                     if (ai == null) {
                        VLog.w("ResolverActivity", "No activity found for " + ii);
                     } else {
                        rix = new ResolveInfo();
                        rix.activityInfo = ai;
                        if (ii instanceof LabeledIntent) {
                           LabeledIntent li = (LabeledIntent)ii;
                           rix.resolvePackageName = li.getSourcePackage();
                           rix.labelRes = li.getLabelResource();
                           rix.nonLocalizedLabel = li.getNonLocalizedLabel();
                           rix.icon = li.getIconResource();
                        }

                        this.mList.add(ResolverActivity.this.new DisplayResolveInfo(rix, rix.loadLabel(ResolverActivity.this.getPackageManager()), (CharSequence)null, ii));
                     }
                  }
               }
            }

            r0 = (ResolveInfo)currentResolveList.get(0);
            start = 0;
            CharSequence r0Label = r0.loadLabel(ResolverActivity.this.mPm);
            ResolverActivity.this.mShowExtended = false;

            for(int i = 1; i < N; ++i) {
               if (r0Label == null) {
                  r0Label = r0.activityInfo.packageName;
               }

               rix = (ResolveInfo)currentResolveList.get(i);
               CharSequence riLabel = rix.loadLabel(ResolverActivity.this.mPm);
               if (riLabel == null) {
                  riLabel = rix.activityInfo.packageName;
               }

               if (!riLabel.equals(r0Label)) {
                  this.processGroup(currentResolveList, start, i - 1, r0, (CharSequence)r0Label);
                  r0 = rix;
                  r0Label = riLabel;
                  start = i;
               }
            }

            this.processGroup(currentResolveList, start, N - 1, r0, (CharSequence)r0Label);
         }

      }

      private void processGroup(List<ResolveInfo> rList, int start, int end, ResolveInfo ro, CharSequence roLabel) {
         int num = end - start + 1;
         if (num == 1) {
            if (this.mLastChosen != null && this.mLastChosen.activityInfo.packageName.equals(ro.activityInfo.packageName) && this.mLastChosen.activityInfo.name.equals(ro.activityInfo.name)) {
               this.mInitialHighlight = this.mList.size();
            }

            this.mList.add(ResolverActivity.this.new DisplayResolveInfo(ro, roLabel, (CharSequence)null, (Intent)null));
         } else {
            ResolverActivity.this.mShowExtended = true;
            boolean usePkg = false;
            CharSequence startApp = ro.activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
            if (startApp == null) {
               usePkg = true;
            }

            if (!usePkg) {
               HashSet<CharSequence> duplicates = new HashSet();
               duplicates.add(startApp);

               for(int j = start + 1; j <= end; ++j) {
                  ResolveInfo jRi = (ResolveInfo)rList.get(j);
                  CharSequence jApp = jRi.activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm);
                  if (jApp == null || duplicates.contains(jApp)) {
                     usePkg = true;
                     break;
                  }

                  duplicates.add(jApp);
               }

               duplicates.clear();
            }

            for(int k = start; k <= end; ++k) {
               ResolveInfo add = (ResolveInfo)rList.get(k);
               if (this.mLastChosen != null && this.mLastChosen.activityInfo.packageName.equals(add.activityInfo.packageName) && this.mLastChosen.activityInfo.name.equals(add.activityInfo.name)) {
                  this.mInitialHighlight = this.mList.size();
               }

               if (usePkg) {
                  this.mList.add(ResolverActivity.this.new DisplayResolveInfo(add, roLabel, add.activityInfo.packageName, (Intent)null));
               } else {
                  this.mList.add(ResolverActivity.this.new DisplayResolveInfo(add, roLabel, add.activityInfo.applicationInfo.loadLabel(ResolverActivity.this.mPm), (Intent)null));
               }
            }
         }

      }

      public ResolveInfo resolveInfoForPosition(int position) {
         return ((DisplayResolveInfo)this.mList.get(position)).ri;
      }

      public Intent intentForPosition(int position) {
         DisplayResolveInfo dri = (DisplayResolveInfo)this.mList.get(position);
         Intent intent = new Intent(dri.origIntent != null ? dri.origIntent : this.mIntent);
         intent.addFlags(50331648);
         ActivityInfo ai = dri.ri.activityInfo;
         intent.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
         return intent;
      }

      public int getCount() {
         return this.mList.size();
      }

      public Object getItem(int position) {
         return this.mList.get(position);
      }

      public long getItemId(int position) {
         return (long)position;
      }

      public View getView(int position, View convertView, ViewGroup parent) {
         View view;
         if (convertView == null) {
            view = this.mInflater.inflate(layout.resolve_list_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            ViewGroup.LayoutParams lp = holder.icon.getLayoutParams();
            lp.width = lp.height = ResolverActivity.this.mIconSize;
         } else {
            view = convertView;
         }

         this.bindView(view, (DisplayResolveInfo)this.mList.get(position));
         return view;
      }

      private final void bindView(View view, DisplayResolveInfo info) {
         ViewHolder holder = (ViewHolder)view.getTag();
         holder.text.setText(info.displayLabel);
         if (ResolverActivity.this.mShowExtended) {
            holder.text2.setVisibility(0);
            holder.text2.setText(info.extendedInfo);
         } else {
            holder.text2.setVisibility(8);
         }

         if (info.displayIcon == null) {
            (ResolverActivity.this.new LoadIconTask()).execute(new DisplayResolveInfo[]{info});
         }

         holder.icon.setImageDrawable(info.displayIcon);
      }
   }

   private final class DisplayResolveInfo {
      ResolveInfo ri;
      CharSequence displayLabel;
      Drawable displayIcon;
      CharSequence extendedInfo;
      Intent origIntent;

      DisplayResolveInfo(ResolveInfo pri, CharSequence pLabel, CharSequence pInfo, Intent pOrigIntent) {
         this.ri = pri;
         this.displayLabel = pLabel;
         this.extendedInfo = pInfo;
         this.origIntent = pOrigIntent;
      }
   }

   static class ViewHolder {
      public TextView text;
      public TextView text2;
      public ImageView icon;

      public ViewHolder(View view) {
         this.text = (TextView)view.findViewById(id.text1);
         this.text2 = (TextView)view.findViewById(id.text2);
         this.icon = (ImageView)view.findViewById(id.icon);
      }
   }
}
