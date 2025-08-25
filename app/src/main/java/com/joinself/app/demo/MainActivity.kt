package com.joinself.app.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.joinself.app.demo.ui.theme.SelfDemoTheme
import fr.bipi.treessence.file.FileLoggerTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupTimberLog()

        setContent {
            SelfDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    SelfDemoApp(
                        modifier = Modifier.padding(innerPadding),
                        onOpenSettings = { openAppSettings() }
                    )
                }
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    private fun setupTimberLog() {
        CoroutineScope(Dispatchers.Default).launch {
            if (Timber.treeCount == 0) {
                Timber.plant(Timber.DebugTree())
            }

            val fileTree =  FileLoggerTree.Builder()
                .withFileName("file%g.log")
                .withDir(applicationContext.cacheDir)
                .withSizeLimit(5000000)
                .withFileLimit(1)
                .withMinPriority(Log.DEBUG)
                .appendToFile(true)
                .buildQuietly()
            Timber.plant(fileTree)

            Timber.tag("DemoApp").d("Timber setup done")
        }
    }
}