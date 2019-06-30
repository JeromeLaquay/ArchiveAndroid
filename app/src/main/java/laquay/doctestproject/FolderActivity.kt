package laquay.doctestproject

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import laquay.doctestproject.adapters.FileInfoAdapter
import laquay.jerome.services.RetrofitFactory
import retrofit2.HttpException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_folder.*


class FolderActivity : AppCompatActivity() {

    var path = "filestorage"
    lateinit var files : List<FileInfo>
    lateinit var recycler : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1001
            )
        }
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1001
            )
        }

        if(intent.hasExtra("path")){
            path = intent.getStringExtra("path");
        }

        recycler = findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.hasFixedSize()
        recycler.adapter = FileInfoAdapter(listOf(), { fileInfo : FileInfo -> fileInfoClicked(fileInfo) },this)
        getFilesByPath(path)

        floating_action_button.setOnClickListener {
            val intent = Intent(this, AddFileActivity::class.java)
            startActivity(intent)
        }


    }

    fun getFilesByPath(path : String){
        val service = RetrofitFactory.makeRetrofitService()
        GlobalScope.launch(Dispatchers.Main){
            println("pathh : "+ path)
            val request = service.getFiles(path)
            try{
                val response = request.await()
                files = response.body()!!
                displayInfos()
            }catch(e : HttpException){
                Log.e("HttpException", e.code().toString())
            }catch(e:Throwable){
                Log.e("Throwable", e.message)
            }
        }
    }

    fun displayInfos(){
        this.runOnUiThread {
            recycler.adapter = FileInfoAdapter(files, { fileInfo : FileInfo -> fileInfoClicked(fileInfo) },this)
        }
    }

    private fun fileInfoClicked(fileInfo : FileInfo) {
        if(fileInfo.extension == ""){
            path +=  "/"+fileInfo.filename
            val intent = Intent(this, FolderActivity::class.java)
            intent.putExtra("path", path);
            startActivity(intent)
            Toast.makeText(this, "Clicked: ${fileInfo.filename}", Toast.LENGTH_LONG).show()
        }

    }
    private fun downloadClicked(fileInfo : FileInfo) {


    }


}
