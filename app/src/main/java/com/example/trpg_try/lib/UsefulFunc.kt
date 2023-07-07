package com.example.trpg_try.lib

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.File


//fun getRealPathFromURI(context: Context, uri: Uri): String? {
//
//    // DocumentProvider
//    if (DocumentsContract.isDocumentUri(context, uri)) {
//
//        // ExternalStorageProvider
//        if (isExternalStorageDocument(uri)) {
//            val docId = DocumentsContract.getDocumentId(uri)
//            val split = docId.split(":").toTypedArray()
//            val type = split[0]
//            return if ("primary".equals(type, ignoreCase = true)) {
//                (Environment.getExternalStorageDirectory().toString() + "/"
//                        + split[1])
//            } else {
//                val SDcardpath = getRemovableSDCardPath(context).split("/Android").toTypedArray()[0]
//                SDcardpath + "/" + split[1]
//            }
//        } else if (isDownloadsDocument(uri)) {
//            val id = DocumentsContract.getDocumentId(uri)
//            val contentUri = ContentUris.withAppendedId(
//                Uri.parse("content://downloads/public_downloads"),
//                java.lang.Long.valueOf(id)
//            )
//            return getDataColumn(context, contentUri, null, null)
//        } else if (isMediaDocument(uri)) {
//            val docId = DocumentsContract.getDocumentId(uri)
//            val split = docId.split(":").toTypedArray()
//            val type = split[0]
//            var contentUri: Uri? = null
//            if ("image" == type) {
//                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            } else if ("video" == type) {
//                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//            } else if ("audio" == type) {
//                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//            }
//            val selection = "_id=?"
//            val selectionArgs = arrayOf(split[1])
//            return getDataColumn(
//                context, contentUri, selection,
//                selectionArgs
//            )
//        }
//    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//        // Return the remote address
//        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
//            context,
//            uri,
//            null,
//            null
//        )
//    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//        return uri.path
//    }
//    return null
//}

fun getRealPathFromURI(context: Context, uri: Uri): String? {
    var cursor: Cursor? = null
    try {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}
fun getRemovableSDCardPath(context: Context?): String {
    val storages: Array<File?> = ContextCompat.getExternalFilesDirs(context!!, null)
    return if (storages.size > 1 && storages[0] != null && storages[1] != null) storages[1].toString() else ""
}


fun getDataColumn(
    context: Context, uri: Uri?,
    selection: String?, selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(
            uri!!, projection,
            selection, selectionArgs, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri
        .authority
}


fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri
        .authority
}


fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri
        .authority
}


fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri
        .authority
}