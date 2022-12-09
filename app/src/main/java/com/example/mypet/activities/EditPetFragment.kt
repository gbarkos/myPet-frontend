package com.example.mypet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.mypet.R
import com.example.mypet.databinding.FragmentEditPetBinding
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.PetsViewModel
import java.io.*


class EditPetFragment : Fragment(R.layout.fragment_edit_pet), ResponseFunctions {

    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentEditPetBinding
    private lateinit var observer : PhotoPickerLifecycleObserver
    private lateinit var petInfoFragment : PetInfoFragment
    lateinit var dialog : LoadingCircleDialog
    private var uri: Uri? = null
    private var path : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observer = PhotoPickerLifecycleObserver(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditPetBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        viewmodel.responseListener = this   //assign responseListener
        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner) {
            if (it.pet != null) {
                populateViews(it)
            }
        }
        dialog = LoadingCircleDialog()

        binding.choosePetProfilePic.setOnClickListener{
            observer.selectImage()
            observer.getUri().observeOnce(viewLifecycleOwner){
                if(it != null){
                    /*var bitmap: Bitmap? = null
                    try {
                        bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                        } else {
                            val source: ImageDecoder.Source =
                                ImageDecoder.createSource(requireContext().contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val outputStream = ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    val byteArray: ByteArray = outputStream.toByteArray()

                    encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    Log.d("Encoded", encodedString)*/
                    Log.d("uri", it.toString())
                    uri = it

                    var contentResolver = requireContext().contentResolver
                    val filePath = (requireContext().applicationInfo.dataDir.toString() + File.separator
                            + System.currentTimeMillis()+".png")

                    val file = File(filePath)
                    try {
                        val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
                        //if (inputStream == null) return null
                        val outputStream: OutputStream = FileOutputStream(file)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (inputStream?.read(buf).also { len = it!! }!! > 0) outputStream.write(
                            buf,
                            0,
                            len
                        )
                        outputStream.close()
                        inputStream?.close()
                    } catch (ignore: IOException) {
                        Log.d("exception", ignore.toString())
                    }

                    path = file.absolutePath
                    Log.d("path", path!!)
                    binding.editPetPhoto.text = it.lastPathSegment?.toEditable()
                    binding.editPetPhoto.isEnabled = false
                }
            }
        }

        binding.submitEditPet.setOnClickListener{

            if(binding.editPetDistinguishingMarks.text.isNullOrEmpty() &&
               binding.editPetHeight.text.isNullOrEmpty() &&
               binding.editPetWeight.text.isNullOrEmpty() &&
               binding.editPetID.text.isNullOrEmpty()
            ){
                binding.petUpdateErrorTextView.text = "Παρακαλώ, συμπληρώστε τουλάχιστον 1 πεδίο"

            }else{
                var id =  binding.editPetID.text.toString()
                var distinguishingMarks = binding.editPetDistinguishingMarks.text.toString()
                var height = binding.editPetHeight.text.toString()
                var weight = binding.editPetWeight.text.toString()
                var photo = path
                var _id = viewmodel._id

                viewmodel.doUpdatePet(id, distinguishingMarks, weight, height, photo, _id)
            }
        }
    }

    private fun populateViews(it : PetGetResponse){
        binding.apply{
            editPetDistinguishingMarks.text = it.pet.distinguishingMarks?.toEditable()
            editPetHeight.text = it.pet.height?.toEditable()
            editPetWeight.text = it.pet.weight?.toEditable()
            editPetID.text = it.pet.id?.toEditable()
        }

    }

    override fun OnStarted() {
        Log.d("Update pet fragment", "Commencing...")
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        Log.d("Update pet fragment", "Success")
        dialog.dismiss()
        val intent = Intent (getActivity(), MainContentActivity::class.java)
        activity?.startActivity(intent)
        Toast.makeText(context, "Επιτυχής επεξεργασία κατοικίδιου", Toast.LENGTH_LONG).show()
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
        dialog.dismiss()
        Log.d("Update pet fragment", "Error")
        Toast.makeText(context, "Αδυναμία επεξεργασίας κατοικίδιου", Toast.LENGTH_LONG).show()

    }

}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}

fun <T> MutableLiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}