package com.udacity.loadapp.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.loadapp.R
import com.udacity.loadapp.models.DownloadStatus
import com.udacity.loadapp.utils.NotificationUtils
import com.udacity.loadapp.widgets.LoadingButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var downloadManager: DownloadManager
    private var filesIdList = mutableSetOf<Long>()
    private var filesDownloading = 0

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (filesIdList.remove(downloadId)) {
                if (--filesDownloading == 0) {
                    btn_download.setState(LoadingButton.State.NORMAL)
                }

                val extras = intent!!.extras
                val query = DownloadManager.Query()
                    .setFilterById(extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    )
                    val fileName = cursor.getString(
                        cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)
                    )
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            NotificationUtils.sendDownloadNotification(
                                this@MainActivity,
                                fileName,
                                DownloadStatus.SUCCESS,
                                downloadId!!.toInt()
                            )
                        }
                        DownloadManager.STATUS_FAILED -> {
                            NotificationUtils.sendDownloadNotification(
                                this@MainActivity,
                                fileName,
                                DownloadStatus.FAIL,
                                downloadId!!.toInt()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.createNotificationChannel(
                this,
                NotificationUtils.getDownloadsChannel(this)
            )
        }

        btn_download.setOnClickListener {
            when (rg_options.checkedRadioButtonId) {
                R.id.option_load_app -> {
                    download(URL_LOAD_APP)
                    btn_download.setState(LoadingButton.State.LOADING)
                }
                R.id.option_glide -> {
                    download(URL_GLIDE)
                    btn_download.setState(LoadingButton.State.LOADING)
                }
                R.id.option_retrofit -> {
                    download(URL_RETROFIT)
                    btn_download.setState(LoadingButton.State.LOADING)
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.select_a_file),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun download(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setDescription(getString(R.string.downloading))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        // enqueue puts the download request in the queue.
        filesIdList.add(downloadManager.enqueue(request))
        filesDownloading++
    }

    companion object {
        private const val URL_LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE = "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT = "https://github.com/square/retrofit/archive/master.zip"
    }
}
