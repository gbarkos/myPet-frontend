package com.example.mypet.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData


class PhotoPickerLifecycleObserver(private val registry : ActivityResultRegistry) : DefaultLifecycleObserver {

    lateinit var getContent : ActivityResultLauncher<String>
    private var uri: MutableLiveData<Uri> = MutableLiveData()

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, ActivityResultContracts.GetContent()) { uri ->
            this.uri.value = uri
        }
    }

    fun selectImage() {
        getContent.launch("image/*")
    }

    fun getUri() : MutableLiveData<Uri>{
        return uri
    }
}