package com.example.permissionsall.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.permissionsall.contactService.Contact
import com.example.permissionsall.databinding.ItemSwipeBinding
import com.zerobranch.layout.SwipeLayout

class MyAdaptersContact(var list:ArrayList<Contact>, var listener: OnItemClickItemListener) : RecyclerView.Adapter<MyAdaptersContact.MyViewHolder>() {

    inner class MyViewHolder(var itemSwipeBinding: ItemSwipeBinding):RecyclerView.ViewHolder(itemSwipeBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemSwipeBinding =
            ItemSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemSwipeBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact: Contact = list[position]
        holder.itemSwipeBinding.name.text = "${contact.name}\n${contact.number}"
        holder.itemSwipeBinding.tel.setOnClickListener {
            listener.onItemClickCallPhone(position,contact)
        }
//        holder.itemSwipeBinding.swipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
//            override fun onOpen(direction: Int, isContinuous: Boolean) {
//
//            }
//
//            override fun onClose() {
//
//            }
//
//        })
//        holder.itemSwipeBinding.sms.setOnClickListener {
//            listener.onItemClickMessageTransfer(position,contact)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickItemListener{
        fun onItemClickCallPhone(position: Int,contact: Contact)
        fun onItemClickMessageTransfer(position: Int,contact: Contact)
    }
}