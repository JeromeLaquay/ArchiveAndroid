package laquay.doctestproject.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.file_info_item.view.*
import laquay.doctestproject.FileInfo
import laquay.doctestproject.R
import laquay.doctestproject.services.DownloadService


class FileInfoAdapter(val mItems: List<FileInfo>, val clickListener: (FileInfo) -> Unit, val context : Context) :
RecyclerView.Adapter<FileInfoAdapter.InfosHolder>(){

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return mItems.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileInfoAdapter.InfosHolder {
        val inflater = LayoutInflater.from(parent.context)
        val inflatedView = inflater.inflate(laquay.doctestproject.R.layout.file_info_item,null, false)
        return InfosHolder(inflatedView)
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: FileInfoAdapter.InfosHolder, position: Int) {
        holder?.name?.text = mItems.get(position).filename
        (holder as InfosHolder).bind(mItems[position], clickListener)
        val imageInt = setImageFile(mItems.get(position).extension)
        holder?.image.setImageResource(imageInt)
        holder.download.visibility = downloadVisibleForFile(mItems.get(position).extension)
        holder.download.setOnClickListener {
            val fullUrl = "http://192.168.43.234:8080/files/download?path="+mItems.get(position).url+"&filename="+mItems.get(position).filename
           val downloadService : DownloadService = DownloadService()
            downloadService.downloadFile(fullUrl, context)
        }
    }


    class InfosHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name
        val image = view.imageFile
        var download = view.imageDownload


        fun bind(fileInfo: FileInfo, clickListener: (FileInfo) -> Unit) {
            itemView.name.text = fileInfo.filename
            itemView.setOnClickListener { clickListener(fileInfo)}
        }


    }

    fun setImageFile(extension : String) : Int{
        var imageInt = R.drawable.folder
        when (extension) {
            "pdf" -> imageInt = R.drawable.pdf
            "png" -> imageInt = R.drawable.png
            "jpg" -> imageInt = R.drawable.jpg
            "xls" -> imageInt = R.drawable.xls

            else -> { println(" it's a folder")
            }
        }
        return imageInt
    }

    fun downloadVisibleForFile(extension : String) : Int{
        if(extension != ""){
            return View.VISIBLE
        }
        return View.INVISIBLE
    }

}