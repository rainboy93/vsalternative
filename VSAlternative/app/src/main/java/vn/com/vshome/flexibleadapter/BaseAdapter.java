package vn.com.vshome.flexibleadapter;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class BaseAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

	private static final String TAG = BaseAdapter.class.getSimpleName();

	public static final int CHILD_VIEW_TYPE = 0;
	public static final int EXAMPLE_VIEW_TYPE = 1;

	private AbstractFlexibleItem mUseCaseItem;
	private int positionOld = -1;

	public BaseAdapter(List<AbstractFlexibleItem> listItem) {
		super(listItem);
		setNotifyChangeOfUnfilteredItems(true);
	}

	@Override
	public void updateDataSet(List<AbstractFlexibleItem> items, boolean animate) {
		super.updateDataSet(items, animate);
	}


	@Override
	public synchronized void filterItems(@NonNull List<AbstractFlexibleItem> unfilteredItems) {
		super.filterItems(unfilteredItems);
	}

	@Override
	public void selectAll(Integer... viewTypes) {
		super.selectAll();
	}

	@Override
	public List<Animator> getAnimators(View itemView, int position, boolean isSelected) {
		List<Animator> animators = new ArrayList<Animator>();
		if (isSelected)
			addSlideInFromRightAnimator(animators, itemView, 0.5f);
		else
			addSlideInFromLeftAnimator(animators, itemView, 0.5f);

		if (position != positionOld)
			positionOld = position;

		return animators;
	}

	@Override
	public String onCreateBubbleText(int position) {
		return super.onCreateBubbleText(position);
	}
}