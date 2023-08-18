package com.example.test.ui.main

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.test.R
import com.example.test.databinding.FragmentQrCodeReaderBinding
import androidx.core.content.ContextCompat.getSystemService

import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat
import com.example.test.FlashLightUtils


class QrCodeReaderFragment : Fragment() {

    private lateinit var viewModel: QrCodeReaderViewModel
    private lateinit var codeScanner: CodeScanner


    private var _binding: FragmentQrCodeReaderBinding? = null
    private val binding: FragmentQrCodeReaderBinding
        get() = _binding!!

    private lateinit var flashLightUtils:FlashLightUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(QrCodeReaderViewModel::class.java)
        setUpScannerView()
        setUpFlashButton()
        flashLightUtils = FlashLightUtils(requireContext())

    }

    private fun setUpScannerView() {

        val activity = requireActivity()
        codeScanner = CodeScanner(activity, binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun setUpFlashButton() {

        if (hasFlash()) {
            binding.ivFlash.visibility = View.VISIBLE
            binding.ivFlash.setOnClickListener {
//                turnOnFlash()
                if (flashLightUtils.flashOn){
                    flashLightUtils.turnOffFlash()
                }else {
                    flashLightUtils.turnOnFlash()
                }
            }

        } else {
            binding.ivFlash.visibility = View.GONE
        }

    }

    private fun turnOnFlash() {
        try {
            val camManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
            val cameraId =
                camManager?.cameraIdList?.get(1) // Usually front camera is at 0 position.
            if (cameraId != null) {
                camManager.setTorchMode(cameraId, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun turnOffFlash() {
        try {
            val camManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
            val cameraId =
                camManager?.cameraIdList?.get(0) // Usually front camera is at 0 position.
            if (cameraId != null) {
                camManager.setTorchMode(cameraId, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun hasFlash(): Boolean {
        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == true
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


    companion object {
        @JvmStatic
        fun newInstance(): QrCodeReaderFragment {
            val fragment = QrCodeReaderFragment()
            return fragment
        }
    }

    /**
     *  @author Manohar
     *  not clearing _binding may cause memory leak
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}