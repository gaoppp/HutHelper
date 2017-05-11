package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelper.R;
import com.gaop.huthelper.model.rxbus.RxBus;
import com.gaop.huthelper.model.rxbus.event.MainEvent;
import com.gaop.huthelper.view.adapter.MenuRVAdapter;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelper.view.CustomGirdLayoutManager;
import com.gaop.huthelper.view.CustomItemTouchHelper;
import com.gaop.huthelperdao.Menu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 高沛 on 2017/4/10.
 */

public class AllActivity extends BaseActivity {

    @BindView(R.id.imgbtn_toolbar_back)
    ImageButton imgbtnToolbarBack;
    @BindView(R.id.tv_al_msg)
    TextView tvAlMsg;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_edit)
    TextView tvToolbarEdit;
    @BindView(R.id.rv_all_main)
    RecyclerView rvAllMain;
    @BindView(R.id.rv_all_other)
    RecyclerView rvAllOther;


    private List<Menu> mainMenuItemList;
    private List<Menu> otherMenuItemList;

    MenuRVAdapter adapterMain, adapterOther;

    private boolean isEdit;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_all;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("全部应用");
        MApplication.getDaoSession(mContext).clear();
        mainMenuItemList = DBHelper.getMenuInMainSortByIndex();
        otherMenuItemList = DBHelper.getMenuNotInMain();

        adapterMain = new MenuRVAdapter(mContext, mainMenuItemList);
        adapterMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit)
                    return;
                Menu item = mainMenuItemList.get(position);
                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", item.getMsg());
                    startActivity(Class.forName(item.getPath()), bundle);
                } catch (ClassNotFoundException e) {
                    ToastUtil.showToastShort("找不到该页面~");
                    e.printStackTrace();
                }
            }
        });
        rvAllMain.setLayoutManager(new CustomGirdLayoutManager(this, 4));

        ItemTouchHelper.Callback callback =
                new CustomItemTouchHelper(adapterMain);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvAllMain);
        rvAllMain.setAdapter(adapterMain);

        adapterOther = new MenuRVAdapter(mContext, otherMenuItemList);
        adapterOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit)
                    return;
                Menu item = otherMenuItemList.get(position);
                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", item.getMsg());
                    startActivity(Class.forName(item.getPath()), bundle);
                } catch (ClassNotFoundException e) {
                    ToastUtil.showToastShort("找不到该页面~");
                    e.printStackTrace();
                }
            }
        });
        rvAllOther.setLayoutManager(new CustomGirdLayoutManager(this, 4));
        rvAllOther.setAdapter(adapterOther);

        adapterMain.setOnEditClickListenter(new MenuRVAdapter.EditClickLister() {
            @Override
            public void onEditClick(View v, int postion) {
                Menu item = mainMenuItemList.remove(postion);
                item.setIsMain(false);
                otherMenuItemList.add(otherMenuItemList.size(), item);
                adapterMain.notifyItemRemoved(postion);
                adapterOther.notifyItemInserted(otherMenuItemList.size());
            }
        });
        adapterOther.setOnEditClickListenter(new MenuRVAdapter.EditClickLister() {
            @Override
            public void onEditClick(View v, int postion) {
                if (mainMenuItemList.size() >= 11) {
                    ToastUtil.showToastShort("主页面最多放置11个应用~");
                    return;
                }
                Menu item = otherMenuItemList.remove(postion);
                item.setIsMain(true);
                mainMenuItemList.add(mainMenuItemList.size(), item);
                adapterOther.notifyItemRemoved(postion);
                adapterMain.notifyItemInserted(mainMenuItemList.size());
            }
        });
    }


    @OnClick({R.id.imgbtn_toolbar_back, R.id.tv_toolbar_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_toolbar_back:
                mainMenuItemList.clear();
                otherMenuItemList.clear();
                finish();
                break;
            case R.id.tv_toolbar_edit:
                isEdit = !isEdit;
                adapterMain.setEdit(isEdit);
                adapterOther.setEdit(isEdit);
                if (isEdit) {
                    tvToolbarEdit.setText("完成");
                    tvAlMsg.setVisibility(View.VISIBLE);
                } else {
                    tvToolbarEdit.setText("编辑");
                    tvAlMsg.setVisibility(View.GONE);
                    saveChange();
                }
                break;
        }
    }

    private void saveChange() {
        DBHelper.deleteAllMenu();
        int index = 0;
        for (Menu m : mainMenuItemList) {
            m.setIndex(index++);
        }
        DBHelper.insertListMenu(mainMenuItemList);
        DBHelper.insertListMenu(otherMenuItemList);
        RxBus.getInstance().send(new MainEvent(1));
        ToastUtil.showToastShort("保存成功~");
    }


}
