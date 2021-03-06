package belp.base.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import belp.base.R
import belp.base.databinding.ItemLoadMoreBinding

/**
 * Created by vophamtuananh on 1/7/18.
 */
class LoadMoreRecyclerView : RecyclerView {

    companion object {
        private const val NUMBER_TO_LOAD_MORE_DEFAULT = 1
    }

    private var mAdapterWrapper: AdapterWrapper? = null

    private var isLoading: Boolean = false

    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    private var mNumberToLoadMore = 1

    private var mNoMore: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreRecyclerView, defStyle, 0)

        mNumberToLoadMore = a.getInt(R.styleable.LoadMoreRecyclerView_number_to_load_more, NUMBER_TO_LOAD_MORE_DEFAULT)

        a.recycle()
    }

    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (mNoMore)
                return

            val layoutManager = layoutManager

            val totalItemCount = layoutManager!!.itemCount

            val lastVisibleItemPosition = when (layoutManager) {
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is StaggeredGridLayoutManager -> {
                    val into = IntArray(layoutManager.spanCount)
                    layoutManager.findLastVisibleItemPositions(into)
                    findMax(into)
                }
                else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            if (!isLoading && totalItemCount <= lastVisibleItemPosition + mNumberToLoadMore) {
                mOnLoadMoreListener?.onLoadMore()
                isLoading = true
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnScrollListener(mOnScrollListener)
    }

    override fun onDetachedFromWindow() {
        mOnLoadMoreListener = null
        removeOnScrollListener(mOnScrollListener)
        super.onDetachedFromWindow()
    }

    override fun setLayoutManager(layout: RecyclerView.LayoutManager?) {
        super.setLayoutManager(layout)
        if (layout is GridLayoutManager) {
            layout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mAdapterWrapper != null && mAdapterWrapper!!.isLoadMoreView(position)) {
                        layout.spanCount
                    } else 1
                }
            }
        }
    }

    private fun findMax(lastPositions: IntArray): Int {
        return lastPositions.max() ?: lastPositions[0]
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener
    }

    fun setLoading(loading: Boolean) {
        isLoading = loading
    }

    fun setNoMore(noMore: Boolean) {
        mNoMore = noMore
        mAdapterWrapper?.setNoMore(noMore)
    }

    fun <R : RecyclerAdapter<RecyclerAdapter.BaseHolder<*, Any>, Any>> setAdapter(adapter: R) {
        mAdapterWrapper = AdapterWrapper(adapter)
        val dataObserver = DataObserver(mAdapterWrapper!!)
        super.setAdapter(mAdapterWrapper)
        adapter.registerDataObserver(dataObserver)
    }

    private class AdapterWrapper(private val mAdapter: RecyclerAdapter<BaseHolder<*, Any>, Any>) :
        RecyclerAdapter<RecyclerAdapter.BaseHolder<*, Any>, Any>() {

        companion object {
            private const val LOAD_MORE_TYPE = 100
        }

        private var mNoMore = false

        override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<*, Any>? {
            return null
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<*, Any> {
            if (viewType == LOAD_MORE_TYPE) {
                val itemLoadMoreBinding = ItemLoadMoreBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
                return LoadMoreHolder(itemLoadMoreBinding)
            }
            return mAdapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: BaseHolder<*, Any>, position: Int) {
            if (!mNoMore && position == mAdapter.itemCount)
                return
            mAdapter.onBindViewHolder(holder, position)
        }

        override fun getItemCount(): Int {
            if (mAdapter.itemCount > 0) {
                val additional = if (mNoMore) 0 else 1
                return mAdapter.itemCount + additional
            }
            return 0
        }


        override fun getItemViewType(position: Int): Int {
            return if (position == mAdapter.itemCount) LOAD_MORE_TYPE else mAdapter.getItemViewType(position)
        }

        internal fun setNoMore(noMore: Boolean) {
            if (mNoMore == noMore)
                return
            mNoMore = noMore
            if (mNoMore) {
                notifyItemRemoved(mAdapter.itemCount + 1)
            } else {
                notifyItemInserted(mAdapter.itemCount + 1)
            }
        }

        internal fun isLoadMoreView(position: Int): Boolean {
            return getItemViewType(position) == LOAD_MORE_TYPE
        }

        private class LoadMoreHolder<in T>(viewDataBinding: ItemLoadMoreBinding) :
            BaseHolder<ItemLoadMoreBinding, T>(viewDataBinding)

    }

    private class DataObserver(private val mAdapterWrapper: AdapterWrapper) : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            mAdapterWrapper.notifyDataSetChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (mAdapterWrapper.itemCount == 0) {
                mAdapterWrapper.notifyDataSetChanged()
                return
            }
            mAdapterWrapper.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (mAdapterWrapper.itemCount == 0) {
                mAdapterWrapper.notifyDataSetChanged()
                return
            }
            mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (mAdapterWrapper.itemCount == 0) {
                mAdapterWrapper.notifyDataSetChanged()
                return
            }
            mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (mAdapterWrapper.itemCount == 0) {
                mAdapterWrapper.notifyDataSetChanged()
                return
            }
            mAdapterWrapper.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (mAdapterWrapper.itemCount == 0) {
                mAdapterWrapper.notifyDataSetChanged()
                return
            }
            mAdapterWrapper.notifyItemMoved(fromPosition, toPosition)
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}