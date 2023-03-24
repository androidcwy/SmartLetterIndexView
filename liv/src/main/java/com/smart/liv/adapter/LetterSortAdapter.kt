package com.smart.liv.adapter

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.keyIterator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.smart.liv.R
import com.smart.liv.const.Constants
import com.smart.liv.model.LetterSortModel
import com.smart.liv.utils.LimitFastClickListener

/**
 * @author JoeYe
 * @date 2023/3/8 11:28
 */
class LetterSortAdapter: Adapter<LetterSortAdapter.LetterSortViewHolder>() {

    var viewHolderListeners: SparseArray<ViewHolderListener> = SparseArray()

    var onItemClickListener: OnItemClickListener? = null

    private var _data: MutableList<LetterSortModel>? = null

    var headTypeLayoutId: Int = R.layout.item_letter_head
    var contentTypeLayoutId: Int = R.layout.item_letter_content

    var models: List<LetterSortModel>?
        get() = _data
        @SuppressLint("NotifyDataSetChanged") set(value) {
            if (value != null) {
                _data = value.toMutableList()
            }
            notifyDataSetChanged()
        }

    private val modelCount: Int
        get() {
            return if (_data == null) 0 else _data!!.size
        }

    fun registerListener(viewType: Int, listener: ViewHolderListener){
        viewHolderListeners[viewType] = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterSortViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            if(viewType == Constants.TYPE_LETTER) headTypeLayoutId else contentTypeLayoutId, parent, false)
        return LetterSortViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LetterSortViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return _data!![position].type
    }

    override fun getItemCount(): Int {
        return modelCount
    }

    inner class LetterSortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bind(position: Int) {
            itemView.setOnClickListener(object : LimitFastClickListener() {
                override fun onSafeLimitClick(v: View?) {
                    onItemClickListener?.onItemClick(adapterPosition)
                }
            })
            viewHolderListeners.keyIterator().forEach {
                if (itemViewType == it) {
                    viewHolderListeners[it].onBind(position, this)
                }
            }
        }

    }
}