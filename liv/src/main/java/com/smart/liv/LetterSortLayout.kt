package com.smart.liv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.liv.adapter.LetterSortAdapter
import com.smart.liv.adapter.OnItemClickListener
import com.smart.liv.adapter.ViewHolderListener
import com.smart.liv.comparator.SortComparator
import com.smart.liv.const.Constants
import com.smart.liv.model.LetterSortModel
import com.smart.liv.view.LetterIndexView

/**
 * @author JoeYe
 * @date 2023/3/8 11:26
 */
class LetterSortLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var rvContent: RecyclerView? = null
    private var livBar: LetterIndexView? = null
    private val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private val sortComparator: SortComparator = SortComparator()

    var sortItemClickListener: OnSortItemClickListener? = null
        set(value) {
            field = value
            letterSortAdapter.onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (letterSortAdapter.models != null &&
                        letterSortAdapter.itemCount > position
                    ) {
                        value?.onSortItemClick(position)
                    }
                }
            }
        }

    var headTypeLayoutId: Int = R.layout.item_letter_head
        set(value) {
            letterSortAdapter.headTypeLayoutId = value
            field = value
        }

    var contentTypeLayoutId: Int = R.layout.item_letter_content
        set(value) {
            letterSortAdapter.contentTypeLayoutId = value
            field = value
        }

    /**
     * 设置页面数据，需要传入处理后的数据
     */
    var dataList: List<LetterSortModel>? = null
        set(value) {
            field = value
            // 过滤传入的字符列表字母类型，更新到控件中
            value?.filter {
                it.type == Constants.TYPE_LETTER
            }?.map { it.word }
                .let {
                    if (it != null) {
                        livBar!!.letters = it.toTypedArray()
                    }
                }
            // 给进来时sorted后并且 插入完字母头 方便外部缓存
            letterSortAdapter.models = value
        }

    var letterSortAdapter: LetterSortAdapter = LetterSortAdapter()
        set(value) {
            field = value
            rvContent!!.adapter = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_letter_sort, this)
            .let {
                rvContent = it.findViewById(R.id.rv_content)
                livBar = it.findViewById(R.id.liv_bar)
            }

        rvContent!!.let {
            it.layoutManager = layoutManager
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    /**
                     * 滑动时需要手动设置setCurrentLetter更新LetterIndexView的选中字母UI。
                     * 但是需要判断当前如果已经显示到最后一项了，就无需再主动触发setCurrentLetter。
                     * 否则会出现点击右侧栏选中较后的字母时，由于layoutManager.findFirstVisibleItemPosition拿到的会是屏幕显示
                     * 的第一个，所以会出现字母往跳回的情况。
                     */
                    val lastChildView = layoutManager.getChildAt(layoutManager.childCount - 1)
                    val lastChildBottom = lastChildView?.bottom
                    val recyclerViewBottom = it.bottom - it.paddingBottom
                    val lastPosition = layoutManager.getPosition(lastChildView as View)

                    if(lastChildBottom != recyclerViewBottom || lastPosition != layoutManager.itemCount - 1){
                        letterSortAdapter.models?.get(layoutManager.findFirstVisibleItemPosition())!!.word.let { word ->
                            livBar!!.setCurrentLetter(word)
                        }
                    }
                }

            })
            it.adapter = letterSortAdapter
        }

        letterSortAdapter.registerListener(Constants.TYPE_NORMAL, listener = object :
            ViewHolderListener {
            override fun onBind(
                position: Int,
                letterSortViewHolder: LetterSortAdapter.LetterSortViewHolder
            ) {
                letterSortViewHolder.itemView.findViewById<TextView>(R.id.tv_name)
                    .text = letterSortAdapter.models?.get(position)!!.name
            }
        })

        letterSortAdapter.registerListener(Constants.TYPE_LETTER, listener = object :
            ViewHolderListener {
            override fun onBind(
                position: Int,
                letterSortViewHolder: LetterSortAdapter.LetterSortViewHolder
            ) {
                letterSortViewHolder.itemView.findViewById<TextView>(R.id.tv_name)
                    .text = letterSortAdapter.models?.get(position)!!.word
            }
        })

        livBar!!.letterChangedListener = object : LetterIndexView.LetterChangedListener {
            override fun onLetterSelected(letter: String) {

            }

            override fun onLetterChanged(letter: String) {
                letterSortAdapter.models!!.firstOrNull() {
                    it.word == letter
                }.let { model ->
                    if(model != null){
                        layoutManager.scrollToPositionWithOffset(letterSortAdapter.models!!.indexOf(model), 0)
                    }
                }
            }

        }
    }

    interface OnSortItemClickListener{
        fun onSortItemClick(position: Int)
    }

}