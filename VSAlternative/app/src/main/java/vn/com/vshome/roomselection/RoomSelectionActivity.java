package vn.com.vshome.roomselection;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.roomselection.FloorItem;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.NonSwipeViewPager;


public class RoomSelectionActivity extends BaseActivity implements View.OnClickListener, FlexibleAdapter.OnItemClickListener {

    private RoomSelectionAdapter mAdapter;
    private NonSwipeViewPager mViewPager;
    private ImageButton mBack, mHome;
    private RecyclerView mRecyclerView;
    private BaseAdapter mFloorAdapter;
    private List<AbstractFlexibleItem> mListFloor;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_selection);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setText(Utils.getString(R.string.title_lighting));

        mBack = (ImageButton) findViewById(R.id.action_bar_menu);
        mBack.setImageResource(R.drawable.icon_back);
        mBack.setOnClickListener(this);

        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mHome.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.room_selection_recycler_view);
        mListFloor = DatabaseService.getListFloorSelection();
        mFloorAdapter = new BaseAdapter(mListFloor);
        mFloorAdapter.initializeListeners(this);
        mFloorAdapter.setDisplayHeadersAtStartUp(true)
                .setAutoCollapseOnExpand(true)
                .setAutoScrollOnExpand(true)
                .setRemoveOrphanHeaders(false)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        mFloorAdapter.enableStickyHeaders();
        SmoothScrollLinearLayoutManager layoutManager = new SmoothScrollLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFloorAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });

        mAdapter = new RoomSelectionAdapter(getSupportFragmentManager());
        mViewPager = (NonSwipeViewPager) findViewById(R.id.room_selection_view_pager);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack || v == mHome) {
            onBackPressed();
        }
    }

    private int currentPosition = 0;

    @Override
    public boolean onItemClick(int position) {
        if (position > mAdapter.getCount() - 1) {
            return true;
        }
        if (position != currentPosition) {
            ((FloorItem) mListFloor.get(currentPosition)).isSelected = false;
            ((FloorItem) mListFloor.get(position)).isSelected = true;
            mFloorAdapter.notifyItemChanged(currentPosition);
            mFloorAdapter.notifyItemChanged(position);
            currentPosition = position;
        }

        mViewPager.setCurrentItem(position);
        return true;
    }
}
