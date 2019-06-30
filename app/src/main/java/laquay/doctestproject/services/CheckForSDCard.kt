package laquay.doctestproject.services

import android.os.Environment

/**
 * Created by Abhi on 11 Mar 2018 011.
 */

object CheckForSDCard {
    //Method to Check If SD Card is mounted or not
    val isSDCardPresent: Boolean
        get() = if (Environment.getExternalStorageState() ==

            Environment.MEDIA_MOUNTED
        ) {
            println("card sd present")
            true
        } else false
}