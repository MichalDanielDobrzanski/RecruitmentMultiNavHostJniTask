package com.hr.test.main.detail.actors

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hr.models.Actor
import com.hr.test.R
import com.hr.test.utils.inflateNoAttach
import com.hr.test.view.createCircularProgressDrawable
import kotlinx.android.synthetic.main.detail_movie_actor_item_view.view.*

class ActorsAdapter(
    private val context: Context
) : RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {

    private val items: MutableList<Actor> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder =
        ActorsViewHolder(parent.inflateNoAttach(R.layout.detail_movie_actor_item_view))

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(campaignModelList: List<Actor>) {
        items.clear()
        items.addAll(campaignModelList)
        notifyDataSetChanged()
    }

    inner class ActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(actor: Actor) {
            itemView.apply {
                actorNameTextView.text = actor.name
                actorAgeTextView.text = actor.age.toString()
                Glide.with(this@ActorsAdapter.context)
                    .load(actor.imageUrl)
                    .placeholder(createCircularProgressDrawable(context.applicationContext))
                    .error(R.drawable.ic_no_network)
                    .into(actorAvatarImageView)
            }
        }
    }
}