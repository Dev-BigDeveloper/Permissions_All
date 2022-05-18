package com.example.permissionsall.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.permissionsall.contactService.Contact
import com.example.permissionsall.databinding.FragmentMessage1Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MessageFragment1 : Fragment() {
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getSerializable(ARG_PARAM1) as Contact?
        }
    }

    private lateinit var binding: FragmentMessage1Binding

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessage1Binding.inflate(inflater,container,false)
        binding.nameContact.text = contact?.name.toString()
        binding.contactNumber.text = contact?.number.toString()

        val contact:Contact = arguments!!.getSerializable("contact") as Contact

        binding.nameContact.text = contact.name
        binding.contactNumber.text = contact.number

        with(binding) {
            buttonSms.setOnClickListener {
                if (binding.editText.text.isEmpty()){
                    Toast.makeText(requireContext(), "Malumot yozmasdan jo`natib bo`lmaydi", Toast.LENGTH_SHORT).show()
                }else{
                    var smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(contact?.number, null, binding.editText.text.toString(), null, null)
                    Navigation.findNavController(requireView()).popBackStack()
                }
            }
            exitCard.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MessageFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}