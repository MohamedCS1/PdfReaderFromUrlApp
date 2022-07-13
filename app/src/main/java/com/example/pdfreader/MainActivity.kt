package com.example.pdfreader

import android.app.ProgressDialog
import android.graphics.Canvas
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import java.io.File


class MainActivity : AppCompatActivity() ,OnLoadCompleteListener ,OnPageChangeListener ,OnPageErrorListener{

    lateinit var pdfView: PDFView
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)

        pdfView = findViewById(R.id.pdfView)
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        FileLoader.with(this)
            .load("https://excerpts.numilog.com/books/9782402620543.pdf", false)
            .fromDirectory("test4", FileLoader.DIR_INTERNAL)
            .asFile(object : FileRequestListener<File?> {
                override fun onLoad(request: FileLoadRequest, response: FileResponse<File?>) {
                    val url: File? = response.body
                    pdfView.fromFile(url)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .enableAnnotationRendering(true)
                        .onLoad(this@MainActivity)
                        .onPageChange(this@MainActivity)
                        .scrollHandle(DefaultScrollHandle(this@MainActivity))
                        .enableDoubletap(true)
                        .onPageError(this@MainActivity)
                        .swipeHorizontal(true)
                        .spacing(0)
                        .fitEachPage(false)
                        .nightMode(false)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .autoSpacing(false)
                        .load()
                }

                override fun onError(request: FileLoadRequest, t: Throwable) {}
            })

    }

    override fun loadComplete(nbPages: Int) {
        progressDialog.dismiss()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        progressDialog.dismiss()
    }

    override fun onPageError(page: Int, t: Throwable?) {
        progressDialog.dismiss()
    }
}