package id.hanifalfaqih.dicodingevent_submissionbfaa.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import id.hanifalfaqih.dicodingevent_submissionbfaa.R

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)
        .into(this)
}