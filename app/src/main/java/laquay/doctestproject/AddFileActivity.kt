package laquay.doctestproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_add_file.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import laquay.jerome.services.RetrofitFactory
import retrofit2.HttpException
import java.io.File
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import okhttp3.RequestBody




class AddFileActivity : AppCompatActivity() {

    lateinit var multipart : MultipartBody.Part
    var path = "filestorage"
    lateinit var file : File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_file)


        button.setOnClickListener {
            MaterialFilePicker()
                .withActivity(this@AddFileActivity)
                .withRequestCode(1)
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
        }
        save.setOnClickListener {
            stockFile(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            textView.text = filePath
            file = File(filePath)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)
            Toast.makeText(this, "Clicked: ${file.name}", Toast.LENGTH_LONG).show()
        }
    }

    fun stockFile(context : Context){
        val service = RetrofitFactory.makeRetrofitService()
        GlobalScope.launch(Dispatchers.Main){
            val request = service.stockFile(path, multipart)
            try{
                runOnUiThread(
                    Runnable {
                        Toast.makeText(context, "fichier enregistré", Toast.LENGTH_LONG).show()
                    }
                )
            }catch(e : HttpException){
                println("error 1")
                Toast.makeText(context, "error http", Toast.LENGTH_LONG).show()
                Log.e("HttpException", e.code().toString())
            }catch(e:Throwable){
                println("error 2")
                Toast.makeText(context, "error throwable : "+ e.message, Toast.LENGTH_LONG).show()
                Log.e("Throwable", e.message)
            }finally {
                println("success")
            }
        }
    }
}