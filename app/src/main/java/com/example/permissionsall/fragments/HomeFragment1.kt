package com.example.permissionsall.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.permissionsall.contactService.Contact
import com.example.permissionsall.MainActivity
import com.example.permissionsall.R
import com.example.permissionsall.adapters.MyAdaptersContact
import com.example.permissionsall.databinding.FragmentHome1Binding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment1 : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHome1Binding
    private lateinit var myAdaptersContact: MyAdaptersContact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHome1Binding.inflate(inflater, container, false)
        dexterLibrary()
        return binding.root
    }

    private fun dexterLibrary() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                @SuppressLint("Range")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val contactList = arrayListOf<Contact>()
                    val act = activity as MainActivity
                    val contacts = act.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )

                    if (contacts != null && contacts.moveToFirst()) {
                        while (contacts.moveToNext()) {
                            val name =
                                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            val number =
                                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactList.add(Contact(name, number))
                        }
                        contacts.close()
                    }

                    myAdaptersContact =
                        MyAdaptersContact(
                            contactList,
                            object : MyAdaptersContact.OnItemClickItemListener {
                                override fun onItemClickCallPhone(position: Int, contact: Contact) {
                                    Dexter.withContext(requireContext())
                                        .withPermission(Manifest.permission.CALL_PHONE)
                                        .withListener(object : PermissionListener {
                                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                                val callIntent = Intent(Intent.ACTION_CALL)
                                                callIntent.data = Uri.parse("tel:" + contact.number)
                                                startActivity(callIntent)
                                            }

                                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                                if (response.isPermanentlyDenied) {
                                                    val intent =
                                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                    val uri: Uri = Uri.fromParts(
                                                        "package",
                                                        requireContext().packageName,
                                                        null
                                                    )
                                                    intent.data = uri

                                                    startActivity(intent)
                                                }
                                            }

                                            override fun onPermissionRationaleShouldBeShown(
                                                permission: PermissionRequest?,
                                                token: PermissionToken?
                                            ) {
                                                val alertDialog =
                                                    AlertDialog.Builder(requireContext())
                                                alertDialog.setMessage("Aloqaga chiqish uchun ruxsat bering")
                                                alertDialog.setPositiveButton(
                                                    "Ruxsat so`rash"
                                                ) { _, _ ->
                                                    token?.continuePermissionRequest()
                                                }
                                                alertDialog.setNegativeButton(
                                                    "Ruxsat so`ramaslik"
                                                ) { _, _ ->
                                                    requestPermissions(
                                                        arrayOf(Manifest.permission.RECORD_AUDIO),
                                                        1
                                                    )
                                                }
                                                alertDialog.show()
                                            }
                                        }).check()
                                }

                                override fun onItemClickMessageTransfer(
                                    position: Int,
                                    contact: Contact
                                ) {
                                    Dexter.withContext(requireContext())
                                        .withPermission(Manifest.permission.SEND_SMS)
                                        .withListener(object : PermissionListener {
                                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                                val bundle = Bundle()
                                                bundle.putSerializable("contact", contact)
                                                Navigation.findNavController(requireView())
                                                    .navigate(R.id.messageFragment1, bundle)
                                            }

                                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                                if (response.isPermanentlyDenied) {
                                                    val intent =
                                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                    val uri: Uri = Uri.fromParts(
                                                        "package",
                                                        requireContext().packageName,
                                                        null
                                                    )
                                                    intent.data = uri
                                                    startActivity(intent)
                                                }
                                            }

                                            override fun onPermissionRationaleShouldBeShown(
                                                permission: PermissionRequest?,
                                                token: PermissionToken?
                                            ) {
                                                val alertDialog =
                                                    AlertDialog.Builder(requireContext())
                                                alertDialog.setMessage("SMS yuborish uchun ruxsat ruxsat bering")
                                                alertDialog.setPositiveButton(
                                                    "Ruxsat so`rash"
                                                ) { _, _ ->
                                                    token?.continuePermissionRequest()
                                                }
                                                alertDialog.setNegativeButton(
                                                    "Ruxsat so`ramaslik"
                                                ) { _, _ ->
                                                    requestPermissions(
                                                        arrayOf(Manifest.permission.RECORD_AUDIO),
                                                        1
                                                    )
                                                }
                                                alertDialog.show()
                                            }
                                        }).check()
                                }
                            })
                    binding.rvSwipe.adapter = myAdaptersContact
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setMessage("Contact larni o`qib olish uchun ruxsat bering")
                    alertDialog.setPositiveButton(
                        "Ruxsat so`rash"
                    ) { _, _ ->
                        token?.continuePermissionRequest()
                    }
                    alertDialog.setNegativeButton(
                        "Ruxsat so`ramaslik"
                    ) { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            1
                        )
                    }
                    alertDialog.show()
                }
            }).check()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}