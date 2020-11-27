

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.momecarefoundation.app.R
import com.momecarefoundation.app.Tutorial


// Empty constructor, required as per Fragment docs
class TutorialScreenFragment : Fragment() {

    private var imageView: AppCompatImageView? = null
    private var imageResId: Int = 0
    private var textView: TextView? = null
    private var titleResId: Int = 0
    private var textViewMessage: TextView? = null
    private var messageResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.imageResId = arguments.getInt(IMG_ID)
            this.titleResId = arguments.getInt(TIT_ID)
            this.messageResId = arguments.getInt(MSG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Identify and set fields!
        val rootView = inflater.inflate(R.layout.fragment_tutorial_screen, container, false) as ViewGroup
        imageView = rootView.findViewById(R.id.tutorial_screen_image) as AppCompatImageView
        textView = rootView.findViewById<View>(R.id.tutorial_screen_title) as TextView
        textViewMessage = rootView.findViewById<View>(R.id.tutorial_screen_message) as TextView
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Tutorial::class.java.isInstance(activity)) {
            textView!!.setText(titleResId)
            textViewMessage!!.setText(messageResId)
            imageView!!.setImageResource(imageResId)
        }
    }

    companion object {
        private val IMG_ID = "imgId"
        private val TIT_ID = "titId"
        private val MSG_ID = "msgId"

        fun newInstance(imageResId: Int, titleResId: Int, messageResId: Int): TutorialScreenFragment {
            val f = TutorialScreenFragment()
            val args = Bundle()
            args.putInt(IMG_ID, imageResId)
            args.putInt(TIT_ID, titleResId)
            args.putInt(MSG_ID, messageResId)
            f.arguments = args
            return f
        }
    }
}
